import "../../jquery/jquery-3.2.1.min.js";
import "../../jquery/jquery.marquee.min.js"
import {httpGetAsync, move} from "../../rs/requests.js";
import {setLocalStorage} from "../../utils/storage/storage.js";
import {getSettings} from "../../views.paths/paths.js";
import {getMarquee} from "../../rs/marquee/marquee.paths.js";
import {getStatusNetwork, getStatusSip} from "../../rs/status/status.paths.js";
import {key_tab, previous_tab} from "../../utils/storage/keys.js";
import {websocket} from "../../utils/socket/init.js";
import {clearTimer, startTimer} from "../../utils/timer/timer.interval.js";
import {rescue} from "../../utils/socket/socket.paths.js";


const settings = $('#settings');
const msg = $('#msg');
const incoming = $('#container_incoming');
const incident = $('#incident');
const left = $('#left');
const sip = $('#sip_register');
const network = $('#network');
const localhost = $('#localhost');
const plan = $('#plan');
let ws = null;
let timer = null;

$(document).ready(() => {

    ws = websocket(rescue);

    ws.onopen = () => {
        console.log('Connection: OK.');
        changeCSSStatus(localhost, 'localhost_online', 'localhost_offline');
    };

    ws.onmessage = (message) => {
        let obj = message.data;
        try {
            obj = JSON.parse(obj);
            if (obj.hasOwnProperty('marquee')) {
                if (obj.marquee !== ' ') {
                    setMarquee(obj.marquee);
                } else {
                    left.empty();
                    left.hide();
                }
            } else if (obj.hasOwnProperty('type')) {
                if (obj.type === 'plan') {
                    plan.prop("src", '../rescue/plan?rand=' + Math.random());
                }
            } else if (obj.hasOwnProperty('incoming')) {
                incomingCallUI();
            } else if (obj.hasOwnProperty('clearUI')) {
                clearIncomingCallUI();
            } else if (obj.hasOwnProperty('emergencyCategoryName')) {
                if (obj.active) {
                    clearIncomingCallUI();
                    incidentUI(obj);
                } else {
                    clearIncidentUI();
                }
            }
        } catch (exception) {
        }
    }

    ws.onerror = () => {
        console.clear();
        changeCSSStatus(localhost, 'localhost_offline', 'localhost_online');
    }

    httpGetAsync(getMarquee());
    plan.prop("src", '../rescue/plan?rand=' + Math.random());
    setTimeout(() => {
        checkAllStates();
    }, 2000);

});

const checkAllStates = () => {
    httpGetAsync(getStatusSip(), setSipStatus);
    httpGetAsync(getStatusNetwork(), setNetworkStatus);
    if (timer === null) {
        timer = startTimer(timer, checkAllStates, null);
    }
}

export const setSipStatus = (data) => {
    if (data.status) {
        changeCSSStatus(sip, 'sip_reg_online', 'sip_reg_offline');
    } else {
        changeCSSStatus(sip, 'sip_reg_offline', 'sip_reg_online');
    }
}

export const setNetworkStatus = (data) => {
    if (data.status) {
        changeCSSStatus(network, 'net_online', 'net_offline');
    } else {
        changeCSSStatus(network, 'net_offline', 'net_online');
    }
}

const incidentUI = (data) => {
    left.hide();
    msg.html(data.emergencyCategoryName);
    changeCSSStatus(incident, 'alert', 'incoming');
    incident.css('display', 'block');
}

const incomingCallUI = () => {
    incoming.css("visibility", "visible");
}

const clearIncomingCallUI = () => {
    incoming.css("visibility", "hidden");
}

const clearIncidentUI = () => {
    incident.css('display', 'none');
    left.show();
}

const changeCSSStatus = (obj, add, remove) => {
    obj.removeClass(remove);
    obj.addClass(add);
}

const setMarquee = (s) => {
    left.show();
    left.empty();
    left.html(s);
}

const closeSocket = () => {
    if (ws !== null) {
        ws.close();
    }
}

settings.click(() => {
    closeSocket();
    clearTimer(timer);
    setLocalStorage(previous_tab, 'tab_network');
    setLocalStorage(key_tab, 'tab_network');
    move(getSettings());
});

