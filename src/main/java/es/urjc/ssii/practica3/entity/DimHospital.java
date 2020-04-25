package es.urjc.ssii.practica3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "dim_hospital")
public class DimHospital {

    @Id
    @Column(name = "hospital_id")
    private int hospitalId;

    private String nombre;

    private int cpostal;

    private String autopista;

    private String gestor;

    @JsonIgnore
    @OneToMany(mappedBy = "hospitalId", cascade = CascadeType.ALL)
    private Set<TablaHechos> hechos;

    public DimHospital() {
    }

    public DimHospital(int id, String nombre, int cpostal, String autopista, String gestor) {
        this.hospitalId = id;
        this.nombre = nombre;
        this.cpostal = cpostal;
        this.autopista = autopista;
        this.gestor = gestor;
    }

    public DimHospital(int id, String nombre, String cpostal, String autopista, String gestor) {
        this.hospitalId = id;
        this.nombre = nombre;
        this.cpostal = Integer.parseInt(cpostal);
        this.autopista = autopista;
        this.gestor = gestor;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
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

    public Set<TablaHechos> getHechos() {
        return hechos;
    }

    public void setHechos(Set<TablaHechos> hechos) {
        this.hechos = hechos;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + hospitalId +
                ", nombre='" + nombre + '\'' +
                ", cpostal=" + cpostal +
                ", autopista='" + autopista + '\'' +
                ", gestor='" + gestor + '\'' +
                '}';
    }
}
