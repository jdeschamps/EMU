# EMU tutorial: building a UI

This tutorial is a simple example walk-through of how to build a plugin for EMU, in order to obtain a re-configurable user interface within Micro-Manager. 

Here is a preview of the UI:

<p align="center">
<img src="images/0-Final.PNG">
</p>

#### Prerequisite

- [Eclipse](https://www.eclipse.org/) with the [WindowBuilder plugin]() running java 8.
- Micro-Manager 2gamma installed.
- EMU installed (refer to the [installing section](https://github.com/jdeschamps/EMU)).



#### During the tutorial

- You can consult the [guide]() for more in-depth details.
- The [source code]() can be found in the "source" folder.



## A - Making sure that eclipse runs java 8

**1)** In Eclipse, go to "**Window -\> Preferences -\> Java -\> Installed JREs**" and make sure that java 1.8 or 8 is known and is the default. 



## B - Setting up the eclipse project

 **2)** Create your own project: in the "**Package Explorer**" window, right-click and create a new "**Java Project**".

<p align="center">
<img src="images/C-1_new_project.png">
</p>



**3)** In the pop-up window, set the name of your project (e.g. "MyUI"). Make sure that Java 8 (or 1.8) is selected in the **JRE** section. Click **next**.



**4)** In the "**Librairies**" tab, click on "Add External JARs" and navigate to your Micro-Manager folder and then to the "**mmplugins**" subfolder. Select the EMU jar. Click **finish**.

> Note: If the jar is not present in the folder, then EMU was not properly installed (refer to the [EMU repository]( https://github.com/jdeschamps/EMU)).



**5)** Right click on the project and create a new "**package**". By convention, package names start with "main.java" and followed by reverse domain name. e.g. main.java.com.myname.myui.

<p align="center">
<img src="images/C-2_package.png">
</p>



## C - Setting up the laser panel: ConfigurablePanel

In this section, we will encounter the first EMU class: [ConfigurablePanel](). ConfigurablePanels are the building blocks of an EMU UI. The important steps before building a ConfigurablePanel are:

- What will the panel look like? (what [JComponents](), in which order)
- What configurable [parameters]() do we want? (title, title color, names...etc...)
- What [properties]() should be modified by the panel? (e.g. a laser on/off)

In our case, here we want:

- JComponents: a JToggleButton for on/off and a JSlider to set the power percentage.

- UIParameters: name and color of the laser.

- UIProperties: laser on/off and laser power percentage.

  

**6)** Right click on your package (here "main.java.com.myname.myui") and select "**new -\> other**". In the pop-up, select "**WindowBuilder/Swing Designer/JPanel**". Click **next**.

> Note: If WindowBuilder or Swing Designer do not appear, then refer to the prerequisite section and follow the link to install the WindowBuilder. 



**7)** Name the JPanel, e.g. "*LaserPanel*". In the "**superclass**" section, search for "**ConfigurablePanel**". Click **finish**. The LaserPanel should now appear in the package explorer.

> Note:  If ConfigurablePanel does not appear, then EMU was not properly imported (refer to **4**). 



**8)** Delete the LaserPanel() constructor, you "LaserPanel.java" should then look like the following:

```java
package main.java.com.myname.myui;

import javax.swing.JPanel;
import de.embl.rieslab.emu.ui.ConfigurablePanel;

public class LaserPanel extends ConfigurablePanel {

}
```



**9)** Place your cursor on the "LaserPanel" class name and select "**Add constructor**":

<p align="center">
<img src="images/E-1_suggestion.PNG">
</p>

> Note: If Eclipse does not suggest anything when the mouse is placed on the error, then look at the preferences in "**Window/Preferences**" in the section "**Java/Editor/ContentAssist**".



Eclipse should generate a new Constructor with a String parameter. This parameter will ultimately be the name of the ConfigurablePanel.



**10)** The class name should still show an error. Place the cursor on the class name again, and select "**Add unimplemented methods**".



**11)** In the bottom of the Java editor, a tab allows you to visit the WindowBuilder designer. Click on "design".

