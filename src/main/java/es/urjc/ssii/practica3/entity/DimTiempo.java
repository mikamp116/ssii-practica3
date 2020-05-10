package es.urjc.ssii.practica3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "dim_tiempo")
public class DimTiempo {

    @Id
    @Column(name = "tiempo_id")
    private int tiempoId;

    private Date fecha;

    private int dia;

    private int mes;

    private int anio;

    private int cuatrimestre;

    @Column(name = "dia_semana")
    private DiaSemana diaSemana;

    @Column(name = "es_finde")
    private boolean esFinde;

    @JsonIgnore
    @OneToMany(mappedBy = "fechaIngresoId", cascade = CascadeType.ALL)
    private Set<TablaHechos> hechos;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DimTiempo() {
    }

    public DimTiempo(int id, Date fecha, int dia, int mes, int anio, int cuatri, DiaSemana diaSemana, boolean esFinde) {
        this.tiempoId = id;
        this.fecha = fecha;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.cuatrimestre = cuatri;
        this.diaSemana = diaSemana;
        this.esFinde = esFinde;
    }

    public DimTiempo(int id, String fecha, String dia, String mes, String anio, String cuatri, String diaSemana,
                     String esFinde) {
        this.tiempoId = id;
        // Se suma el offset en milisegundos de UTC a GMT para obtener la fecha correcta
        this.fecha = new Date(Date.valueOf(LocalDate.parse(fecha, formatter)).getTime() + 8000000);
        this.dia = Integer.parseInt(dia);
        this.mes = Integer.parseInt(mes);
        this.anio = Integer.parseInt(anio);
        this.cuatrimestre = Integer.parseInt(cuatri);
        this.diaSemana = DiaSemana.valueOf(diaSemana.toUpperCase());
        this.esFinde = esFinde.equals("1");
    }

    public int getTiempoId() {
        return tiempoId;
    }

    public void setTiempoId(int tiempoId) {
        this.tiempoId = tiempoId;
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

    public Set<TablaHechos> getHechos() {
        return hechos;
    }

    public void setHechos(Set<TablaHechos> hechos) {
        this.hechos = hechos;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd-MM-yyyy");
        return "DimTiempo{" +
                "id=" + tiempoId +
                ", fecha=" + sdf.format(fecha) +
                ", dia=" + dia +
                ", mes=" + mes +
                ", anio=" + anio +
                ", cuatrimestre=" + cuatrimestre +
                ", diaSemana=" + diaSemana +
                ", esFinde=" + esFinde +
                '}';
    }
}
