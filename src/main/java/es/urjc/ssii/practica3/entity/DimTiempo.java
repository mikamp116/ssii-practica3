package es.urjc.ssii.practica3.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "dim_tiempo")
public class DimTiempo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tiempo_id")
    private int tiempoId;

    private LocalDate fecha;

    private int dia;

    private int mes;

    private int anio;

    private int cuatrimestre;

    @Column(name = "dia_semana")
    private DiaSemana diaSemana;

    @Column(name = "es_finde")
    private boolean esFinde;

    @OneToMany(mappedBy = "fechaIngresoId", cascade = CascadeType.ALL)
    private Set<TablaHechos> hechos;

    public DimTiempo() {
    }

    public DimTiempo(int id, LocalDate fecha, int dia, int mes, int anio, int cuatri, DiaSemana diaSemana, boolean esFinde) {
        this.tiempoId = id;
        this.fecha = fecha;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.cuatrimestre = cuatri;
        this.diaSemana = diaSemana;
        this.esFinde = esFinde;
    }

    public int getTiempoId() {
        return tiempoId;
    }

    public void setTiempoId(int tiempoId) {
        this.tiempoId = tiempoId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public boolean isEsFinde() {
        return esFinde;
    }

    public void setEsFinde(boolean esFinde) {
        this.esFinde = esFinde;
    }

    public Set<TablaHechos> getHechos() {
        return hechos;
    }

    public void setHechos(Set<TablaHechos> hechos) {
        this.hechos = hechos;
    }

    @Override
    public String toString() {
        return "DimTiempo{" +
                "id=" + tiempoId +
                ", fecha=" + fecha +
                ", dia=" + dia +
                ", mes=" + mes +
                ", anio=" + anio +
                ", cuatrimestre=" + cuatrimestre +
                ", diaSemana=" + diaSemana +
                ", esFinde=" + esFinde +
                '}';
    }
}
