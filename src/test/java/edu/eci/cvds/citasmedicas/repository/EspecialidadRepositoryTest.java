package edu.eci.cvds.citasmedicas.repository;

import edu.eci.cvds.citasmedicas.model.Especialidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = {
    "spring.data.mongodb.database=test_citasmedicas"
})
@DisplayName("Tests para EspecialidadRepository")
class EspecialidadRepositoryTest {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    private Especialidad especialidad1;
    private Especialidad especialidad2;

    @BeforeEach
    void setUp() {
        especialidadRepository.deleteAll();
        
        especialidad1 = new Especialidad(
            "Cardiología",
            "Especialidad del corazón",
            "Dr. Juan Pérez",
            "Consultorio 101",
            "cardiologia.jpg"
        );
        
        especialidad2 = new Especialidad(
            "Dermatología",
            "Especialidad de la piel",
            "Dra. Ana López",
            "Consultorio 102",
            "dermatologia.jpg"
        );
        
        especialidadRepository.save(especialidad1);
        especialidadRepository.save(especialidad2);
    }

    @Test
    @DisplayName("Debe encontrar especialidad por nombre exacto")
    void debeEncontrarEspecialidadPorNombreExacto() {
        Optional<Especialidad> found = especialidadRepository.findByNombre("Cardiología");
        
        assertTrue(found.isPresent());
        assertEquals("Cardiología", found.get().getNombre());
        assertEquals("Dr. Juan Pérez", found.get().getDoctor());
    }

    @Test
    @DisplayName("Debe encontrar especialidad ignorando mayúsculas y minúsculas")
    void debeEncontrarEspecialidadIgnorandoMayusculas() {
        Optional<Especialidad> found = especialidadRepository.findByNombreIgnoreCase("cardiología");
        
        assertTrue(found.isPresent());
        assertEquals("Cardiología", found.get().getNombre());
    }

    @Test
    @DisplayName("No debe encontrar especialidad inexistente")
    void noDebeEncontrarEspecialidadInexistente() {
        Optional<Especialidad> found = especialidadRepository.findByNombre("Neurología");
        
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Debe verificar existencia por nombre")
    void debeVerificarExistenciaPorNombre() {
        assertTrue(especialidadRepository.existsByNombre("Cardiología"));
        assertFalse(especialidadRepository.existsByNombre("Neurología"));
    }

    @Test
    @DisplayName("Debe guardar y recuperar todas las especialidades")
    void debeGuardarYRecuperarTodasLasEspecialidades() {
        assertEquals(2, especialidadRepository.count());
        assertEquals(2, especialidadRepository.findAll().size());
    }
}