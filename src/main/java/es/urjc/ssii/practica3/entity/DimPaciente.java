package es.urjc.ssii.practica3.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "dim_paciente")
public class DimPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "paciente_id")
    private int pacienteId;

    private int edad;

    private byte sexo;

    private double imc;

    @Column(name = "forma_fisica")
    private int formaFisica;

    private boolean tabaquismo;

    private boolean alcoholismo;

    private boolean colesterol;

    private boolean hipertension;

    private boolean cardiopatia;

    private boolean reuma;

    private boolean epoc;

    private int hepatitis;

    private boolean cancer;

    @OneToMany(mappedBy = "pacienteId", cascade = CascadeType.ALL)
    private Set<TablaHechos> hechos;

    public DimPaciente() {
    }

    public DimPaciente(int id, int edad, byte sexo, double imc, int formaFisica, boolean tabaquismo,
                       boolean alcoholismo, boolean colesterol, boolean hipertension, boolean cardiopatia,
                       boolean reuma, boolean epoc, int hepatitis, boolean cancer) {
        this.pacienteId = id;
        this.edad = edad;
        this.sexo = sexo;
        this.imc = imc;
        this.formaFisica = formaFisica;
        this.tabaquismo = tabaquismo;
        this.alcoholismo = alcoholismo;
        this.colesterol = colesterol;
        this.hipertension = hipertension;
        this.cardiopatia = cardiopatia;
        this.reuma = reuma;
        this.epoc = epoc;
        this.hepatitis = hepatitis;
        this.cancer = cancer;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public byte getSexo() {
        return sexo;
    }

    public void setSexo(byte sexo) {
        this.sexo = sexo;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public int getFormaFisica() {
        return formaFisica;
    }

    public void setFormaFisica(int formaFisica) {
        this.formaFisica = formaFisica;
    }

    public boolean isTabaquismo() {
        return tabaquismo;
    }

    public void setTabaquismo(boolean tabaquismo) {
        this.tabaquismo = tabaquismo;
    }

    public boolean isAlcoholismo() {
        return alcoholismo;
    }

    public void setAlcoholismo(boolean alcoholismo) {
        this.alcoholismo = alcoholismo;
    }

    public boolean isColesterol() {
        return colesterol;
    }

    public void setColesterol(boolean colesterol) {
        this.colesterol = colesterol;
    }

    public boolean isHipertension() {
        return hipertension;
    }

    public void setHipertension(boolean hipertension) {
        this.hipertension = hipertension;
    }

    public boolean isCardiopatia() {
        return cardiopatia;
    }

    public void setCardiopatia(boolean cardiopatia) {
        this.cardiopatia = cardiopatia;
    }

    public boolean isReuma() {
        return reuma;
    }

    public void setReuma(boolean reuma) {
        this.reuma = reuma;
    }

    public boolean isEpoc() {
        return epoc;
    }

    public void setEpoc(boolean epoc) {
        this.epoc = epoc;
    }

    public int getHepatitis() {
        return hepatitis;
    }

    public void setHepatitis(int hepatitis) {
        this.hepatitis = hepatitis;
    }

    public boolean isCancer() {
        return cancer;
    }

    public void setCancer(boolean cancer) {
        this.cancer = cancer;
    }

    public Set<TablaHechos> getHechos() {
        return hechos;
    }

    public void setHechos(Set<TablaHechos> hechos) {
        this.hechos = hechos;
    }

    @Override
    public String toString() {
        String toReturn = "DimPaciente{" +
                "id=" + pacienteId +
                ", edad=" + edad +
                ", sexo=" + sexo +
                ", imc=" + imc +
                ", formaFisica=" + formaFisica +
                ", tiene: {";
        if (tabaquismo)
            toReturn += "tabaquismo, ";
        if (alcoholismo)
            toReturn += "alcoholismo, ";
        if (colesterol)
            toReturn += "colesterol, ";
        if (hipertension)
            toReturn += "hipertension, ";
        if (cardiopatia)
            toReturn += "cardiopatia, ";
        if (reuma)
            toReturn += "reuma, ";
        if (epoc)
            toReturn += "epoc, ";
        if (hepatitis > 0)
            toReturn += "hepatitis=" + hepatitis;
        if (cancer)
            toReturn += ", cancer";
        toReturn += "}}";

        return toReturn;
    }
}
