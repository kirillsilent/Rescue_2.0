import {dialog_waiting_hide, showError} from "../dialogs/js/dialog.js";
import "../jquery/jquery-3.2.1.min.js"

export const move = path => {
    window.location = path;
};

export const httpGetAsync = (link, callback, obj) => {
    $.ajax({
        url: link,
        async: true,
        error: function (err) {
            console.clear();
        },
        success: function (response) {
            initData(response, callback, obj);
        }
    });
};

export const httpPutAsync = (link, callback, obj) => {
    $.ajax({
        url: link,
        async: true,
        type: 'PUT',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(obj),
        success: function (response) {
            initData(response, callback);
        }, error: function (err) {
            console.clear();
        }
    });
};

export const httpPostAsync = (link, callback, data) => {
    $.ajax({
        url: link,
        async: true,
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function (response) {
            initData(response, callback);
        }, error: function (err) {
            console.clear();
        }
    });
};

export const initData = (data, callback, obj) => {
    if (data.error) {
        dialog_waiting_hide();
        showError(data);
    } else {
        if (callback !== undefined) {
            if (callback !== null) {
                callback(data, obj);
            }
        }
    }

}