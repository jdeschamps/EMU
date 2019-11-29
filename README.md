# What is EMU?

[Micro-Manager](https://micro-manager.org/wiki/Micro-Manager) controls the devices on your microscope using device properties (e.g. laser on/off). All device properties can be access through the device property browser. However, controlling the microscope through a long list of properties is cumbersome, slow and rather aimed at the microscope engineer than at the user. While interaction with device properties can be facilitated by using configuration preset groups or the quick access plugin, these cannot replace a user interface (UI) tailored to the microscope. 

Tailored interfaces have the advantage of rendering the control of the microscope intuitive. However, tailoring the UI often means hard-coded references to the specific device properties and a need to recompile it every time something changes on the microscope.

Easier Micro-manager User interface (EMU) offers means to make your Micro-Manager interface reconfigurable:

<img align="right" src="img/emu-logo.png">

<br>

- Rapid design compatible with **drag and drop softwares** (e.g. Eclipse WindowBuilder).
- Functional Micro-Manager UI with only few lines of code thanks to EMU's back-end.
- Flexible and transferable: easy and intuitive configuration through EMU's interface.

<br>

# How to install EMU?

1. Download and install [Micro-Manager 2-gamma](https://micro-manager.org/wiki/Download_Micro-Manager_Latest_Release).

2. Install git (for windows users: [git bash](https://gitforwindows.org/))

3. Download the [Java SE Development Kit 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

4. Download and install [Maven](https://maven.apache.org/install.html).

5. Using the git console, go to the folder you wish to install EMU in and type (omitting the $):

   ```bash
   $ git clone https://github.com/jdeschamps/EMU.git
   ```

6. Navigate to the EMU folder and compile it using the path to Micro-manager:

   ```bash
   $ cd emu
   $ ./compileAndDeploy.sh "C:\Path\to\MicroManager2gamma"
   ```

   > Note: the script requires bash, which is shipped with the git console on Windows.
   
   

# Build your own EMU UI

#### Guide

[The EMU guide](https://jdeschamps.github.io/EMU-guide/) offers both a description of EMU menus for users and a programming guide for implementing your own EMU plugin.

#### Tutorial

[Tutorial for Eclipse Windows Builder](  https://github.com/jdeschamps/EMU-guide/tree/master/tutorial ): a detailed walk through on how to easily assemble an EMU user interface using drag and drop software.

#### Examples

[EMU examples]( https://github.com/jdeschamps/EMU-guide/tree/master/examples ): these projects include an empty base project, a simple UI for controlling lasers and a filterwheel and an interface for a commercial laser.

#### htSMLM

The Ries lab is using its own EMU plugin to control its automated localization microscopes: [htSMLM](https://github.com/jdeschamps/htSMLM). This plugin is more advanced than the examples and can be used to explore more complex behaviours.

