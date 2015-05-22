package ro.pub.cs.systems.pdsd.practicaltest02var05;

public class Value {

	private String v;
	private int timestamp;
	
	public Value(){
		this.v = "";
		this.timestamp = 0;
	}
	
	
	public Value(String value, int timestamp){
		this.v = value;
		this.timestamp = timestamp;
	}


	public String getV() {
		return v;
	}


	public void setV(String v) {
		this.v = v;
	}


	public int getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}


	@Override
	public String toString() {
		return "Value [v=" + v + ", timestamp=" + timestamp + "]";
	}
	
	
}
