package edu.eci.cvds.citasmedicas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para el modelo Especialidad")
class EspecialidadTest {

    private Validator validator;
    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        especialidad = new Especialidad(
            "Cardiología",
            "Especialidad médica que se encarga del estudio, diagnóstico y tratamiento de las enfermedades del corazón",
            "Dr. Juan Pérez",
            "Consultorio 101",
            "cardiologia.jpg"
        );
    }

    @Test
    @DisplayName("Debe crear una especialidad válida")
    void debeCrearEspecialidadValida() {
        Set<ConstraintViolation<Especialidad>> violations = validator.validate(especialidad);
        assertTrue(violations.isEmpty());
        
        assertEquals("Cardiología", especialidad.getNombre());
        assertEquals("Dr. Juan Pérez", especialidad.getDoctor());
        assertEquals("Consultorio 101", especialidad.getUbicacion());
        assertEquals("cardiologia.jpg", especialidad.getImagen());
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre está vacío")
    void debeFallarCuandoNombreEstaVacio() {
        especialidad.setNombre("");
        
        Set<ConstraintViolation<Especialidad>> violations = validator.validate(especialidad);
        assertFalse(violations.isEmpty());
        
        boolean nombreViolationFound = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("nombre"));
        assertTrue(nombreViolationFound);
    }

    @Test
    @DisplayName("Debe fallar cuando la descripción está vacía")
    void debeFallarCuandoDescripcionEstaVacia() {
        especialidad.setDescripcion("");
        
        Set<ConstraintViolation<Especialidad>> violations = validator.validate(especialidad);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Debe fallar cuando el doctor está vacío")
    void debeFallarCuandoDoctorEstaVacio() {
        especialidad.setDoctor("");
        
        Set<ConstraintViolation<Especialidad>> violations = validator.validate(especialidad);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Debe generar toString correctamente")
    void debeGenerarToStringCorrectamente() {
        especialidad.setId("123");
        String expected = "Especialidad{id='123', nombre='Cardiología', descripcion='Especialidad médica que se encarga del estudio, diagnóstico y tratamiento de las enfermedades del corazón', doctor='Dr. Juan Pérez', ubicacion='Consultorio 101', imagen='cardiologia.jpg'}";
        assertEquals(expected, especialidad.toString());
    }
}