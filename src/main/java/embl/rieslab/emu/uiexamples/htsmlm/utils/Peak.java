package main.java.embl.rieslab.emu.uiexamples.htsmlm.utils;

public class Peak {
	private int x_,y_,value_;
	
	public Peak(int x, int y, int value){
		x_ = x;
		y_ = y;
		value_ = value;
	}
	
	public void set(int x, int y, int value){
		x_ = x;
		y_ = y;
		value_ = value;
	}

	public int getX(){
		return x_;
	}
	
	public int getY(){
		return y_;
	}
	
	public int getValue(){
		return value_;
	}

	public void print(){
		System.out.println("["+x_+","+y_+","+value_+"]");
	}
	
	public String toString(){
		String s = "["+x_+","+y_+","+value_+"]";
		return s;
	}
	
}
