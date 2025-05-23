package edu.eci.cvds.citasmedicas.repository;

import edu.eci.cvds.citasmedicas.model.Cita;
import edu.eci.cvds.citasmedicas.model.EstadoCita;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends MongoRepository<Cita, String> {
    
    /**
     * Busca todas las citas de un usuario por su correo electrónico
     * @param correoElectronico El correo electrónico del usuario
     * @return List<Cita> Lista de citas del usuario
     */
    List<Cita> findByCorreoElectronico(String correoElectronico);
    
    /**
     * Busca citas por correo electrónico y estado
     * @param correoElectronico El correo electrónico del usuario
     * @param estado El estado de la cita
     * @return List<Cita> Lista de citas filtradas
     */
    List<Cita> findByCorreoElectronicoAndEstado(String correoElectronico, EstadoCita estado);
    
    /**
     * Busca todas las citas por estado
     * @param estado El estado de la cita
     * @return List<Cita> Lista de citas con el estado especificado
     */
    List<Cita> findByEstado(EstadoCita estado);
    
    /**
     * Busca citas por especialidad
     * @param especialidad La especialidad médica
     * @return List<Cita> Lista de citas de la especialidad
     */
    List<Cita> findByEspecialidad(String especialidad);
    
    /**
     * Busca citas por fecha
     * @param fechaCita La fecha de la cita
     * @return List<Cita> Lista de citas en la fecha especificada
     */
    List<Cita> findByFechaCita(LocalDate fechaCita);
    
    /**
     * Busca citas por cédula
     * @param cedula La cédula del paciente
     * @return List<Cita> Lista de citas del paciente
     */
    List<Cita> findByCedula(String cedula);
    
    /**
     * Busca citas entre dos fechas
     * @param fechaInicio La fecha de inicio
     * @param fechaFin La fecha de fin
     * @return List<Cita> Lista de citas en el rango de fechas
     */
    List<Cita> findByFechaCitaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    /**
     * Cuenta el número de citas por estado
     * @param estado El estado de la cita
     * @return long Número de citas con el estado especificado
     */
    long countByEstado(EstadoCita estado);
}