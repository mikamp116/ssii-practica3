package es.urjc.ssii.practica3.entity;

import javax.persistence.*;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "regla_asociacion")
public class ReglaAsociacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "regla_id")
    private int reglaId;

    private boolean exito;

    private String probados;

    private int recomendado;

    private double conf;
    private double lift;
    private double lev;
    private double conv;

    public ReglaAsociacion() {
    }

    public ReglaAsociacion(boolean exito, String probados, int recomendado, double conf, double lift, double lev,
                           double conv) {
        this.exito = exito;
        this.probados = probados;
        this.recomendado = recomendado;
        this.conf = conf;
        this.lift = lift;
        this.lev = lev;
        this.conv = conv;
    }

    public int getReglaId() {
        return reglaId;
    }

    public void setReglaId(int reglaId) {
        this.reglaId = reglaId;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getProbados() {
        return probados;
    }

    public void setProbados(String probados) {
        this.probados = probados;
    }

    public int getRecomendado() {
        return recomendado;
    }

    public void setRecomendado(int recomendado) {
        this.recomendado = recomendado;
    }

    public double getConf() {
        return conf;
    }

    public void setConf(double conf) {
        this.conf = conf;
    }

    public double getLift() {
        return lift;
    }

    public void setLift(double lift) {
        this.lift = lift;
    }

    public double getLev() {
        return lev;
    }

    public void setLev(double lev) {
        this.lev = lev;
    }

    public double getConv() {
        return conv;
    }

    public void setConv(double conv) {
        this.conv = conv;
    }
}
