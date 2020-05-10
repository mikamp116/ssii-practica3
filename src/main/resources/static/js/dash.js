/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */

google.charts.load('current', {'packages': ['corechart', 'table']});

drawNumbers();
google.charts.setOnLoadCallback(drawChart);
google.charts.setOnLoadCallback(drawPrototipos);
google.charts.setOnLoadCallback(drawReglas);

async function getResponse(endpoint){
    let request = new Request('http://localhost:8080/' + endpoint, {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json'
        })
    });

    try {
        const response = await fetch(request);
        return response.json();
    } catch (err) {
        //
    }
}

async function drawNumbers() {
    let numeros = await getResponse('numeros');
    $('#num_pacientes').append(numeros[0]);
    $('#num_uci').append(numeros[1]);
    $('#num_fallecidos').append(numeros[2]);
}

async function drawChart() {

    /* GRAFICOS DE LINEAS START */
    let hechos = await getResponse('hechos');
    let tiempos = await getResponse('tiempos');

    let casosTotales = [[{label: 'Fecha', type: 'date'}, {label: 'Pacientes', type: 'number'}]];
    let casosActivos = [[{label: 'Fecha', type: 'date'}, {label: 'Pacientes', type: 'number'}]];

    for (let i = 0; i < tiempos.length; i++){
        let fecha = new Date(tiempos[i].anio, tiempos[i].mes - 1, tiempos[i].dia);
        casosActivos[i+1] = [fecha, 0];
        casosTotales[i+1] = [fecha, 0];
    }

    for (const hecho of hechos){
        let inicio = hecho.fechaIngresoId.tiempoId - tiempos[0].tiempoId;
        let fin = inicio + hecho.duracion;
        for (let i = inicio; i < fin; i++) {
            casosActivos[i + 1][1]++;
        }
        casosTotales[inicio + 1][1]++;
    }


    for (let i = 1; i < casosTotales.length - 1; i++) {
        casosTotales[i + 1][1] += casosTotales[i][1];
    }

    const dataActivos = google.visualization.arrayToDataTable(casosActivos);
    const dataTotales = google.visualization.arrayToDataTable(casosTotales);

    let options = {
        title: "Enfermos activos",
        width: 1200,
        height: 400,
        bar: {groupWidth: "95%"},
        legend: {position: "none"},
        hAxis: {format: 'dd/MM/yyyy'}
    };

    var lineas = new google.visualization.LineChart(document.getElementById('lineas-activo'));
    lineas.draw(dataActivos, options);


    var lineas2 = new google.visualization.LineChart(document.getElementById('lineas-total'));
    options.title = "Enfermos totales";
    lineas2.draw(dataTotales, options);
    $("#lineas-total").hide();


    /* GRAFICOS DE LINEAS END */

    /* GRAFICOS DE ROSCO START */
    for (let i = 1; i <= 5; i++) {

        let num_fallecidos = await getResponse('num_fallecidos/' + i.toString());
        let num_no_fallecidos = await getResponse('num_no_fallecidos/' + i.toString());

        var datarosco = google.visualization.arrayToDataTable([
            ['Task', 'Hours per Day'],
            ['Fallecidos', num_fallecidos],
            ['Dados de alta', num_no_fallecidos]
        ]);

        options = {
            title: 'Pacientes tratados con tratamiento ' + i.toString(),
        };

        var chartrosco = new google.visualization.PieChart(document.getElementById('rosco' + i.toString()));

        chartrosco.draw(datarosco, options);
    }
    /* GRAFICOS DE ROSCO END */


    /* GRAFICOS DE BARRAS START*/
    let compuestos = await getResponse('compuesto-recomendado');

    var chartContent = [[], [], [], []];
    var apariciones = [[], [], [], []];
    for (let i = 0; i < 4; i++) {
        chartContent[i][0] = ['Compuesto', 'Tercera opción', 'Segunda opción', 'Primera opción', 'Valor medio']
        for (let j = 1; j <= 20; j++)
            chartContent[i][j] = [j.toString(), 0, 0, 0, 0];
        for (let k = 0; k < 20; k++)
            apariciones[i][k] = 0
    }

    compuestos.forEach((compuesto) => {
        if (compuesto.item1 !== 0) {
            chartContent[compuesto.hospital - 1][compuesto.item1][3] += 1;
            chartContent[compuesto.hospital - 1][compuesto.item1][4] += compuesto.value1;
            apariciones[compuesto.hospital - 1][compuesto.item1 - 1] += 1;

            if (compuesto.item2 !== 0) {
                chartContent[compuesto.hospital - 1][compuesto.item2][2] += 1;
                chartContent[compuesto.hospital - 1][compuesto.item2][4] += compuesto.value2;
                apariciones[compuesto.hospital - 1][compuesto.item2 - 1] += 1;

                if (compuesto.item3 !== 0) {
                    chartContent[compuesto.hospital - 1][compuesto.item3][1] += 1;
                    chartContent[compuesto.hospital - 1][compuesto.item3][4] += compuesto.value3;
                    apariciones[compuesto.hospital - 1][compuesto.item3 - 1] += 1;
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

    var data1 = google.visualization.arrayToDataTable(chartContent[0]);
    var data2 = google.visualization.arrayToDataTable(chartContent[1]);
    var data3 = google.visualization.arrayToDataTable(chartContent[2]);
    var data4 = google.visualization.arrayToDataTable(chartContent[3]);

    options = {
        title: 'Compuestos recomendados en Hospital 1',
        width: 850,
        height: 410,
        legend: {position: 'bottom'},
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
    };

    $('#filtrado').show();
    var chart1 = new google.visualization.ComboChart(document.getElementById('chart1'));
    chart1.draw(data1, options);

    options.title = 'Compuestos recomendados en Hospital 2';
    var chart2 = new google.visualization.ComboChart(document.getElementById('chart2'));
    chart2.draw(data2, options);

    options.title = 'Compuestos recomendados en Hospital 3';
    var chart3 = new google.visualization.ComboChart(document.getElementById('chart3'));
    chart3.draw(data3, options);

    options.title = 'Compuestos recomendados en Hospital 4';
    var chart4 = new google.visualization.ComboChart(document.getElementById('chart4'));
    chart4.draw(data4, options);

    var datas = [];
    for (let i = 0; i < 4; i++) {
        datas[i] = new google.visualization.DataTable();
        datas[i].addColumn('number', 'Id paciente');
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
        line = [compuesto.pacienteId];
        line.push(compuesto.item1 !== 0 ? compuesto.item1 : NaN);
        line.push(compuesto.item2 !== 0 ? compuesto.item2 : NaN);
        line.push(compuesto.item3 !== 0 ? compuesto.item3 : NaN);
        datas[compuesto.hospital - 1].addRow(line)
    });

    options = {
        showRowNumber: false, width: '100%', height: '100%',
        page: 'enable',
        pageSize: 25
    };

    var table1 = new google.visualization.Table(document.getElementById('table1'));
    table1.draw(datas[0], options);

    var table2 = new google.visualization.Table(document.getElementById('table2'));
    table2.draw(datas[1], options);

    var table3 = new google.visualization.Table(document.getElementById('table3'));
    table3.draw(datas[2], options);

    var table4 = new google.visualization.Table(document.getElementById('table4'));
    table4.draw(datas[3], options);
    $('#filtrado').hide();

    /* GRAFICOS DE BARRAS END*/
}

function getPrototipoDataTable(prototipos){
    let prototiposDataTable = new google.visualization.DataTable();
    prototiposDataTable.addColumn('number', 'Edad');
    prototiposDataTable.addColumn('string', 'Sexo');
    prototiposDataTable.addColumn('number', 'IMC');
    prototiposDataTable.addColumn('number', 'Forma fisica');
    prototiposDataTable.addColumn('boolean', 'Tabaquismo');
    prototiposDataTable.addColumn('boolean', 'Alcoholismo');
    prototiposDataTable.addColumn('boolean', 'Colesterol');
    prototiposDataTable.addColumn('boolean', 'Hipertensión');
    prototiposDataTable.addColumn('boolean', 'Cardiopatía');
    prototiposDataTable.addColumn('boolean', 'Reuma');
    prototiposDataTable.addColumn('boolean', 'EPOC');
    prototiposDataTable.addColumn('number', 'Hepatitis');
    prototiposDataTable.addColumn('boolean', 'Cáncer');
    prototiposDataTable.addRows([
        [prototipos[0].edad, (prototipos[0].sexo === 1) ? 'M' : 'V', prototipos[0].imc, prototipos[0].formaFisica,
            prototipos[0].tabaquismo, prototipos[0].alcoholismo, prototipos[0].colesterol,
            prototipos[0].hipertension, prototipos[0].cardiopatia, prototipos[0].reuma,
            prototipos[0].epoc, prototipos[0].hepatitis, prototipos[0].cancer],
        [prototipos[1].edad, (prototipos[1].sexo === 1) ? 'M' : 'V', prototipos[1].imc, prototipos[1].formaFisica,
            prototipos[1].tabaquismo, prototipos[1].alcoholismo, prototipos[1].colesterol,
            prototipos[1].hipertension, prototipos[1].cardiopatia, prototipos[1].reuma,
            prototipos[1].epoc, prototipos[1].hepatitis, prototipos[1].cancer],
        [prototipos[2].edad, (prototipos[2].sexo === 1) ? 'M' : 'V', prototipos[2].imc, prototipos[2].formaFisica,
            prototipos[2].tabaquismo, prototipos[2].alcoholismo, prototipos[2].colesterol,
            prototipos[2].hipertension, prototipos[2].cardiopatia, prototipos[2].reuma,
            prototipos[2].epoc, prototipos[2].hepatitis, prototipos[2].cancer]
    ]);

    return prototiposDataTable;
}

async function drawPrototipos() {

    /* GRAFICOS DE PROTOTIPOS END */
    let optionsPrototipos = {showRowNumber: true, width: '100%', height: '100%'};

    $('#agrupamiento').show();

    let prototiposUci = await getResponse('prototipo_uci');
    let prototiposUciDataTable = getPrototipoDataTable(prototiposUci);
    let tableUci = new google.visualization.Table(document.getElementById('prototipoUci'));
    tableUci.draw(prototiposUciDataTable, {showRowNumber: true, width: '100%', height: '100%'});

    let prototiposFallecido = await getResponse('prototipo_fallecido');
    let prototiposFallecidoDataTable = getPrototipoDataTable(prototiposFallecido);
    let tableFallecido = new google.visualization.Table(document.getElementById('prototipoFallecido'));
    tableFallecido.draw(prototiposFallecidoDataTable, optionsPrototipos);

    let prototiposResto = await getResponse('prototipo_resto');
    let prototiposRestoDataTable = getPrototipoDataTable(prototiposResto);
    let tableResto = new google.visualization.Table(document.getElementById('prototipoResto'));
    tableResto.draw(prototiposRestoDataTable, optionsPrototipos);
    $('#agrupamiento').hide();
}

function getReglasDataTable(reglas){
    let reglasDataTable = new google.visualization.DataTable();

    reglasDataTable.addColumn('boolean', '1');
    reglasDataTable.addColumn('boolean', '2');
    reglasDataTable.addColumn('boolean', '3');
    reglasDataTable.addColumn('boolean', '4');
    reglasDataTable.addColumn('boolean', '5');
    reglasDataTable.addColumn('boolean', '6');
    reglasDataTable.addColumn('boolean', '7');
    reglasDataTable.addColumn('boolean', '8');
    reglasDataTable.addColumn('boolean', '9');
    reglasDataTable.addColumn('boolean', '10');
    reglasDataTable.addColumn('boolean', '11');
    reglasDataTable.addColumn('boolean', '12');
    reglasDataTable.addColumn('boolean', '13');
    reglasDataTable.addColumn('boolean', '14');
    reglasDataTable.addColumn('boolean', '15');
    reglasDataTable.addColumn('boolean', '16');
    reglasDataTable.addColumn('boolean', '17');
    reglasDataTable.addColumn('boolean', '18');
    reglasDataTable.addColumn('boolean', '19');
    reglasDataTable.addColumn('boolean', '20');

    reglasDataTable.addColumn('string', '→');

    reglasDataTable.addColumn('number', 'Recomendado');
    reglasDataTable.addColumn('number', 'Confidence');
    reglasDataTable.addColumn('number', 'Lift');
    /*rreglass.addColumn('number', 'Lev');
    rreglass.addColumn('number', 'Conv');*/

    let arr = [];

     reglas.forEach((regla) => {
            let probados = regla.probados.split(',');
            let content = [null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '→'];
            for (let i = 0; i < probados.length; i++) {
                let compuesto = parseInt(probados[i]);
                content[compuesto - 1] = true
            }
            content[21] = parseInt(regla.recomendado);
            content[22] = parseFloat(regla.conf);
            content[23] = parseFloat(regla.lift);
            /*content[24] = parseFloat(regla.lev)
            content[25] = parseFloat(regla.conv)*/
            arr.push(content)
        });

    reglasDataTable.addRows(arr);
    return reglasDataTable;
}

async function drawReglas() {

    $('#reglas').show();
    let reglasExito = await getResponse('reglas_exito');
    let tableExito = new google.visualization.Table(document.getElementById('reglasExito'));
    let reglasExitoData = getReglasDataTable(reglasExito);
    tableExito.draw(reglasExitoData, {showRowNumber: true, width: '100%', height: '100%'});

    let reglasFallo = await getResponse('reglas_fallo');
    let tableFallo = new google.visualization.Table(document.getElementById('reglasFallo'));
    let reglasFalloData = getReglasDataTable(reglasFallo);
    tableFallo.draw(reglasFalloData, {showRowNumber: true, width: '100%', height: '100%'});
    $('#reglas').hide();

}


