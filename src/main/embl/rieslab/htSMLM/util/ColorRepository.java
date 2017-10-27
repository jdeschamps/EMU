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
	public static Color darkviolet = new Color(116, 67, 178);
	public static String strblue = "blue";
	public static String strdarkblue = "darkblue";
	public static String strblack = "black";
	public static String strgreen = "green";
	public static String strdarkgreen = "darkgreen";
	public static String strgray = "gray";
	public static String strdarkred = "darkred";
	public static String strorange = "orange";
	public static String strdarkorange = "darkorange";
	public static String strviolet = "violet";
	public static String strdarkviolet = "darkviolet";
	
	private static String[] colors = {strblack,strblue,strdarkblue,strgreen,strdarkgreen,strgray,strdarkred,strorange,strdarkorange,strviolet,strdarkviolet};
	
	public static Color getColor(String colorname){
		if(colorname.equals(strblue)){
			return blue;
		} else if(colorname.equals(strdarkblue)){
			return darkblue;
		} else if(colorname.equals(strgreen)){
			return green;
		} else if(colorname.equals(strdarkgreen)){
			return gray;
		} else if(colorname.equals(strgray)){
			return darkgreen;
		} else if(colorname.equals(strdarkred)){
			return darkred;
		} else if(colorname.equals(strdarkorange)){
			return darkorange;
		} else if(colorname.equals(strorange)){
			return orange;
		} else if(colorname.equals(strviolet)){
			return violet;
		} else if(colorname.equals(strdarkviolet)){
			return darkviolet;
		} else {
			return Color.black;
		}
	}
	
	public static String[] getColors(){
		return colors;
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
