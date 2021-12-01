package DiaryProgram.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static DiaryProgram.util.MemberCommon.*;
import DiaryProgram.util.*;
import DiaryProgram.vo.*;
public class DiaryServiceImpl implements DiaryService {
	static Member tmpLoginMember; 
	// 로그인 시, 임시적으로 회원 정보가 담김
	static Member tmpFindAccount;
	// 계정 찾기 할 때 정보 담기
	public static List<Member>members = new ArrayList<Member>();
	//회원들의 정보를 담을 리스트 생성
	List<Relationship> relationships=new ArrayList<>();
	//관계정보를 담을 리스트 생성
	List<Plan> plans=new ArrayList<>();
	//계획정보를 담을 리스트 생성
	List<Room> rooms=new ArrayList<>();
	//생성한 방들에 대한 리스트
	List<Belong> belongs=new ArrayList<>();
	//생성한 소속관계에 대한 리스트
	Calendar planDate=Calendar.getInstance();
	//planDate용 임시 값
	
	{
		/*테스트 데이터
		 * 달력*/
		Calendar planDate1=Calendar.getInstance();
		planDate1.set(2021, Calendar.JULY, 2);
		Calendar planDate2=Calendar.getInstance();
		planDate2.set(2021, Calendar.JULY, 5);
		Calendar planDate3=Calendar.getInstance();
		planDate3.set(2021, Calendar.JULY, 7);
		Calendar planDate4=Calendar.getInstance();
		planDate4.set(2021, Calendar.JULY, 9);
		Calendar planDate5=Calendar.getInstance();
		planDate5.set(2021, Calendar.JULY, 13);
		Calendar planDate6=Calendar.getInstance();
		planDate6.set(2021, Calendar.JULY, 15);
		Calendar planDate7=Calendar.getInstance();
		planDate7.set(2021, Calendar.JULY, 17);	
		Calendar planDate8=Calendar.getInstance();
		planDate8.set(2021, Calendar.JULY, 18);
		Calendar planDate9=Calendar.getInstance();
		planDate9.set(2021, Calendar.JULY, 20);
		Calendar planDate10=Calendar.getInstance();
		planDate10.set(2021, Calendar.JULY, 21);
		Calendar planDate11=Calendar.getInstance();
		planDate11.set(2021, Calendar.JULY, 24);
		Calendar planDate12=Calendar.getInstance();
		planDate12.set(2021, Calendar.JULY, 26);
		Calendar planDate13=Calendar.getInstance();
		planDate13.set(2021, Calendar.JULY, 28);
		Calendar planDate14=Calendar.getInstance();
		planDate14.set(2021, Calendar.JULY, 30);
		Calendar planDate15=Calendar.getInstance();
		planDate15.set(2021, Calendar.JULY, 31);		
		
		Calendar planDate16=Calendar.getInstance();
		planDate16.set(2021, Calendar.AUGUST, 12);
		Calendar planDate17=Calendar.getInstance();
		planDate17.set(2021, Calendar.AUGUST, 12);
		
		// 테스트 데이터 - 회원
		members.add(new Member("javaman","1234", "킹길동", 190, 82, "ja@naver.com"));
		members.add(new Member("aaa"    ,"1234", "a길동" , 185, 78, "aa@naver.com"));
		members.add(new Member("bbb"    ,"1234", "b길동" , 181, 72, "bb@google.com"));
		members.add(new Member("ccc"    ,"1234", "c길동" , 177, 68, "cc@google.com"));
		members.add(new Member("ddd"    ,"1234", "d길동" , 173, 67, "dd@daum.net"));
		members.add(new Member("eee"    ,"1234", "e길동" , 173, 67, "ee@daum.net"));
		// 테스트 데이터 - 회원
		relationships.add(new Relationship(1, "javaman", "aaa", "친구해주라줘",      1, planDate.getTimeInMillis()));
		relationships.add(new Relationship(2, "javaman", "bbb", "친구해주겐니",      0, planDate.getTimeInMillis()));
		relationships.add(new Relationship(3, "aaa",     "bbb", "너 내 친구가 되라", 1, planDate.getTimeInMillis()));
		relationships.add(new Relationship(4, "eee",     "javaman", "줄 바꿈을 실험해보려면 메시지가 정말정말"
												+ "길어야하거든요. 오늘은 8월9일 영어로는 august 오케이",0,planDate.getTimeInMillis()));
		relationships.add(new Relationship(5, "bbb", "ccc",     "친구해주던가", 1, planDate.getTimeInMillis()));
		relationships.add(new Relationship(6, "bbb", "ddd",     "친구하쉴?",    1, planDate.getTimeInMillis()));
		relationships.add(new Relationship(7, "ccc", "javaman", "거절ㄴㄴ요",   1, planDate.getTimeInMillis()));
		relationships.add(new Relationship(8, "ccc", "ddd",     "같이 놀자",    1, planDate.getTimeInMillis()));
		relationships.add(new Relationship(9, "ddd", "javaman", "친구 해줘",    1, planDate.getTimeInMillis()));
		relationships.add(new Relationship(10,"ddd", "aaa",     "나랑 친구ㄱ",  1, planDate.getTimeInMillis()));
		// 테스트 데이터 - 계획
		plans.add(new Plan(1, "기타치기",      180000*6, 420000*6, "javaman",planDate1,true));
		plans.add(new Plan(2, "게임하기",      180000*6, 420000*6, "javaman",planDate2,true));
		plans.add(new Plan(3, "바리스타 연습", 180000*6, 180000*6, "javaman",planDate3,true));
		plans.add(new Plan(4, "요리연습",      180000*6, 180000*6, "aaa",planDate4,true));
		plans.add(new Plan(5, "빨래하기",      180000*6, 180000*6, "aaa",planDate5,true));
		plans.add(new Plan(6, "몽자훈련",      180000*6, 180000*6, "aaa",planDate6,true));
		plans.add(new Exercise(7, "줄넘기",  180000*60000, 180000*60000, "bbb", planDate7,true,10,50,50));
		plans.add(new Exercise(8, "축구",    180000*60000, 180000*60000, "bbb", planDate8,true,10,50,50));
		plans.add(new Exercise(9, "멀리뛰기",180000*60000, 180000*60000, "bbb", planDate9,true,10,50,50));
		plans.add(new Study(10, "HTML",      180000*6, 140000*6, "ccc", planDate10,false,50,30));
		plans.add(new Study(11, "SPRING",    180000*6, 140000*6, "ccc", planDate11,false,50,30));
		plans.add(new Study(12, "JSP",       180000*6, 140000*6, "ccc", planDate12,false,50,30));
		plans.add(new Plan(13, "강아지 샤워",150000*6, 180000*6, "ddd", planDate13,true));
		plans.add(new Plan(14, "영화 보기",  150000*6, 180000*6, "ddd", planDate14,true));
		plans.add(new Plan(15, "간식 먹기",  150000*6, 180000*6, "ddd", planDate15,true));
		plans.add(new Study(16, "자바의 정석", 180000*6, 140000*6, "javaman", planDate16,false,50,70));
		plans.add(new Study(17, "자바의 정석", 180000*6, 140000*6, "aaa", planDate16,false,50,50));
//		plans.add(new Study(18, "자바의 정석", 180000*6, 140000*6, "bbb", planDate16,false,50,50));
		// 테스트 데이터 - 방 종류
		rooms.add(new Room(1, "자바짱되자",   "javaman",new Study("자바의 정석",180000*6,50)));
		rooms.add(new Room(2, "쌩쌩이 달인",  "aaa",new Exercise("쌩쌩이 연습",120000,10,300)));
		rooms.add(new Room(3, "리코더달인",   "bbb",new Plan("리코더 연습",150000)));
		rooms.add(new Room(4, "헬스장",       "ccc",new Exercise("하체 운동",120000,10,300)));
		rooms.add(new Room(5, "수학공부모임", "ddd",new Study("수학의 정석",150000,50)));

		// 테스트 데이터 - 방 소속관계 방장
		belongs.add(new Belong(1, "javaman",1, "1번방 주인 java맨!!", 1, planDate.getTimeInMillis()));
		belongs.add(new Belong(2, "aaa", 2, " 2번방 주인 aaa", 1, planDate.getTimeInMillis()));
		belongs.add(new Belong(3, "bbb", 3, " 3번방 주인 bbb", 1, planDate.getTimeInMillis()));
		belongs.add(new Belong(4, "ccc", 4, " 4번방 주인 ccc", 1, planDate.getTimeInMillis()));
		belongs.add(new Belong(5, "ddd", 5, " 5번방 주인 ddd", 1, planDate.getTimeInMillis()));
		// 테스트 데이터 - 방 소속관계 가입한 방
		belongs.add(new Belong(6,"javaman",3,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(7,"javaman",4,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(8,"aaa",1,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(9,"aaa",3,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(10,"bbb",1,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(11,"bbb",5,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(12,"ccc",2,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(13,"ccc",5,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(14,"ddd",2,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
		belongs.add(new Belong(15,"ddd",4,"털뭉치가 미래야",1,planDate.getTimeInMillis()));
	}
	

	/**
	 * @author 박영준
	 * 회원가입*/
	public void signUp() {

		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓\n"
						 + "┃      회원 가입      ┃\n"
						 + "┗━━━━━━━━━━━━━━━━━━━━━┛\n");
		
		Member account = new Member(
				checkDuplicatedId  ("아이디를 입력해주세요  ( 7자 이내)  ▶▶ ", 7)
				,noEmptyLimitStr   ("비밀번호를 입력해주세요(10자 이내)  ▶▶ ", 10)
				,noEmptyLimitStr   ("이름을 입력해주세요    ( 4자 이내)  ▶▶ ", 4)
				,nextDouble        ("키를 입력해주세요          ▶▶ ")
				,nextDouble        ("몸무게를 입력해주세요      ▶▶ ")
				,checkCorrectFormat("이메일 주소를 입력해주세요 ▶▶ ")
				);	
		members.add(account);
	}
	
	/**
	 * @author 박영준
	 * (중복아이디, 공백)체크, 글자수제한*/
	private String checkDuplicatedId(String text, int number) {
		check : while(true) {
			String id = nextLine(text);
			for(int i = 0 ; i<members.size() ; i++) {
				if(id.equals(members.get(i).getId())) {
					System.out.println("이미 존재합니다. 다시 입력해주세요.");
					continue check;
				}else if(id.trim().isEmpty() || id.length()==0) {
					System.out.println("아무것도 입력하지 않았습니다.");
					continue check;
				}else if(id.length()>number) {
					System.out.println("입력가능한 글자수를 초과하였습니다.(" + number + "글자 이하)");
					continue check;
				}else if(id != id.replaceAll(" ", "")) {
					System.out.println("문자 사이에 공백이 있습니다");
					continue check;
				}else if(checkKorSpecialChr(id)) {
					System.out.println("특수문자 또는 한글이 포함되어 있습니다");
					continue check;
				}
			}
			return id;		
		}
	}
	
	/**
	 * @author 박영준
	 * 아이디 특수문자 한글 체크*/
	private boolean checkKorSpecialChr(String id) {
		for(int i=0 ; i<id.length() ; i++) {
			if( !('a' <= id.charAt(i) && id.charAt(i) <= 'z')|| ('A' <= id.charAt(i) && id.charAt(i) <= 'Z') || ('0' <= id.charAt(i) && id.charAt(i) <= '9') )  {
				return true;
			}
		}
		return false;
	}
		
	/**
	 * @author 박영준
	 * (중복이메일, 이메일형식, 한글, 공백)체크*/
	private String checkCorrectFormat(String text) {
		String pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
		check : while(true) {
			String checkEmailFormat = nextLine(text);
			for(int i = 0 ; i<members.size() ; i++) {
				if(checkEmailFormat.equals(members.get(i).getId())) {
					System.out.println("이미 존재합니다. 다시 입력해주세요.");
					continue check;
				}else if(Pattern.matches(pattern, checkEmailFormat)==false) {
					System.out.println("잘못된 이메일 형식입니다. 다시입력해주세요.");
					continue check;
				}	
			}
			return checkEmailFormat;
		}
	}

	
	/**
	 * @author 박영준
	 * 로그인 */
	public void signIn() throws Exception {
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n"
						 + "┃   회원 ID & PASSWORD 입력  ┃\n"
						 + "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		boolean isTyping = true;
		while(isTyping) {
			
			String accountId = noEmptyLimitStr("회원님의 아이디를 입력해주세요   ▶▶ ", 7);
			tmpLoginMember = matchId(accountId);			
			String accountPw = noEmptyLimitStr("회원님의 비밀번호를 입력해주세요 ▶▶ ", 10);
			System.out.println();
			
			if(tmpLoginMember != null && tmpLoginMember.getPassword().equals(accountPw)) {
				System.out.println("- 로그인 성공 -\n" + tmpLoginMember.getName() + "님 안녕하세요!\n");
				addRoomPlan(tmpLoginMember.getId());
				boolean isUserLoggedIn = true;
				while(isUserLoggedIn) {
						int input = nextInt("┏━━━━━━━━━━━━━━━━━━━━━━━┓\n" //메뉴 순서 변경 가능
										  + "┃       실행 메뉴       ┃\n"
										  + "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
										  + "┃ 1. 나의 계획          ┃\n"
										  + "┃ 2. 친구               ┃\n"
										  + "┃ 3. 마이 룸            ┃\n"
										  + "┃ 4. 달력 조회          ┃\n"
										  + "┃ 5. 마이 페이지        ┃\n"
										  + "┃ 6. 로그아웃           ┃\n"
										  + "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n");
						switch(input) {
						case 1:
							planExe();
							break;
						case 2:
							friend();
							break;
						case 3:
							myRoom();
							break;
						case 4:
							monthly();	
							break;
						case 5:
							myPage();
							break;
						case 6:
							System.out.println("로그아웃 합니다\n");
							isTyping = false;
							isUserLoggedIn = false;
							break;
						default :
							System.out.println("정확한 번호를 입력해주세요\n");
						}											
				} // Fuction루프 끝
			} else {
				System.out.println("아이디 또는 비밀번호가 틀렸습니다");
				
				boolean isChoosing = true;
				while(isChoosing) {
						int input = nextInt("┏━━━━━━━━━━━━━━━━━━━━┓\n"
										  + "┃ 1.재입력           ┃\n"
										  + "┃ 2.로그인 취소      ┃\n"
										  + "┗━━━━━━━━━━━━━━━━━━━━┛\n");		
						switch(input) {
						case 1:
							isChoosing = false;
							break;
						case 2:
							System.out.println("로그인을 취소합니다");
							isChoosing = false;
							isTyping = false;
							break; 
						default :
							System.out.println("정확한 번호를 입력해주세요\n");
						}				
				} // while문 종료			 
			} // if-else if문 종료
		} // Signin 루프 종료
	} // signIn메서드 종료
	
	
	/**
	 * @author 박영준
	 * 마이페이지(조회,수정 등)*/
	private void myPage() {
		boolean isMyPage = true;
		while(isMyPage) {			
				int input = nextInt("┏━━━━━━━━━━━━━━━━━━━━━━━┓\n"
					  	  		  + "┃    마이 페이지 메뉴   ┃\n"
					  	  		  + "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
					  	  		  + "┃ 1. 회원정보 조회      ┃\n"
					  	  		  + "┃ 2. 회원정보 수정      ┃\n"
					  	  		  + "┃ 3. 메인 화면          ┃\n"
					  	  		  + "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n");
				switch(input) {			
				case 1:
					checkInfo();
					break;
				case 2:
					editInfo();
					break;
				case 3:
					System.out.println("메인 화면으로 돌아갑니다");
					isMyPage = false;
					break;

				default :
					System.out.println("정확한 번호를 입력해주세요\n");
				} 	
		}
	}
	
	/**
	 * @author 박영준
	 * 회원정보 출력*/
	private void checkInfo() {
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓\n"
			  	  		  +"┃      회원 정보      ┃\n"
			  	  		  +"┗━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.println("   이름 : " + tmpLoginMember.getName());
		System.out.println("     키 : " + tmpLoginMember.getHeight());
		System.out.println(" 몸무게 : " + tmpLoginMember.getWeight());
		System.out.println(" 이메일 : " + tmpLoginMember.getEmailAdr() + "\n");
	}
	
	/**
	 * @author 박영준
	 * 회원정보 수정 실행 (로그인 후 사용가능)*/
	private void editInfo() { 
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n"
						 + "┃ 수정할 항목을 선택해주세요 ┃\n"
						 + "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");				
		executeEdit(tmpLoginMember);
	}			
			
	
	/**
	 * @author 박영준
	 * 회원정보 수정*/
	private void executeEdit(Member id) {
		boolean isEditing = true;
		while(isEditing) {	
				int input = nextInt("┏━━━━━━━━━━━━━━━━━━━━━━━┓\n"
								  + "┃       수정 목록       ┃\n"
								  + "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
								  + "┃ 1.이름                ┃\n"
								  + "┃ 2.키                  ┃\n"
								  + "┃ 3.몸무게              ┃\n"
								  + "┃ 4.이메일              ┃\n"
								  + "┃ 5.비밀번호            ┃\n"
								  + "┃ 6.수정 종료           ┃\n"
								  + "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n");
				switch(input) {
				case 1:
					String tmpName = id.getName();
					id.setName(nextLine("이름 수정 ▶▶ "));
					System.out.println("회원님의 이름이 변경되었습니다 \n"
							+"변경 전 : "+ tmpName + " ==>  변경 후 : " + id.getName() + "\n");
					break;
				case 2:
					double tmpHeight = id.getHeight();
					id.setHeight(nextDouble("키 수정 ▶▶ "));
					System.out.println("회원님의 키가 변경되었습니다 \n"
							+"변경 전 : "+ tmpHeight + " ==>  변경 후 : " + id.getHeight() + "\n");
					break;
				case 3:
					double tmpWeight = id.getWeight();
					id.setWeight(nextDouble("몸무게 수정 ▶▶ "));
					System.out.println("회원님의 몸무게가 변경되었습니다 \n"
							+"변경 전 : "+ tmpWeight + " ==>  변경 후 : " + id.getWeight() + "\n");
					break;
				case 4:
					String tmpEmailAdr = id.getEmailAdr();
					id.setEmailAdr(nextLine("이메일 수정 ▶▶ "));
					System.out.println("회원님의 이메일 주소가 변경되었습니다 \n"
							+"변경 전 : "+ tmpEmailAdr + " ==>  변경 후 : " + id.getEmailAdr() + "\n");
					break;
				case 5:
					String tmpPassword = id.getPassword();
					id.setPassword(nextLine("비밀번호 수정 ▶▶ "));
					System.out.println("회원님의 비밀번호가 변경되었습니다 \n"
							+"변경 전 : "+ tmpPassword + " ==>  변경 후 : " + id.getPassword() + "\n");
					break;
				case 6:
					System.out.println("수정을 종료합니다");
					isEditing = false;
					break;
				default :
					System.out.println("정확한 번호를 입력해주세요\n");
				}			
		}
	}
			
		
	/**
	 * @author 박영준
	 * 로그인 - ID 매칭, 맴버 반환*/
	public Member matchId(String id) { // 로그인 메서드에서 사용 아이디 매칭하고 해당 맴버 반환
		for(int i = 0 ; i<members.size() ; i++) {
			if(id.equals(members.get(i).getId())) {
				return members.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @author 박영준
	 * 아이디찾기 - 이름 매칭, 맴버 반환*/
	public Member matchName(String name) { // 계정찾기 - 아이디 메서드에서 사용 이름 매칭하고 해당 맴버 반환
		for(int i = 0 ; i<members.size() ; i++) {
			if(name.equals(members.get(i).getName())) {
				return members.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @author 박영준
	 * 아이디 비번 찾기*/
	public void findAccount() {
		
		boolean isFinding = true;
		while(isFinding) {
			
				int input = nextInt("┏━━━━━━━━━━━━━━━━━━━━━━━┓\n"
						  	  	  + "┃     회원 정보 찾기    ┃\n"
						  	  	  + "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
						  	  	  + "┃ 1.아이디 찾기         ┃\n"
						  	  	  + "┃ 2.비밀번호 찾기       ┃\n"
						  	  	  + "┃ 3.메인으로            ┃\n"
						  	  	  + "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n");
				switch(input) {
				case 1:  // 회원 아이디 이메일로 발송
					String name = noEmptyLimitStr("가입시 등록했던 이름을 입력해주세요   ▶▶ ", 7);
					tmpFindAccount = matchName(name);
					String idEmail = noEmptyLimitStr("가입시 등록했던 이메일을 입력해주세요 ▶▶ ", 30);
					System.out.println();
					
					if(tmpFindAccount != null && tmpFindAccount.getEmailAdr().equals(idEmail)) {
						System.out.println("완료 메세지가 나올때 까지 잠시만 기다려주세요");
						
						sendMail(idEmail // 메일을 받는 회원의 이메일
								, name + " 님 안녕하세요. 다이어리 운영자 입니다." // 메일 제목
								, "안녕하세요.\n"                                  // 메일 내용
								+ "사용자 아이디 찾기를 요청 받았습니다.\n"
								+ name + " 님의 아이디는 " + matchName(name).getId() + " 입니다.");
						
						System.out.println("회원님의 아이디가 등록된 이메일로 발송되었습니다\n");
					} else {
						System.out.println("이름 또는 이메일이 일치하지 않습니다.\n");
					}
					break;
				
				case 2: // 회원 임시비밀번호 이메일로 발송
					String id = nextLine("가입시 등록했던 아이디를 입력해주세요 ▶▶ ");
					String pwEmail = noEmptyLimitStr("가입시 등록했던 이메일을 입력해주세요 ▶▶ ", 30);
					tmpFindAccount = matchId(id);
				
					if(tmpFindAccount.getEmailAdr().contentEquals(pwEmail)) {
						System.out.println("완료 메세지가 나올때 까지 잠시만 기다려주세요");
											
						matchId(id).setPassword(createTmpPw());
												
						sendMail(pwEmail
								, matchId(id).getName() + " 님 안녕하세요. 다이어리 운영자 입니다."
								, "안녕하세요.\n"
								+ "사용자 비밀번호 찾기를 요청 받았습니다.\n"
								+ "임시 비밀번호를 발급 해드리오니 로그인 후\n"
								+ "반드시 변경 바랍니다.\n"
								+ "임시 비밀번호 : " + matchId(id).getPassword());
						
						System.out.println("회원님의 비밀번호가 등록된 이메일로 발송되었습니다\n");
					}
					break;
					
				case 3: //메인화면 돌아가기
					isFinding = false;
					break;
				
				default :
					System.out.println("정확한 번호를 입력해주세요\n");
				}						
		}
	}

	/**
	 * @author 박영준
	 * 임시 비밀번호 생성*/
	private String createTmpPw() {
		String pw = "";		
		for(int i = 1 ; i <= 6 ; i++) {  // 6자리 비밀번호 생성을 위해 6번 반복
			
			int r = (int)(Math.random() * 3) + 1; //랜덤 숫자 1 ~ 3			
			// 1 이면 숫자 0 ~ 9
			if(r==1) { 
				pw += (int)(Math.random() * 10) + "";	
			// 2 이면 대문자 A ~ Z	
			} else if(r==2) {      
				up : while(true) {
					int upCase = (int)(Math.random() * 90) + 1;
					if(upCase >= 'A') {
						pw += "" + (char)upCase;
						break up;
					} else {
						continue up;
					}	
				}
			// 3 이면 소문자 a ~ z
			} else {      
				low : while(true) {
					int lowCase = (int)(Math.random() * 122) + 1;
					if(lowCase >= 'a') {
						pw += "" + (char)lowCase;
						break low;
					} else {
						continue low;
					}	
				}								
			}
		} return pw;	
	}
	
	/**
	 * @author 박영준
	 * 메일 보내기*/
	public void sendMail(String to, String title, String content) { 
		// 1. 계정찾기 서비스를 이용하는 사람의 이메일
		// 2. 이메일 제목 (ex. ???님 안녕하세요. 다이어리 운영자 입니다.)
		// 3. 이메일 내용 (ex. 안녕하세요.
		// 사용자 비밀번호를 다시 설정하도록 요청 받았습니다.
		// 임시 비밀번호를 발급해드립니다. 
		//  로그인 후 꼭 비밀번호를 변경하시길 바랍니다.
		
		// SMTP 설정
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		Properties props = System.getProperties();
//		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		// 세션 및 메세지 객체 생성
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				String name = "zerojun1022@gmail.com";
				String pw = "dbfl1022!";  // 보내는계정(관리자의 계정)
				return new PasswordAuthentication(name, pw);
			}
		}); 
		
		MimeMessage msg = new MimeMessage(session);
		
		try {
			msg.setFrom(new InternetAddress("test@gmail.com", "다이어리 운영자"));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject(title);
			msg.setText(content, "utf-8");
			
			Transport.send(msg);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} //이메일 발송 메서드 종료
	
	
	/**
	 * @author 김우성
	 * 계획 입력
	 * */
	public void addPlan() {
		Calendar today = Calendar.getInstance();
		
		int exCode=(plans.size()==0?0:plans.get(plans.size()-1).getPlanCode());
		
		System.out.println("어떤 타입의 계획을 입력하시겠습니까? ▶▶");	
		switch (nextInt("(1) 일반    (2) 운동    (3) 공부")) {
		case 1:
			String planName = checkDuplicatedPlan("계획의 이름을 정해주세요 ▶▶", 10);
			long targetTime = nextLong("목표 시간을 정해주세요 (분단위로 작성해주세요.) ▶▶");
			Plan plan = new Plan(
					++exCode
					,planName
					,targetTime * 60000
					,0
					,tmpLoginMember.getId()
					,today
					,false);
			plans.add(plan);
			break;
		case 2:
			String exName= checkDuplicatedPlan("계획의 이름을 정해주세요 ▶▶", 10);
			long exTarget= nextLong("목표 시간을 적어주세요 (분단위로 작성해주세요.) ▶▶");
			showMet();
			double exMet=nextDouble("산소량(Met)을 적어주세요 ▶▶");
			double targetCal=nextDouble("목표 칼로리를 적어주세요 ▶▶");
			Exercise ex = new Exercise(
					++exCode
					,exName
					,exTarget*60000
					,0
					,tmpLoginMember.getId()
					,today
					,false
					,exMet
					,targetCal
					,0);
			plans.add(ex);
			break;
		case 3:
			String stName = checkDuplicatedPlan("계획의 이름을 정해주세요 ▶▶", 10);
			long targetSt = nextLong("공부 할 시간을 적어주세요 ▶▶");
			int targetpage = nextInt("공부할 페이지를 적어주세요. ▶▶");
			Study study = new Study(
					++exCode
					,stName
					,targetSt* 60000
					,0
					,tmpLoginMember.getId()
					,today
					,false
					,targetpage
					,0
					);
			plans.add(study);
		default:
			System.out.println("번호를 잘못 입력하셨습니다.");
			break;
			}
	}
	
	/**
	 * 
	 * @author 김우성
	 * @author 박영준
	 * 같은 날짜에 있는 계획 중복&공백&글자수 제한 예외처리 메서드
	 */
	private String checkDuplicatedPlan(String text, int number) { 
		Calendar today = Calendar.getInstance();
		check : while(true) {
			String checkPlan = nextLine(text);
			for(int i = 0 ; i< plans.size() ; i++) {
				if(hasPlan(checkPlan) && findByPlan(checkPlan).getPlanDate().get(Calendar.YEAR) == today.get(Calendar.YEAR) && 
					findByPlan(checkPlan).getPlanDate().get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
					findByPlan(checkPlan).getPlanDate().get(Calendar.DATE) == today.get(Calendar.DATE)) {
					System.out.println("없는 계획이거나 이미 존재하는 계획이름입니다.");
					continue check;
				}else if(checkPlan.trim().isEmpty() || checkPlan.length()==0) {
					System.out.println("아무것도 입력하지 않았습니다.");
					continue check;
				}else if(checkPlan.length()>number) {
					System.out.println("입력가능한 글자수를 초과하였습니다.(" + number + "글자 이하)");
			}
			return checkPlan;		
			}
		}
	}
	/**
	 * @author 김우성
	 * 해당이름의 계획이름이 존재하는지 확인하는 메서드
	 */
	private boolean hasPlan(String planName) { 
		for(int i = 0 ; i<plans.size() ; i++) {
			if(planName.equals(plans.get(i).getPlanName())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @author 김우성
	 *  공통된 플랜 조회 메서드
	 */
	public void commonPlan(Plan p) {
		
		System.out.println("계획코드   ▶▶  " + p.getPlanCode());
		System.out.println("계획이름   ▶▶  " + p.getPlanName());
		System.out.println("목표시간   ▶▶  " +(p.getTargetTime()/60000>=60?p.getTargetTime()/3600000:p.getTargetTime()/1000>=60?p.getTargetTime()/60000:p.getTargetTime()/1000)+
																					(p.getTargetTime()/60000>=60?"시간":p.getTargetTime()/1000>=60?"분":"초"));
		System.out.println("성취시간   ▶▶  " +(p.getAchievedTime()/60000>=60?p.getAchievedTime()/3600000:p.getAchievedTime()/1000>=60?p.getAchievedTime()/60000:p.getAchievedTime()/1000)+
				(p.getAchievedTime()/60000>=60?"시간":p.getAchievedTime()/1000>=60?"분":"초"));
	}
	/**
	 * 
	 * @author 김우성
	 * 계획 조회 부분
	 */
	public void planList(Plan p) {
		Calendar today = Calendar.getInstance();
			if(p instanceof Exercise && isDateOfPlan(p, today)) {
				commonPlan(p);
				System.out.println("산소량     ▶▶  " + ((Exercise)p).getMet());
				System.out.println("목표칼로리 ▶▶  " + ((Exercise)p).getTargetCal());
				System.out.println("달성칼로리 ▶▶  " + ((Exercise)p).getAchievedCal());
				System.out.println(p.isAchieved() == true?"성취여부   ▶▶  ♥ 완료 ♥":"성취여부   ▶▶  노력중T_T");
				System.out.println("｡･:*:･ﾟ★,｡･:*:･ﾟ☆｡･:*:･ﾟ★,｡･:*:･ﾟ☆｡･:*:･ﾟ★,｡･:*:･ﾟ");

			} else if(p instanceof Study && isDateOfPlan(p, today)) {
				commonPlan(p);
				System.out.println("목표 페이지수 ▶▶  " + ((Study)p).getTargetPage());
				System.out.println("달성 페이지수 ▶▶  " + ((Study)p).getAchievedPage());
				System.out.println(p.isAchieved() == true?"성취여부   ▶▶  ♥ 완료 ♥":"성취여부   ▶▶  노력중T_T");
				System.out.println("｡･:*:･ﾟ★,｡･:*:･ﾟ☆｡･:*:･ﾟ★,｡･:*:･ﾟ☆｡･:*:･ﾟ★,｡･:*:･ﾟ");
				
			} else if(isDateOfPlan(p, today)) {
				commonPlan(p);
				System.out.println(p.isAchieved() == true?"성취여부   ▶▶  ♥ 완료 ♥":"성취여부   ▶▶  노력중T_T");
				System.out.println("｡･:*:･ﾟ★,｡･:*:･ﾟ☆｡･:*:･ﾟ★,｡･:*:･ﾟ☆｡･:*:･ﾟ★,｡･:*:･ﾟ");
			}
			return;
	}
	
	/**
	 * @author 김우성
	 *  플랜이름을 이용항 회원 찾기
	 */
	public Plan findByPlan(String planName) {
		for(int i = 0 ; i<plans.size() ; i++) {
			if(planName.equals(plans.get(i).getPlanName())) {
				return plans.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @author 김우성
	 * 아이디를 이용한 회원 찾기
	 */
	public Plan findById(String id) {
		for(int i = 0 ; i<plans.size() ; i++) {
			if(id.equals(plans.get(i).getMemberId())) {
				return plans.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @author 김우성
	 * 회원의 칼로리 계산
	 */
	public double calculate(Exercise p) {
		return (double)(p.getMet() * (3.5 * tmpLoginMember.getWeight() * (p.getAchievedTime()/60000))*5)/1000;
	}
	
	/**
	 * @author 조윤정
	 * met 값 보여주기
	 */
	public void showMet() {
		System.out.println(
				"┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n"
			  + "┃    METs   ┃                          활동내용                          ┃\n"+
			    "┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n"+
			    "┃    3.0    ┃  가벼운 활동,웨이트트레이닝(경중도),볼링,배구,프리즈비     ┃\n" 
			   +"┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n"+
	  		    "┃    4.0    ┃  속보,수중운동,탁구,태극권,아쿠아빅                        ┃\n" 
			   +"┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n"+
			    "┃    4.5    ┃  배드민턴,골프                                             ┃\n" 
			   +"┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n"+
			    "┃    6.0    ┃  웨이트트레이닝(고강도),미용체조,인터벌러닝,농구,에어로빅  ┃\n" 
			   +"┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n"+
			    "┃    7.0    ┃  조깅,축구,테니스,스케이트,스키                            ┃\n" 
			   +"┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n"+
			    "┃    8.0    ┃  사이클(20km/h),런닝(134m/m),수영(자유영)                  ┃\n" 
			   +"┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n"+
			    "┃    10.0   ┃  런닝(161m/m),유도,킥복싱,태권도,럭비                      ┃\n" 
			   +"┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	}
	
	/**
	 * @author 조윤정
	 * 친구에 관한 기능들*/
	public void friend() throws Exception {
		while(true) {
			try {
					int input=nextInt("┏━━━━━━━━━━━━━━━━━━━━━━━┓\n" //메뉴 순서 변경 가능
									+ "┃         친구          ┃\n"
									+ "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
									+ "┃ 1. 친구 신청하기      ┃\n"
									+ "┃ 2. 친구 목록 조회     ┃\n"
									+ "┃ 3. 친구 삭제하기      ┃\n"
									+ "┃ 4. 친구 요청함        ┃\n"
									+ "┃ 5. 메인으로 돌아가기  ┃\n"
									+ "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n",1,5);
		
					switch (input) {
					case 1:
						addFriend();
						break;
			
					case 2:
						System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓\n"
			   							  +"┃      친구 목록      ┃\n"
									      +"┗━━━━━━━━━━━━━━━━━━━━━┛");
						friendList();
						break;
			
					case 3:
						removeFriend();
						break;
			
					case 4:
						checkRequest();
						break;
			
					case 5:
						return;
					}
				}catch(NumberFormatException e) {
					System.out.println("잘못 입력하셨습니다.");
				}
		}
		
	}
	
	/**
	 * @author 조윤정
	 * 친구신청보내기*/
	public void addFriend() {
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓\n"
	  	         		  +"┃    친구 신청하기    ┃\n"
	  	        		  +"┗━━━━━━━━━━━━━━━━━━━━━┛");
		int relNum=(relationships.size()==0?0:relationships.get(relationships.size()-1).getRelationNumber());
		//친구하고픈 아이디 받아오기
		String beMyFriend=nextLine("요청을 보내고싶은 친구의 아이디를 입력해주세요 ▶▶");
		
		//아이디가 내 아이디면 친구추가 불가
		if(beMyFriend.equals(tmpLoginMember.getId())) {
			System.out.println("자신과는 이미 친구예요 ^_^");
			return;
		}
		
		//이미 친구이거나 친구신청이 된 상태면 신청불가. 단 거절된 상태면 또 친구신청 가능
		for(int i=0;i<relationships.size();i++) {
			if(areWeFriend(i,beMyFriend)) {
				System.out.println("이미 친구이거나 친구신청이 완료된 상태입니다.");
				return;
			}
		}
		// 해당 아이디를 검색했는데 맞는 회원없을 시 메시지 띄우기
		Member respondant=matchId(beMyFriend);
		if(respondant==null) {
			System.out.println("입력하신 아이디에 해당하는 회원이 없습니다.");
			return;
		}
		
		// 친구 객체 생성
		Calendar today=Calendar.getInstance();
		String msg=nextLine("친구에게 보낼 요청메시지를 입력해주세요 ▶▶");
		System.out.println(msg.equals("")?"메시지를 입력하지않으셨습니다.":"'"+msg+"'"+"라고 작성하셨습니다.");
		System.out.println("이대로 보낼까요?? ▶▶");
		int input=nextInt("(1) 네      (2) 아니오");
		switch (input) {
		case 1:
			relationships.add(new Relationship(
					++relNum,
					tmpLoginMember.getId(),
					respondant.getId(),
					msg,
					0,
					today.getTimeInMillis()));	
			System.out.println("\n친구요청이 완료되었습니다.");
			break;
			
		case 2:
			System.out.println("\n친구요청을 취소하셨습니다.");
			break;
			
		default:
			System.out.println("\n번호를 잘못입력하셨습니다.");
			System.out.println("친구 메인으로 돌아갑니다.");
			break;
		}
	}
	
	
	
	/**
	 * @author 조윤정
	 * 친구들 조회기능*/
	public void friendList() {
		int cnt=0;
		for(int i=0;i<relationships.size();i++) {
			if(friendByMe(i,1)||friendByYou(i,1)) {
				cnt++;
				System.out.printf("%3d.  친구아이디:: %9s  관계코드:: %3d   ♥ %s ♥\n",cnt,
						(friendByMe(i)?relationships.get(i).getRespondentId():relationships.get(i).getRequestorId()),
						relationships.get(i).getRelationNumber(),difference(relationships.get(i).getrDate())
						);
			}
		}
		if(cnt==0) {
			System.out.println("　　*｡☆");
			System.out.println("　  　+ﾟ☆*　친구를");
			System.out.println("　   　ﾟ｡;*｜∧ ∧　사귀어보는 건");
			System.out.println("　    ･ﾟ＊* ∩･ω･`)　어떨까요~?~?");
			System.out.println("    ☆:+｡･　ﾉ　　⊃");
			System.out.println(" *:＊+*･　 し－Ｊ");
			System.out.println(":*:ﾟ･ ｡☆");
			System.out.println();
			int input=nextInt("(1) 친구신청하러갈래요.        (2) 그냥 혼자 놀래요.");
			switch (input) {
			case 1:
				addFriend();
				return;
			case 2:
				System.out.println();
				System.out.println("나중에 친구추가하고 방기능도 이용해보세요 ʚ˘◡˘ɞ");
				return;
			default:
				System.out.println();
				System.out.println("잘못입력하셨습니다");
				System.out.println("친구 메뉴로 돌아갑니다.");
				return;
			}
		}
	}
	
	/**
	 * @author 조윤정
	 * 친구삭제기능*/
	public void removeFriend() {
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓\n"
  	                 	  +"┃      친구 삭제      ┃\n"
  		                  +"┗━━━━━━━━━━━━━━━━━━━━━┛");
		friendList();
		boolean flag=true;
		int cnt=0;
		int input=nextInt("삭제하려는 친구관계의 코드를 입력해주세요. ▶▶"); 
		//삭제하려고 입력한 친구관계코드에서 내가 요청자 혹은 응답자여야하고 이미 받아들여진 상태(친구인 상태)여야 함.
		//이 조건 없으면 내 친구관계 아닌데 엄한 사람 친구관계 삭제함
		while(flag) {		
			if(friendByMe(findRBy(input))||friendByYou(findRBy(input))){
				System.out.println((friendByMe(findRBy(input))?
						findRBy(input).getRespondentId():findRBy(input).getRequestorId())
						+" 친구를 정말 삭제하시겠습니까? ▶▶");
				flag=false;
			} else {
				cnt++;
				System.out.println("친구코드를 잘못 입력하셨습니다.");
				if(cnt==2) {
					return;
				}
				input=nextInt("다시 입력해주세요. ▶▶");
			}
		}
		int check=nextInt("(1) 예.       (2) 아니오.      ▶▶");
		switch (check) {
		case 1:
			relationships.remove(findRBy(input));
			System.out.println("\n친구 삭제를 완료하였습니다.");
			break;
		case 2:
			System.out.println("\n친구 삭제를 취소하셨습니다.");
			break;
		default:
			System.out.println();
			System.out.println("잘못 입력하셨습니다.");
		}
	}
	
	/**
	 * @author 조윤정
	 * 친구요청 확인기능*/
	public void checkRequest() throws Exception {

		List<Relationship> tmpList=new ArrayList<>();
		int cnt=0;
		boolean flag=true;
		int chance=0;
		System.out.println( "┏━━━━━━━━━━━━━━━━━━━━━━━┓\n"
						  + "┃      요청함 선택      ┃\n"
						  + "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
						  + "┃ 1. 받은 요청함        ┃\n"
						  + "┃ 2. 보낸 요청함        ┃\n"
						  + "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n");
		int choice=nextInt("어떤 요청함을 확인하시겠습니까? ▶▶",1,2);
		switch (choice) {
		case 1:
			cnt=0;
			System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓\n"
                 	          +"┃     받은 요청함     ┃\n"
	                          +"┗━━━━━━━━━━━━━━━━━━━━━┛");
			for(int i=0;i<relationships.size();i++) {
				if(friendByYou(i,0)) {
					tmpList.add(relationships.get(i));
				}
			}
			
			//조건에 맞는 것들을 tmpList에 넣어서 사용자가 자기꺼말고 다른 계획 제어하는 것 방지
			for(int i=0;i<tmpList.size();i++) {
				System.out.printf("%3d.  요청코드:: %3d   %9s님이 보낸 요청입니다.   ♥ %s ♥\n",i+1,
						tmpList.get(i).getRelationNumber(),tmpList.get(i).getRequestorId(),
						difference(relationships.get(i).getrDate()));
			}
			if(tmpList.size()==0) {
				System.out.println("새로운 친구 요청이 없습니다.");
				break;
			}
			int relationCode=nextInt("어떤 친구의 요청을 확인하시겠습니까? (요청코드를 입력해주세요.) ▶▶");
			while(flag) {
				for(int i=0;i<relationships.size();i++) {
					if(relationships.get(i).getRelationNumber()==relationCode && tmpList.contains(relationships.get(i))) {
						flag=false;
						msgAscii(relationships.get(i).getMsg());
						System.out.println("친구요청을 수락하시겠습니까?? ▶▶");
						int input=nextInt("(1) 예        (2) 아니오    ");
						Calendar today=Calendar.getInstance();
						switch (input) {
						case 1:
							relationships.get(i).setStatus(1);
							relationships.get(i).setrDate(today.getTimeInMillis());
							break;

						case 2:
							relationships.get(i).setStatus(-1);
							relationships.get(i).setrDate(today.getTimeInMillis());
							break;
							
						default:
							System.out.println("\n");
						}
					}
				}
				if(chance==2) {
					System.out.println("계속된 입력오류로 친구메뉴로 돌아갑니다.");
					flag=false;
				}
				if(flag) {
				chance++;
				relationCode=nextInt("올바른 코드를 입력해주세요. >");
				}
			}
			break;
			
		case 2:
			cnt=0;
			System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓\n"
       	                      +"┃     보낸 요청함     ┃\n"
                              +"┗━━━━━━━━━━━━━━━━━━━━━┛");
			for(int i=0;i<relationships.size();i++) {
				if(friendByMe(i)) {
						cnt++;
						int stat= relationships.get(i).getStatus();	
						System.out.printf("%3d.  관계코드:: %3d  %9s님에게 보낸 요청입니다.  상태:: %s   ♥ %s ♥\n",cnt,
								relationships.get(i).getRelationNumber(),relationships.get(i).getRespondentId(),
								(stat==-1?"거절":stat==0?"읽지않음":"수락"),difference(relationships.get(i).getrDate())
								);
				}
			}
			if(cnt==0) {
				System.out.println("보낸 친구 요청이 없습니다.");
				break;
			}
			break;
		}
	}
	
	
	/**
	 * @author 조윤정
	 * 요청보내고 난 후 경과시간, 친구가 된 지 몇일 째 등을 나타내기 위한 메서드*/
	public String difference(long date) {
		Calendar date2=Calendar.getInstance();
		long difference=(date2.getTimeInMillis()-date)/(1000*60);
		if (difference>=60*24) {
			return difference/(60*24)+"일";
		}
		else if (difference>=60) {
			return difference/60+"시간";
		}
		else if(difference>=1) {
			return difference+"분";
		}
		return "방금";		
	}
	
	/**
	 * @author 조윤정
	 *관계코드로 해당하는 관계찾기*/
	public Relationship findRBy(int relationshipNumber) {
		for(int i=0;i<relationships.size();i++) {
			if(relationships.get(i).getRelationNumber()==relationshipNumber) {
				return relationships.get(i);
			}
		}		
		return null;
	}
	
	
	/**
	 * @author 조윤정
	 *내가 요청자일 때의 관계들을 나타내기*/
	public boolean friendByMe(int i) {
		if(relationships.get(i).getRequestorId().equals(tmpLoginMember.getId())) {
			return true;
		}
		return false;
	}
	
	public boolean friendByMe(int i,int stat) {
		if(relationships.get(i).getRequestorId().equals(tmpLoginMember.getId())&&relationships.get(i).getStatus()==stat) {
			return true;
		}
		return false;
	}
	
	public boolean friendByMe(int i,String beMyFriend) {
		if(relationships.get(i).getRequestorId().equals(tmpLoginMember.getId())&&
				relationships.get(i).getRespondentId().equals(beMyFriend)) {
			return true;
		}
		return false;
	}
	
	public boolean friendByMe(Relationship rel) {
		if(rel.getRequestorId().equals(tmpLoginMember.getId())&&rel.getStatus()==1){
			return true;
		}
		return false;
	}
	
	/**
	 * @author 조윤정
	 * 친구가 요청자일 때의 관계들을 나타내기*/
	public boolean friendByYou(int i,int stat) {
		if(relationships.get(i).getRespondentId().equals(tmpLoginMember.getId())&&relationships.get(i).getStatus()==stat) {
			return true;
		}
		return false;
	}
	
	public boolean friendByYou(int i,String beMyFriend) {
		if(relationships.get(i).getRespondentId().equals(tmpLoginMember.getId())&&
				relationships.get(i).getRequestorId().equals(beMyFriend)) {
			return true;
		}
		return false;
	}
	
	public boolean friendByYou(Relationship rel) {
		if(rel.getRespondentId().equals(tmpLoginMember.getId())&&rel.getStatus()==1){
			return true;
		}
		return false;
	}
	
	public boolean areWeFriend(int i,String beMyFriend) {
		if((friendByYou(i,beMyFriend)||friendByMe(i,beMyFriend))&&
				relationships.get(i).getStatus()!=-1) {
			return true;
		}
		return false;
	}
	


	/**
	 * @author 조윤정 
	 * 이달의 계획 성취도조회 및 날짜별로 조회 가능하게 하는 기능*/
	public void monthly() {
		Calendar firstDayOfMonth=Calendar.getInstance();
		firstDayOfMonth.set(firstDayOfMonth.get(Calendar.YEAR),firstDayOfMonth.get(Calendar.MONTH),1);
		showCalendar(firstDayOfMonth);
		System.out.println();
		System.out.println();
		while(true) {
			String input=nextLine("보고싶은 날짜를 입력해주세요. (이전 달 조회 < , 다음 달 조회 > )  ▶▶");
			if(input.equals("<")||input.equals(">")) {
				firstDayOfMonth.set(firstDayOfMonth.get(Calendar.YEAR),
						firstDayOfMonth.get(Calendar.MONTH)+(input.equals("<")?-1:1),
						1);
				showCalendar(firstDayOfMonth);
				}
			else if(Integer.parseInt(input)>=1
					&&Integer.parseInt(input)<=firstDayOfMonth.getActualMaximum(Calendar.DATE)) {
				int intInput=Integer.parseInt(input);
				firstDayOfMonth.set(firstDayOfMonth.get(Calendar.YEAR),firstDayOfMonth.get(Calendar.MONTH),intInput);
				findPlanByDate(firstDayOfMonth);
				System.out.println();
				System.out.println("다른 날짜도 조회하시겠습니까? ▶▶");
				int choice=nextInt("(1) 다른 날짜도 조회      (2) 메인으로");
				switch (choice) {
				case 1:
					break;
				case 2:
					return;

				default:
					System.out.println("\n번호를 잘못입력하셨습니다.");
					System.out.println("메인으로 돌아갑니다.");
					return;
				}
			
			}	
		}
	}
	
	/**
	 * @author 조윤정
	 * 달력나타내는 메서드*/
	public void showCalendar(Calendar firstDayOfMonth) {
		System.out.println();
		System.out.println();
		System.out.println("                                     "+firstDayOfMonth.get(Calendar.YEAR)+"년 "+(firstDayOfMonth.get(Calendar.MONTH)+1)+"월");
		System.out.println();
		System.out.println("        SUN        MON        TUE        WED        THU        FRI        SAT");
		System.out.println();
		List<Integer> tmpI=new ArrayList<Integer>();
		List<List<Integer>> dates=new ArrayList<>();
		for(int i=1;i<firstDayOfMonth.getActualMaximum(Calendar.DATE)+firstDayOfMonth.get(Calendar.DAY_OF_WEEK);i++) {
			int tmp=i-firstDayOfMonth.get(Calendar.DAY_OF_WEEK)+1;
			tmpI.add(tmp<1?null:tmp);
			if(i%7==0) {
				dates.add(tmpI);
				tmpI=new ArrayList<Integer>();
			}
		}
		dates.add(tmpI);
		for(int i=0;i<dates.size()*2;i++) {
			if(i%2==0) {
				for(int j=0;j<dates.get(i/2).size();j++) {
					System.out.printf("%11s",(dates.get(i/2).get(j)==null?"":dates.get(i/2).get(j)+""));	
				}
				System.out.println();
			}
			else {
				String str="";
				for(int j=0;j<dates.get(i/2).size();j++) {
					Calendar tmpDate=Calendar.getInstance();
					if(dates.get(i/2).get(j)!=null){
						tmpDate.set(firstDayOfMonth.get(Calendar.YEAR),firstDayOfMonth.get(Calendar.MONTH)
								,dates.get(i/2).get(j));
						}
					str+=String.format("%11s",(dates.get(i/2).get(j)==null?" "
								:isAllAchieved(tmpDate)?"^_^":havePlan(tmpDate)?
										"T_T":""));	
				}
  				System.out.println(str);
			}
			System.out.println();
		}			 
	}

	/**
	 * @author 조윤정
	 * 해당 날짜에 일치하는 계획 출력*/
	public void findPlanByDate(Calendar firstDayOfMonth) {
		int cnt=0;
		for(int i=0;i<plans.size();i++) {
			if(isMyPlan(i)&&isDateOfPlan(i,firstDayOfMonth)) {
					cnt++;
					System.out.println(plans.get(i));
				}	
		}
		if(cnt==0) {
			System.out.println("해당 날짜에 계획이 없습니다.");
		}
	}
	
	/**
	 * @author 조윤정
	 * 해당 날짜에 계획이 있는지 boolean으로 리턴*/
	public boolean havePlan(Calendar firstDayOfMonth) {
		for(int i=0;i<plans.size();i++) {
			if(isMyPlan(i)&&isDateOfPlan(i,firstDayOfMonth)) {
					return true;
				}	
		}
			return false;
	}
	
	
	/**
	 * @author 조윤정
	 * 해당 날짜의 계획들이 전부 성취되어있는지 
	 * 불린값으로 리턴*/
	public boolean isAllAchieved(Calendar date) {
		int cnt=0;
		boolean achieved = false;
		for(int i=0;i<plans.size();i++) {
			if(isMyPlan(i)&&isDateOfPlan(i,date)) {
				cnt++;
				achieved=plans.get(i).isAchieved();
			}
		}
		if(cnt==0) {
			achieved=false;
		}
		return achieved;
	}
	
	/**
	 * @author 조윤정
	 * 나의 계획이 맞는지 boolean 값 리턴*/
	public boolean isMyPlan(int i) {
		if(plans.get(i).getMemberId().equals(tmpLoginMember.getId())) {
			return true;
		}
		return false;
	}
	
	public boolean isMyPlan(Plan p) {
		if(p.getMemberId().equals(tmpLoginMember.getId())) {
			return true;
		}
		return false;
	}
	
	/**
	 * @author 조윤정
	 * 해당 날짜의 계획이 맞는 지 확인*/
	public boolean isDateOfPlan(int i,Calendar date) {
		if(plans.get(i).getPlanDate().get(Calendar.YEAR)==date.get(Calendar.YEAR)&&
				plans.get(i).getPlanDate().get(Calendar.MONTH)==date.get(Calendar.MONTH)&&
				plans.get(i).getPlanDate().get(Calendar.DATE)==date.get(Calendar.DATE)) {
			return true;
		}
		return false;
	}
	
	public boolean isDateOfPlan(Plan p,Calendar date) {
		if(p.getPlanDate().get(Calendar.YEAR)==date.get(Calendar.YEAR)&&
				p.getPlanDate().get(Calendar.MONTH)==date.get(Calendar.MONTH)&&
				p.getPlanDate().get(Calendar.DATE)==date.get(Calendar.DATE)) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * @author 조윤정
	 * 날마다 로그인 시 방공동목표가 새롭게 생성*/
	public void addRoomPlan(String tmpLoginId) {
		Plan roomPlan=null; 
		for(int i=0;i<belongs.size();i++) { 
			if(isMyRoom(i)&&belongs.get(i).getStatus()==1) { 
				/*belongs 중 아이디가 내 아이디와 같고 상태값 1(소속) 이면
				 해당 방의 공동목표를 roomPlan에 담음*/				
				roomPlan=getRoomPlan(findRoomBy(belongs.get(i).getRoomCode()).getPlan());
			}
			if(roomPlan!=null&&!plans.contains(roomPlan)) {
				/*이때 roomPlan이 null이 아니고
				 이미 오늘자로 공동목표가 추가되지 않았어야만 plans에 추가*/		
				plans.add(roomPlan);
			}
		}
	}
	
	
	/**
	 * @author 조윤정
	 공동 목표를 본격적으로 plans에 추가하기 전 지금 로그인 한 사람에 맞도록 변경*/
	public Plan getRoomPlan(Plan p) {
		Plan roomPlan;
		int planNum=(plans.size()==0?0:plans.get(plans.size()-1).getPlanCode());
		Calendar now=Calendar.getInstance();
			if(p instanceof Study) {
				roomPlan=new Study(++planNum,p.getPlanName(),
						p.getTargetTime(),0,tmpLoginMember.getId(),now,false,((Study) p).getTargetPage(),0);
			}
			else if(p instanceof Exercise) {
				roomPlan=new Exercise(++planNum,p.getPlanName(),
						p.getTargetTime(),0,tmpLoginMember.getId(),now,false,
						((Exercise) p).getMet(),((Exercise) p).getTargetCal(),0);
			}
			else{
				roomPlan=new Plan(++planNum,p.getPlanName(),
						p.getTargetTime(),0,tmpLoginMember.getId(),now,false);
			}
		return roomPlan;
	}
	
	/**
	 * @author 조윤정
	 * roomCode를 통해서 해당되는 방 리턴하는 메서드*/
	public Room findRoomBy(int roomCode) {
		for(int i=0;i<rooms.size();i++) {
			if(rooms.get(i).getRoomCode()==roomCode) {
				return rooms.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @author 조윤정
	 * 해당 belong이 나에 대한 belong인지 확인*/
	public boolean isMyRoom(int i) {
		if(belongs.get(i).getId().equals(tmpLoginMember.getId())){
			return true;
		}
		return false;
	}
	
	/**
	 * @author 조윤정
	 * 해당 belong이 그 방에 대한 것인지 확인*/
	public boolean membersInRoom(int roomCode) {
		for(int i=0;i<belongs.size();i++) {
			if(belongs.get(i).getRoomCode()==roomCode){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @author 조윤정
	 * 방에 있는 회원들의 이름을 리스트로 반환
	 */
	public List<String> membersInRoomList(int roomCode) {
		List<String> memberIds=new ArrayList<>();
		for(Belong b:belongs) {
			if(b.getRoomCode()==roomCode){
				memberIds.add(b.getId());
			}
		}
		return memberIds;
	}
	
	/**
	 * @author 조윤정
	 * 방안에 있는 사람들이 오늘 날짜로 실행하는 계획가지고 순위보여줌
	 * 3등까지만!
	 * @return 
	 */
	public String rank(int roomCode) {
		Calendar today=Calendar.getInstance();
		Plan roomPlan=findRoomBy(roomCode).getPlan();
		List<Plan> roomRank=new ArrayList<>();
		List<String> memberIds=membersInRoomList(roomCode);
		for(Plan p:plans) {
			if(isDateOfPlan(p, today)&&
				memberIds.contains(p.getMemberId())&&
				p.getPlanName().equals(roomPlan.getPlanName())) {
				roomRank.add(p);
			}
		}
		Collections.sort(roomRank); 
		String str="★★★";
		String print="";
		String tmp="";
		if(roomRank.size()<3) {
			for(int i=0;i<roomRank.size();i++) {
				tmp=String.format("%-43s", str.substring(0,3-i)+" "+roomRank.get(i).rankString(i));
				print+="           |   |  "+tmp+"  |   |\n";
			}
		}
		else {
			for(int i=0;i<3;i++) {
				tmp=String.format("%-43s", str.substring(0,3-i)+" "+roomRank.get(i).rankString(i));
				print+="           |   |  "+tmp+"  |   |\n";
			}
			print+="           |   |                                                     |   |\r\n"
				 + "           |   |  FIGHTING:\\> 나머지 분들은 더욱 화이팅해봅시다!     |   |\r\n"
				 + "           |   |               o(*'▽'*)/☆ﾟ _                          |   |\r\n"	;
		}
		return print;
	}
	/**@author 조윤정
	 * 
	 * @throws InterruptedException
	 */
	public void asciiAni() throws InterruptedException {
		int index=(int)(Math.random()*3);
		String[][] arr=new String[][] {{"　　　*,':\r\n" + 
		                                "　　,:\"\"\r\n",
		                                "　//　　　  ♪\r\n" + 
		             				    "`｜｜　　　♩♬♪\r\n",
		             				    "｜ ､\r\n" + 
		            				    "､＼ ヽ_\r\n",
		            				    "   　､＼　二_ ∧∞_∧\r\n" + 
		            					"　  ｀-_ ￣(｀ ･ω ･)\r\n" + 
		            					"　 　　　 ー(.. O┳O\r\n" + 
		            				    "　　　　　 ◎ ) `J_))    룰루♪"},{" ∧,,∧\r\n" + 
		            													  "(；ω；) 하얗게\r\n" + 
		            													  ".∪　∪\r\n" + 
		            													  "し‐Ｊ\r\n" + 
		            													  "\r\n",
		            													  " ∧ ,';,';,',.\r\n" + 
		            													  "( ; ω,';,',. 불태웠어..\r\n" + 
		            													  ".∪　∪\r\n" + 
		            													  "し‐Ｊ\r\n" + 
		            													  "\r\n",
		            													  " 　　,';,',.\r\n" + 
		            													  "　';',,'...\r\n" + 
		            													  ".∪';',,'...\r\n" + 
		            													  "し‐Ｊ\r\n" + 
		            													  "\r\n",
		            													  "　　';,';,',.\r\n" + 
		            													  "　,';,',',..\r\n" + 
		            													  "';',,'..."},
			{"　  ∧_∧　！\r\n" + 
			 "　(´ﾞﾟωﾟ')\r\n" + 
	  		 "＿(_つ/￣￣￣/＿\r\n" + 
			 "　 ＼/　　　/\r\n" + 
			 "　　　￣￣￣\r\n" + 
			 "\r\n" ,
			 "　  ∧_∧\r\n" + 
			 "　(;ﾞﾟωﾟ')\r\n" + 
			 "＿(_つ__ミ　헉\r\n" + 
			 "　＼￣￣￣＼ミ\r\n" + 
			 "　　￣￣￣￣\r\n" + 
			 "\r\n" ,
			 "　 .:∧_∧:\r\n" + 
			 "＿:(;ﾞﾟωﾟ'): 이건 너무 무섭잖아!\r\n" + 
			 "　＼￣￣￣＼\r\n" + 
			 "　　￣￣￣￣"},{"|         꼭 기필코        |\r\n" + 
			    	   		  "|   자바짱이 되겠습니다!!  |\r\n" + 
			    	   		  "￣￣￣￣∨￣￣￣￣￣￣￣\r\n" + 
			    	   		  "　　　 ∧_,,∧\r\n" + 
			    	   		  "　  　(`･ω･´)\r\n" + 
			    	   		  "　　　Ｕ θ Ｕ\r\n" + 
			    	   		  "　／￣￣｜￣￣＼\r\n" + 
			    	   		  "　|二二二二二二二|\r\n" + 
			    	   		  "  ｜　　　　　　 ｜\r\n" + 
			    	   		  "찰칵       찰칵   찰\r\n" + 
			    	   		  "　 ∧ ∧└　   ∧ ∧     칵\r\n" + 
			    	   		  "　(　　)】 (　　)】\r\n" + 
			    	   		  "　/　/┘　/　/┘\r\n" + 
							  "ノ￣ヽ　ノ￣ヽ{ 그래서 객체가 뭔가요?!"}};
		for(int i=0;i<arr[index].length;i++) {
			System.out.print(arr[index][i]);
			Thread.sleep(300);
		}
	}
	/**
	 * @author 조윤정
	 * 메시지 보냈을 때 메시지에 틀 적용*/
	public void msgAscii(String str) {
		//템플릿 종류 두가지 중에 랜덤으로 출력
		int index=(int)(Math.random()*2);
		//틀 위쪽
		String[] header= {"\"　     ♡ ♡ ♡ ᕬ ᕬ ♡ ♡ ♡\n"
            	+ "       + ♡  ( ⌯′-′⌯)  ♡ +\n"
            	+ "  ┏━━━♡━━━━━━ U U ━━━━━━♡━━━┓",
            	"。　  ♡    。    ♡    。　  ♡\n"
            	+ "♡。   　＼　　  ｜　   　／。　 ♡"," ╭ ◜◝ ͡ ◜◝ ͡◜◝ ͡  ◜◝ ͡ ◜◝ ͡ ◜◝╮"};
		//틀 아래쪽
		String[] footer= {"  ┗━━♡━━━━━━━━━♡━━━━━━━━━♡━━┛",
	"♡  。    ／   。｜　  　＼   。 ♡\n"
	+ "。  　♡  。 　   ♡    。　 ♡ 。"," ╰ ◟◞ ͜  ◟ ͜   ◟◞ ͜  ◟◞ ͜  ◟◞◟ ͜   ◟◞ ͜  ◟ ͜   ◟◞ ╯\r\n" + 
	                                    "                       O\r\n" + 
	                                    "                         °."};
		System.out.println(header[index]);
		String[] arr=str.split(""); // 입력한 문자열 하나하나 잘라서 배열로 만들기
		if(arr.length==1&&arr[0].equals("")) { //그냥 빈 거면 입력한 메시지 없도록
			arr= new String[0];
			System.out.println("         상대방이 입력한");
			System.out.print("        메시지가 없습니다.");
		}
		if(14-arr.length>0) { // 만약에 첫 줄 다 못채울 양이면 가운데 정렬
			for(int i=0;i<14-arr.length;i++) {
				System.out.print(" ");
			}
		}
		System.out.print("   ");
		// 한 줄 예쁘게 채우는 거 계산하는데 한글이랑 영어,숫자
		//공간차지하는 거 통일
		for(int i=0;i<arr.length;i++) {
			char tmp=arr[i].charAt(0);
			if((tmp>='A'&&tmp<='Z')||
					(tmp>='a'&&tmp<='z')||(tmp>='0'&&tmp<='9')) {
				System.out.printf("%2s",arr[i]);
			}
			else System.out.print(arr[i]);
			//한 줄 넘치면 줄바꿈
			if(i!=0&&i%13==0) {
				System.out.print("\n"+"   ");
			}
		}
		System.out.print("\n");
		System.out.println(footer[index]);
	}
	
	/**
	 * @author 조윤정, 박영준
	 * 디자인-조윤정, 기능-박영준 */
	public void showRoomAscii(int code) {
		boolean flag=true;
		System.out.printf("             ____________________________________________________________\r\n" + 
	          	           "           /                                                             \\\r\n" + 
	          	           "           |    _____________________________________________________    |\r\n" + 
	          	           "           |   |___________________________________________________🅇_|   |\r\n" + 
	          	           "           |   |                                                     |   |\r\n" + 
	          	           "           |   |  ROOMCODE:\\>%-3d                                     |   |\r\n" + 
	          	           "           |   |  GOAL:\\> "+"%-38s"+"|   |\r\n" + 
	          	           "           |   |  MEMBERS:\\> %s" +     
	          	           "           |   |  RANKING:\\>                                         |   |\r\n" + 
	          	           "%s"+
	          	           "           |   |                                                     |   |\r\n" + 
	          	           "           |   |_____________________________________________________|   |\r\n" + 
	          	           "           |                                                             |\r\n" + 
	          	           "            \\__________________________________________________________/\r\n" + 
	          	           "                 \\_______________________________________________/\r\n" + 
	          	           "                     ___________________________________________ \r\n" + 
	          	           "                  _-'    .-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.  --- `-_\r\n" + 
	          	           "               _-'.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.--.  .-.-.`-_\r\n" + 
	          	           "            _-'.-.-.-. .---.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-`__`. .-.-.-.`-_\r\n" + 
	          	           "         _-'.-.-.-.-. .-----.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-----. .-.-.-.-.`-_\r\n" + 
	          	           "      _-'.-.-.-.-.-. .---.-. .-------------------------. .-.---. .---.-.-.-.`-_\r\n" + 
	          	           "     :-------------------------------------------------------------------------:\r\n" + 
				           "     `---._.-------------------------------------------------------------._.---'\r\n",
				           code,findRoomBy(code).getPlan().getPlanName(),printRoomMembers(code),rank(code));
		while(flag) {
			System.out.println("메뉴로 돌아가시겠습니까? ▶▶");
			int menu = nextInt("(1) 예.          (2) 아니오.");
			switch (menu) {
			case 1:
				flag=false;
				break;
			case 2:
				break;
			default:
				flag=false;
				System.out.println("잘못입력하셨습니다.\n이전화면으로 돌아갑니다.");
				break;
		}
		}
	}
	
	/*@author 박영준
	 *마이룸 메뉴*/
	private void myRoom() {
		boolean flag = true;
		while(flag) {
				int input = nextInt("┏━━━━━━━━━━━━━━━━━━━━━━━┓\n"
				  	  	  		  + "┃      마이룸 메뉴      ┃\n"
				  	  	  		  + "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
				  	  	  		  + "┃ 1.방 만들기           ┃\n"
				  	  	  		  + "┃ 2.방 조회 및 입장     ┃\n"
				  	  	  		  + "┃ 3.방에 초대하기       ┃\n"
				  	  	  		  + "┃ 4.방 초대함           ┃\n"
				  	  	  		  + "┃ 5.방 삭제             ┃\n"
				  	  	  		  + "┃ 6.메인으로            ┃\n"
				  	  	  		  + "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n");
				switch(input) {
				case 1:
					createRoom();
					break;
				case 2:
					enterRoom();
					break;
				case 3:
					inviteToRoom();
					break;
				case 4:
					invitationBox();
					break;
				case 5:
					removeRoom();
					break;
				case 6:
					System.out.println("메인 메뉴로 돌아갑니다");
					flag = false;
					break;
				default :
					System.out.println("정확한 번호를 입력해주세요");
					break;
				}
		}
	}

	/*@author 박영준
	 *메뉴1번 ▶▶ 방만들기*/
	private void createRoom() {
		setUpRoom();
		containBelongInfo();
		
		System.out.println(".    ᘏ▸◂ᘏ                ╭◜◝◜◝◜◝◜◝◜◝◜◝◜◝◜◝╮\r\n" + 
						   "   ꒰  ɞ̴̶̷ ·̮ ɞ̴̶̷ ꒱   .oO    방 생성 완료! ᝰꪑ\r\n" + 
						  "    ( つ旦O         ╰◟◞◟◞◟◞◟◞◟◞◟◞◟◞◟◞╯\r\n");
	}

	/*@author 박영준
	 * 방 설정*/ 	
	private void setUpRoom() {
		int i = (rooms.size()==0 ? 0 : rooms.get(rooms.size()-1).getRoomCode());
		//방 생성
		Room memberRoom = new Room(
				// 방 코드
				++i
				// 방 이름
				,noEmptyLimitStr("생성할 방의 이름을 입력해주세요. ▶▶", 10)
				// 방 관리자 코드 
				,tmpLoginMember.getId()
				// 방 공동계획
				,inputPlan()
		);		
		rooms.add(memberRoom);
	}
	
	/*@author 박영준
	 * 공동계획 입력*/ 
	private Plan inputPlan() { 

		Plan plan = null;
		while(true) {				
			switch(nextInt("계획 종류 선택 ①운동 ②공부 ③커스텀 \n")) {
			case 1:
			showMet();
			Plan ex = new Exercise(
						noEmptyLimitStr("계획이름 입력하세요 ▶▶ ", 10)
						,nextLong	   ("목표시간 입력하세요 ▶▶ ") * 60000
						,nextDouble    ("산소소비량을 입력해주세요 ▶▶ ")
						,nextDouble	   ("목표칼로리 소비량을 입력하세요 ▶▶ ")
						);
						plan = ex;
						break;
			case 2:
			Plan st = new Study(
						noEmptyLimitStr("계획이름 입력하세요 ▶▶ ", 10)
						,nextLong	   ("목표시간 입력하세요 ▶▶ ") * 60000
						,nextInt       ("목표 페이지 수를 입력하세요 ▶▶ ")
						);
						plan = st;
						break;
			case 3:
			Plan pl = new Plan(
						noEmptyLimitStr("계획이름 입력하세요 ▶▶ ", 10)
						,nextLong	   ("목표시간 입력하세요 ▶▶ ") * 60000
						);	
						plan = pl;
						break;
			default :
				System.out.println("정확한 번호를 입력해주세요.\n");
				continue;
			} return plan;
		}		
	}
		
		
	/*@author 박영준
	 * 방생성과 동시에 방 만든 회원의 소속정보가 소속클래스에 담김*/
	private void containBelongInfo() {
		int i = (belongs.size()==0 ? 0 : belongs.get(belongs.size()-1).getBelongCode());
		Calendar today = Calendar.getInstance();
		Belong memberBelong = new Belong(
				// 기본키 부여
				++i
				// 회원클래스 기본키 = 방만든 사람
				,tmpLoginMember.getId()
				//룸클래스 기본키 = 방 코드
				,i
				,"내가 만든 방!!"
				,1
				,today.getTimeInMillis()
		);					
		belongs.add(memberBelong);					
	}

	
	

	
	/*@author 박영준
	 * 자신이 만든 방*/
	private void checkOwnRoom() {
		for(int i = 0 ; i<rooms.size() ; i++) {
			if(tmpLoginMember.getId().equals(rooms.get(i).getHostId())) {
				System.out.println("방  코드 : " + rooms.get(i).getRoomCode() + " / 방  이름 : " + rooms.get(i).getRoomName());
				System.out.println("계획이름 : " + rooms.get(i).getPlan().getPlanName());
				System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			} 
		}
	}

	/*@author 박영준
	 * 자신이 가입한 다른사람의 방*/	
	private void checkFriendRoom() {
		for(int i = 0 ; i<belongs.size() ; i++) {
			for(int j = 0 ; j<rooms.size() ; j++) {				 
				if( inviteByFriend(i, 1) && getRoomOwner(i, j) && !(belongs.get(i).getId().equals(rooms.get(j).getHostId())) ) {	
					System.out.println("방  코드 : " + rooms.get(j).getRoomCode() + " / 방이름 : " + rooms.get(j).getRoomName());
					System.out.println("계획이름 : " + rooms.get(j).getPlan().getPlanName());
					System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
				}
			}
		} 
	}
	
	private void printRoom() {
		System.out.println("꙳✧˖°⌖꙳✧˖°⌖꙳✧˖° 내가  만든  방 ˖°⌖꙳✧˖°⌖꙳✧˖°");
		checkOwnRoom();
		System.out.println();
		
		System.out.println("꙳✧˖°⌖꙳✧˖°⌖꙳✧˖° 내가 참가한 방 ˖°⌖꙳✧˖°⌖꙳✧˖°");
		checkFriendRoom();
		
	}
	
	
	
	/*@author 박영준
	 *메뉴2번 ▶▶ 방 조회 및 입장*/
	private void enterRoom() {
		
		boolean flag = true;
		while(flag) {
				switch(nextInt("1.방에 입장하기 2.마이룸 메인으로")) {
				case 1:
					printRoom();
					inputRoomCode();
					break;
				case 2:
					flag = false;
					break;
				default :
					System.out.println("정확한 번호를 입력해주세요\n");
					break;
				}	
		}				
	}
	
	
	/*@author 박영준
	 * 방 입장하기*/
	public void inputRoomCode() {
		int enter = nextInt("입장할 방의 코드를 입력해주세요. ▶▶");
		Room r = findRoomBy(enter);
		if(r== null) {
			System.out.println("존재하지 않는 방입니다.");
			return;
		}
		for(int j = 0 ; j<belongs.size() ; j++) {
			
//			#Debug Code#
//			System.out.println(belongs.get(j).getRoomCode());
//			System.out.println(r.getRoomCode());
//			System.out.println(belongs.get(j).getId());
//			System.out.println(tmpLoginMember.getId());
//			System.out.println("=================================================");
			
			if(belongs.get(j).getRoomCode()==r.getRoomCode()&& belongs.get(j).getId().equals(tmpLoginMember.getId())) {
	 
				System.out.println("  Λ＿Λ\r\n" + 
								   "（ㆍωㆍ)つ━☆*。\r\n" + 
								   "⊂    ノ    .뾰\r\n" + 
								   " し-Ｊ   °。로 *´¨)\r\n" + 
								   "       .. .· ´¸.·롱*´¨) ¸.·*¨)\r\n" + 
								   "                 (¸.·´ ("+tmpLoginMember.getName()+"님이 입장하셨습니다.¸.'*\r\n" ); 
								   
			showRoomAscii(enter);
			return;
			} 
		}
		System.out.println("|존재하지 않거나 가입한 방이 아닙니다  |\r\n" + 
						   "￣￣￣￣∨￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣\r\n" + 
						   "　　  ∧_,,∧\r\n" + 
						   "　  (  ◞‸◟  )\r\n" + 
						   "　　　Ｕθ Ｕ\r\n" + 
						   "　／￣￣｜￣￣＼\r\n" + 
						   "|二二二二二二二|\r\n" + 
						   "｜　　 　　　 ｜\r\n" + 
						   "찰칵      찰칵   찰\r\n" + 
						   "　 ∧∧└　    ∧∧    칵\r\n" + 
						   "　(　　)】 (　　)】\r\n" + 
						   "　/　/┘　/　/┘\r\n" + 
						   "ノ￣ヽ　ノ￣ヽ{ 제대로 확인을 안했군요?!\r\n" + 
						   "");	
		return;	
	}
	
	
	/**
	 * @author 박영준,조윤정
	 * 방 구성원 출력
	 * 영준님께서 기능구현 하신 후에
	 * 아스키 아트에 맞는 형태로 수정했음*/
	public String printRoomMembers (int roomCode) {
		String str="";
		String print="";
		for(int i=0 ; i<belongs.size() ; i++) {
			str+=getBelongMember(roomCode, i);
		}
		String[] arr=str.trim().split("");
		for(int i=0;i<arr.length;i++) {
			print+=arr[i];
			if(i!=0&&i%39==0) {
				print+="|   |\r\n           |   |             ";
			}
		}
		for(int i=0;i<=39-(arr.length>40?(arr.length-1)%39:arr.length%39);i++) {
			print+=" ";
		}
		print+="|   |\r\n";
		return print;
	}
	

	/*@author 박영준
	 * 자신이 만든 방 있는지 체크 - 있다면 true 없으면 false */
	public boolean hasOwnRoom(String id) {
		for(int i = 0 ; i<rooms.size() ; i++) {
			if(id.equals(rooms.get(i).getHostId())) {
				return true;
			}
		}
		return false;
	}
	
	
	/*@author 박영준
	 * Belong code*/
	public Belong getBelong(int roomCode) {
		for(int i = 0 ; i<belongs.size() ; i++) {
			if(roomCode == belongs.get(i).getRoomCode()) {
				return belongs.get(i);
			}
		}
		return null;
	}
	
	/*@author 박영준
	 * Belong id*/
	public Belong getBelong(String memberId) {
		for(int i = 0 ; i<belongs.size() ; i++) {
			if(memberId.equals(belongs.get(i).getId())) {
				return belongs.get(i);
			}
		}
		return null;
	}
	
	/*@author 박영준
	 * 특정 방코드 입력했을 때 해당 코드를 기본키로 가지고 있는 관계정보에 자신의 아이디와 가입상태가 되어있는지 확인*/
	public boolean getMyBelong(int i, String id) {		
		if( id.equals(belongs.get(i).getId()) &&  (belongs.get(i).getStatus()==1) ) {
			return true;
		}
		return false;
	}
	
	/*@author 박영준
	 * 방에 가입한 회원들 출력 특정 룸코드를 가지고 있고 가입상태 1 인 사람들*/
	public String getBelongMember(int roomCode, int i) {
		String str="";
		if(roomCode==belongs.get(i).getRoomCode() && belongs.get(i).getStatus()==1) {
			str=String.format("%s ", belongs.get(i).getId());
//			System.out.printf("%8s ", belongs.get(i).getId());
		}
		return str;	
	}
	
	
	/*@author 박영준
	 * Room roomCode*/
	public Room getRoom(int code) {
		for(int i = 0 ; i<rooms.size() ; i++) {
			if(code == rooms.get(i).getRoomCode()) {
				return rooms.get(i);
			}
		}
		return null;
	}
	
	
	/*@author 박영준
	 * Room hostId*/
	public Room getRoom(String Id) {
		for(int i = 0 ; i<rooms.size() ; i++) {
			if(Id.equals(rooms.get(i).getHostId())) {
				return rooms.get(i);
			}
		}
		return null;
	}
	
	
	/*@author 박영준
	 * 초대할 아이디가 내 친구가 맞는지 확인 */
	private boolean checkMyFriend(String id) { 
			for(int i = 0 ; i<relationships.size() ; i++) {
				if((friendByMe(i, id) || friendByYou(i, id)) && relationships.get(i).getStatus()==1) {					
					return true;	
				}
			}
			return false;
		}
	/*@author 박영준
	 * 이 룸코드가 내가 만든 방의 코드인지 확인*/
	private boolean checkOwnRoomCode(int code) { 	
		for(int i = 0 ; i<rooms.size() ; i++) {
			if(getRoom(code).getHostId().equals(tmpLoginMember.getId())) {					
				return true;	
			}
		}
		return false;
	}
	
	
	
	/*@author 박영준
	 * 메뉴3번 ▶▶ 친구 방으로 초대하기*/
	private void inviteToRoom() {
		//자신이 만든방이 있어야 초대가능 (true, false)
		//있다면 자신이 만든 방 목록 보여주며 초대
		if(hasOwnRoom(tmpLoginMember.getId())) {
			
			checkOwnRoom();
			System.out.println();
			setUpInvitation();
						
		} else {
			System.out.println("생성하신 방이 없습니다\n먼저 방을 만들어주세요\n");
		}
	}
	
	/*@author 박영준
	 * 방 설정*/
	private void setUpInvitation () {		
		Calendar today = Calendar.getInstance();
		int i = (belongs.size()==0 ? 0 : belongs.get(belongs.size()-1).getBelongCode());
		boolean isCorrect = true;
		while(isCorrect) {			
			int roomCode = nextInt("친구를 초대할 자신의 방 코드를 입력해주세요. ▶▶");					
			String friendId = noEmptyLimitStr("방으로 초대할 친구의 아이디를 입력해주세요. ▶▶", 10);
			
			//  (입력한코드 = 자신이 만든 방의 코드) && (입력한 아이디 = 나랑 친구상태인 아이디)
			if(checkOwnRoomCode(roomCode) && checkMyFriend(friendId)) {									

				Belong memberBelong = new Belong(
						// 기본키 부여
						++i
						// 방으로 초대할 친구 아이디
						,friendId
						//룸클래스 기본키 = 방 코드					
						,roomCode
						//메시지
						,nextLine("친구에게 남길 메시지를 입력해주세요")
						//수락상태 -1: 거절, 0:대기중 1: 수락
						,0
						,today.getTimeInMillis()
				);					
				belongs.add(memberBelong);
				break;
				
			} else if( (checkOwnRoomCode(roomCode) == false) && checkMyFriend(friendId) == true ) {
				System.out.println("존재하지 않거나 다른 회원의 방 코드 입니다.");
				isCorrect = stopInvite();
								
			} else if( (checkOwnRoomCode(roomCode) == true) && checkMyFriend(friendId) == false ){
				System.out.println("존재하지 않거나 친구가 아닌 회원입니다.");
				isCorrect = stopInvite();
			} else {
				System.out.println("친구 아이디 와 방 코드 모두 틀렸습니다");
				isCorrect = stopInvite();
			}
		}
		
	}
	
	/*@author 박영준
	 * 초대 그만두기*/
	private boolean stopInvite() {
		boolean flag = true;
		while(true) {	
				switch(nextInt("재입력은 1번, 그만두려면 2번을 입력해주세요 ▶▶ ")) {
				case 1:
					break;
				case 2:
					flag = false;
					break;
				default :
					System.out.println("정확한 번호를 입력해주세요\n");
					continue;
				} return flag;	
			}			
		}

	/*@author 박영준
	 * 메뉴4번 ▶▶ 방 초대함*/
	private void invitationBox() {
		
		boolean flag = true;
		while(flag) {
				switch(nextInt("1.초대알림 2.가입하기 3.마이룸 메뉴")) {
				case 1:
					simpleCheck();
					checkMenu();
					break;
				case 2:
					joinRoom();
					flag = false;
					break;
				case 3:
					flag = false;
					break;
				default :
					System.out.println("정확한 번호를 입력해주세요\n");
				}			
		}				
	}
	
	/*@author 박영준
	 * 상세 조회*/
	private void checkMenu() {
		boolean flag = true;
		while(flag) {			
				switch(nextInt("1.상세보기 2.이전 메뉴")) {
				case 1:
					detailedCheck();
					break;
				case 2:
					flag =false;
					break;
				default :
					System.out.println("정확한 번호를 입력해주세요");
				}			
		}
	}			

	/*@author 박영준
	 * 상세 조회*/
	private void detailedCheck() {
		
		int code = nextInt("상세 조회할 방의 코드를 입력해주세요. ▶▶");
		if( !getBelong(code).getId().equals(tmpLoginMember.getId()) ) {
			System.out.println("회원님 초대된 방의 코드가 아닙니다.");
			return;
		}
		
		for(int i=0 ; i<belongs.size() ; i++) {
			for(int j=0 ; j<rooms.size() ; j++) {
				if( getRoomOwner(i, j, code) && inviteByFriend(i, 0)) {
					System.out.println("방  코드 : " + rooms.get(j).getRoomCode());
					System.out.println("방  이름 : " + rooms.get(j).getRoomName());
					System.out.println("공동목표 : " + rooms.get(j).getPlan().getPlanName());
					System.out.println("나를 초대한 친구   : " + rooms.get(j).getHostId());
					System.out.println("친구가 보낸 메세지 : ");
					msgAscii(belongs.get(i).getMsg());	
				}
			}
		}
	}
	
	
	/*@author 박영준
	 * 간단 조회*/
	private void simpleCheck() {
		
		if(inviteByFriend() == null) {
			System.out.println("┌───────────────────────┐\r\n" + 
							   " 방 가입 요청이 없습니다 \r\n" + 
							   "└───────────────────────┘\r\n" + 
							   "　　 ᕱ ᕱ   ||\r\n" + 
							   "　  ( ･ω･ )||\r\n" + 
							   "　  / 　つ Φ\r\n" ); 
			return;
		}
					System.out.println("┌─────────────────────┐\n"
									 + "│ 코드   친구   시간  │\n"
									 + "├─────────────────────┤");					
		for(int i=0 ; i<belongs.size() ; i++) {
			for(int j=0 ; j<rooms.size() ; j++) {			
				if( inviteByFriend(i, 0) && getRoomOwner(i, j)) {
					 System.out.printf("│ %4d  %4s   %-4s│\n",rooms.get(j).getRoomCode(), rooms.get(j).getHostId(), difference(belongs.get(i).getbDate()) );
				}			
			}
		} 
					System.out.println("└─────────────────────┘\r\n" + 
									   "　　 ᕱ ᕱ   ||\r\n" + 
									   "　  ( ･ω･ )||\r\n" + 
							           "　  / 　つ Φ\r\n" ); 
	}
	
	
	/*@author 박영준
	 * 방 가입하기*/
	public void joinRoom() {
		int roomCode = nextInt("가입할 방의 코드를 입력해주세요. ▶▶");
		for(int i=0 ; i<belongs.size() ; i++) {
			if(inviteByFriend(i, 0) && belongs.get(i).getRoomCode() == roomCode) {	
				belongs.get(i).setStatus(1);
				System.out.println("가입 성공!!");
			}
		}
		
	}

	
	/*@author 박영준
	 * 초대요청 받은 방*/
	public boolean inviteByFriend(int i, int status) {
		if( tmpLoginMember.getId().equals(belongs.get(i).getId()) && belongs.get(i).getStatus()==status) {
			return true;
		}
		return false;
	}
	
	/*@author 박영준
	 * 초대요청받은게 있다면 관계정보 반환*/
	public Belong inviteByFriend() {
		for(int i=0 ; i<belongs.size() ; i++) {
			if( tmpLoginMember.getId().equals(belongs.get(i).getId()) && belongs.get(i).getStatus()==0) {
				return belongs.get(i);
			}
		}
		return null;		
	}
	
	
	/*@author 박영준
	 * inviteByFriend 와 같이 사용*/
	public boolean getRoomOwner(int i, int j) {
		if( belongs.get(i).getRoomCode() == rooms.get(j).getRoomCode() ) {
			return true;
		}
		return false;
	}
	
	/*@author 박영준
	 * inviteByFriend 와 같이 사용*/
	public boolean getRoomOwner(int i, int j, int code) {
		if(code == belongs.get(i).getRoomCode() && code == rooms.get(j).getRoomCode() ) {
			return true;
		}
		return false;
	}

	
	/*@author 박영준
	 * 메뉴5번 ▶▶ 방 삭제 */		
	private void removeRoom() {
		checkOwnRoom();
		int roomCode = nextInt("삭제할 방의 코드를 입력해주세요. ▶▶");
		if(getRoom(roomCode).getHostId().equals(tmpLoginMember.getId()) == false) {
			System.out.println("존재하지 않거나 다른 회원이 만든 방입니다.");
			return;
		}
		
		boolean flag = true;
		while(flag) {
			switch(nextInt("정말로 삭제 하시겠습니까? 1.예 2.아니오")) {
			case 1:
				setBelong(roomCode);
				break;
			case 2:
				flag = false;
				break;
			default :
				System.out.println("정확한 번호를 입력해주세요.");
			}
		}								
	}
	
	/*@author 박영준
	 * 방삭제와 동시에 관계상태 0으로 변경*/
	private void setBelong(int code) {
		rooms.remove(code-1);
		for(int i=0 ; i<belongs.size() ; i++) {
			if(belongs.get(i).getRoomCode()==code) {
				belongs.get(i).setStatus(-1);
			}
		}
	}
	
	
		
	/*
	 * @author 안병수<br>
	 * 계획 메뉴
	 */
	public void planExe() throws Exception {
		while (true) {
			try {
				int input=nextInt(
						  "┏━━━━━━━━━━━━━━━━━━━━━━━┓\n" 
						+ "┃       계획 메뉴       ┃\n" 
						+ "┣━━━━━━━━━━━━━━━━━━━━━━━┫\n"
						+ "┃ 1. 계획 수행          ┃\n" 
						+ "┃ 2. 오늘의 계획 조회   ┃\n"
						+ "┃ 3. 계획 입력          ┃\n" 
						+ "┃ 4. 계획 삭제          ┃\n" 
						+ "┃ 5. 계획 수정          ┃\n" 
						+ "┃ 6. 메인으로           ┃\n" 
						+ "┗━━━━━━━━━━━━━━━━━━━━━━━┛\n");

				switch (input) {
				case 1:
					carryPlan();
					break;
				case 2:
					for(int i=0;i<plans.size();i++) {
						if(isMyPlan(i)) {
							planList(plans.get(i));
						}
					}
					break;
				case 3:
					addPlan();
					break;
				case 4:
					removePlan();
					break;
				case 5:
					planChange();
					break;
				case 6:
					return;

				default:
					System.out.println("정확한 번호를 입력해주세요\n");
					return;
				}
			} catch (NumberFormatException e) {
			}
		}
	} // planexe() 종료

	/**
	 * @author 안병수<br>
	 *         계획 변경
	 */
	public void planChange() {
		Calendar today=Calendar.getInstance();
		Plan changeP;
		for(int i=0;i<plans.size();i++) {
			if(isMyPlan(i)) {
				planList(plans.get(i));
			}
		}
		int planCode = nextInt("수정하고싶은 계획의 계획코드를 입력해주세요. ▶▶");
		changeP=findPlanBy(planCode);
		if(changeP==null||!isMyPlan(changeP)||!isDateOfPlan(changeP, today)) {
			System.out.println("오늘자 내 계획만 수정가능합니다.");
			return;
		}
		System.out.println(changeP.getPlanName()+"계획을 수정하시겠습니까?");
		int input=nextInt("(1) 예     (2) 아니오.");
		switch (input) {
		case 1:
			if (changeP instanceof Study) {
				int setTime=nextInt("목표시간을" + changeP.getTargetTime()/60000 + "분에서 몇 분으로 바꾸시겠습니까?")*60000;
				if(setTime<=0) {
					System.out.println("목표시간은 0보다 커야합니다.");
					return;
				}
				changeP.setTargetTime(setTime);
				int setPage=nextInt("목표페이지를" + ((Study)changeP).getTargetPage() + "페이지에서 몇 페이지로 바꾸시겠습니까?");
				if(setPage<=0) {
					System.out.println("목표페이지는 0보다 커야합니다.");
					return;
				}
				((Study)changeP).setTargetPage(setPage);
				if(changeP.getTargetTime()<=changeP.getAchievedTime()
						&&((Study) changeP).getTargetPage()<=((Study) changeP).getAchievedPage()) {
					changeP.setAchieved(true);
				}
			}
			else if (changeP instanceof Exercise) {
				int setTime=nextInt("목표시간을 " + changeP.getTargetTime()/60000 + "분에서 몇 분으로 바꾸시겠습니까?")*60000;
				if(setTime<=0) {
					System.out.println("목표시간은 0보다 커야합니다.");
					return;
				}
				changeP.setTargetTime(setTime);
				showMet();
				double targetMet=nextDouble("Met값을" + ((Exercise)changeP).getMet() + "에서 몇 Met으로 바꾸시겠습니까?");
				if(targetMet<=0) {
					System.out.println("Met값은 0보다 작을 수 없습니다.");
				}
				((Exercise)changeP).setMet(targetMet);
				double targetCal=nextDouble("목표 칼로리를" + ((Exercise)changeP).getTargetCal() + "칼로리에서 몇 칼로리로 바꾸시겠습니까?");
				if(targetCal<=0) {
					System.out.println("목표칼로리는 0보다 작을 수 없습니다.");
				}
				((Exercise)changeP).setTargetCal(targetCal);
				if(changeP.getTargetTime()<=changeP.getAchievedTime()
						&&((Exercise) changeP).getTargetCal()<=((Exercise) changeP).getAchievedCal()) {
					changeP.setAchieved(true);
				}
			} else {
				int setTime=nextInt("목표시간을" + changeP.getTargetTime()/60000 + "분에서 몇 분으로 바꾸시겠습니까?")*60000;
				if(setTime<=0) {
					System.out.println("목표시간은 0보다 커야합니다.");
				}	
				changeP.setTargetTime(setTime);	
				if(changeP.getTargetTime()<=changeP.getAchievedTime()) {
						changeP.setAchieved(true);
				}
				return;	
			}
			break;
			
		case 2:
			System.out.println("계획수정을 취소하셨습니다.");
			break;	

		default:
			System.out.println("번호를 잘못 입력하셨습니다.");
			break;
		}

	} // planchange()종료

	/*
	 * @author 안병수 <br> 
	 * utf-8에서의 표 공백 계산 메서드 <br> 
	 * 오른쪽 정렬해서 출력
	 */
	public String utf_8KorFrame(String Str, int Space) {
		List<String> text = new ArrayList<String>();
		int cnt = calcSpace(Str);
		String allStr = "";
		for (int i = 0; i < Space + Str.length() - cnt; i++) {
			text.add(" ");
		}
		text.add(Str);
		if ((Space)*3 < cnt) {
			for (int j = Space-3; j < text.size(); j++) {
				text.remove(j);
			}
			text.add("...");
		}
		allStr = text.toString().replaceAll("\\[|\\]", "").replaceAll(", ", "");
		return allStr;
	}

	/*
	 * @author 안병수 <br> 
	 * 한글일경우 cnt=cnt+3
	 * 그 외의 경우 cnt=cnt+2
	 */
	public int calcSpace(String text) {
		int cnt = 0;
		for (int i = 0; i < text.length(); i++) {
			if ('ㄱ' <= text.charAt(i) && text.charAt(i) <= '힣') {
				++cnt;
				++cnt;
				++cnt;
			} else {
				++cnt;
				++cnt;
			}
		}
		return cnt;
	}

	/**
	 * @author 안병수<br>
	 *         계획 실행
	 * @throws InterruptedException 
	 */
	public void carryPlan() throws InterruptedException {
		Plan doing;
		Calendar today=Calendar.getInstance();
		long time;
		for(int i=0;i<plans.size();i++) {
			if(isMyPlan(i)) {
				planList(plans.get(i));
			}
		}
		int input=nextInt("실행하시고자 하는 계획의 계획코드를 입력해주세요.  ▶▶");
		doing=findPlanBy(input);
		if(doing==null||!isMyPlan(doing)||!isDateOfPlan(doing, today)) {
			System.out.println("입력하신 계획코드와 일치하는 계획이 없습니다.");
			return;
		}
		System.out.println(doing.getPlanName() + "수행을 시작합니다.");
		asciiAni();
		System.out.println("\n실행이 끝나면 엔터를 눌러주세요.");
		if(doing instanceof Study) {
			time=stopWatch();
			String stop=nextLine("");
			if(!stop.equals("")) {
				System.out.println("아잇.. 엔터누르라니까.. 이번만 봐드립니다.");
			}
			int pages=nextInt("\n몇 페이지 공부했는지 적어주세요. ▶▶");
			System.out.println((time>=60000?time/60000+"분":time/1000+"초")+"동안 "+pages+"페이지만큼 공부하였습니다!");
			doing.setAchievedTime(time);
			((Study) doing).setAchievedPage(pages);
			if(doing.getAchievedTime()>=doing.getTargetTime()&&
					((Study) doing).getAchievedPage()>=((Study) doing).getTargetPage()) {
				doing.setAchieved(true);
			}
			return;
		}
		else if(doing instanceof Exercise) {
			time=stopWatch();
			String stop=nextLine("");
			if(!stop.equals("")) {
				System.out.println("아잇.. 엔터누르라니까.. 이번만 봐드립니다.");
			}
			doing.setAchievedTime(time);
			((Exercise) doing).setAchievedCal(calculate((Exercise) doing));
			System.out.println((time>=60000?time/60000+"분":time/1000+"초")+"동안 "+calculate((Exercise) doing)+"칼로리를 소모하였습니다!");
			if(doing.getAchievedTime()>=doing.getTargetTime()&&
					((Exercise) doing).getAchievedCal()>=((Exercise) doing).getTargetCal()) {
					doing.setAchieved(true);
			}
			return;
		}
		else {
			time=stopWatch();
			String stop=nextLine("");
			if(!stop.equals("")) {
				System.out.println("아잇.. 엔터누르라니까.. 이번만 봐드립니다.");
			}
			doing.setAchievedTime(time);
			System.out.println((time>=60000?time/60000+"분":time/1000+"초")+"동안 계획을 수행하셨습니다!");
			if(doing.getAchievedTime()>=doing.getTargetTime()) {
				doing.setAchieved(true);
			}
			return;
		}
	} // carryPlan() 종료

	
	public Plan findPlanBy(int i) {
		for(Plan p:plans) {
			if(p.getPlanCode()==i) {
				return p;
			}
		}
		return null;
	}


	/**
	 * @author 안병수<br>
	 *         계획 제거
	 */
	public void removePlan() {
		Plan rePlan;
		Calendar today=Calendar.getInstance();
		for(int i=0;i<plans.size();i++) {
			if(isMyPlan(i)) {
				planList(plans.get(i));
			}
		}
		int rP = nextInt("지울 계획의 번호를 입력해 주세요.");
		rePlan=findPlanBy(rP);
		if (rePlan!= null&&isMyPlan(rePlan)&&isDateOfPlan(rePlan, today)) {
			plans.remove(rePlan);
			System.out.println("해당 계획을 삭제했습니다.");
		}
		else {
			System.out.println("내가 가진 오늘 날짜의 계획만 삭제가능합니다.");
		}
	} // 계획 제거 종료

	/**
	 * @author 안병수<br>
	 *         어떤 값을 읽을때까지 대기하는 메서드
	 */
	public void pause() {
		try {
			System.in.read();
		} catch (IOException e) {
		}
	} // pause() 종료

	/**
	 * @author 안병수<br>
	 *         나중시간(엔터칠 때 까지의 시간)- 시작시간 타임워치<br>
	 *         시간계산하는 메서드(분)
	 */
	public long stopWatch() {
		long time = System.currentTimeMillis();
		pause();
		return (System.currentTimeMillis() - time);
	} // stopWatch() 종료


} //DiaryServiceImpl클래스 종료

