package es.urjc.ssii.practica3.entity;

import javax.persistence.*;
import java.sql.Date;

/**
 *
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "tabla_hechos")
public class TablaHechos {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name="cliente_id")
    private int clienteId;

    @Column(name="hospital_id")
    private int hospitalId;

    @Column(name="fecha_ingreso_id")
    private Date fechaIngresoId;

    private int duracion;

    private boolean uci;

    private boolean fallecido;

    private int tratamiento;

    public TablaHechos() {
    }

    public TablaHechos(int id, int clienteId, int hospitalId, Date fechaIngresoId, int duracion, boolean uci,
                       boolean fallecido, int tratamiento) {

        this.id = id;
        this.clienteId = clienteId;
        this.hospitalId = hospitalId;
        this.fechaIngresoId = fechaIngresoId;
        this.duracion = duracion;
        this.uci = uci;
        this.fallecido = fallecido;
        this.tratamiento = tratamiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Date getFechaIngresoId() {
        return fechaIngresoId;
    }

    public void setFechaIngresoId(Date fechaIngresoId) {
        this.fechaIngresoId = fechaIngresoId;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public boolean isUci() {
        return uci;
    }

    public void setUci(boolean uci) {
        this.uci = uci;
    }

    public boolean isFallecido() {
        return fallecido;
    }

    public void setFallecido(boolean fallecido) {
        this.fallecido = fallecido;
    }

    public int getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(int tratamiento) {
        this.tratamiento = tratamiento;
    }

    @Override
    public String toString() {
        return "TablaHechos{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", hospitalId=" + hospitalId +
                ", fechaIngresoId=" + fechaIngresoId +
                ", duracion=" + duracion +
                ", uci=" + uci +
                ", fallecido=" + fallecido +
                ", tratamiento=" + tratamiento +
                '}';
    }
}
