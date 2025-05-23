package edu.eci.cvds.citasmedicas.service;

import edu.eci.cvds.citasmedicas.model.Cita;
import edu.eci.cvds.citasmedicas.model.EstadoCita;
import edu.eci.cvds.citasmedicas.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService implements ICitaService {

    private final CitaRepository citaRepository;

    @Autowired
    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Override
    public Cita programarCita(Cita cita) {
        validarCita(cita);
        
        // Validar que la fecha no sea anterior a hoy
        if (cita.getFechaCita().isBefore(LocalDate.now())) {
            cita.setEstado(EstadoCita.RECHAZADA);
            throw new RuntimeException("No se puede programar una cita en una fecha anterior a hoy");
        }
        
        cita.setEstado(EstadoCita.CONFIRMADA);
        cita.setFechaCreacion(LocalDateTime.now());
        cita.setFechaActualizacion(LocalDateTime.now());
        
        return citaRepository.save(cita);
    }

    @Override
    public List<Cita> obtenerCitasPorCorreo(String correoElectronico) {
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            throw new RuntimeException("El correo electrónico es obligatorio");
        }
        return citaRepository.findByCorreoElectronico(correoElectronico);
    }

    @Override
    public List<Cita> filtrarCitasPorEstado(String correoElectronico, EstadoCita estado) {
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            throw new RuntimeException("El correo electrónico es obligatorio");
        }
        return citaRepository.findByCorreoElectronicoAndEstado(correoElectronico, estado);
    }

    @Override
    public Cita cancelarCita(String id) {
        return citaRepository.findById(id)
                .map(cita -> {
                    if (cita.getEstado() == EstadoCita.CANCELADA) {
                        throw new RuntimeException("La cita ya está cancelada");
                    }
                    cita.setEstado(EstadoCita.CANCELADA);
                    cita.setFechaActualizacion(LocalDateTime.now());
                    return citaRepository.save(cita);
                })
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
    }

    @Override
    public Optional<Cita> obtenerCitaPorId(String id) {
        return citaRepository.findById(id);
    }

    @Override
    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    @Override
    public List<Cita> obtenerCitasPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }

    @Override
    public List<Cita> obtenerCitasPorEspecialidad(String especialidad) {
        return citaRepository.findByEspecialidad(especialidad);
    }

    @Override
    public List<Cita> obtenerCitasPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaCita(fecha);
    }

    @Override
    public long contarCitasPorEstado(EstadoCita estado) {
        return citaRepository.countByEstado(estado);
    }

    @Override
    public void eliminarCita(String id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con ID: " + id);
        }
        citaRepository.deleteById(id);
    }

    /**
     * Valida los datos de la cita antes de guardarla
     * @param cita La cita a validar
     */
    private void validarCita(Cita cita) {
        if (cita.getNombreCompleto() == null || cita.getNombreCompleto().trim().isEmpty()) {
            throw new RuntimeException("El nombre completo es obligatorio");
        }
        
        if (cita.getCedula() == null || cita.getCedula().trim().isEmpty()) {
            throw new RuntimeException("La cédula es obligatoria");
        }
        
        if (!cita.getCedula().matches("\\d{8,15}")) {
            throw new RuntimeException("La cédula debe contener entre 8 y 15 dígitos");
        }
        
        if (cita.getCorreoElectronico() == null || cita.getCorreoElectronico().trim().isEmpty()) {
            throw new RuntimeException("El correo electrónico es obligatorio");
        }
        
        if (!esCorreoValido(cita.getCorreoElectronico())) {
            throw new RuntimeException("El correo electrónico debe tener un formato válido");
        }
        
        if (cita.getFechaCita() == null) {
            throw new RuntimeException("La fecha de la cita es obligatoria");
        }
        
        if (cita.getEspecialidad() == null || cita.getEspecialidad().trim().isEmpty()) {
            throw new RuntimeException("La especialidad es obligatoria");
        }
        
        if (cita.getDoctor() == null || cita.getDoctor().trim().isEmpty()) {
            throw new RuntimeException("El doctor es obligatorio");
        }
        
        if (cita.getUbicacion() == null || cita.getUbicacion().trim().isEmpty()) {
            throw new RuntimeException("La ubicación es obligatoria");
        }
    }

    /**
     * Valida el formato del correo electrónico
     * @param correo El correo a validar
     * @return boolean true si el formato es válido, false en caso contrario
     */
    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return correo.matches(regex);
    }
}