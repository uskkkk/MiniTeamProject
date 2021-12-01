package DiaryProgram.vo;



public class Room {
//========== 필드 ===========
	
	private int roomCode;								// 방 번호
	private String roomName;							// 방 이름
	private String hostId;                              // 방 생성한 사람의 아이디
	private Plan plan;									// 공동 목표
	
//========== getter and setter ===========

	public int getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(int roomCode) {
		this.roomCode = roomCode;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	
	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	

//=========== 생성자 =============
	
	public Room() {}
	
	public Room(int roomCode, String roomName, String hostId, Plan plan) {
		this.roomCode = roomCode;
		this.roomName = roomName;
		this.hostId=hostId;
		this.plan = plan;
	}



}
