# What is EMU?

[Micro-Manager](https://micro-manager.org/wiki/Micro-Manager) controls the devices on your microscope using device properties (e.g. laser on/off). All device properties can be accessed through the device property browser. However, controlling the microscope through a long list of properties is cumbersome, slow and rather aimed at the microscope engineer than at the user. While interactions with device properties can be facilitated by using configuration preset groups or the quick access plugin, these cannot replace a user interface (UI) tailored to the microscope. 

Tailored interfaces have the advantage of rendering the control of the microscope intuitive. However, tailoring the UI often means hard-coded references to the specific device properties of the microscope and a need to recompile it every time something changes on the microscope.

Easier Micro-manager User interface (EMU) offers means to make your Micro-Manager interface reconfigurable:

<img align="right" src="img/emu-logo.png">

<br>

- Rapid design compatible with **drag and drop softwares** (e.g. Eclipse WindowBuilder).
- Functional Micro-Manager UI with only few lines of code thanks to EMU's back-end.
- Flexible and transferable: easy and intuitive configuration through EMU's interface.


Check out the [EMU guide]( https://jdeschamps.github.io/EMU-guide ) for a quick introduction, a user guide and a programming guide. Resources are linked at the bottom of this page.

# How to install EMU?

**EMU is now distributed with Micro-Manager 2.0-gamma (nightly build). To download the latest Micro-Manager 2.0-gamma nightly build, refer [to this page and choose your platform](https://micro-manager.org/wiki/Micro-Manager_Nightly_Builds). EMU is then available from the Plugin menu, under "User Interface".** 

If you wish to install it from the source code, then follow the next steps:

1. Download and install [Micro-Manager 2-gamma](https://micro-manager.org/wiki/Download_Micro-Manager_Latest_Release).

2. Install git (for windows users: [git bash](https://gitforwindows.org/), or for os mac users: [source forge](https://sourceforge.net/projects/git-osx-installer/files/))

3. Download the [Java SE Development Kit 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Set the environment variable [JAVA_HOME to the JDK 1.8 folder path](https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html) (windows).

4. Download and install [Maven](https://maven.apache.org/install.html).

5. Using the git console, go to the folder you wish to install EMU in and type (omitting the $):

   ```bash
   $ git clone https://github.com/jdeschamps/EMU.git
   ```

6. Navigate to the EMU folder and compile it using the path to Micro-manager:
   
   Windows (git bash)
   ```bash
   $ cd emu
   $ ./compileAndDeploy-Win.sh "C:\Program\Path\to\MicroManager2gamma"
   ```
  
   macOs
   ```bash
   $ cd emu
   $ sh compileAndDeploy-Mac.sh /Application/Path/to/MicroManager2gamma/
   ```

   > Note: the script requires bash, which is shipped with the git console on Windows.
   
   > Note: the EMU .jar shipped with Micro-Manager will be overwritten (it is located in the /mmplugins/ folder).

7. Install an EMU plugin by placing the plugin compiled .jar file in "C:\Program\Path\to\MicroManager2gamma\EMU\" or use the default plugins.

> Note: For macOs users, [few manual steps](https://micro-manager.org/wiki/Micro-Manager_Installation_Notes) might be required to start Micro-manager.

# Cite us
Joran Deschamps and Jonas Ries, ["EMU: reconfigurable graphical user interfaces for Micro-Manager", bioRxiv 2020.03.18.997494.](https://www.biorxiv.org/content/10.1101/2020.03.18.997494v1)

# EMU plugins and resources

#### Guide

[The EMU guide](https://jdeschamps.github.io/EMU-guide/) offers both a description of EMU menus for users and a programming guide for implementing your own EMU plugin.

#### Examples

[EMU plugin examples]( https://github.com/jdeschamps/EMU-guide/tree/master/examples ): these projects include an empty base project, a simple UI for controlling lasers and a filterwheel and an interface for a commercial laser. The latter two are shipped with EMU in Micro-Manager 2-gamma.

#### Tutorial

[Tutorial for Eclipse Windows Builder](  https://github.com/jdeschamps/EMU-guide/tree/master/tutorial ): a detailed walk through on how to easily assemble an EMU user interface using drag and drop software.

#### htSMLM

The Ries lab is using its own advanced EMU plugin to control automated localization microscopes: [htSMLM](https://github.com/jdeschamps/htSMLM). 

#### Javadoc
The javadoc is available [here](https://jdeschamps.github.io/EMU/).
