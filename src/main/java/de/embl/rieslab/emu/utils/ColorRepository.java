package de.embl.rieslab.emu.utils;

import java.awt.Color;

public class ColorRepository {

	public static String strblack = "black";
	public static String strgray = "gray";
	public static String strblue = "blue";
	public static String strgreen = "green";
	public static String strred = "red";
	public static String strorange = "orange";
	public static String strwhite = "white";
	
	public static String strdarkblue = "dark blue";
	public static Color darkblue = new Color(21, 68, 150);

	public static String strdarkgreen = "dark green";
	public static Color darkgreen = new Color(32, 140, 77);

	public static String strdarkred = "dark red";
	public static Color darkred = new Color(132, 27, 27);
	
	public static String strdarkorange = "dark orange";
	public static Color darkorange = new Color(153, 99, 30);
	
	public static String strviolet = "violet";
	public static Color violet = new Color(164, 110, 234);
	public static String strdarkviolet = "dark violet";
	public static Color darkviolet = new Color(116, 67, 178);
	
	private static String[] colors = {strblack,strwhite,strgray,strblue,strdarkblue,strgreen,strdarkgreen,strred,strdarkred,strorange,strdarkorange,strviolet,strdarkviolet};
	
	public static Color getColor(String colorname){
		if(colorname.equals(strblue)){
			return Color.blue;
		} else if(colorname.equals(strdarkblue)){
			return darkblue;
		} else if(colorname.equals(strgreen)){
			return Color.green;
		} else if(colorname.equals(strdarkgreen)){
			return darkgreen;
		} else if(colorname.equals(strgray)){
			return Color.gray;
		} else if(colorname.equals(strdarkred)){
			return darkred;
		} else if(colorname.equals(strdarkorange)){
			return darkorange;
		} else if(colorname.equals(strorange)){
			return Color.orange;
		} else if(colorname.equals(strviolet)){
			return violet;
		} else if(colorname.equals(strdarkviolet)){
			return darkviolet;
		} else if(colorname.equals(strred)){
			return Color.red;
		} else if(colorname.equals(strwhite)){
			return Color.white;
		} else {
			return Color.black;
		}
	}
	
	public static String[] getColors(){
		return colors;
	}
	
	public static String getColorsInOneColumn(){
		String s = colors[0];
		for(int i=1;i<colors.length;i++){
			s += "\n"+colors[i];
		}
		return s;
	}
	
	public static String getStringColor(Color c){
		for(int i=0;i<colors.length;i++){
			if(c.getRGB() == getColor(colors[i]).getRGB()){
				return colors[i];
			}
		}
		return strblack;
	}
}
