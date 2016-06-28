var Main = (function ($, window) {
if (!$) {
        throw "MainPage.js requires jQuery";
    }
    var self = {};

    self.funcaoTeste = function(){
      prompt("Ola");
    }

    return self;
})(jQuery, window);