package com.microservice.agendacita.repository;

import com.microservice.agendacita.model.Agendar;
import com.microservice.agendacita.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendarRe extends JpaRepository<Agendar, Long> {

    List<Agendar> findByPacienteId(Long pacienteId);

    List<Agendar> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);

    List<Agendar> findByEstado(EstadoCita estado);
}