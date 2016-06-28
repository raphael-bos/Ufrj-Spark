/*----- SEND AJAX UI -----*/
(function ($, window) {
    if (!$) {
        throw "jQuery required for AjaxUtils.js";
    }

    $.sendAjax = function (url, parametros, sucesso, erro, async) {
        if (async !== false) {
            async = true;
        }
        return $.ajax({
            type: "POST",
            url: url,
            async: async,
            dataType: "text json",
            contentType: "application/json; charset=utf-8",
            data: parametros,
            success: sucesso,
            error: function (xmlHttpRequest, textStatus, errorThrown) {
                tratamentoDeErroAjax(xmlHttpRequest, erro, errorThrown);
            }
        });
    };

    $.sendBlockPost = function (url, parametros, sucesso, erro) {
        Block();
        var jq = $.sendPost(url, parametros, sucesso, erro);
        jq.done(function () {
            Unblock();
        });
    };

    $.sendBlockPost2 = function (url, parametros, sucesso, erro) {
        Block();
        var jq = $.sendPost2(url, parametros, sucesso, erro);
        jq.done(function () {
            Unblock();
        });
    };

    $.sendPost = function (url, parametros, sucesso, erro) {
        var jqxhr = $.post(url, parametros, sucesso, "json")
	        .fail(function (xmlHttpRequest, textStatus, errorThrown) {
	            tratamentoDeErroAjax(xmlHttpRequest, erro, errorThrown);
	        });
        return jqxhr;
    };

    $.sendPost2 = function (url, parametros, sucesso, erro) {
        var jqxhr = $.ajax({
            type: "POST",
            url: url,
            async: true,
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(parametros),
            success: sucesso,
            error: function (xmlHttpRequest, textStatus, errorThrown) {
                tratamentoDeErroAjax(xmlHttpRequest, erro, errorThrown);
            }
        });

        return jqxhr;
    };

    $.whenBlock = function () {
        Block();
        var callback = [].pop.apply(arguments, arguments);
        return $.when.apply(null, arguments).done(function () {
            callback.apply(null, arguments);
            Unblock();
        });
    };

    $.sendGet = function (url, parametros, sucesso, erro) {
        var jqxhr = $.get(url, parametros, sucesso)
	        .fail(function (xmlHttpRequest, textStatus, errorThrown) {
	            tratamentoDeErroAjax(xmlHttpRequest, erro, errorThrown);
	        });
    };

    function tratamentoDeErroAjax(xmlHttpRequest, erro, errorThrown) {
        if (erro != undefined) {
            erro(errorThrown);
            return;
        }

        if (xmlHttpRequest.status == 404) {
            window.location = window.siteRoot + '/Error/Error404';
        } else if (xmlHttpRequest.status == 401 || xmlHttpRequest.status == 403) {
            window.ToastrErrorDialog(window.sessionExpired, null, null, function () { window.location = window.ceosRoot + '/Auth/Login'; });
        } else if (xmlHttpRequest.status == 409 && xmlHttpRequest.responseText != "") {
            toastr.error(xmlHttpRequest.responseText);
        } else if (xmlHttpRequest.status == 499 && xmlHttpRequest.responseText != ""){
            toastr.warning(xmlHttpRequest.responseText);
            //Erros tratados
        } else {
            //toastr.error(window.generalError);
            alert(errorThrown);
        }
        Unblock();
    };

    $.download = function (url, data, method) {
        if (url && data) {
            var inputs = '';
            for (var property in data) {
                if (data.hasOwnProperty(property)) {
                    inputs += '<input type="hidden" name="' + property + '" value="' + data[property] + '" />';
                }
            }
            $('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>').appendTo('body').submit().remove();
        } else {
            throw "$.download: url and data must be defined";
        }
    };

    $.downloadFileBlock = function (url, parametros) {
        $.sendBlockPost(url, parametros, function (data) {
            $.download(window.currentPath + "GetSessionExcelFile", {});
        });
    };

})(jQuery, window);

var when = (function (url) {
    var self = {};

    self.thens = [];
    self.url = url;
    self.successMessage = "";
    self.with = function (params) {
        self.params = params;
        return $.sendBlockPost2(window.currentPath + self.url, self.params, function (data) {
            if (self.successMessage != "") {
                toastr["success"](self.successMessage);
            }
            self.thens.each(function (callback) {
                if (typeof (callback) === "function")
                { callback(data); }
            });
        });
    };

    self.withNoBlock = function (params) {
        self.params = params;
        if (self.thens.length) {
            return $.sendPost2(window.currentPath + self.url, self.params, function (data) {
                if (self.successMessage != "") {
                    toastr["success"](self.successMessage);
                }
                self.thens.each(function (callback) {
                    callback(data);
                });
            });
        } else {
            return $.sendPost2(window.currentPath + self.url, self.params);
        }
    };

    self.post = function (params) {
        return self.with(params);
    }


    self.then = function (callback) {
        self.thens.push(callback);
        return self;
    }

    self.thenSuccess = function (msg) {
        self.successMessage = msg;
        return self;
    }

    return self;
});