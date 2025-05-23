package edu.eci.cvds.citasmedicas.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para el enum EstadoCita")
class EstadoCitaTest {

    @Test
    @DisplayName("Debe tener las descripciones correctas")
    void debeTenerDescripcionesCorrectas() {
        assertEquals("Confirmada", EstadoCita.CONFIRMADA.getDescripcion());
        assertEquals("Cancelada", EstadoCita.CANCELADA.getDescripcion());
        assertEquals("Rechazada", EstadoCita.RECHAZADA.getDescripcion());
    }

    @Test
    @DisplayName("ToString debe devolver la descripción")
    void toStringDebeRevolverDescripcion() {
        assertEquals("Confirmada", EstadoCita.CONFIRMADA.toString());
        assertEquals("Cancelada", EstadoCita.CANCELADA.toString());
        assertEquals("Rechazada", EstadoCita.RECHAZADA.toString());
    }

    @Test
    @DisplayName("Debe tener exactamente 3 valores")
    void debeTenerTresValores() {
        EstadoCita[] estados = EstadoCita.values();
        assertEquals(3, estados.length);
    }
}