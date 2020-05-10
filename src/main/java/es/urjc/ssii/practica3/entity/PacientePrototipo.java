package es.urjc.ssii.practica3.entity;

import javax.persistence.*;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Entity
@Table(name = "paciente_prototipo")
public class PacientePrototipo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "paciente_prototipo_id")
    private int pacientePrototipoId;

    private String grupo;

    private int edad;

    private byte sexo;

    private int imc;

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

    public PacientePrototipo() {
    }

    public PacientePrototipo(int edad, byte sexo, int imc, int formaFisica, boolean tabaquismo,
                             boolean alcoholismo, boolean colesterol, boolean hipertension, boolean cardiopatia,
                             boolean reuma, boolean epoc, int hepatitis, boolean cancer, String grupo) {
        this.grupo = grupo;
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

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getPacientePrototipoId() {
        return pacientePrototipoId;
    }

    public void setPacientePrototipoId(int pacienteId) {
        this.pacientePrototipoId = pacienteId;
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

    public int getImc() {
        return imc;
    }

    public void setImc(int imc) {
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
        String toReturn = "DimPaciente{" +
                "id=" + pacientePrototipoId +
                ", edad=" + edad +
                ", sexo=" + sexo +
                ", imc=" + imc +
                ", formaFisica=" + formaFisica +
                ", grupo=" + grupo +
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
            toReturn += "hepatitis=" + hepatitis + ", ";
        if (cancer)
            toReturn += "cancer";
        toReturn += "}}";

        return toReturn;
    }
}
