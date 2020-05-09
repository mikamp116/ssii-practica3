google.charts.load('current', {'packages': ['corechart', 'table']});

google.charts.setOnLoadCallback(drawChart);
google.charts.setOnLoadCallback(drawPrototipos);
google.charts.setOnLoadCallback(drawReglas);

async function drawChart() {
    let request = new Request('http://localhost:8080/compuesto-recomendado', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var compuestos = await fullresponse.json();
    } catch (err) {
        //
    }

    var chartContent = [[], [], [], []]
    var apariciones = [[], [], [], []]
    for (let i = 0; i < 4; i++) {
        chartContent[i][0] = ['Compuesto', 'Tercera opción', 'Segunda opción', 'Primera opción', 'Valor medio']
        for (let j = 1; j <= 20; j++)
            chartContent[i][j] = [j.toString(), 0, 0, 0, 0]
        for (let k = 0; k < 20; k++)
            apariciones[i][k] = 0
    }

    compuestos.forEach((compuesto) => {
        if (compuesto.item1 !== 0) {
            chartContent[compuesto.hospital - 1][compuesto.item1][3] += 1
            chartContent[compuesto.hospital - 1][compuesto.item1][4] += compuesto.value1
            apariciones[compuesto.hospital - 1][compuesto.item1 - 1] += 1

            if (compuesto.item2 !== 0) {
                chartContent[compuesto.hospital - 1][compuesto.item2][2] += 1
                chartContent[compuesto.hospital - 1][compuesto.item2][4] += compuesto.value2
                apariciones[compuesto.hospital - 1][compuesto.item2 - 1] += 1

                if (compuesto.item3 !== 0) {
                    chartContent[compuesto.hospital - 1][compuesto.item3][1] += 1
                    chartContent[compuesto.hospital - 1][compuesto.item3][4] += compuesto.value3
                    apariciones[compuesto.hospital - 1][compuesto.item3 - 1] += 1
                }
            }
        }
    });
    for (let i = 0; i < 4; i++) {
        for (let j = 1; j < 21; j++) {
            if (apariciones[i][j - 1] !== 0)
                chartContent[i][j][4] = chartContent[i][j][4] / apariciones[i][j - 1]
        }
    }
    console.log(chartContent)
    console.log(apariciones)
    /*
        var primera_opcion = new Array(20);
        var segunda_opcion = new Array(20);
        var tercera_opcion = new Array(20);
        for (let x = 0; x < 20; x++){
            primera_opcion[x]=0
            segunda_opcion[x]=0
            tercera_opcion[x]=0
        }*/

    /* var data = google.visualization.arrayToDataTable([
         ['Compuesto', 'Tercera opción', 'Segunda opción', 'Primera opción'],
         ['1', tercera_opcion[0], segunda_opcion[0], primera_opcion[0]],
         ['2', tercera_opcion[1], segunda_opcion[1], primera_opcion[1]],
         ['3', tercera_opcion[2], segunda_opcion[2], primera_opcion[2]],
         ['4', tercera_opcion[3], segunda_opcion[3], primera_opcion[3]],
         ['5', tercera_opcion[4], segunda_opcion[4], primera_opcion[4]],
         ['6', tercera_opcion[5], segunda_opcion[5], primera_opcion[5]],
         ['7', tercera_opcion[6], segunda_opcion[6], primera_opcion[6]],
         ['8', tercera_opcion[7], segunda_opcion[7], primera_opcion[7]],
         ['9', tercera_opcion[8], segunda_opcion[8], primera_opcion[8]],
         ['10', tercera_opcion[9], segunda_opcion[9], primera_opcion[9]],
         ['11', tercera_opcion[10], segunda_opcion[10], primera_opcion[10]],
         ['12', tercera_opcion[11], segunda_opcion[11], primera_opcion[11]],
         ['13', tercera_opcion[12], segunda_opcion[12], primera_opcion[12]],
         ['14', tercera_opcion[13], segunda_opcion[13], primera_opcion[13]],
         ['15', tercera_opcion[14], segunda_opcion[14], primera_opcion[14]],
         ['16', tercera_opcion[15], segunda_opcion[15], primera_opcion[15]],
         ['17', tercera_opcion[16], segunda_opcion[16], primera_opcion[16]],
         ['18', tercera_opcion[17], segunda_opcion[17], primera_opcion[17]],
         ['19', tercera_opcion[18], segunda_opcion[18], primera_opcion[18]],
         ['20', tercera_opcion[19], segunda_opcion[19], primera_opcion[19]]
     ]);*/

    var data1 = google.visualization.arrayToDataTable(chartContent[0]);
    var data2 = google.visualization.arrayToDataTable(chartContent[1]);
    var data3 = google.visualization.arrayToDataTable(chartContent[2]);
    var data4 = google.visualization.arrayToDataTable(chartContent[3]);

    var options = {
        title: 'Compuestos recomendados en Hospital 1',
        width: 400,
        height: 300,
        legend: {position: 'top', maxLines: 3},
        isStacked: true,
        vAxes: {0: {title: 'Veces recomendado'}, 1: {title: 'Valoración de efecto estimado'}},
        hAxis: {title: 'Compuesto'},
        seriesType: 'bars',
        series: {
            0: {targetAxisIndex: 0},
            1: {targetAxisIndex: 0},
            2: {targetAxisIndex: 0},
            3: {targetAxisIndex: 1, type: 'line'}
        }
    }

    var chart1 = new google.visualization.ComboChart(document.getElementById('chart1'));
    chart1.draw(data1, options);

    options.title = 'Compuestos recomendados en Hospital 2'
    var chart2 = new google.visualization.ComboChart(document.getElementById('chart2'));
    chart2.draw(data2, options);

    options.title = 'Compuestos recomendados en Hospital 3'
    var chart3 = new google.visualization.ComboChart(document.getElementById('chart3'));
    chart3.draw(data3, options);

    options.title = 'Compuestos recomendados en Hospital 4'
    var chart4 = new google.visualization.ComboChart(document.getElementById('chart4'));
    chart4.draw(data4, options);


    var datas = []
    for (let i = 0; i < 4; i++) {
        datas[i] = new google.visualization.DataTable();
        datas[i].addColumn('number', 'Identificador paciente');
        datas[i].addColumn('number', 'Primer compuesto');
        datas[i].addColumn('number', 'Segundo compuesto');
        datas[i].addColumn('number', 'Tercer compuesto');
    }
    /*data.addRows([
        ['Mike', {v: 10000, f: '$10,000'}, true],
        ['Jim', {v: 8000, f: '$8,000'}, false],
        ['Alice', {v: 12500, f: '$12,500'}, true],
        ['Bob', {v: 7000, f: '$7,000'}, true]
    ]);*/

    compuestos.forEach((compuesto) => {
        line = [compuesto.pacienteId]
        line.push(compuesto.item1 !== 0 ? compuesto.item1 : NaN)
        line.push(compuesto.item2 !== 0 ? compuesto.item2 : NaN)
        line.push(compuesto.item3 !== 0 ? compuesto.item3 : NaN)
        datas[compuesto.hospital - 1].addRow(line)
    });

    var table1 = new google.visualization.Table(document.getElementById('table1'));
    table1.draw(datas[0], {showRowNumber: false, width: '40%', height: '10%'});

    var table2 = new google.visualization.Table(document.getElementById('table2'));
    table2.draw(datas[1], {showRowNumber: false, width: '40%', height: '5%'});

    var table3 = new google.visualization.Table(document.getElementById('table3'));
    table3.draw(datas[2], {showRowNumber: false, width: '40%', height: '10%'});

    var table4 = new google.visualization.Table(document.getElementById('table4'));
    table4.draw(datas[3], {showRowNumber: false, width: '40%', height: '10%'});



    request = new Request('http://localhost:8080/hechos', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var hechos = await fullresponse.json();
    } catch (err) {
        //
    }

    request = new Request('http://localhost:8080/tiempos', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var tiempos = await fullresponse.json();
    } catch (err) {
        //
    }

    var casosTotales = [['Fecha', 'Pacientes']]
    var casosActivos = [['Fecha', 'Pacientes']]
    for (let i = 510; i < 795; i++) {
        casosActivos[i-509] = [tiempos[i].fecha, parseInt('0')]
        casosTotales[i-509] = [tiempos[i].fecha, parseInt('0')]
    }
    hechos.forEach((hecho) => {
        console.log(hecho)
        let inicio = hecho.fechaIngresoId.tiempoId - 510
        let fin = inicio + hecho.duracion
        for (let i = inicio; i < fin; i++) {
            casosActivos[i+1][1]++
        }
        casosTotales[inicio+1][1]++
    })

    for (let i = 1; i < casosTotales.length-1; i++) {
        casosTotales[i+1][1] += casosTotales[i][1]
    }

    var data = google.visualization.arrayToDataTable(casosActivos);

    var view = new google.visualization.DataView(data);
    /*view.setColumns([0, 1,
        { calc: "stringify",
            sourceColumn: 1,
            type: "string",
            role: "annotation" },
        2]);*/

    var options = {
        title: "Enfermos activos",
        width: 1200,
        height: 400,
        bar: {groupWidth: "95%"},
        legend: { position: "none" },
    };

    var graphe = new google.visualization.ColumnChart(document.getElementById("table"));
    graphe.draw(view, options);

    var datar = google.visualization.arrayToDataTable(casosTotales);
    options.title = "Enfermos totales"

    var graphe2 = new google.visualization.ColumnChart(document.getElementById("tablee"));
    graphe2.draw(new google.visualization.DataView(datar), options);
    options.title = "Enfermos activos"
    var lineas = new google.visualization.LineChart(document.getElementById('lineas-activo'));

    lineas.draw(data, options);
    options.title = "Enfermos totales"
    var lineas2 = new google.visualization.LineChart(document.getElementById('lineas-total'));

    // lineas2.draw(datar, options);



    for (let i = 1; i <= 5; i++) {
        request = new Request('http://localhost:8080/num_fallecidos/'+i.toString(), {
            method: 'GET',
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        });

        try {
            const fullresponse = await fetch(request);
            num_fallecidos = await fullresponse.json();
        } catch (err) {
            //
        }
        request = new Request('http://localhost:8080/num_no_fallecidos/'+i.toString(), {
            method: 'GET',
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        });

        try {
            const fullresponse = await fetch(request);
            num_no_fallecidos = await fullresponse.json();
        } catch (err) {
            //
        }

        var datarosco = google.visualization.arrayToDataTable([
            ['Task', 'Hours per Day'],
            ['Fallecidos', num_fallecidos],
            ['Dados de alta', num_no_fallecidos]
        ]);

        var options = {
            title: 'Pacientes tratados con tratamiento ' + i.toString()
        };

        var chartrosco = new google.visualization.PieChart(document.getElementById('rosco'+i.toString()));

        chartrosco.draw(datarosco, options);
    }

}

