/**
 *  Ring Alarm State
 *  Licence Details.
 *	https://opensource.org/licenses/MIT
 *
 *  Copyright 2019 Asish Soudhamma
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 *  and associated documentation files (the "Software"), to deal in the Software without restriction, 
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute, 
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial 
 *  portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
 
definition(
	name: "Ring Alarm State",
	namespace: "asishrs",
	author: "Topher Savoie (Original: Asish Soudhamma)",
	description: "Automatically sets the Ring Alarm alarm state based on the Smartthings mode, modified by Topher Savoie to sync only SHM and Ring Alarm",
	category: "Convenience",
	iconUrl: "https://cdn.shopify.com/s/files/1/2922/1686/t/2/assets/ring_logo.png?8137716793231487980",
	iconX2Url: "https://cdn.shopify.com/s/files/1/2922/1686/t/2/assets/ring_logo.png?8137716793231487980"
)

preferences {
	page(name: "selectProgram", title: "Ring Alarm State", install: false, uninstall: true,
    			nextPage: "Notifications") {
		section("Use this Alarm...") {
			input "alarmsystem", "capability.alarm", multiple: false, required: true
		}
	}
    page(name: "Notifications", title: "Notifications Options", install: true, uninstall: true) {
		section("Notifications") {
			input "sendPushMessage", "enum", title: "Send a push notification?", options: ["Yes", "No"], 
            		required: false
			input "phone", "phone", title: "Send a Text Message?", required: false
		}
        section([mobileOnly:true]) {
			label title: "Assign a name", required: false
		}
	}
}

def installed() {
	init()
}

def updated() {
    unsubscribe()
    unschedule()
    init()
}
  
def init() {
    subscribe(location, "alarmSystemStatus", shmaction)
    subscribe(alarmsystem, "alarm", alarmaction)
    state.caller = "parent"
}


def alarmaction(evt) {
    if (state.caller == "parent") {
        state.alarmstate = evt.value.toLowerCase()
        state.shmmode = location.currentState("alarmSystemStatus").value.toLowerCase()
        log.debug("***Alarm state changed to ${state.alarmstate}")
        log.debug("Syncing SHM mode, current mode is ${state.shmmode}")
        if(state.alarmstate == "off" && state.shmmode !="off") {
            setSHMoff()
        } else if(state.alarmstate == "away" && state.shmmode !="away") {
            setSHMaway()
        } else if(state.alarmstate == "home" && state.shmmode !="stay") {
            setSHMhome()
        } else {
            log.debug("No action required.")
        }
    } else {
    	log.debug("Skipping call, this is the child call.  This is to prevent recursive calls")
        state.caller = "parent"
    }
}

def shmaction(evt) {
	if (state.caller == "parent") {
        state.shmmode = evt.value.toLowerCase()
        state.alarmstate = alarmsystem.currentState("alarm").value.toLowerCase()
        log.debug("***SHM mode changed to ${state.shmmode}")
        log.debug("Syncing Alarm state, current state is ${state.alarmstate}")    
        if(state.shmmode == "off" && state.alarmstate !="off") {
            setalarmoff()
        } else if(state.shmmode == "away" && state.alarmstate !="away") {
            setalarmaway()
        } else if(state.shmmode == "stay" && state.alarmstate !="home") {
            setalarmhome()
        } else {
            log.debug("No action required.")
        }
    } else {
    	log.debug("Skipping call, this is the child call.  This is to prevent recursive calls")
        state.caller = "parent"
    }
}

def setalarmoff() {
    def message = "Syncing Ring Alarm to Disarm"
    log.info(message)
    state.caller = "child"
    alarmsystem.off()
    send(message)    
}
  
def setalarmaway() {
    def message = "Syncing Ring Alarm to Away"
    log.info(message)
    state.caller = "child"
    alarmsystem.away()
    send(message)    
}
  
def setalarmhome() {
    def message = "Syncing Ring Alarm to Home/Stay"
    log.info(message)
    state.caller = "child"
    alarmsystem.home()
    send(message)    
}

def setSHMoff() {
    def message = "Syncing SHM mode to Disarm"
    log.info(message)
    state.caller = "child"
    sendLocationEvent(name: "alarmSystemStatus" , value : "off" )
    send(message)    
}
  
def setSHMaway() {
    def message = "Syncing SHM mode to Away"
    log.info(message)
    state.caller = "child"
    sendLocationEvent(name: "alarmSystemStatus" , value : "away" )
    send(message)    
}
  
def setSHMhome() {
    def message = "Syncing SHM mode to Home/Stay"
    log.info(message)
    state.caller = "child"
    sendLocationEvent(name: "alarmSystemStatus" , value : "stay" )
    send(message)
}

  
private send(msg) {
	if (sendPushMessage != "No") {
		log.debug("sending push message: ${msg}")
		sendPush(msg)
	}
	if (phone) {
		log.debug("sending text message: ${msg}")
		sendSms(phone, msg)
	}
}
