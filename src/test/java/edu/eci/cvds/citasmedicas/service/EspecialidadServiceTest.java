package edu.eci.cvds.citasmedicas.service;

import edu.eci.cvds.citasmedicas.model.Especialidad;
import edu.eci.cvds.citasmedicas.repository.EspecialidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EspecialidadService especialidadService;

    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        especialidad = new Especialidad(
            "Medicina General",
            "Atención médica integral",
            "Dr. Carlos Rodríguez",
            "Consulta 101",
            "https://example.com/medicina-general.jpg"
        );
        especialidad.setId("1");
    }

    @Test
    void obtenerTodasLasEspecialidades_DeberiaRetornarListaCompleta() {
        // Given
        List<Especialidad> especialidades = Arrays.asList(especialidad);
        when(especialidadRepository.findAll()).thenReturn(especialidades);

        // When
        List<Especialidad> resultado = especialidadService.obtenerTodasLasEspecialidades();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Medicina General", resultado.get(0).getNombre());
        verify(especialidadRepository).findAll();
    }

    @Test
    void obtenerEspecialidadPorId_CuandoExiste_DeberiaRetornarEspecialidad() {
        // Given
        when(especialidadRepository.findById("1")).thenReturn(Optional.of(especialidad));

        // When
        Optional<Especialidad> resultado = especialidadService.obtenerEspecialidadPorId("1");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Medicina General", resultado.get().getNombre());
        verify(especialidadRepository).findById("1");
    }

    @Test
    void obtenerEspecialidadPorId_CuandoNoExiste_DeberiaRetornarVacio() {
        // Given
        when(especialidadRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Especialidad> resultado = especialidadService.obtenerEspecialidadPorId("999");

        // Then
        assertFalse(resultado.isPresent());
        verify(especialidadRepository).findById("999");
    }

    @Test
    void obtenerEspecialidadPorNombre_DeberiaRetornarEspecialidad() {
        // Given
        when(especialidadRepository.findByNombreIgnoreCase("medicina general"))
            .thenReturn(Optional.of(especialidad));

        // When
        Optional<Especialidad> resultado = especialidadService.obtenerEspecialidadPorNombre("medicina general");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Medicina General", resultado.get().getNombre());
        verify(especialidadRepository).findByNombreIgnoreCase("medicina general");
    }

    @Test
    void crearEspecialidad_CuandoNoExiste_DeberiaCrearEspecialidad() {
        // Given
        when(especialidadRepository.existsByNombre(anyString())).thenReturn(false);
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(especialidad);

        // When
        Especialidad resultado = especialidadService.crearEspecialidad(especialidad);

        // Then
        assertNotNull(resultado);
        assertEquals("Medicina General", resultado.getNombre());
        verify(especialidadRepository).existsByNombre("Medicina General");
        verify(especialidadRepository).save(especialidad);
    }

    @Test
    void crearEspecialidad_CuandoYaExiste_DeberiaThrownException() {
        // Given
        when(especialidadRepository.existsByNombre("Medicina General")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> especialidadService.crearEspecialidad(especialidad));
        
        assertEquals("Ya existe una especialidad con el nombre: Medicina General", exception.getMessage());
        verify(especialidadRepository).existsByNombre("Medicina General");
        verify(especialidadRepository, never()).save(any());
    }

    @Test
    void actualizarEspecialidad_CuandoExiste_DeberiaActualizarEspecialidad() {
        // Given
        Especialidad especialidadActualizada = new Especialidad(
            "Medicina Interna",
            "Nueva descripción",
            "Dr. Nuevo",
            "Nueva ubicación",
            "nueva-imagen.jpg"
        );
        
        when(especialidadRepository.findById("1")).thenReturn(Optional.of(especialidad));
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(especialidad);

        // When
        Especialidad resultado = especialidadService.actualizarEspecialidad("1", especialidadActualizada);

        // Then
        assertNotNull(resultado);
        verify(especialidadRepository).findById("1");
        verify(especialidadRepository).save(any(Especialidad.class));
    }

    @Test
    void actualizarEspecialidad_CuandoNoExiste_DeberiaThrownException() {
        // Given
        when(especialidadRepository.findById("999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> especialidadService.actualizarEspecialidad("999", especialidad));
        
        assertEquals("Especialidad no encontrada con ID: 999", exception.getMessage());
        verify(especialidadRepository).findById("999");
        verify(especialidadRepository, never()).save(any());
    }

    @Test
    void eliminarEspecialidad_CuandoExiste_DeberiaEliminarEspecialidad() {
        // Given
        when(especialidadRepository.existsById("1")).thenReturn(true);

        // When
        assertDoesNotThrow(() -> especialidadService.eliminarEspecialidad("1"));

        // Then
        verify(especialidadRepository).existsById("1");
        verify(especialidadRepository).deleteById("1");
    }

    @Test
    void eliminarEspecialidad_CuandoNoExiste_DeberiaThrownException() {
        // Given
        when(especialidadRepository.existsById("999")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> especialidadService.eliminarEspecialidad("999"));
        
        assertEquals("Especialidad no encontrada con ID: 999", exception.getMessage());
        verify(especialidadRepository).existsById("999");
        verify(especialidadRepository, never()).deleteById(any());
    }

    @Test
    void existeEspecialidad_CuandoExiste_DeberiaRetornarTrue() {
        // Given
        when(especialidadRepository.existsByNombre("Medicina General")).thenReturn(true);

        // When
        boolean resultado = especialidadService.existeEspecialidad("Medicina General");

        // Then
        assertTrue(resultado);
        verify(especialidadRepository).existsByNombre("Medicina General");
    }

    @Test
    void inicializarEspecialidades_CuandoNoHayEspecialidades_DeberiaInicializar() {
        // Given
        when(especialidadRepository.count()).thenReturn(0L);
        when(especialidadRepository.saveAll(any())).thenReturn(Arrays.asList());

        // When
        especialidadService.inicializarEspecialidades();

        // Then
        verify(especialidadRepository).count();
        verify(especialidadRepository).saveAll(any());
    }

    @Test
    void inicializarEspecialidades_CuandoYaHayEspecialidades_NoDeberiaInicializar() {
        // Given
        when(especialidadRepository.count()).thenReturn(4L);

        // When
        especialidadService.inicializarEspecialidades();

        // Then
        verify(especialidadRepository).count();
        verify(especialidadRepository, never()).saveAll(any());
    }
}