## Easier Micro-manager User interface (EMU)

EMU is a plugin for [Micro-Manager](https://micro-manager.org/wiki/Micro-Manager) aimed at loading easily reconfigurable and transferable user interfaces. 

### Motivation for EMU

One is often tempted to implement the user interface for a Micro-Manager controlled set-up by hard-coding the device names. This requires recompiling the plugin everytime a device changes or as soon as the plugin needs to be transferred to another microscope.

EMU detaches the definition of the user interface from the actual devices, and allows any user to intuitively set which device should be linked to the UI without coding and recompiling.  

### How does EMU work? (the short way)

The EMU library defines two classes that are the building blocks of the reconfigurable UIs: [ConfigurablePanel](https://jdeschamps.github.io/EMU/main/java/embl/rieslab/emu/ui/ConfigurablePanel.html) and [ConfigurableMainFrame](https://jdeschamps.github.io/EMU/main/java/embl/rieslab/emu/ui/ConfigurableMainFrame.html). ConfigurablePanels contain components (such as buttons or text fields), while the ConfigurableMainFrame assembles these ConfigurablePanels within on window.

In addition, ConfigurablePanels declare [UIProperties](https://jdeschamps.github.io/EMU/main/java/embl/rieslab/emu/ui/uiproperties/UIProperty.html) and [UIParameters](https://jdeschamps.github.io/EMU/main/java/embl/rieslab/emu/ui/uiparameters/UIParameter.html). UIProperties are meant to represent actions of the UI, such as "change laser percentage". UIParameters, on the other hand, offer the possibility to change some aspects of the UI, for instance the titles of the panels, the colors of the buttons or some parameter default values.

EMU loads the ConfigurableMainFrame and allows the user to map the device properties of Micro-Manager to the UIProperties of the interface and set the default values for the UIParameters. The configuration file remembers the choices for each plugin and EMU can switch from one plugin to the other, or from one Plugin configuration to another.

Check out the [wiki](https://github.com/jdeschamps/EMU/wiki) and the [javadoc](https://jdeschamps.github.io/EMU).

### Is EMU compatible with drag and drop UI design?

Absolutely! Check out [the tutorial](tutorial) on how to graphically design a UI for EMU with Eclipse.

