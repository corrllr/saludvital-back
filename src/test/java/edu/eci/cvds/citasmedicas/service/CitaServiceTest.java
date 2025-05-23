package edu.eci.cvds.citasmedicas.service;

import edu.eci.cvds.citasmedicas.model.Cita;
import edu.eci.cvds.citasmedicas.model.EstadoCita;
import edu.eci.cvds.citasmedicas.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaService citaService;

    private Cita cita;

    @BeforeEach
    void setUp() {
        cita = new Cita(
            "Juan Pérez",
            "12345678",
            "juan.perez@email.com",
            LocalDate.now().plusDays(1),
            "Medicina General",
            "Dr. Carlos Rodríguez",
            "Consulta 101"
        );
        cita.setId("1");
    }

    @Test
    void programarCita_ConDatosValidos_DeberiaCrearCita() {
        // Given
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // When
        Cita resultado = citaService.programarCita(cita);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombreCompleto());
        assertEquals(EstadoCita.CONFIRMADA, resultado.getEstado());
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    void programarCita_ConFechaAnterior_DeberiaThrownException() {
        // Given
        cita.setFechaCita(LocalDate.now().minusDays(1));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.programarCita(cita));
        
        assertEquals("No se puede programar una cita en una fecha anterior a hoy", exception.getMessage());
        verify(citaRepository, never()).save(any());
    }

    @Test
    void programarCita_ConNombreVacio_DeberiaThrownException() {
        // Given
        cita.setNombreCompleto("");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.programarCita(cita));
        
        assertEquals("El nombre completo es obligatorio", exception.getMessage());
    }

    @Test
    void programarCita_ConCedulaInvalida_DeberiaThrownException() {
        // Given
        cita.setCedula("123");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.programarCita(cita));
        
        assertEquals("La cédula debe contener entre 8 y 15 dígitos", exception.getMessage());
    }

    @Test
    void programarCita_ConCorreoInvalido_DeberiaThrownException() {
        // Given
        cita.setCorreoElectronico("correo-invalido");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.programarCita(cita));
        
        assertEquals("El correo electrónico debe tener un formato válido", exception.getMessage());
    }

    @Test
    void obtenerCitasPorCorreo_ConCorreoValido_DeberiaRetornarCitas() {
        // Given
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByCorreoElectronico("juan.perez@email.com")).thenReturn(citas);

        // When
        List<Cita> resultado = citaService.obtenerCitasPorCorreo("juan.perez@email.com");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombreCompleto());
        verify(citaRepository).findByCorreoElectronico("juan.perez@email.com");
    }

    @Test
    void obtenerCitasPorCorreo_ConCorreoVacio_DeberiaThrownException() {
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.obtenerCitasPorCorreo(""));
        
        assertEquals("El correo electrónico es obligatorio", exception.getMessage());
    }

    @Test
    void filtrarCitasPorEstado_DeberiaRetornarCitasFiltradas() {
        // Given
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByCorreoElectronicoAndEstado("juan.perez@email.com", EstadoCita.CONFIRMADA))
            .thenReturn(citas);

        // When
        List<Cita> resultado = citaService.filtrarCitasPorEstado("juan.perez@email.com", EstadoCita.CONFIRMADA);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository).findByCorreoElectronicoAndEstado("juan.perez@email.com", EstadoCita.CONFIRMADA);
    }

    @Test
    void cancelarCita_CuandoExiste_DeberiaCancelarCita() {
        // Given
        when(citaRepository.findById("1")).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // When
        Cita resultado = citaService.cancelarCita("1");

        // Then
        assertNotNull(resultado);
        assertEquals(EstadoCita.CANCELADA, resultado.getEstado());
        verify(citaRepository).findById("1");
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    void cancelarCita_CuandoYaEstaCancelada_DeberiaThrownException() {
        // Given
        cita.setEstado(EstadoCita.CANCELADA);
        when(citaRepository.findById("1")).thenReturn(Optional.of(cita));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.cancelarCita("1"));
        
        assertEquals("La cita ya está cancelada", exception.getMessage());
        verify(citaRepository).findById("1");
        verify(citaRepository, never()).save(any());
    }

    @Test
    void cancelarCita_CuandoNoExiste_DeberiaThrownException() {
        // Given
        when(citaRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.cancelarCita("999"));
        
        assertEquals("Cita no encontrada con ID: 999", exception.getMessage());
    }

    @Test
    void obtenerCitaPorId_CuandoExiste_DeberiaRetornarCita() {
        // Given
        when(citaRepository.findById("1")).thenReturn(Optional.of(cita));

        // When
        Optional<Cita> resultado = citaService.obtenerCitaPorId("1");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombreCompleto());
    }

    @Test
    void obtenerCitasPorEstado_DeberiaRetornarCitasConEstado() {
        // Given
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByEstado(EstadoCita.CONFIRMADA)).thenReturn(citas);

        // When
        List<Cita> resultado = citaService.obtenerCitasPorEstado(EstadoCita.CONFIRMADA);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository).findByEstado(EstadoCita.CONFIRMADA);
    }

    @Test
    void obtenerCitasPorEspecialidad_DeberiaRetornarCitasDeEspecialidad() {
        // Given
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByEspecialidad("Medicina General")).thenReturn(citas);

        // When
        List<Cita> resultado = citaService.obtenerCitasPorEspecialidad("Medicina General");

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository).findByEspecialidad("Medicina General");
    }

    @Test
    void obtenerCitasPorFecha_DeberiaRetornarCitasEnFecha() {
        // Given
        LocalDate fecha = LocalDate.now().plusDays(1);
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByFechaCita(fecha)).thenReturn(citas);

        // When
        List<Cita> resultado = citaService.obtenerCitasPorFecha(fecha);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository).findByFechaCita(fecha);
    }

    @Test
    void contarCitasPorEstado_DeberiaRetornarConteo() {
        // Given
        when(citaRepository.countByEstado(EstadoCita.CONFIRMADA)).thenReturn(5L);

        // When
        long resultado = citaService.contarCitasPorEstado(EstadoCita.CONFIRMADA);

        // Then
        assertEquals(5L, resultado);
        verify(citaRepository).countByEstado(EstadoCita.CONFIRMADA);
    }

    @Test
    void eliminarCita_CuandoExiste_DeberiaEliminarCita() {
        // Given
        when(citaRepository.existsById("1")).thenReturn(true);

        // When
        assertDoesNotThrow(() -> citaService.eliminarCita("1"));

        // Then
        verify(citaRepository).existsById("1");
        verify(citaRepository).deleteById("1");
    }

    @Test
    void eliminarCita_CuandoNoExiste_DeberiaThrownException() {
        // Given
        when(citaRepository.existsById("999")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> citaService.eliminarCita("999"));
        
        assertEquals("Cita no encontrada con ID: 999", exception.getMessage());
    }
}