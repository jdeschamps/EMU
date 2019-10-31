# EMU tutorial: building a UI

This tutorial is a simple example walk-through of how to build a plugin for EMU, in order to obtain a re-configurable user interface within Micro-Manager. 

Here is a preview of the UI:

<p align="center">
<img src="images/0-Final.PNG">
</p>

#### Consult the EMU guide for more in-depth definitions of the EMU classes during the tutorial.

#### Prerequisite

- [Eclipse](https://www.eclipse.org/) with the [WindowBuilder plugin]() running java 8.
- Micro-Manager 2gamma installed.
- EMU installed (see [installing section](https://github.com/jdeschamps/EMU)).



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



```java
	@Override
	protected void initializeProperties() {
		String text1 = "Property changing the power percentage of the laser.";
		String text2 = "Property turning the laser on and off.";
        String propertyName1 = getLabel() + " " + LASER_PERCENTAGE;
        String propertyName2 = getLabel() + " " + LASER_OPERATION;
		
		addUIProperty(new RescaledUIProperty(this, propertyName1, text1, new NoFlag()));
		addUIProperty(new TwoStateUIProperty(this, propertyName2, text2, new NoFlag()));
	}
```































