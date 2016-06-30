var Main = (function ($, window) {
if (!$) {
        throw "MainPage.js requires jQuery";
    }

    var ajax = {
        Professores: "professores",
        Media: "medias",
        Aprovacao: "aprovacao"
    };

    var titleSize = 26;
    var pieHole = 0.4;

    var self = {};

    var data;
    var options;
    var chart;

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

    var metodosBusca = {};

    metodosBusca[ajax.Professores] = function(){
        loadSimbol();
        $.sendAjax(window.currentPath + ajax.Professores, JSON.stringify(""), function (retorno) {
            var formatedData = retorno.map(function(x){return [x.descricao_materia, x.quantidade]})
                .sort((y,x) => x[1] - y[1]); //Ordenar por quantidade
            var dataTable = [["Professores", "Quantidade"]];
            dataTable = dataTable.concat(formatedData);
            data = google.visualization.arrayToDataTable(dataTable);
            options = {
                title: 'Distribuicao de Professores',
                titleTextStyle: { fontSize: titleSize },
                pieHole: pieHole,
                chartArea: {width: '90%', heigth: '90%'},
            };

            chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            chart.draw(data, options);
            unload();
        });
    }

    metodosBusca[ajax.Media] = function(){
        loadSimbol();
        $.sendAjax(window.currentPath + ajax.Media, JSON.stringify(""), function(retorno){
          var formatedData = retorno.map(function(x){return {ano: x.ano, bimestre: x.bimestre, media: x.media, descricao: x.descricao_materia}})
          .sort(function(y,x){
            if(parseInt(y.ano) != parseInt(x.ano))
              return y.ano - x.ano;
            return y.bimestre - x.bimestre;
          })
          .map(function(x){ return [(x.ano + "/" + x.bimestre),x.media]})
          var dataTable = [["Ano/Bimestre", "Matematica"]];
          dataTable = dataTable.concat(formatedData);
          data = google.visualization.arrayToDataTable(dataTable);
          options = {
              title: 'Evolucao da Media',
              titleTextStyle: { fontSize: titleSize },
              curveType: 'function',
              legend: { position: 'bottom' },
              chartArea: {width: '90%', heigth: '90%'},
          };
          chart = new google.visualization.LineChart(document.getElementById('chart_div'));
          chart.draw(data, options);
          unload();
        });
    }

    metodosBusca[ajax.Aprovacao] = function(){
        loadSimbol();
        $.sendAjax(window.currentPath + ajax.Aprovacao, JSON.stringify(""), function(retorno){
            var formatedData = retorno.map(function(x){return [(x.nome + " - " + x.bairro),x.info]});
            var dataTable = [["Escola - Bairro", "Aproveitamento %"]];
            dataTable = dataTable.concat(formatedData);
            data = google.visualization.arrayToDataTable(dataTable);
            options = {
                title: 'Melhores Aproveitamentos',
                titleTextStyle: { fontSize: titleSize },
                curveType: 'function',
                legend: { position: 'bottom' },
                chartArea: {width: '90%', heigth: '90%'},
            };
            chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
            chart.draw(data, options);
            unload();
        });
    }

    var spinner = new Spinner(spinOpts);

    self.buscarDados = function(){
        var metodo = $('#selecao').val();
        metodosBusca[metodo]();
    }

    self.redrawAllCharts = function(){
      if(chart)
        chart.draw(data,options);
    }

    function loadSimbol(){
        $("#chart_div").empty();
        spinner.spin(document.getElementById("chart_div"));
    }

    function unload(){
        spinner.stop();
    }

    return self;
})(jQuery, window);