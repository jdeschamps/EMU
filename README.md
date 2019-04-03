# What is EMU?

Easier Micro-manager User interface (EMU) is both a [Micro-Manager](https://micro-manager.org/wiki/Micro-Manager) plugin and a library to build easily reconfigurable user interfaces. By using EMU classes, you can put together a user interface to control your devices without having to spell out in your code the name of those devices, their properties or which value they accept. 

Once your UI is compiled, the EMU plugin loads it and allows you to map the device properties known to Micro-Manager to the actions of your UI. EMU remembers the configuration and can switch from one configuration to another, or even from one UI to another. Later on, any change in the hardware can be easily and rapidly applied through the EMU interface without having to recompile your code. Transferring your UI to another microscope also then becomes child's play.
  
# How does this work?

EMU defines two classes that are the building blocks of your UI: [ConfigurablePanels](https://github.com/jdeschamps/EMU/wiki/ConfigurablePanel) and [ConfigurableMainFrames](https://github.com/jdeschamps/EMU/wiki/ConfigurableMainFrame). These classes are extensions of the well-known JPanel and JFrame for the Java Swing library. As in Swing, the ConfigurablePanels contain JComponents (e.g. buttons) and are assembled into a (unique) ConfigurableMainFrame.

What is special about the ConfigurablePanels is that they also declare [UIProperties](https://github.com/jdeschamps/EMU/wiki/UIProperty) and [UIParameters](https://github.com/jdeschamps/EMU/wiki/UIParameter). UIProperties represent the actions of your UI and are intended to be linked to device property from Micro-Manager. Interacting with buttons within your UI would then trigger a call to the UIProperty and subsequently change the value of the device property. 

UIParameters, on the other hand, only take one value at the start of your UI. The UIParameters are a mechanism to modify some aspects of your UI, such as the color of a button or the title of a panel.

EMU automatically aggregates the UIProperties and the UIParameters and creates an interface to graphically modify which device property is linked to which UIProperty or what is the value of a UIParameter. Effectively, EMU detaches the definition of the user interface from the actual devices, and allows any user to intuitively configure the UI without coding and recompiling.  

# Example using drag and drop UI tools

Let's say you have three lasers and one filterwheel in Micro-Manager. In the device property browser, you would then have a list of properties with the names of your lasers and filterwheels. Among them you would expect properties to turn on and off the lasers, change their respective powers and a property to set the position of the filterwheel. 
You can use Eclipse's WindowBuilder plugin to create a user interface that looks like that:

<p align="center">
<img src="https://github.com/jdeschamps/EMU/blob/master/tutorial/images/0-Final.PNG">
</p>

However, the lasers might be different, so these properties can be named differently. For instance:

| Property | Value |
|---|---|
| CaltechFW-State | "0" to "6" |
| JivaLaser1-Name | "UV" |
| JivaLaser1-Operation | "1" or "0" |
| JivaLaser1-Power (mW)| "0" to "50" |
| JivaLaser2-Name  | "561" |
| JivaLaser2-Operation | "1" or "0" |
| JivaLaser1-Power (mW)| "0" to "300" |
| LaseXS-Name | "DL154-S" |
| LaseXS-Enable | "enable" or "disable" |
| LaseXS-Power (%)| "0" to "100" |

Here, not only do the laser properties have different names, but they also don't share the same value ranges. Hard-coding in your UI the property names and their values would then be error-prone, unpractical and would require you to recompile your code every time a change happens on the microscope.

By using EMU, you do not use any prior knowledge on your laser in your ConfigurablePanel beyond the fact that it should control a laser. You can use drag and drop tools to automatically create your UI using the EMU classes and then implements few necessary functions:

* declaration of the UIProperties 
* declaration of the UIParameters
* how are the UIProperties modified by the components (e.g. button B1 changes property Prop1)
* how are the components impacted by a change in the UIProperties (e.g. if Prop1 changes, then B1's state changes)
* how are the components impacted by a change in the UIParameters (e.g. if Param1 changes, then B1's color changes)

All the tailoring to your microscope happens then through the EMU interface by selecting in the menu which device property corresponds to which UIProperty and what are the values of the UIParameters.

Checkout the extensive [Tutorial](https://github.com/jdeschamps/EMU/tree/master/tutorial) on how to build this UI!

# Going further

The [Wiki](https://github.com/jdeschamps/EMU/wiki) includes more detailed explanations on the mechanisms behind EMU classes. You can also check out the [Javadoc](https://jdeschamps.github.io/EMU/).

The Ries lab is using a complex user interface in EMU: [htSMLM - an EMU plugin](https://github.com/jdeschamps/htSMLM)

