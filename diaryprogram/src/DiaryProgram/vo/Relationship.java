package DiaryProgram.vo;


public class Relationship {
	
	private int relationNumber;      // 나중에 손쉽게 조회 등등을 하기 위해서 번호부여
	private String requestorId;      // 친구신청자의 아이디
	private String respondentId;     // 응답자의 아이디
	private String msg;              // 친구 요청 시 할 말
	private int status;              // 이 요청의 상태 -1: 거절,0:응답없음,1: 수락
	private long rDate;              // 친구 요청한 날짜

	
//============getters and setters============
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRelationNumber() {
		return relationNumber;
	}

	public void setRelationNumber(int relationNumber) {
		this.relationNumber = relationNumber;
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestor) {
		this.requestorId = requestor;
	}

	public String getRespondentId() {
		return respondentId;
	}

	public void setRespondentId(String respondent) {
		this.respondentId = respondent;
	}
	
	public long getrDate() {
		return rDate;
	}

	public void setrDate(long rDate) {
		this.rDate = rDate;
	}
	
//===================생성자=========================	
	
	public Relationship() {}
	
	public Relationship(int relationNumber,String requestorId, String respondentId, String msg, int status,long rDate) {
		this.relationNumber=relationNumber;
		this.requestorId=requestorId;
		this.respondentId=respondentId;
		this.msg = msg;
		this.status = status;
		this.rDate=rDate;
	}	
}
