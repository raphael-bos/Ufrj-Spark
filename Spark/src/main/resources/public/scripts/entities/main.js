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

    var basicFilter = {
        bairros: [],
        disciplinas: [],
        anos: [],
        turmas: []
    };

    metodosBusca[ajax.Professores] = function(){
        loadSimbol();
        $.sendAjax(window.currentPath + ajax.Professores, JSON.stringify(basicFilter), function (retorno) {
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
        $.sendAjax(window.currentPath + ajax.Media, JSON.stringify(basicFilter), function(retorno){
          var legenda = retorno.disciplinas;
          legenda.unshift("Ano/Bimestre");
          for(i = 0; i < retorno.tempos.length ;i++)
            retorno.medias[i].unshift(retorno.tempos[i]);

          var dataTable = [legenda];
          dataTable = dataTable.concat(retorno.medias);

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
        $.sendAjax(window.currentPath + ajax.Aprovacao, JSON.stringify(basicFilter), function(retorno){
            var formatedData = retorno.aprovacao.map(function(x){return [x._1, x._2]});
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
        loadBasicFilter();
        metodosBusca[metodo]();
    }

    self.redrawAllCharts = function(){
      if(chart)
        chart.draw(data,options);
    }

    self.loadFilterOptions = function(){
        Sets.listaBairros.forEach(x => $("#selectBairros").append('<option value="'+ x +'">'+ x + '</option>'));
        Sets.disciplinas.forEach(x => $("#selectDisciplinas").append('<option value="'+ x.cod +'">'+ x.descr + '</option>'));
        Sets.anos.forEach(x => $("#selectAnos").append('<option value="'+ x +'">'+ x + '</option>'));
        Sets.turmas.forEach(x => $("#selectTurmas").append('<option value="'+ x.cod +'">'+ x.descr + '</option>'));
    }

    function loadSimbol(){
        $("#chart_div").empty();
        spinner.spin(document.getElementById("chart_div"));
    }

    function unload(){
        spinner.stop();
    }

    function loadBasicFilter(){
        if($("#todosBairros").is(":selected"))
            basicFilter.bairros = Sets.listaBairros.map(x => x);
        else {
            var options = $('#selectBairros option:selected');
            basicFilter.bairros = $.map(options ,function(option) {
                return option.value;
            });
        }

        if($("#todasDisciplinas").is(":selected"))
            basicFilter.disciplinas = Sets.disciplinas.map(x => x.cod);
        else {
            var options = $('#selectDisciplinas option:selected');
            basicFilter.disciplinas = $.map(options ,function(option) {
                return option.value;
            });
        }

        if($("#todosAnos").is(":selected"))
            basicFilter.anos = Sets.anos.map(x => x);
        else {
            var options = $('#selectAnos option:selected');
            basicFilter.anos = $.map(options ,function(option) {
                return option.text;
            });
        }

        if($("#todasTurmas").is(":selected"))
            basicFilter.turmas = Sets.turmas.map(x => x.cod);
        else {
            var options = $('#selectTurmas option:selected');
            basicFilter.turmas = $.map(options ,function(option) {
                return option.value;
            });
        }
        basicFilter
    }

    return self;

})(jQuery, window);