<p align="center">
<img src="images/E-2_design.PNG">

**12)** We are now going to use the drag and drop tools to build the laser panel. Here is an animated gif of the different steps:

<p align="center">
<img src="images/LaserPanel.GIF">

1. In the **Layouts** section of the Palette, select "AbsoluteLayout" and click on the panel.

2. Click on the panel, in the **Properties**, select **border** and, in the pop-up window, choose "TitledBorder". Enter the title and choose the left justification.

3. Place a **JLabel** (from the palette). Enter a placeholder text (such as "70%"). In the **Properties**, choose a CENTER **horizontalAlignment**. Click on the **font** property, increase the font to 12 and make it bold.

4. Place a **JSlider** on the panel. In the **Properties**, choose a VERTICAL **orientation**. Then check **paintLabels** and **paintTicks**. Finally sets the **majorTickSpacing** to 20 (note that the minimum is 0 and the maximum 100).

5. Place a **JToggleButton** at the bottom of the panel and enter its text (e.g.: "On/Off"). Resize the font as previously.

6. Resize all three components and the panel to make it look aesthetically pleasant.

   

**13)** For each component (JLabel, JSlider and JToggleButton), right click on it in the **Structure** tab and select "**Expose component**". Select **protected** and click **OK**.



We will now implement the empty methods created earlier.

**14)** Go back to the java editor by clicking on the **Source** tab.

**15)** The method **getDescription()** returns a String describing the what the panel is intended for. Replace the "**null**" in the return statement:

```java
@Override
public String getDescription() {
	return "Panel controlling the power percentage and operation of a single laser.";
}
```

**16)** Let's create the two properties. First we create two convenience variables:

```java
public class LaserPanel extends ConfigurablePanel {
	private JLabel label_1;
	private JSlider slider;
	private JToggleButton tglbtnOnoff;
	
    //////// Properties
    public final static String LASER_PERCENTAGE = "power percentage";
    public final static String LASER_OPERATION = "enable";
    
    [...]
```

Then in the initializeProperties() method, we declare the two properties:

```java
	@Override
	protected void initializeProperties() {
		String text1 = "Property changing the power percentage of the laser.";
		String text2 = "Property turning the laser on/off.";
        String propertyPercentage = getPanelLabel() + " " + LASER_PERCENTAGE;
        String propertyOperation = getPanelLabel() + " " + LASER_OPERATION;
		
		addUIProperty(new UIProperty(this, propertyName1, text1, new NoFlag()));
		addUIProperty(new TwoStateUIProperty(this, propertyName2, text2, new NoFlag()));
	}
```

Use again Eclipse suggestions to import the UIProperty, TwoStateUIProperty and NoFlag classes.

**17)** Now that the UIProperties have been declared, let's implement the effect of the user interactions with the components on the properties. We are going to use static methods made available by EMU. In the addComponentListeners() method:

```java
@Override
protected void addComponentListeners() {
	String propertyName1 = getPanelLabel() + " " + LASER_PERCENTAGE;
    String propertyName2 = getPanelLabel() + " " + LASER_OPERATION;
    
	SwingUIListeners.addActionListenerOnIntegerValue(this, propertyName1, slider, 
                                                     label_1, "", "%");
    
	try {
		SwingUIListeners.addActionListenerToTwoState(this, propertyName2, tglbtnOnoff);
	} catch (IncorrectUIPropertyTypeException e) {
		e.printStackTrace();
	}
}
```
As done previously, use the Eclipse suggestions to import the missing class. SwingUIListeners offers a bunch of methods to simplify writing the action listeners in EMU. The first method takes the value of JSlider and sets the value of the UIProperty. In addition, it also change the text of the JLabel (adding an empty String to the beginning of the text and "%" to the end, as specified in the method's call). The second method expects a TwoStateUIProperty (hence the try/catch clause). When the JToggleButton is selected, the TwoStateUIProperty will be set to its on state and inversely when the JToggleButton is unselected.

**18)**  The last step that concerns the properties is to define what happens when a UIProperty changes. Let's move to the propertyhasChanged(...) method:

