package edu.eci.cvds.citasmedicas.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para el modelo Cita")
class CitaTest {

    private Validator validator;
    private Cita cita;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        cita = new Cita(
            "Juan Pérez García",
            "12345678",
            "juan.perez@email.com",
            LocalDate.now().plusDays(1),
            "Cardiología",
            "Dr. María López",
            "Consultorio 101"
        );
    }

    @Test
    @DisplayName("Debe crear una cita válida")
    void debeCrearCitaValida() {
        Set<ConstraintViolation<Cita>> violations = validator.validate(cita);
        assertTrue(violations.isEmpty());
        
        assertEquals("Juan Pérez García", cita.getNombreCompleto());
        assertEquals("12345678", cita.getCedula());
        assertEquals("juan.perez@email.com", cita.getCorreoElectronico());
        assertEquals(EstadoCita.CONFIRMADA, cita.getEstado());
        assertNotNull(cita.getFechaCreacion());
        assertNotNull(cita.getFechaActualizacion());
    }

    @Test
    @DisplayName("Debe fallar con cédula inválida")
    void debeFallarConCedulaInvalida() {
        cita.setCedula("123"); // Muy corta
        
        Set<ConstraintViolation<Cita>> violations = validator.validate(cita);
        assertFalse(violations.isEmpty());
        
        boolean cedulaViolationFound = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("cedula"));
        assertTrue(cedulaViolationFound);
    }

    @Test
    @DisplayName("Debe fallar con correo electrónico inválido")
    void debeFallarConCorreoInvalido() {
        cita.setCorreoElectronico("correo-invalido");
        
        Set<ConstraintViolation<Cita>> violations = validator.validate(cita);
        assertFalse(violations.isEmpty());
        
        boolean emailViolationFound = violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("correoElectronico"));
        assertTrue(emailViolationFound);
    }

    @Test
    @DisplayName("Debe actualizar fecha de actualización al cambiar estado")
    void debeActualizarFechaActualizacionAlCambiarEstado() {
        LocalDateTime fechaOriginal = cita.getFechaActualizacion();
        
        try {
            Thread.sleep(10); // Pequeña pausa para asegurar diferencia en tiempo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        cita.setEstado(EstadoCita.CANCELADA);
        
        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        assertTrue(cita.getFechaActualizacion().isAfter(fechaOriginal));
    }

    @Test
    @DisplayName("Debe tener estado CONFIRMADA por defecto")
    void debeTenerEstadoConfirmadaPorDefecto() {
        Cita nuevaCita = new Cita();
        assertEquals(EstadoCita.CONFIRMADA, nuevaCita.getEstado());
    }
}