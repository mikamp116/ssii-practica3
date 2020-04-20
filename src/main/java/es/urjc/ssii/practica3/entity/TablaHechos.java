package es.urjc.ssii.practica3.entity;

import javax.persistence.*;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "tabla_hechos")
public class TablaHechos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hecho_id")
    private int hechoId;

    @ManyToOne
    @JoinColumn(name = "pacienteId")
    @Column(name = "paciente_id")
    private DimPaciente pacienteId;

    @ManyToOne
    @JoinColumn(name = "hospitalId")
    @Column(name = "hospital_id")
    private DimHospital hospitalId;

    @ManyToOne
    @JoinColumn(name = "tiempoId")
    @Column(name = "fecha_ingreso_id")
    private DimTiempo fechaIngresoId;

    private int duracion;

    private boolean uci;

    private boolean fallecido;

    private int tratamiento;

    public TablaHechos() {
    }

    public TablaHechos(int hechoId, DimPaciente pacienteId, DimHospital hospitalId, DimTiempo fechaIngresoId,
                       int duracion, boolean uci, boolean fallecido, int tratamiento) {
        this.hechoId = hechoId;
        this.pacienteId = pacienteId;
        this.hospitalId = hospitalId;
        this.fechaIngresoId = fechaIngresoId;
        this.duracion = duracion;
        this.uci = uci;
        this.fallecido = fallecido;
        this.tratamiento = tratamiento;
    }

    public int getHechoId() {
        return hechoId;
    }

    public void setHechoId(int id) {
        this.hechoId = id;
    }

    public DimPaciente getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(DimPaciente pacienteId) {
        this.pacienteId = pacienteId;
    }

    public DimHospital getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(DimHospital hospitalId) {
        this.hospitalId = hospitalId;
    }

    public DimTiempo getFechaIngresoId() {
        return fechaIngresoId;
    }

    public void setFechaIngresoId(DimTiempo fechaIngresoId) {
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
                "id=" + hechoId +
                ", pacienteId=" + pacienteId +
                ", hospitalId=" + hospitalId +
                ", fechaIngresoId=" + fechaIngresoId +
                ", duracion=" + duracion +
                ", uci=" + uci +
                ", fallecido=" + fallecido +
                ", tratamiento=" + tratamiento +
                '}';
    }
}
