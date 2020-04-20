package es.urjc.ssii.practica3.entity;

import javax.persistence.*;

/**
 *
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "dim_paciente")
public class DimPaciente {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

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

    public DimPaciente() {
    }

    public DimPaciente(int id, int edad, byte sexo, double imc, int formaFisica, boolean tabaquismo,
                       boolean alcoholismo, boolean colesterol, boolean hipertension, boolean cardiopatia,
                       boolean reuma, boolean epoc, int hepatitis, boolean cancer) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "DimPaciente{" +
                "id=" + id +
                ", edad=" + edad +
                ", sexo=" + sexo +
                ", imc=" + imc +
                ", formaFisica=" + formaFisica +
                ", tabaquismo=" + tabaquismo +
                ", alcoholismo=" + alcoholismo +
                ", colesterol=" + colesterol +
                ", hipertension=" + hipertension +
                ", cardiopatia=" + cardiopatia +
                ", reuma=" + reuma +
                ", epoc=" + epoc +
                ", hepatitis=" + hepatitis +
                ", cancer=" + cancer +
                '}';
    }
}