async function drawPrototipos() {

    var request = new Request('http://localhost:8080/prototipo_uci', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var protiposUci = await fullresponse.json();
    } catch (err) {
        //
    }

    var protos = new google.visualization.DataTable();
    protos.addColumn('number', 'Edad');
    protos.addColumn('string', 'Sexo');
    protos.addColumn('number', 'IMC');
    protos.addColumn('number', 'Forma fisica');
    protos.addColumn('boolean', 'Tabaquismo');
    protos.addColumn('boolean', 'Alcoholismo');
    protos.addColumn('boolean', 'Colesterol');
    protos.addColumn('boolean', 'Hipertensión');
    protos.addColumn('boolean', 'Cardiopatía');
    protos.addColumn('boolean', 'Reuma');
    protos.addColumn('boolean', 'EPOC');
    protos.addColumn('number', 'Hepatitis');
    protos.addColumn('boolean', 'Cáncer');
    protos.addRows([
        [protiposUci[0].edad, (protiposUci[0].sexo===1) ?'M':'V', protiposUci[0].imc, protiposUci[0].formaFisica,
            protiposUci[0].tabaquismo, protiposUci[0].alcoholismo, protiposUci[0].colesterol,
            protiposUci[0].hipertension, protiposUci[0].cardiopatia, protiposUci[0].reuma, 
            protiposUci[0].epoc, protiposUci[0].hepatitis, protiposUci[0].cancer],
        [protiposUci[1].edad, (protiposUci[1].sexo===1) ?'M':'V', protiposUci[1].imc, protiposUci[1].formaFisica,
            protiposUci[1].tabaquismo, protiposUci[1].alcoholismo, protiposUci[1].colesterol,
            protiposUci[1].hipertension, protiposUci[1].cardiopatia, protiposUci[1].reuma,
            protiposUci[1].epoc, protiposUci[1].hepatitis, protiposUci[1].cancer],
        [protiposUci[2].edad, (protiposUci[2].sexo==1) ?'M':'V', protiposUci[2].imc, protiposUci[2].formaFisica,
            protiposUci[2].tabaquismo, protiposUci[2].alcoholismo, protiposUci[2].colesterol,
            protiposUci[2].hipertension, protiposUci[2].cardiopatia, protiposUci[2].reuma,
            protiposUci[2].epoc, protiposUci[2].hepatitis, protiposUci[2].cancer]
    ]);

    var table = new google.visualization.Table(document.getElementById('prototipoUci'));

    table.draw(protos, {showRowNumber: true, width: '100%', height: '100%'});
    
    

    request = new Request('http://localhost:8080/prototipo_fallecido', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var protiposFallecido = await fullresponse.json();
    } catch (err) {
        //
    }

    var protos = new google.visualization.DataTable();
    protos.addColumn('number', 'Edad');
    protos.addColumn('string', 'Sexo');
    protos.addColumn('number', 'IMC');
    protos.addColumn('number', 'Forma fisica');
    protos.addColumn('boolean', 'Tabaquismo');
    protos.addColumn('boolean', 'Alcoholismo');
    protos.addColumn('boolean', 'Colesterol');
    protos.addColumn('boolean', 'Hipertensión');
    protos.addColumn('boolean', 'Cardiopatía');
    protos.addColumn('boolean', 'Reuma');
    protos.addColumn('boolean', 'EPOC');
    protos.addColumn('number', 'Hepatitis');
    protos.addColumn('boolean', 'Cáncer');
    protos.addRows([
        [protiposFallecido[0].edad, (protiposFallecido[0].sexo===1) ?'M':'V', protiposFallecido[0].imc, protiposFallecido[0].formaFisica,
            protiposFallecido[0].tabaquismo, protiposFallecido[0].alcoholismo, protiposFallecido[0].colesterol,
            protiposFallecido[0].hipertension, protiposFallecido[0].cardiopatia, protiposFallecido[0].reuma,
            protiposFallecido[0].epoc, protiposFallecido[0].hepatitis, protiposFallecido[0].cancer],
        [protiposFallecido[1].edad, (protiposFallecido[1].sexo===1) ?'M':'V', protiposFallecido[1].imc, protiposFallecido[1].formaFisica,
            protiposFallecido[1].tabaquismo, protiposFallecido[1].alcoholismo, protiposFallecido[1].colesterol,
            protiposFallecido[1].hipertension, protiposFallecido[1].cardiopatia, protiposFallecido[1].reuma,
            protiposFallecido[1].epoc, protiposFallecido[1].hepatitis, protiposFallecido[1].cancer],
        [protiposFallecido[2].edad, (protiposFallecido[2].sexo===1) ?'M':'V', protiposFallecido[2].imc, protiposFallecido[2].formaFisica,
            protiposFallecido[2].tabaquismo, protiposFallecido[2].alcoholismo, protiposFallecido[2].colesterol,
            protiposFallecido[2].hipertension, protiposFallecido[2].cardiopatia, protiposFallecido[2].reuma,
            protiposFallecido[2].epoc, protiposFallecido[2].hepatitis, protiposFallecido[2].cancer]
    ]);

    table = new google.visualization.Table(document.getElementById('prototipoFallecido'));

    table.draw(protos, {showRowNumber: true, width: '100%', height: '100%'});




    request = new Request('http://localhost:8080/prototipo_resto', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var protiposResto = await fullresponse.json();
    } catch (err) {
        //
    }

    var protos = new google.visualization.DataTable();
    protos.addColumn('number', 'Edad');
    protos.addColumn('string', 'Sexo');
    protos.addColumn('number', 'IMC');
    protos.addColumn('number', 'Forma fisica');
    protos.addColumn('boolean', 'Tabaquismo');
    protos.addColumn('boolean', 'Alcoholismo');
    protos.addColumn('boolean', 'Colesterol');
    protos.addColumn('boolean', 'Hipertensión');
    protos.addColumn('boolean', 'Cardiopatía');
    protos.addColumn('boolean', 'Reuma');
    protos.addColumn('boolean', 'EPOC');
    protos.addColumn('number', 'Hepatitis');
    protos.addColumn('boolean', 'Cáncer');
    protos.addRows([
        [protiposResto[0].edad, (protiposResto[0].sexo===1) ?'M':'V', protiposResto[0].imc, protiposResto[0].formaFisica,
            protiposResto[0].tabaquismo, protiposResto[0].alcoholismo, protiposResto[0].colesterol,
            protiposResto[0].hipertension, protiposResto[0].cardiopatia, protiposResto[0].reuma,
            protiposResto[0].epoc, protiposResto[0].hepatitis, protiposResto[0].cancer],
        [protiposResto[1].edad, (protiposResto[1].sexo===1) ?'M':'V', protiposResto[1].imc, protiposResto[1].formaFisica,
            protiposResto[1].tabaquismo, protiposResto[1].alcoholismo, protiposResto[1].colesterol,
            protiposResto[1].hipertension, protiposResto[1].cardiopatia, protiposResto[1].reuma,
            protiposResto[1].epoc, protiposResto[1].hepatitis, protiposResto[1].cancer],
        [protiposResto[2].edad, (protiposResto[2].sexo===1) ?'M':'V', protiposResto[2].imc, protiposResto[2].formaFisica,
            protiposResto[2].tabaquismo, protiposResto[2].alcoholismo, protiposResto[2].colesterol,
            protiposResto[2].hipertension, protiposResto[2].cardiopatia, protiposResto[2].reuma,
            protiposResto[2].epoc, protiposResto[2].hepatitis, protiposResto[2].cancer]
    ]);

    table = new google.visualization.Table(document.getElementById('prototipoResto'));

    table.draw(protos, {showRowNumber: true, width: '100%', height: '100%'});
}

