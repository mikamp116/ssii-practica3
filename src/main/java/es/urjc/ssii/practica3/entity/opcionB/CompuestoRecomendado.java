package es.urjc.ssii.practica3.entity.opcionB;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "compuesto_recomendado")
public class CompuestoRecomendado {

    @Id
    @Column(name = "paciente_id")
    private int pacienteId;

    private int hospital;

    private int item1;

    private float value1;

    private int item2;

    private float value2;

    private int item3;

    private float value3;

    public CompuestoRecomendado() {
    }

    public CompuestoRecomendado(int id, int hospital, int i1, float v1, int i2, float v2, int i3, float v3) {
        this.pacienteId = id;
        this.hospital = hospital;
        this.item1 = i1;
        this.item2 = i2;
        this.item3 = i3;
        this.value1 = v1;
        this.value2 = v2;
        this.value3 = v3;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public int getItem1() {
        return item1;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }

    public int getItem3() {
        return item3;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public float getValue3() {
        return value3;
    }

    public void setValue3(float value3) {
        this.value3 = value3;
    }

    public int getHospital() {
        return hospital;
    }

    public void setHospital(int hospital) {
        this.hospital = hospital;
    }

    @Override
    public String toString() {
        return "CompuestoRecomendado{" +
                "pacienteId=" + pacienteId +
                ", hospital=" + hospital +
                ", item1=" + item1 +
                ", value1=" + value1 +
                ", item2=" + item2 +
                ", value2=" + value2 +
                ", item3=" + item3 +
                ", value3=" + value3 +
                '}';
    }
}
