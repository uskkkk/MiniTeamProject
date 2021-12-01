package DiaryProgram.vo;

import java.util.Calendar;

public class Exercise extends Plan{

//========== 필드 ==========
	private double met;              // 운동할 때 소모되는 산소량 (공식에 활용예정)
	private double targetCal;        // 목표 칼로리 소비량
	private double achievedCal;      // 달성한 칼로리 소비량
	
//========== getter and setter ==========
	public double getTargetCal() {
		return targetCal;
	}
	public void setTargetCal(double targetCal) {
		this.targetCal = targetCal;
	}
	public double getMet() {
		return met;
	}
	public void setMet(double Met) {
		this.met = Met;
	}
	public double getAchievedCal() {
		return achievedCal;
	}
	public void setAchievedCal(double archievedCal) {
		this.achievedCal += archievedCal;
	}

//========== 생성자 ==========
	public Exercise() {}
	
	public Exercise(double achievedCal) {
		this.achievedCal = achievedCal;
	}
	
	public Exercise(double targetCal, double met) {
		this.targetCal = targetCal;
		this.met =met;
	}
	
	public Exercise(double targetCal, double met, double achievedCal) {
		this.targetCal = targetCal;
		this.met = met;
		this.achievedCal = achievedCal;
	}
	
	public Exercise(String planName,long targetTime,double met,double targetCal) {
		super(planName,targetTime);
		this.met=met;
		this.targetCal=targetCal;
	}
	
	public Exercise(int planCode,String planName,long targetTime,long achievedTime,String memberId,Calendar planDate,boolean isAchieved,double met,double targetCal,double achievedCal) {
		super(planCode,planName,targetTime,achievedTime,memberId,planDate,isAchieved);
		this.met = met;
		this.targetCal = targetCal;
		this.achievedCal = achievedCal;
	}
	
	//========== equals ==========
	
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Exercise other = (Exercise) obj;
			if (Double.doubleToLongBits(met) != Double.doubleToLongBits(other.met))
				return false;
			if (Double.doubleToLongBits(targetCal) != Double.doubleToLongBits(other.targetCal))
				return false;
			return true;
		}
		
	//========== toString ============
		@Override
		public String toString() {
			String str=String.format("계획명:: %s , 목표시간:: %d%s , 달성시간:: %d%s , 목표칼로리:: %d , 달성칼로리:: %d  %s",super.getPlanName(),
					(super.getTargetTime()/60000>=60?super.getTargetTime()/3600000:super.getTargetTime()/60000),(super.getTargetTime()/60000>=60?"시간":"분"),
					(super.getAchievedTime()/60000>=60?super.getAchievedTime()/3600000:super.getAchievedTime()/60000),(super.getAchievedTime()/60000>=60?"시간":"분"),
					(int)targetCal,(int)achievedCal,(super.isAchieved()?"♥ 완료 ♥":"힘내세요T_T"));
			return str;
		}
		
		public String rankString(int i) {
			String str=String.format("%d등  ID :: %s , 달성칼로리:: %d", i+1, super.getMemberId(), (int)achievedCal);
			return str;
		}
		
		
	//========== Comparable ============
		@Override
		public int compareTo(Plan o) {
			return -(Double.compare(achievedCal, ((Exercise)o).achievedCal));
		}	
		
		
		
	
}