async function drawReglas() {

    var request = new Request('http://localhost:8080/reglas_exito', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var reglasExito = await fullresponse.json();
    } catch (err) {
        //
    }

    var rreglass = new google.visualization.DataTable();
    rreglass.addColumn('boolean', '1');
    rreglass.addColumn('boolean', '2');
    rreglass.addColumn('boolean', '3');
    rreglass.addColumn('boolean', '4');
    rreglass.addColumn('boolean', '5');
    rreglass.addColumn('boolean', '6');
    rreglass.addColumn('boolean', '7');
    rreglass.addColumn('boolean', '8');
    rreglass.addColumn('boolean', '9');
    rreglass.addColumn('boolean', '10');
    rreglass.addColumn('boolean', '11');
    rreglass.addColumn('boolean', '12');
    rreglass.addColumn('boolean', '13');
    rreglass.addColumn('boolean', '14');
    rreglass.addColumn('boolean', '15');
    rreglass.addColumn('boolean', '16');
    rreglass.addColumn('boolean', '17');
    rreglass.addColumn('boolean', '18');
    rreglass.addColumn('boolean', '19');
    rreglass.addColumn('boolean', '20');

    rreglass.addColumn('string', '→');

    rreglass.addColumn('number', 'Recomendado');
    rreglass.addColumn('number', 'Conf');
    rreglass.addColumn('number', 'Lift');
    rreglass.addColumn('number', 'Lev');
    rreglass.addColumn('number', 'Conv');

    let arr = []

    reglasExito.forEach((regla) => {
        let probados = regla.probados.split(',')
        let content = [null, null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null, '→']
        for (let i = 0; i < probados.length; i++) {
            let compuesto = parseInt(probados[i])
            content[compuesto-1] = true
        }
        content[21] = parseInt(regla.recomendado)
        content[22] = parseFloat(regla.conf)
        content[23] = parseFloat(regla.lift)
        content[24] = parseFloat(regla.lev)
        content[25] = parseFloat(regla.conv)
        arr.push(content)
    })

    rreglass.addRows(arr);

    var table = new google.visualization.Table(document.getElementById('reglasExito'));

    table.draw(rreglass, {showRowNumber: true, width: '100%', height: '100%'});



    request = new Request('http://localhost:8080/reglas_fallo', {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const fullresponse = await fetch(request);
        var reglasFallo = await fullresponse.json();
    } catch (err) {
        //
    }

    rreglass = new google.visualization.DataTable();
    rreglass.addColumn('boolean', '1');
    rreglass.addColumn('boolean', '2');
    rreglass.addColumn('boolean', '3');
    rreglass.addColumn('boolean', '4');
    rreglass.addColumn('boolean', '5');
    rreglass.addColumn('boolean', '6');
    rreglass.addColumn('boolean', '7');
    rreglass.addColumn('boolean', '8');
    rreglass.addColumn('boolean', '9');
    rreglass.addColumn('boolean', '10');
    rreglass.addColumn('boolean', '11');
    rreglass.addColumn('boolean', '12');
    rreglass.addColumn('boolean', '13');
    rreglass.addColumn('boolean', '14');
    rreglass.addColumn('boolean', '15');
    rreglass.addColumn('boolean', '16');
    rreglass.addColumn('boolean', '17');
    rreglass.addColumn('boolean', '18');
    rreglass.addColumn('boolean', '19');
    rreglass.addColumn('boolean', '20');

    rreglass.addColumn('string', '→');

    rreglass.addColumn('number', 'Recomendado');
    rreglass.addColumn('number', 'Conf');
    rreglass.addColumn('number', 'Lift');
    rreglass.addColumn('number', 'Lev');
    rreglass.addColumn('number', 'Conv');

    arr = []

    reglasFallo.forEach((regla) => {
        let probados = regla.probados.split(',')
        let content = [null, null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null, '→']
        for (let i = 0; i < probados.length; i++) {
            let compuesto = parseInt(probados[i])
            content[compuesto-1] = false
        }
        content[21] = parseInt(regla.recomendado)
        content[22] = parseFloat(regla.conf)
        content[23] = parseFloat(regla.lift)
        content[24] = parseFloat(regla.lev)
        content[25] = parseFloat(regla.conv)
        arr.push(content)
    })

    rreglass.addRows(arr);

    var table = new google.visualization.Table(document.getElementById('reglasFallo'));

    table.draw(rreglass, {showRowNumber: true, width: '100%', height: '100%'});
}


$('.numeros').each(function () {
    $(this).prop('Counter',0).animate({
        Counter: $(this).text()
    }, {
        duration: 4000,
        easing: 'swing',
        step: function (now) {
            $(this).text(Math.ceil(now));
        }
    });
});
