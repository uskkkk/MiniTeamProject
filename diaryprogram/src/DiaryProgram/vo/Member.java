package DiaryProgram.vo;


public class Member {
//========== 필드 ==========
	

	private String id;         // 회원아이디
	private String password;   // 회원 비밀번호
	private String name;       // 회원 이름
	private double height;     // 회원 키
	private double weight;     // 회원 몸무게 
	private String emailAdr;   // 회원 이메일 주소

		
//========== getter and setter ========== 
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}
	

	public void setPassword(String password) {
		this.password = password;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



	public double getHeight() {
		return height;
	}


	public void setHeight(double height) {
		this.height = height;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public String getEmailAdr() {
		return emailAdr;
	}

	public void setEmailAdr(String emailAdr) {
		this.emailAdr = emailAdr;
	}


//========== 생성자 ==========

	public Member() {}
		
	
	

	public Member(String id, String password, String name, double height, double weight, String emailAdr) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.height = height;
		this.weight = weight;
		this.emailAdr=emailAdr;
	}

}