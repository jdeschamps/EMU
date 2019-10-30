# What is EMU?

Easier Micro-manager User interface (EMU) is a [Micro-Manager](https://micro-manager.org/wiki/Micro-Manager) plugin that loads easily reconfigurable user interfaces to control your microscope. Build your UI using EMU classes:

- Compatible with drag and drop Java design.
- No need to hard code device property names.
- Easy and intuitive configuration.
- No compiling necessary when you exchange devices.
- One UI to govern them all: easy transfer between your microscopes.



# How to install EMU?

1. Download and install [Micro-Manager 2-gamma](https://micro-manager.org/wiki/Download Micro-Manager_Latest Release).

2. Install git (for windows users: [git bash](https://gitforwindows.org/))

3. Download the [Java SE Development Kit 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

4. Download and install [Maven](https://maven.apache.org/install.html).

5. Using the console, go the folder you wish to install EMU in and type:

   ```bash
   $ git clone https://github.com/jdeschamps/EMU.git
   ```

6. Navigate to the folder and compile using the path to Micro-manager:

   ```bash
   $ git cd EMU
   $ ./compileAndDeploy.sh "C:\Path\to\MicroManager2gamma"
   ```

   

# Build your own EMU UI

You can easily build a UI using drag and drop software, our [tutorial](tutorial) for Eclipse Windows Builder is a detailed walk through.

You can also check out the [EMU example](https://github.com/jdeschamps/EMU-examples), among which:

- The base plugin is an empty plugin with comments on each of the function to implement.
- The simple UI is the UI example used in the tutorial.
- The iBeamSmart UI is aimed at controlling Toptica iBeamSmart lasers.

The Ries lab is using its own EMU plugin to control its automated localization microscopes: [htSMLM](https://github.com/jdeschamps/htSMLM). 