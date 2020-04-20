package es.urjc.ssii.practica3.entity;

import javax.persistence.*;

/**
 *
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "dim_hospital")
public class DimHospital {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String nombre;

    private int cpostal;

    private String autopista;

    private String gestor;

    public DimHospital() {
    }

    public DimHospital(int id, String nombre, int cpostal, String autopista, String gestor) {
        this.id = id;
        this.nombre = nombre;
        this.cpostal = cpostal;
        this.autopista = autopista;
        this.gestor = gestor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCpostal() {
        return cpostal;
    }

    public void setCpostal(int cpostal) {
        this.cpostal = cpostal;
    }

    public String getAutopista() {
        return autopista;
    }

    public void setAutopista(String autopista) {
        this.autopista = autopista;
    }

    public String getGestor() {
        return gestor;
    }

    public void setGestor(String gestor) {
        this.gestor = gestor;
    }

    @Override
    public String toString() {
        return "DimHospital{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cpostal=" + cpostal +
                ", autopista='" + autopista + '\'' +
                ", gestor='" + gestor + '\'' +
                '}';
    }
}
