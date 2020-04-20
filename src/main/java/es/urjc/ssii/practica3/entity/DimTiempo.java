package es.urjc.ssii.practica3.entity;

import javax.persistence.*;
import java.sql.Date;

/**
 *
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "dim_tiempo")
public class DimTiempo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private Date fecha;

    private int dia;

    private int mes;

    private int anio;

    private int cuatrimestre;

    @Column(name = "dia_semana")
    private DiaSemana diaSemana;

    @Column(name = "es_finde")
    private boolean esFinde;

    public DimTiempo() {
    }

    public DimTiempo(int id, Date fecha, int dia, int mes, int anio, int cuatri, DiaSemana diaSemana, boolean esFinde) {
        this.id = id;
        this.fecha = fecha;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.cuatrimestre = cuatri;
        this.diaSemana = diaSemana;
        this.esFinde = esFinde;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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

    @Override
    public String toString() {
        return "DimTiempo{" +
                "id=" + id +
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
