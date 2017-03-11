# MIDIMACRO
This repo will contain all the info, codesnipets about the MIDIMACRO project with arduino. 

This is a project made in 2016 for school. I needed to combine a Arduino microcontroller with a MQTT broker. I often had problems when I was playing games, skyping with friends and when I also wanted to listen to music. I malualy needed to change all the volume settings in the windows Volume Mixer. So I thought what if I created a piece of hardware that could controll the Windows Volume mixer. I had to use an MQTT broker for the communication between the Arduino and the Windows MIDIMACRO software on Windows. I also needed to create a Android Application. You can see all images and information about this project for school here -> http://vanlooverenkoen.be/MIDIMACRO

## After My School Project
After my school project, it was so easy to manipulate the Windows Volume Mixer with the MIDIMACRO Windows software, I wanted to share my project with all of you. But because using a MQTT broker was not the best way to communicate with my project I changed this in a Rest Service in the Windows Software. So at this point the Arduino controller wansn't able to communicate with the Windows software. So I changed this to only serial communication. (update is comming ;-)) You can download the sofware on my website http://vanlooverenkoen.be/MIDIMACRO/#download 

### What isn't included anymore?
 - Windows Software
  - MQTT for midi
  
 - Arduino Controller
  - MQTT broker communication
  
 -Android Application
  - MQTT broker communication
  
### What is still included?
 - Windows Software
  - MIDIMACRO Server (Rest) for midi-signals
  - Midi device for midi-signals
  - Communication with the Windows Volume Mixer
  - Mixer macros
  
 - Arduino Controller
  - Sliders, Faders, Buttons, Distance Sensors
  - Communication over MIDIMACRO Server (REST)
  
 - Android Application
  - Mixer macros
  - Communication over MIDIMACRO Server (REST)
  
### Where will it work?
At this point the software is only tested on Windows 10.
For Android it should work for 93% of the devices. If your phone is Android 6.0 or higher. you get MIDI Over USB 
  
