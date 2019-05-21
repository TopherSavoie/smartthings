# SmartThings - Ring Alarm Device & Smart-App [FORK]

Topher Savoie UPDATE: Changed the Ring Alarm State smart app to sync the classic SHM mode to the Ring Alarm State, and vice versa.  So you can use either the SHM mode to update Ring (and invoke the Ring device handler) or you can use the Ring device to set the alarm state and it will sync the SHM mode.

PLEASE NOTE:  If you use Ring.com or the Ring App, this will sync to ST, but only after the Ring Device handler poles Ring to check on the status (every 5 min).  So while it will sync ST, do not rely on it for critical ST home aoutmations if you change Ring from their app and not ST (up to 5 min delay).


> :mega: This is the `version 2 `of this application. If you are looking for the `version 1`, visit https://github.com/asishrs/smartthings-ringalarm

This repository contains device handler and smart-app for Ring Alarm integration with SmartThings. In order to use this, you need to deploy the API part as described [here](https://github.com/asishrs/smartthings-ringalarmv2 ).

## Prerequisites 
[Ring Alarm API](https://github.com/asishrs/smartthings-ringalarmv2 ) 

 :warning:You need to have a working API before proceed to the next steps.

## Supported Ring Devices

- Alarm Base
- Keypad
- Motion Sensor
- Contact Sensor
- Z-Wave Range Extender 


### Manual Install SmartThings Device Handler
 - Login at http://graph.api.smartthings.com
 - Select **My Locations**, select the location you want to use.
 - Select  **My Device Handlers**
 - Click on the **+ New Device Handler** button on the right.
 - On the **New Device Handler** page, Select the Tab **From Code**
  - Copy the [ringalarm.groovy](devicetypes/asishrs/ringalarm.src/ringalarm.groovy) source code and paste it into the IDE editor window - edit as shown below for devices/counts.
  - Click the **Create** button at the bottom.
  - Click the blue **Save** button above the editor window.
  - Click the **Publish** button next to it and select **For Me**. You have now self-published your Device Handler

:pencil2: Update the number of ring devices in the code, check for below part.

```
//Define number of devices here.
def motionSensorCount = 5
def contactSensorCount = 6
def rangeExtenderCount = 1
def keypadCount = 1
```

### Install Alarm Device  
  - Select **My Devices**
  - Click on the **+ New Device** button on the right.
  - Fill the Name and Network ID Field (can be anything you like)
  - Under Type, select RingAlarm
  - Select appropriate options under Location and Hub
  - Click **Create**
  - Click **Preferences (edit)** 
  - Input below:
    - **Ring User Name**
    - **Ring Password**
    - **API Url** - Invoke URL from Lambda setup (should end with .com/default)
    - **API Key** - API key from Lambda setup
    - **Location Id** - Location Id value found in browser Network panel.
    - **ZID** - ZID value found in browser Network panel.


### Install SmartThings App
 - *(optional)* Login at http://graph.api.smartthings.com
 - *(optional)* Select **My Locations**, select the location you want to use.
 - Select **My SmartApps**
- Click on the **+ New SmartApp** button on the right.
- On the **New SmartApp**  page, Select the Tab **From Code**
- Copy the [ringalarm-watch.groovy](smartapps/asishrs/ringalarm-watch.src/ringalarm-watch.groovy) source code and paste it into the IDE editor window
- Click the **Create** button at the bottom.
- Click the blue **Save** button above the editor window.
- Click the **Publish** button next to it and select **For Me**. You have now self-published your SmartApp

## Setup your SmartThings App
This is based on *Smarthing Classic App*.

- Open your SmartThings app and go to **My Home**
- Tap on the Ring Alarm and then tap on the **settings** (*gear icon*).
- Add below
  - **Ring User Name**
  - **Ring Password**
  - **API Url** - Invoke URL from Lambda setup
  - **API Key** - API key from Lambda setup
  - **Location Id** - Location Id value found in browser Network panel.
  - **ZID** - ZID value found in browser Network panel.
  - **Polling Interval** - Polling interval between Ring Status API call.

|                           My Home                            | Ring Alarm Settings                                          |
| :----------------------------------------------------------: | ------------------------------------------------------------ |
| ![SmartThings - My Home](images/smarthings_classic_app.jpg?raw=true "SmartThings Classic- Home") | ![SmartThings - My Home](images/smartthings-classic-app-settings.jpg?raw=true "SmartThings Classic- Home") |

## Support

**Do you have more devices?**

Either open an issue with the device type, or you can make a PR with the required changes. 

**Other issues?**
Please open an issue.

## License

SmartThings - Ring Alarm Device & Smart-App is released under the [MIT License](https://opensource.org/licenses/MIT).
