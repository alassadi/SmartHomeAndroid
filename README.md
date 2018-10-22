# SmartHomeAndroid
functions: 

House:
*Lights
req: turn on/off indoor lights
res: indoor lights on/off

req: turn on/off outdoor lights
res: outdoor lights on/off

*Room temperature
req: Read temp
res: temp

*Fire Alaram
req: turn on/off fire alarm
res: fire alarm on/off


*Burglar Alarm
req: turn on/off burglar alarm
res: burglar alarm on/off

*Door
req: read door state
res: open/close

*Light timers
req: set timer on(time) /off
res: timer set: time /off


*Events
OnChangeListener(){
Fire alarm state
Burglar alarm state
Door(open/close) state
(Are we supose to listen for any other changes that are made physically in the house?)
}

