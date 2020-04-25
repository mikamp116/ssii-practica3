// Load the Visualization API and the corechart package.
google.charts.load('current', {'packages':['corechart', 'controls', 'bar']});

// Set a callback to run when the Google Visualization API is loaded.
google.charts.setOnLoadCallback(drawDiariosDashboard);
google.charts.setOnLoadCallback(drawSemanalesDashboard);

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawDiariosDashboard() {

    let dashboard = new google.visualization.Dashboard(
        document.getElementById('diarios_dashboard'));


    // Create the data table.
    let data = new google.visualization.arrayToDataTable([
        ['Hospital', 'Ingresos', 'Altas', 'Fallecimientos', 'UCI'],
        ['Hospital 1', 5, 1, 2, 6],
        ['Hospital 2', 6, 2, 5, 6],
        ['Hospital 3', 2, 8, 1, 2],
        ['Hospital 4', 8, 0, 4, 10]
    ]);

    let hospitalSelector = new google.visualization.ControlWrapper({
        'controlType': 'CategoryFilter',
        'containerId': 'diarios_controls',
        'options': {
            'filterColumnLabel': 'Hospital',
            'ui.caption' : 'General'
        },
        'state' : { 'selected_values' : ['Hospital 1', 'Hospital 2', 'Hospital 3', 'Hospital 4']}
    });


    // Instantiate and draw our chart, passing in some options.
    let ingresosDiarios = new google.visualization.ChartWrapper({
        'chartType': 'Bar',
        'containerId': 'diarios_charts',
        'title':'Datos diarios',
        'options': {
            'width': 500,
            'height': 400,
        }
    });

    dashboard.bind(hospitalSelector, ingresosDiarios);

    dashboard.draw(data);
}

function drawSemanalesDashboard() {

    let dashboard = new google.visualization.Dashboard(
        document.getElementById('semanales_dashboard'));


    // Create the data table.
    let data = new google.visualization.DataTable();
    data.addColumn('number', 'Hospital');
    data.addColumn('number', 'Nuevos ingresos');
    data.addColumn('number', 'Fallecimientos');
    data.addColumn('number', 'UCI');
    data.addColumn('number', 'Altas');

    data.addRows([
        [1,5,8,6,2],
        [2,4,8,8,4],
        [3,1,4,2,2],
        [4,3,8,6,2],
        [5,6,6,6,3]
    ]);

    let hospitalSelector = new google.visualization.ControlWrapper({
        'controlType': 'CategoryFilter',
        'containerId': 'semanales_controls',
        'options': {
            'filterColumnLabel': 'Hospital',
            'ui.caption' : 'General'
        },
    });


    // Instantiate and draw our chart, passing in some options.
    let ingresosSemanales = new google.visualization.ChartWrapper({
        'chartType': 'Bar',
        'containerId': 'semanales_charts',
        'title':'Datos semanales',
        'options': {
            'width': 500,
            'height': 400,
        }
    });

    dashboard.bind(hospitalSelector, ingresosSemanales);

    dashboard.draw(data);
}
