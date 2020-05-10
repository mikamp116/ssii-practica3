const homeBtn = $("#home");
const reglasBtn = $("#asociacion-btn")
const agrupamientoBtn = $("#agrupamiento-btn")
const filtradoBtn = $("#filtrado-btn");

const homeSection = $("#principal");
const reglasSection = $("#reglas")
const agrupamientoSection = $("#agrupamiento")
const filtradoSection = $("#filtrado")

const lineasActivoBtn = $("#lineas-activo-btn")
const lineasTotalBtn = $("#lineas-total-btn")

const lineasActivoSection = $("#lineas-activo")
const lineasTotalSection = $("#lineas-total")

homeBtn.click(function () {
    reglasSection.hide()
    agrupamientoSection.hide()
    filtradoSection.hide()
    homeSection.show()

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

    homeBtn.attr('class', 'nav-link dash-option active');
    reglasBtn.attr('class', 'nav-link dash-option');
    agrupamientoBtn.attr('class', 'nav-link dash-option');
    filtradoBtn.attr('class', 'nav-link dash-option');
})

reglasBtn.click(function () {
    reglasSection.show()
    agrupamientoSection.hide()
    filtradoSection.hide()
    homeSection.hide()

    homeBtn.attr('class', 'nav-link dash-option');
    reglasBtn.attr('class', 'nav-link dash-option active');
    agrupamientoBtn.attr('class', 'nav-link dash-option');
    filtradoBtn.attr('class', 'nav-link dash-option');
})

agrupamientoBtn.click(function () {
    reglasSection.hide()
    agrupamientoSection.show()
    filtradoSection.hide()
    homeSection.hide()

    homeBtn.attr('class', 'nav-link dash-option');
    reglasBtn.attr('class', 'nav-link dash-option');
    agrupamientoBtn.attr('class', 'nav-link dash-option active');
    filtradoBtn.attr('class', 'nav-link dash-option');
})

filtradoBtn.click(function () {
    reglasSection.hide()
    agrupamientoSection.hide()
    filtradoSection.show()
    homeSection.hide()

    homeBtn.attr('class', 'nav-link dash-option');
    reglasBtn.attr('class', 'nav-link dash-option');
    agrupamientoBtn.attr('class', 'nav-link dash-option');
    filtradoBtn.attr('class', 'nav-link dash-option active');
})

lineasActivoBtn.click(function () {
    lineasActivoSection.show()
    lineasTotalSection.hide()

    lineasActivoBtn.attr('class', 'nav-link active')
    lineasTotalBtn.attr('class', 'nav-link')
})

lineasTotalBtn.click(function () {
    lineasActivoSection.hide()
    lineasTotalSection.show()

    lineasActivoBtn.attr('class', 'nav-link')
    lineasTotalBtn.attr('class', 'nav-link active')
})