package edu.eci.cvds.citasmedicas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.cvds.citasmedicas.model.Especialidad;
import edu.eci.cvds.citasmedicas.service.IEspecialidadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EspecialidadController.class)
class EspecialidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEspecialidadService especialidadService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void obtenerTodasLasEspecialidades_DeberiaRetornar200ConLista() throws Exception {
        // Given
        List<Especialidad> especialidades = Arrays.asList(especialidad);
        when(especialidadService.obtenerTodasLasEspecialidades()).thenReturn(especialidades);

        // When & Then
        mockMvc.perform(get("/api/especialidades"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Medicina General"))
                .andExpect(jsonPath("$[0].doctor").value("Dr. Carlos Rodríguez"));

        verify(especialidadService).obtenerTodasLasEspecialidades();
    }

    @Test
    void obtenerEspecialidadPorId_CuandoExiste_DeberiaRetornar200() throws Exception {
        // Given
        when(especialidadService.obtenerEspecialidadPorId("1")).thenReturn(Optional.of(especialidad));

        // When & Then
        mockMvc.perform(get("/api/especialidades/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Medicina General"));

        verify(especialidadService).obtenerEspecialidadPorId("1");
    }

    @Test
    void obtenerEspecialidadPorId_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        // Given
        when(especialidadService.obtenerEspecialidadPorId("999")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/especialidades/999"))
                .andExpect(status().isNotFound());

        verify(especialidadService).obtenerEspecialidadPorId("999");
    }

    @Test
    void crearEspecialidad_ConDatosValidos_DeberiaRetornar201() throws Exception {
        // Given
        when(especialidadService.crearEspecialidad(any(Especialidad.class))).thenReturn(especialidad);

        // When & Then
        mockMvc.perform(post("/api/especialidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(especialidad)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Medicina General"));

        verify(especialidadService).crearEspecialidad(any(Especialidad.class));
    }

    @Test
    void crearEspecialidad_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        // Given
        Especialidad especialidadInvalida = new Especialidad();
        especialidadInvalida.setNombre(""); // Nombre vacío

        // When & Then
        mockMvc.perform(post("/api/especialidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(especialidadInvalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearEspecialidad_CuandoYaExiste_DeberiaRetornar400() throws Exception {
        // Given
        when(especialidadService.crearEspecialidad(any(Especialidad.class)))
            .thenThrow(new RuntimeException("Ya existe una especialidad con el nombre: Medicina General"));

        // When & Then
        mockMvc.perform(post("/api/especialidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(especialidad)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe una especialidad con el nombre: Medicina General"));

        verify(especialidadService).crearEspecialidad(any(Especialidad.class));
    }

    @Test
    void actualizarEspecialidad_CuandoExiste_DeberiaRetornar200() throws Exception {
        // Given
        when(especialidadService.actualizarEspecialidad(eq("1"), any(Especialidad.class)))
            .thenReturn(especialidad);

        // When & Then
        mockMvc.perform(put("/api/especialidades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(especialidad)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Medicina General"));

        verify(especialidadService).actualizarEspecialidad(eq("1"), any(Especialidad.class));
    }

    @Test
    void eliminarEspecialidad_CuandoExiste_DeberiaRetornar204() throws Exception {
        // Given
        doNothing().when(especialidadService).eliminarEspecialidad("1");

        // When & Then
        mockMvc.perform(delete("/api/especialidades/1"))
                .andExpect(status().isNoContent());

        verify(especialidadService).eliminarEspecialidad("1");
    }

    @Test
    void eliminarEspecialidad_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        // Given
        doThrow(new RuntimeException("Especialidad no encontrada"))
            .when(especialidadService).eliminarEspecialidad("999");

        // When & Then
        mockMvc.perform(delete("/api/especialidades/999"))
                .andExpect(status().isNotFound());

        verify(especialidadService).eliminarEspecialidad("999");
    }

    @Test
    void inicializarEspecialidades_DeberiaRetornar200() throws Exception {
        // Given
        doNothing().when(especialidadService).inicializarEspecialidades();

        // When & Then
        mockMvc.perform(post("/api/especialidades/inicializar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Especialidades inicializadas exitosamente"));

        verify(especialidadService).inicializarEspecialidades();
    }
}