```java
@Override
protected void propertyhasChanged(String propertyName, String newvalue) {
	String propertyName1 = getPanelLabel() + " " + LASER_PERCENTAGE;
    String propertyName2 = getPanelLabel() + " " + LASER_OPERATION;
		
	if (propertyName.equals(propertyName1)) {
		if (EmuUtils.isNumeric(newvalue)) {
			int val = (int) Double.parseDouble(newvalue);

			if (val >= 0 && val <= 100) {
				slider.setValue(val);
				label_1.setText(String.valueOf(val) + "%");
			}
		}
	} else if (propertyName.equals(propertyName2)) {
		try {
			String onValue = ((TwoStateUIProperty) getUIProperty(propertyName2))
                .getOnStateValue();
			tglbtnOnoff.setSelected(newvalue.equals(onValue));
		} catch (UnknownUIPropertyException e) {
			e.printStackTrace();
		}		
    }
}
```

Here, we first check which property has changed. If the percentage property has changed, we make sure that the String a number, then we round it up to an Integer. If the value is a percentage, we update the JSlider and the JLabel. If the propery is the laser on/off, we retrieve the "ON state" of the TwoStateUIProperty (which, depending on the laser, can be "enable" or "1" or "on"). Then depending on whether the new value corresponds to the "ON state" or not, we update the JToggleButton.

Now that the UIProperties have been tackled, let's turn our attention to the UIParameters.

**19)** We want two UIParameters: the panel title (TitledBorder's title) and its color. As for the properties, let's define some convenience variables:

```java
public class LaserPanel extends ConfigurablePanel {
	private JLabel label_1;
	private JSlider slider;
	private JToggleButton tglbtnOnoff;
	
	//////// Parameters
	public final static String PARAM_TITLE = "Name";
	public final static String PARAM_COLOR = "Color";	
    
    [...]
```

Then in initializeParameters(), we declare the UIParameters:

```java
@Override
protected void initializeParameters() {
	addUIParameter(new StringUIParameter(this, PARAM_TITLE, "Panel title.",
                                         getPanelLabel()));
	addUIParameter(new ColorUIParameter(this, PARAM_COLOR, "Laser color.", Color.black));
}
```

Import the missing classes using Eclipse suggestions. We declared two parameters: a String for the name and a Color. Both UIParameters require a default value: we use here the ConfigurablePanel label (using the getPanelLabel() method) and the black color (Color.black).



**20)** Let's now define what happens when the UIParameters change. This happens when starting EMU or modifying the configuration. 

```java
@Override
protected void parameterhasChanged(String parameterName) {
	if (parameterName.equals(PARAM_TITLE)) {
		try {
            ((TitledBorder) this.getBorder())
                	.setTitle(getStringUIParameterValue(PARAM_TITLE));
			this.repaint();
		} catch (IncorrectUIParameterTypeException | UnknownUIParameterException e) {
			e.printStackTrace();			
        }
    } else if (parameterName.equals(PARAM_COLOR)) {
		try {
			((TitledBorder) this.getBorder())
               	.setTitleColor(getColorUIParameterValue(PARAM_COLOR));
			this.repaint();
		} catch (IncorrectUIParameterTypeException | UnknownUIParameterException e) {
			e.printStackTrace();
		}
	}
}
```

In both cases, we retrieve the TitledBorder of the panel. We then change either its title or its color. The try/catch clauses are here in case the parameterName is wrong or of the wrong type (String or Color).

We have now a fully functional EMU ConfigurablePanel with two UIProperties and two UIParameters.



## D - Setting up the filters panel

This time we want:

- JComponents: a ButtonGroup with six JToggleButtons (button group means only one button can be selected at a time, just like in a filter wheel).

- UIParameters: name and color of the six filters.

- UIProperties: filter wheel position (with value 1 to 6).

**21)** Repeat steps **6** to **10**, albeit with a "FiltersPanel" class.

**22)** Design the ConfigurablePanel:

<p align="center">
<img src="images/FiltersPanel.GIF">

1. In the **Layouts** section of the Palette, select "GridLayout" and click on the panel.
2. Click on the panel, in the **Properties**, select **border** and, in the pop-up window, choose "TitledBorder". Enter the title and choose the left justification.
3. Place a **JToggleButton** (from the palette) on the panel. Enter a placeholder text (such as "Filter 1"). 
4. Repeat by adding **JToggleButtons** on the right of the panel.
5. Select all **JToggleButtons** in the Components section, and **show advanced properties** in the Properties section (upper right button in the same section). Go to **margin**, set the left and right margin to 2 pixels.
6. With all the **JToggleButtons** still selected, right-click on them and set a **new standard ButtonGroup**.
7. Finally, right-click again and **expose the components** as protected elements.
8. **Resize** the panel to make it look aesthetically pleasant.



**23)** Let's now create the UIProperties and UIParameters: 

```java
public class FiltersPanel extends ConfigurablePanel {
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JToggleButton tglbtnFilter;
	private JToggleButton tglbtnFilter_2;
	private JToggleButton tglbtnFilter_1;
	private JToggleButton tglbtnFilter_5;
	private JToggleButton tglbtnFilter_4;
	private JToggleButton tglbtnFilter_3;

	//////// Properties
	public static String FW_POSITION = "Filterwheel position";
	
	//////// Parameters
	public final static String PARAM_NAMES = "Filter names";
	public final static String PARAM_COLORS = "Filter colors";
	
	//////// Initial parameter
	public final static int NUM_POS = 6;
    
    [...]
```

We declare a MultiStateUIProperty with 6 positions (one per filter):

```java
@Override
protected void initializeProperties() {
	String description = "Filter wheel position property.";
	addUIProperty(new MultiStateUIProperty(this, FW_POSITION, description, new NoFlag(), 
                                           NUM_POS));				
}
```

Such UIProperty can only take one of those 6 positions. We also define what happens when the MultiStateUIProperty changes:

```java
	@Override
	protected void propertyhasChanged(String propertyName, String newvalue) {
		if(propertyName.equals(FW_POSITION)){
			int pos;
			try {
				pos = ((MultiStateUIProperty) getUIProperty(FW_POSITION)).getStatePositionNumber(newvalue);
			
				if(pos == 0){
					tglbtnFilter.setSelected(true);
				} else if(pos == 1){
					tglbtnFilter_1.setSelected(true);
				} else if(pos == 2){
					tglbtnFilter_2.setSelected(true);
				} else if(pos == 3){
					tglbtnFilter_3.setSelected(true);
				} else if(pos == 4){
					tglbtnFilter_4.setSelected(true);
				} else if(pos == 5){
					tglbtnFilter_5.setSelected(true);
				}
			} catch (UnknownUIPropertyException e) {
				e.printStackTrace();
			}
		}
	}
```



```java

```



```java
	@Override
	protected void initializeParameters() {
		String names = "None";
		String colors = "grey";
		for (int i = 0; i < NUM_POS - 1; i++) {
			names += "," + "None";
			colors += "," + "grey";
		}

		String helpNames = "Filter names should be entered as \"name1,name2,name3,name4,name5,name6\" with 6 entries separated by a comma. ";
		String helpColors = "Filter color names should be entered as \"color1,color2,color3,color4,color5,color6\" with 6 entries separated by a comma. ";

		addUIParameter(new StringUIParameter(this, PARAM_NAMES, helpNames, names));
		addUIParameter(new StringUIParameter(this, PARAM_COLORS, helpColors, colors));
	}
```



```java
	//////// Properties
	public static String FW_POSITION = "Filterwheel position";
	
	//////// Parameters
	public final static String PARAM_NAMES = " names";
	public final static String PARAM_COLORS = " colors";
	
	//////// Initial parameter
	public final static int NUM_POS = 6;
```



```java
	@Override
	public String getDescription() {
		return "A panel controlling a FilterWheel with 6 positions.";
	}

```

