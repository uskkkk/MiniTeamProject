package DiaryProgram.vo;

public class Belong {
	private int belongCode;      //나중에 손쉽게 조회 등등을 하기 위해서 번호부여(기본키)
	private String id;           //회원클래스의 기본키
	private int roomCode;        //룸클래스의 기본키
	private String msg;          //방 초대보낼 때 혹은 방 삭제할 때 사용자에게 보낼 메시지
	private int status;          //0: 방초대응답없음 1:수락 -1:거절 
	private long bDate;
//========== getter and setter ============


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }
    
	public int getBelongCode() {
		return belongCode;
	}

	public void setBelongCode(int belongCode) {
		this.belongCode = belongCode;
	}
	
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
	
	public long getbDate() {
		return bDate;
	}
	
	public void setbDate(long bDate) {
		this.bDate = bDate;
	}
	

//========== 생성자 ==========


	public Belong() {}

    public Belong(int belongCode,String id,int roomCode,String msg,int status,long bDate) {
        this.belongCode=belongCode;
        this.id=id;
        this.roomCode=roomCode;
        this.msg=msg;
        this.status=status;
        this.bDate = bDate;
    }


}
