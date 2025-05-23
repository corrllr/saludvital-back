package edu.eci.cvds.citasmedicas.service;

import edu.eci.cvds.citasmedicas.model.Especialidad;
import edu.eci.cvds.citasmedicas.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService implements IEspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Autowired
    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public List<Especialidad> obtenerTodasLasEspecialidades() {
        return especialidadRepository.findAll();
    }

    @Override
    public Optional<Especialidad> obtenerEspecialidadPorId(String id) {
        return especialidadRepository.findById(id);
    }

    @Override
    public Optional<Especialidad> obtenerEspecialidadPorNombre(String nombre) {
        return especialidadRepository.findByNombreIgnoreCase(nombre);
    }

    @Override
    public Especialidad crearEspecialidad(Especialidad especialidad) {
        if (especialidadRepository.existsByNombre(especialidad.getNombre())) {
            throw new RuntimeException("Ya existe una especialidad con el nombre: " + especialidad.getNombre());
        }
        return especialidadRepository.save(especialidad);
    }

    @Override
    public Especialidad actualizarEspecialidad(String id, Especialidad especialidad) {
        return especialidadRepository.findById(id)
                .map(especialidadExistente -> {
                    especialidadExistente.setNombre(especialidad.getNombre());
                    especialidadExistente.setDescripcion(especialidad.getDescripcion());
                    especialidadExistente.setDoctor(especialidad.getDoctor());
                    especialidadExistente.setUbicacion(especialidad.getUbicacion());
                    especialidadExistente.setImagen(especialidad.getImagen());
                    return especialidadRepository.save(especialidadExistente);
                })
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
    }

    @Override
    public void eliminarEspecialidad(String id) {
        if (!especialidadRepository.existsById(id)) {
            throw new RuntimeException("Especialidad no encontrada con ID: " + id);
        }
        especialidadRepository.deleteById(id);
    }

    @Override
    public boolean existeEspecialidad(String nombre) {
        return especialidadRepository.existsByNombre(nombre);
    }

    @Override
    public void inicializarEspecialidades() {
        if (especialidadRepository.count() == 0) {
            List<Especialidad> especialidades = List.of(
                new Especialidad(
                    "Medicina General",
                    "Atención médica integral para el cuidado de la salud general del paciente.",
                    "Dr. Carlos Rodríguez",
                    "Consulta 101 - Piso 1",
                    "https://example.com/medicina-general.jpg"
                ),
                new Especialidad(
                    "Psicología",
                    "Atención en salud mental y bienestar emocional del paciente.",
                    "Dra. Ana García",
                    "Consulta 201 - Piso 2",
                    "https://example.com/psicologia.jpg"
                ),
                new Especialidad(
                    "Ortopedia",
                    "Especialidad médica dedicada al diagnóstico y tratamiento de lesiones del sistema musculoesquelético.",
                    "Dr. Miguel Torres",
                    "Consulta 301 - Piso 3",
                    "https://example.com/ortopedia.jpg"
                ),
                new Especialidad(
                    "Odontología",
                    "Cuidado integral de la salud bucodental y tratamientos dentales especializados.",
                    "Dra. Laura Martínez",
                    "Consulta 401 - Piso 4",
                    "https://example.com/odontologia.jpg"
                )
            );
            
            especialidadRepository.saveAll(especialidades);
        }
    }
}