var Main = (function ($, window) {
if (!$) {
        throw "MainPage.js requires jQuery";
    }

    var ajax = {
        metodo: "Metodo",
        outroMetodo: "OutroMetodo"
    };

    var self = {};

    var grafico;

    var spinOpts = {
          lines: 12             // The number of lines to draw
        , length: 7             // The length of each line
        , width: 5              // The line thickness
        , radius: 10            // The radius of the inner circle
        , scale: 3.0            // Scales overall size of the spinner
        , corners: 1            // Roundness (0..1)
        , color: '#000'         // #rgb or #rrggbb
        , opacity: 1/4          // Opacity of the lines
        , rotate: 0             // Rotation offset
        , direction: 1          // 1: clockwise, -1: counterclockwise
        , speed: 1              // Rounds per second
        , trail: 100            // Afterglow percentage
        , fps: 20               // Frames per second when using setTimeout()
        , zIndex: 2e9           // Use a high z-index by default
        , className: 'spinner'  // CSS class to assign to the element
        , top: '50%'            // center vertically
        , left: '50%'           // center horizontally
        , shadow: false         // Whether to render a shadow
        , hwaccel: false        // Whether to use hardware acceleration (might be buggy)
        , position: 'absolute'  // Element positioning
        }
    var spinner = new Spinner(spinOpts);

    self.buscarDados = function(){
        spinner.spin(document.getElementById("chart_div"));
        var metodo = $('#selecao').val()
        $.sendAjax(window.currentPath + metodo, JSON.stringify(""), function (retorno) {
            var formatedData = retorno.map(function(x){return [x.descricao_materia, x.quantidade]})
                .sort((y,x) => x[1] - y[1]); //Ordenar por quantidade
            var dataTable = [["Professores", "Quantidade"]];
            dataTable = dataTable.concat(formatedData);
            var data = google.visualization.arrayToDataTable(dataTable);
            var options = {
                title: 'Distribuicao de Professores',
                pieHole: 0.4,
                };

            var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            grafico = chart.draw(data, options);
            grafico();
            spinner.stop();
        });
    }

    self.redrawAllCharts = function(){
      if(grafico)
        grafico();
    }

    function drawChart() {
        var data = google.visualization.arrayToDataTable([
        ['Task', 'Hours per Day'],
        ['Work',     11],
        ['Eat',      2],
        ['Commute',  2],
        ['Watch TV', 2],
        ['Sleep',    7]
        ]);

        var options = {
            title: 'My Daily Activities',
            pieHole: 0.4,
            };

            var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            chart.draw(data, options);
    }

    return self;
})(jQuery, window);