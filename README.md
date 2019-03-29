## Easier Micro-manager User interface (EMU)

EMU is a plugin for [Micro-Manager](https://micro-manager.org/wiki/Micro-Manager)
 aimed at loading easily reconfigurable and transferable user interfaces. 

### Motivation for EMU

In Micro-manager, the interaction with the devices in your set-up can be done by either of the following ways:
 
* Through the list of device properties known as the PropertyBrowser
* Using user-defined configuration presets in the main window
* Via script calls to the device properties
* Thanks to a quick-access panel, a configurable user interface linked to the configuration presets
* With the help of a plugin, most-likey with a user-interface

If your microscope or set-up has many devices, then the PropertyBrowser can be slow and difficult to navigate for non-specialists. Configuration presets are intuitive and easy to use but are limited to a small space in the main window of Micro-Manager and only appear as drop-down menus. Scripts are great to dictate the behaviour of the microscope, in particular acquisitions, but are not an alternative for a user interface. The quick-access panel on the other hand offers a configurable user interface that can be tailored to do different tasks, such as setting a preset, running a script..etc.. However, the quick-access panel is limited to small set of actions and cannot replace a microscope user interface.

Finally, plugin are the choice method to obtain an intuitive user interface to control a microscope with many devices. Unfortunately, this often means hard-coded references to the device properties and aspects of the user interface.

EMU operates as a Micro-Manager plugin and loads its own plugins. Once a plugin loaded, the user can navigate the EMU settings to map the device properties (from Micro-Manager) to functions of the user interface. This makes the user interface easily reconfigurable (for instance when a device is exchanged) and transferable to other set-up. In addition, the user interface can define some properties (such as titles or colors) that can also be set through the EMU interface.

### How does EMU work?

The EMU library defines two classes that are the building blocks of the reconfigurable UIs: [ConfigurablePanel](https://jdeschamps.github.io/EMU/main/java/embl/rieslab/emu/ui/ConfigurablePanel.html) and [ConfigurableMainFrame](https://jdeschamps.github.io/EMU/main/java/embl/rieslab/emu/ui/ConfigurableMainFrame.html).

(to be updated soon)

### Is EMU compatible with drag and drop design of user interfaces?

Absolutely! Check out [the tutorial on how to graphically design a UI for EMU with Eclipse](tutorial).
