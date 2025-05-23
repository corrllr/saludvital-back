package edu.eci.cvds.citasmedicas.repository;

import edu.eci.cvds.citasmedicas.model.Especialidad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspecialidadRepository extends MongoRepository<Especialidad, String> {
    
    /**
     * Busca una especialidad por su nombre
     * @param nombre El nombre de la especialidad
     * @return Optional<Especialidad> La especialidad encontrada o vacío
     */
    Optional<Especialidad> findByNombre(String nombre);
    
    /**
     * Busca una especialidad por el nombre ignorando mayúsculas y minúsculas
     * @param nombre El nombre de la especialidad
     * @return Optional<Especialidad> La especialidad encontrada o vacío
     */
    Optional<Especialidad> findByNombreIgnoreCase(String nombre);
    
    /**
     * Verifica si existe una especialidad con el nombre dado
     * @param nombre El nombre de la especialidad
     * @return boolean true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}
