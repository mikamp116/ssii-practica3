package es.urjc.ssii.practica3.entity;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
public enum DiaSemana {
    LUNES("lunes"), MARTES("martes"), MIERCOLES("miercoles"), JUEVES("jueves"),
    VIERNES("viernes"), SABADO("sabado"), DOMINGO("domingo");

    private final String descripcion;

    private DiaSemana(String value) {
        descripcion = value;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
