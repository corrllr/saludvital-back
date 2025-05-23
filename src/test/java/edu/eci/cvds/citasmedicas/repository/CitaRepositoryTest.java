package edu.eci.cvds.citasmedicas.repository;

import edu.eci.cvds.citasmedicas.model.Cita;
import edu.eci.cvds.citasmedicas.model.EstadoCita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = {
    "spring.data.mongodb.database=test_citasmedicas"
})
@DisplayName("Tests para CitaRepository")
class CitaRepositoryTest {

    @Autowired
    private CitaRepository citaRepository;

    private Cita cita1;
    private Cita cita2;
    private Cita cita3;

    @BeforeEach
    void setUp() {
        citaRepository.deleteAll();
        
        cita1 = new Cita(
            "Juan Pérez",
            "12345678",
            "juan@email.com",
            LocalDate.now().plusDays(1),
            "Cardiología",
            "Dr. López",
            "Consultorio 101"
        );
        
        cita2 = new Cita(
            "Ana García",
            "87654321",
            "ana@email.com",
            LocalDate.now().plusDays(2),
            "Dermatología",
            "Dra. Martín",
            "Consultorio 102"
        );
        
        cita3 = new Cita(
            "Carlos Ruiz",
            "11223344",
            "juan@email.com", // Mismo correo que cita1
            LocalDate.now().plusDays(3),
            "Cardiología",
            "Dr. López",
            "Consultorio 101"
        );
        
        cita2.setEstado(EstadoCita.CANCELADA);
        
        citaRepository.save(cita1);
        citaRepository.save(cita2);
        citaRepository.save(cita3);
    }

    @Test
    @DisplayName("Debe encontrar citas por correo electrónico")
    void debeEncontrarCitasPorCorreo() {
        List<Cita> citas = citaRepository.findByCorreoElectronico("juan@email.com");
        
        assertEquals(2, citas.size());
        assertTrue(citas.stream().allMatch(c -> c.getCorreoElectronico().equals("juan@email.com")));
    }

    @Test
    @DisplayName("Debe encontrar citas por correo y estado")
    void debeEncontrarCitasPorCorreoYEstado() {
        List<Cita> citas = citaRepository.findByCorreoElectronicoAndEstado("juan@email.com", EstadoCita.CONFIRMADA);
        
        assertEquals(2, citas.size());
        assertTrue(citas.stream().allMatch(c -> c.getEstado() == EstadoCita.CONFIRMADA));
    }

    @Test
    @DisplayName("Debe encontrar citas por estado")
    void debeEncontrarCitasPorEstado() {
        List<Cita> confirmadas = citaRepository.findByEstado(EstadoCita.CONFIRMADA);
        List<Cita> canceladas = citaRepository.findByEstado(EstadoCita.CANCELADA);
        
        assertEquals(2, confirmadas.size());
        assertEquals(1, canceladas.size());
    }

    @Test
    @DisplayName("Debe encontrar citas por especialidad")
    void debeEncontrarCitasPorEspecialidad() {
        List<Cita> cardiologia = citaRepository.findByEspecialidad("Cardiología");
        List<Cita> dermatologia = citaRepository.findByEspecialidad("Dermatología");
        
        assertEquals(2, cardiologia.size());
        assertEquals(1, dermatologia.size());
    }

    @Test
    @DisplayName("Debe encontrar citas por fecha")
    void debeEncontrarCitasPorFecha() {
        List<Cita> citasManana = citaRepository.findByFechaCita(LocalDate.now().plusDays(1));
        
        assertEquals(1, citasManana.size());
        assertEquals("Juan Pérez", citasManana.get(0).getNombreCompleto());
    }

    @Test
    @DisplayName("Debe encontrar citas por cédula")
    void debeEncontrarCitasPorCedula() {
        List<Cita> citasJuan = citaRepository.findByCedula("12345678");
        
        assertEquals(1, citasJuan.size());
        assertEquals("Juan Pérez", citasJuan.get(0).getNombreCompleto());
    }

    @Test
    @DisplayName("Debe encontrar citas en rango de fechas")
    void debeEncontrarCitasEnRangoFechas() {
        LocalDate inicio = LocalDate.now().plusDays(1);
        LocalDate fin = LocalDate.now().plusDays(2);
        
        List<Cita> citasEnRango = citaRepository.findByFechaCitaBetween(inicio, fin);
        
        assertEquals(2, citasEnRango.size());
    }

    @Test
    @DisplayName("Debe contar citas por estado")
    void debeContarCitasPorEstado() {
        long confirmadas = citaRepository.countByEstado(EstadoCita.CONFIRMADA);
        long canceladas = citaRepository.countByEstado(EstadoCita.CANCELADA);
        long rechazadas = citaRepository.countByEstado(EstadoCita.RECHAZADA);
        
        assertEquals(2, confirmadas);
        assertEquals(1, canceladas);
        assertEquals(0, rechazadas);
    }
}