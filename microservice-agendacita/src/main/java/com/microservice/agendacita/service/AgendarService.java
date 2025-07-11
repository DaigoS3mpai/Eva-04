package com.microservice.agendacita.service;

import com.microservice.agendacita.model.Agendar;
import com.microservice.agendacita.model.EstadoCita;
import com.microservice.agendacita.repository.AgendarRe;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendarService {

    private final AgendarRe agendarRepository;

    public Agendar agendar(Agendar agendar) {
        agendar.setEstado(EstadoCita.AGENDADA);
        return agendarRepository.save(agendar);
    }

    public Agendar obtenerPorId(Long id) {
        return agendarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con ID: " + id));
    }

    public List<Agendar> listarPorPaciente(Long pacienteId) {
        return agendarRepository.findByPacienteId(pacienteId);
    }

    public Agendar cancelar(Long id) {
        Agendar cita = obtenerPorId(id);
        cita.setEstado(EstadoCita.CANCELADA);
        return agendarRepository.save(cita);
    }

    public List<Agendar> listarPorRangoDeFecha(LocalDateTime desde, LocalDateTime hasta) {
        return agendarRepository.findByFechaHoraBetween(desde, hasta);
    }

    public List<Agendar> listarPorEstado(EstadoCita estado) {
        return agendarRepository.findByEstado(estado);
    }

    public List<Agendar> obtenerCitas() {
        return agendarRepository.findAll();
    }

}