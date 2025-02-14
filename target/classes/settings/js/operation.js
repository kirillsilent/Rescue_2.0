import {network, api_settings, io, tab_network, tab_api_settings, tab_io, select_network} from "./settings.js";
import {save_settings, text_err_settings_is_not_set} from "../../dialogs/js/dialog.strings.js";
import {dialog, dialog_text, init_dialog_default, showError} from "../../dialogs/js/dialog.js";
import {getValue} from "../../utils/storage/storage.js";
import {previous_tab} from "../../utils/storage/keys.js";
import {httpGetAsync, move} from "../../rs/requests.js";
import {timerIOKill} from "./devices/io.js";
import {timerNetworkKill} from "./network/network.js";
import {isSettingsSet} from "../../rs/settings/settings.paths.js";
//import {getRoot} from "../../views.paths/paths.js";
import {restartSipService} from "../../rs/sip/sip.paths.js";
import {closeSocket} from "./api_settings/api.js";
import {getBackRoot, getRoot} from "../../views.paths/paths.js";

const save = $('#save');

save.click(function () {
    httpGetAsync(restartSipService(), null, null);
    timerNetworkKill();
    dialog_text.html(save_settings);
    dialog.show();
});

$('#set_default').click(function () {
    init_dialog_default();
});

$('#back').click(() => {
    closeSocket();
    if(select_network.is(':visible')){
        settingsNotSet();
    }else if(network.is(':visible')){
        httpGetAsync(isSettingsSet(), moveToPage);
    }else if(api_settings.is(':visible')){
        tab_network.click();
    }else if(io.is(':visible')){
        tab_api_settings.click();
    }else {
        if(getValue(previous_tab) === tab_network.attr('id')){
            $('#'+getValue(previous_tab)).click();
        }else if(getValue(previous_tab) === tab_io.attr('id')){
            timerIOKill();
            tab_io.click();
        }
    }
});

const moveToPage = (data) => {
    if(data.set){
            //httpGetAsync(restartSipService(), null, null);
            move(getBackRoot());
    }else {
        settingsNotSet();
    }
}

const settingsNotSet = () => {
    //httpGetAsync(restartSipService(), null, null);
    move(getBackRoot());
}

export const saveHide = () => {
    save.hide();
};

export const saveShow = () => {
    save.show()
};
