package edu.eci.cvds.citasmedicas.service;

import edu.eci.cvds.citasmedicas.model.Especialidad;

import java.util.List;
import java.util.Optional;

public interface IEspecialidadService {

    /**
     * Obtiene todas las especialidades disponibles
     * @return List<Especialidad> Lista de todas las especialidades
     */
    List<Especialidad> obtenerTodasLasEspecialidades();

    /**
     * Obtiene una especialidad por su ID
     * @param id El ID de la especialidad
     * @return Optional<Especialidad> La especialidad encontrada o vacío
     */
    Optional<Especialidad> obtenerEspecialidadPorId(String id);

    /**
     * Obtiene una especialidad por su nombre
     * @param nombre El nombre de la especialidad
     * @return Optional<Especialidad> La especialidad encontrada o vacío
     */
    Optional<Especialidad> obtenerEspecialidadPorNombre(String nombre);

    /**
     * Crea una nueva especialidad
     * @param especialidad La especialidad a crear
     * @return Especialidad La especialidad creada
     */
    Especialidad crearEspecialidad(Especialidad especialidad);

    /**
     * Actualiza una especialidad existente
     * @param id El ID de la especialidad a actualizar
     * @param especialidad Los nuevos datos de la especialidad
     * @return Especialidad La especialidad actualizada
     */
    Especialidad actualizarEspecialidad(String id, Especialidad especialidad);

    /**
     * Elimina una especialidad por su ID
     * @param id El ID de la especialidad a eliminar
     */
    void eliminarEspecialidad(String id);

    /**
     * Verifica si existe una especialidad con el nombre dado
     * @param nombre El nombre de la especialidad
     * @return boolean true si existe, false en caso contrario
     */
    boolean existeEspecialidad(String nombre);

    /**
     * Inicializa las especialidades básicas si no existen
     */
    void inicializarEspecialidades();
}