var Main = (function ($, window) {
if (!$) {
        throw "MainPage.js requires jQuery";
    }

    var ajax = {
        metodo: "Metodo",
        outroMetodo: "OutroMetodo"
    };

    var self = {};

    self.outraFuncao = function(){
        prompt("Ola");
    }

    self.funcaoTeste = function(){
        $.sendAjax(window.currentPath + ajax.metodo, JSON.stringify({ Id: 10 }), function (retorno) {
            var l = retorno.length;
            for(var i = 0; i < retorno.length; i++){
                console.log("Numero: " + retorno[i]);
            }
        });
    }

    return self;
})(jQuery, window);