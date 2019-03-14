# Hollow-Knight-SaveManager
Manage and edit Hollow Knight saves for all current releases, Windows/Linux/Mac/Switch.

#### Usage:
 On the left of the main window there is the 4 currently loaded saves, each save has 3 buttons
   * < loads the selected save into this slot
   * \> saves this slot into the selected save
   * floppy disk saves this slot to a new file
   
Double click a save in the file tree to open the save editor. From there you can edit your data in two tabs: Inventory, where your health, soul, geo, and items are contained; and Charms, where you can set whether you have charms, have them equipped, and their notch cost. You can even manually activate overcharmed status.

## Releases

  * [Latest](https://github.com/KayDeeTee/Hollow-Knight-SaveManger/releases/latest)

## Screenshots

### Save Manager

![Save Manager](screenshots/save_manager.png?raw=true "Save Manager")

### Save Editor

#### Inventory

![Save Editor Inventory](screenshots/save_editor_inventory.png?raw=true "Save Editor Inventory")

#### Charms

![Save Editor Charms](screenshots/save_editor_charms.png?raw=true "Save Editor Charms")

## Install instructions
   * Download and install java 8 jdk [here](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
   * Go the save directory
      * Win: %APPDATA%\..\LocalLow\Team Cherry\Hollow Knight\
      * Mac: ~/Library/Application Support/unity.Team Cherry.Hollow Knight/
      * Linux: ~/.config/unity3d/Team Cherry/Hollow Knight/
   * Move the jar file from releases into save directory
      * Not technically required as you can put the jar anywhere and use Set Save Location button to set the save directory
   * Run the jar
      * via cmd: java -jar HKSMv0.3.2.jar
      * or double click for windows users

## Licenses

  * [Hollow-Knight-SaveManager](https://github.com/KayDeeTee/Hollow-Knight-SaveManger/blob/master/LICENSE)
  
  * #### Dependencies

    * [jIconFont](https://github.com/jIconFont/jiconfont/blob/master/LICENSE)
    * [gson](https://github.com/google/gson/blob/master/LICENSE)
	* [gnu-crypto](https://mvnrepository.com/artifact/org.gnu/gnu-crypto/2.0.1)
