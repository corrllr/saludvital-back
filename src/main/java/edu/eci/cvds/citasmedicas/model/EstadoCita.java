package edu.eci.cvds.citasmedicas.model;

public enum EstadoCita {
    CONFIRMADA("Confirmada"),
    CANCELADA("Cancelada"),
    RECHAZADA("Rechazada");

    private final String descripcion;

    EstadoCita(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
