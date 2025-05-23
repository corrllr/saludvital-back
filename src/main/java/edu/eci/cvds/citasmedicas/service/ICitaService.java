package edu.eci.cvds.citasmedicas.service;

import edu.eci.cvds.citasmedicas.model.Cita;
import edu.eci.cvds.citasmedicas.model.EstadoCita;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ICitaService {

    /**
     * Programa una nueva cita médica
     * @param cita La cita a programar
     * @return Cita La cita programada
     */
    Cita programarCita(Cita cita);

    /**
     * Obtiene todas las citas de un usuario por su correo electrónico
     * @param correoElectronico El correo electrónico del usuario
     * @return List<Cita> Lista de citas del usuario
     */
    List<Cita> obtenerCitasPorCorreo(String correoElectronico);

    /**
     * Filtra las citas de un usuario por estado
     * @param correoElectronico El correo electrónico del usuario
     * @param estado El estado de la cita
     * @return List<Cita> Lista de citas filtradas
     */
    List<Cita> filtrarCitasPorEstado(String correoElectronico, EstadoCita estado);

    /**
     * Cancela una cita existente
     * @param id El ID de la cita a cancelar
     * @return Cita La cita cancelada
     */
    Cita cancelarCita(String id);

    /**
     * Obtiene una cita por su ID
     * @param id El ID de la cita
     * @return Optional<Cita> La cita encontrada o vacío
     */
    Optional<Cita> obtenerCitaPorId(String id);

    /**
     * Obtiene todas las citas del sistema
     * @return List<Cita> Lista de todas las citas
     */
    List<Cita> obtenerTodasLasCitas();

    /**
     * Obtiene todas las citas por estado
     * @param estado El estado de la cita
     * @return List<Cita> Lista de citas con el estado especificado
     */
    List<Cita> obtenerCitasPorEstado(EstadoCita estado);

    /**
     * Obtiene citas por especialidad
     * @param especialidad La especialidad médica
     * @return List<Cita> Lista de citas de la especialidad
     */
    List<Cita> obtenerCitasPorEspecialidad(String especialidad);

    /**
     * Obtiene citas por fecha
     * @param fecha La fecha de la cita
     * @return List<Cita> Lista de citas en la fecha especificada
     */
    List<Cita> obtenerCitasPorFecha(LocalDate fecha);

    /**
     * Cuenta el número de citas por estado
     * @param estado El estado de la cita
     * @return long Número de citas con el estado especificado
     */
    long contarCitasPorEstado(EstadoCita estado);

    /**
     * Elimina una cita por su ID
     * @param id El ID de la cita a eliminar
     */
    void eliminarCita(String id);
}