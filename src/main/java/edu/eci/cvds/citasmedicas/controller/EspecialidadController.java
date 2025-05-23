package edu.eci.cvds.citasmedicas.controller;

import edu.eci.cvds.citasmedicas.model.Especialidad;
import edu.eci.cvds.citasmedicas.service.IEspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Especialidades", description = "Gestión de especialidades médicas")
public class EspecialidadController {

    private final IEspecialidadService especialidadService;

    @Autowired
    public EspecialidadController(IEspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las especialidades", 
               description = "Obtiene la lista completa de especialidades médicas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Especialidad>> obtenerTodasLasEspecialidades() {
        try {
            List<Especialidad> especialidades = especialidadService.obtenerTodasLasEspecialidades();
            return ResponseEntity.ok(especialidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener especialidad por ID", 
               description = "Obtiene una especialidad específica por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad encontrada"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Especialidad> obtenerEspecialidadPorId(
            @Parameter(description = "ID de la especialidad") @PathVariable String id) {
        try {
            Optional<Especialidad> especialidad = especialidadService.obtenerEspecialidadPorId(id);
            return especialidad.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar/{nombre}")
    @Operation(summary = "Buscar especialidad por nombre", 
               description = "Busca una especialidad por su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad encontrada"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Especialidad> obtenerEspecialidadPorNombre(
            @Parameter(description = "Nombre de la especialidad") @PathVariable String nombre) {
        try {
            Optional<Especialidad> especialidad = especialidadService.obtenerEspecialidadPorNombre(nombre);
            return especialidad.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva especialidad", 
               description = "Crea una nueva especialidad médica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Especialidad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o especialidad ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crearEspecialidad(@Valid @RequestBody Especialidad especialidad) {
        try {
            Especialidad nuevaEspecialidad = especialidadService.crearEspecialidad(especialidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEspecialidad);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar especialidad", 
               description = "Actualiza una especialidad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> actualizarEspecialidad(
            @Parameter(description = "ID de la especialidad") @PathVariable String id,
            @Valid @RequestBody Especialidad especialidad) {
        try {
            Especialidad especialidadActualizada = especialidadService.actualizarEspecialidad(id, especialidad);
            return ResponseEntity.ok(especialidadActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar especialidad", 
               description = "Elimina una especialidad por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Especialidad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> eliminarEspecialidad(
            @Parameter(description = "ID de la especialidad") @PathVariable String id) {
        try {
            especialidadService.eliminarEspecialidad(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }

    @PostMapping("/inicializar")
    @Operation(summary = "Inicializar especialidades por defecto", 
               description = "Crea las especialidades médicas básicas si no existen")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidades inicializadas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> inicializarEspecialidades() {
        try {
            especialidadService.inicializarEspecialidades();
            return ResponseEntity.ok("Especialidades inicializadas exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error al inicializar especialidades");
        }
    }
}