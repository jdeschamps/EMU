# What is EMU?

Easier Micro-manager User interface (EMU) is a [Micro-Manager](https://micro-manager.org/wiki/Micro-Manager) plugin that loads easily reconfigurable user interfaces to control your microscope. Build your UI using EMU classes:

<img align="right" src="img/emu-logo.png">

<br>

- Compatible with **drag and drop Java design**.

- No need to hard code device property names.
- Easy and intuitive configuration of the UI through EMU's interface.
- No compiling necessary when you exchange devices.
- One UI to rule them all: easy transfer between your microscopes.

<br>

# How to install EMU?

1. Download and install [Micro-Manager 2-gamma](https://micro-manager.org/wiki/Download Micro-Manager_Latest Release).

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

#### Tutorial

[Tutorial for Eclipse Windows Builder]( https://github.com/jdeschamps/EMU-tutorial ): a detailed walk through on how to easily assemble an EMU user interface using drag and drop software.

#### Examples

[EMU examples](https://github.com/jdeschamps/EMU-examples): these projects include an empty base project, a simple UI for controlling lasers and a filterwheel and an interface for a commercial laser.

#### htSMLM

The Ries lab is using its own EMU plugin to control its automated localization microscopes: [htSMLM](https://github.com/jdeschamps/htSMLM). This plugin is more advanced than the examples and can be used to explore more complex behaviours.

#### Going further

For more details, read the [guide]() or the [javadoc]().