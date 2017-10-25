package main.embl.rieslab.htSMLM.ui.uiparameters;

import java.awt.Color;

public class ColorUIParameter extends UIParameter<Color>{
	
	public static String blue = "blue";
	public static String cyan = "cyan";
	public static String darkGray = "darkGray";
	public static String gray = "gray";
	public static String green = "green";
	public static String lightGray = "lightGray";
	public static String magenta = "magenta";
	public static String orange = "orange";
	public static String pink = "pink";
	public static String red = "red";
	public static String white = "white";
	public static String yellow = "yellow";
	public static String black = "black";
	
	public ColorUIParameter(String name, String description, Color value) {
		super(name, description);
		setValue(value);
	}
	
	@Override
	public void setType() {
		type_ = UIParameterType.COLOUR;
	}

	@Override
	public boolean isSuitable(String val) {
		return true;
	}

	@Override
	protected Color convertValue(String val) {
		return getColor(val);
	}
	
	public static Color getColor(String colorname){
		if(colorname.equals("blue")){
			return Color.blue;
		} else if(colorname.equals("cyan")){
			return Color.cyan;
		} else if(colorname.equals("darkGray")){
			return Color.darkGray;
		} else if(colorname.equals("gray")){
			return Color.gray;
		} else if(colorname.equals("green")){
			return Color.green;
		} else if(colorname.equals("lightGray")){
			return Color.lightGray;
		} else if(colorname.equals("magenta")){
			return Color.magenta;
		} else if(colorname.equals("orange")){
			return Color.orange;
		} else if(colorname.equals("pink")){
			return Color.pink;
		} else if(colorname.equals("red")){
			return Color.red;
		} else if(colorname.equals("white")){
			return Color.white;
		} else if(colorname.equals("yellow")){
			return Color.yellow;
		} else {
			return Color.black;
		}
	}
	
	public static String[] getColors(){
		String[] colors = new String[13];
		
		colors[0] = blue;
		colors[1] = cyan;
		colors[2] = darkGray;
		colors[3] = gray;
		colors[4] = green;
		colors[5] = lightGray;
		colors[6] = magenta;
		colors[7] = orange;
		colors[8] = pink;
		colors[9] = red;
		colors[10] = white;
		colors[11] = yellow;
		colors[12] = black;
		
		return colors;
	}
}
