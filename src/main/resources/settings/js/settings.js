import "../../jquery/jquery-3.2.1.min.js"
import "../js/network/select/network.js"
import {httpGetAsync} from "../../rs/requests.js";
import {getHardware} from "../../rs/hardware/hardware.paths.js";
import {getTypeNetwork} from "../../rs/hardware/hardware.type.js";
import {timerNetworkKill, get_net, init_static_ip, set_first_request, set_work_on_vpn} from "./network/network.js"
import {setApiSettings, startApiSettingsSocket, closeSocket } from "./api_settings/api.js"
import {saveShow, saveHide} from "./operation.js"
import {setLocalStorage, getValue} from "../../utils/storage/storage.js";
import {key_tab, previous_tab} from "../../utils/storage/keys.js";
import {timerIOKill, getIOCurrent} from "./devices/io.js";
import {wifi} from "./network/constants.js";
import {dialog_waiting_hide} from "../../dialogs/js/dialog.js";
import {netInterfaceType} from "./network/checkers/net.interface.type.js";
import {is_wifi_ap_set} from "./network/wifi.js";
import {getCurrentNetworkInterface, getWorkOnVPN} from "../../rs/network/network.paths.js";
import {getPoint} from "../../rs/wifi/wifi.paths.js";
import {getApiSettings} from "../../rs/api/api.paths.js";

let tab;
export const network = $("#network");
export const select_network = $("#select_network");
export const select_interfaces = $("#net_interfaces_list");
export const wifi_list = $("#wifi_list");
export const api_settings = $("#api_settings");
export const io = $("#io");
export const tab_network = $('#tab_network');
export const tab_api_settings = $('#tab_api_settings');
export const tab_io = $('#tab_io');

$(document).ready(() => {
    if (getValue(key_tab) === null || tab_network.attr('id') === getValue(key_tab)) {
        tab_network.click();
    } else if (getValue(key_tab) === tab_api_settings.attr('id')) {
        tab_api_settings.click();
    } else if (getValue(key_tab) === tab_io.attr('id')) {
        tab_io.click();
    } else {
        tab = tab_io;
        changeCssTab(tab_io, true);
    }
});

export const changeCssTab = (tab, change) => {
    if (tab === undefined) {
        return;
    }
    tab.removeClass();
    if (change === true) {
        tab.attr('class', 'tab tab_bottom_bar_selected tab_background_selected tab_padding');
    } else {
        tab.attr('class', 'tab tab_bottom_bar tab_background tab_padding')
    }
}

tab_network.click(function () {
    closeSocket();
    timerNetworkKill();
    timerIOKill();
    set_first_request();
    httpGetAsync(getHardware(getTypeNetwork()), isNetworkInit);
    httpGetAsync(getWorkOnVPN(), set_work_on_vpn);
});

const isNetworkInit = (data) => {
    if (!data.hasOwnProperty('device')) {
        if (!select_network.is(':visible')) {
            dialog_waiting_hide();
            saveHide();
            changeTab(tab_network, select_network, getValue(key_tab));
        }
    } else {
        if (netInterfaceType(data.device) === wifi) {
            saveHide();
            httpGetAsync(getPoint(), is_wifi_ap_set)
        } else {
            if (!network.is(':visible')) {
                changeTab(tab_network, network, getValue(key_tab));
                init_static_ip();
                httpGetAsync(getCurrentNetworkInterface(), get_net);
            }
        }
    }
}


tab_api_settings.click(function () {
    closeSocket();
    timerNetworkKill();
    timerIOKill();
    startApiSettingsSocket();
    if (!api_settings.is(':visible')) {
        httpGetAsync(getApiSettings(), setApiSettings);
        changeTab($(this), api_settings, getValue(key_tab));
        saveHide();
    }
});

tab_io.click(function () {
    closeSocket();
    timerNetworkKill();
    timerIOKill();
    if (!io.is(':visible')) {
        getIOCurrent();
        changeTab($(this), io, getValue(key_tab));
        saveHide();
    }
});

export function changeTab (new_tab, view, previous) {
    setLocalStorage(previous_tab, previous);
    setLocalStorage(key_tab, new_tab.attr('id'));
    changeCssTab(tab, false);
    tab = new_tab;
    changeCssTab(tab, true);
    hideAll();
    if (view === select_network
        || view === wifi_list
        || view === select_interfaces) {
        saveHide();
    } else {
        saveShow();
    }
    view.show();
}

const hideAll = () => $(".container").css({"display":"none"});
