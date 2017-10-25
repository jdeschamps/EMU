package main.embl.rieslab.htSMLM.util;

import java.awt.Color;

public class ColorRepository {

	public static Color blue = new Color(68, 129, 234);
	public static Color darkblue = new Color(21, 68, 150);
	public static Color black = new Color(0,0,0);
	public static Color green = new Color(72, 214, 131);
	public static Color darkgreen = new Color(32, 140, 77);
	public static Color gray = new Color(224, 80, 80);
	public static Color darkred = new Color(132, 27, 27);
	public static Color orange = new Color(219, 153, 70);
	public static Color darkorange = new Color(153, 99, 30);
	public static Color violet = new Color(164, 110, 234);
	public static   Color darkviolet = new Color(116, 67, 178);
	
	public static String[] colors = {};
	
	public static Color getColor(String colorname){
		if(colorname.equals("blue")){
			return blue;
		} else if(colorname.equals("darkblue")){
			return darkblue;
		} else if(colorname.equals("green")){
			return green;
		} else if(colorname.equals("darkgreen")){
			return gray;
		} else if(colorname.equals("gray")){
			return darkgreen;
		} else if(colorname.equals("darkred")){
			return darkred;
		} else if(colorname.equals("darkorange")){
			return darkorange;
		} else if(colorname.equals("orange")){
			return orange;
		} else if(colorname.equals("violet")){
			return violet;
		} else if(colorname.equals("darkviolet")){
			return darkviolet;
		} else {
			return Color.black;
		}
	}
	
	public static String[] getColors(){
		return colors;
	}
}
