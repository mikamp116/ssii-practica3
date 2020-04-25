package es.urjc.ssii.practica3.dto;

import es.urjc.ssii.practica3.entity.DimTiempo;
import es.urjc.ssii.practica3.entity.TablaHechos;

public class TablaHechosDTO {

    private int pacienteId;

    private int hospitalId;

    private DimTiempo fechaIngreso;

    private int duracion;

    private boolean uci;

    private boolean fallecido;

    private int tratamiento;

    public TablaHechosDTO(int pacienteId, int hospitalId, DimTiempo fechaIngreso, int duracion, boolean uci, boolean fallecido, int tratamiento) {
        this.pacienteId = pacienteId;
        this.hospitalId = hospitalId;
        this.fechaIngreso = fechaIngreso;
        this.duracion = duracion;
        this.uci = uci;
        this.fallecido = fallecido;
        this.tratamiento = tratamiento;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public DimTiempo getFechaIngreso() {
        return fechaIngreso;
    }

    public int getDuracion() {
        return duracion;
    }

    public boolean isUci() {
        return uci;
    }

    public boolean isFallecido() {
        return fallecido;
    }

    public int getTratamiento() {
        return tratamiento;
    }

    public static TablaHechosDTO convertTablaHechosToDTO(TablaHechos tablaHechos){
        return new TablaHechosDTO(tablaHechos.getPacienteId().getPacienteId(),
                tablaHechos.getHospitalId().getHospitalId(), tablaHechos.getFechaIngresoId(),
                tablaHechos.getDuracion(), tablaHechos.isUci(), tablaHechos.isFallecido(), tablaHechos.getTratamiento());
    }
}
