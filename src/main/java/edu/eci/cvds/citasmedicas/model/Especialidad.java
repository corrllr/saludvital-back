package edu.eci.cvds.citasmedicas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Document(collection = "especialidades")
public class Especialidad {
    
    @Id
    private String id;
    
    @NotBlank(message = "El nombre de la especialidad es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @NotBlank(message = "El doctor es obligatorio")
    private String doctor;
    
    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;
    
    @NotBlank(message = "La imagen es obligatoria")
    private String imagen;

    // Constructor por defecto
    public Especialidad() {}

    // Constructor con parámetros
    public Especialidad(String nombre, String descripcion, String doctor, String ubicacion, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.doctor = doctor;
        this.ubicacion = ubicacion;
        this.imagen = imagen;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Especialidad{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", doctor='" + doctor + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
