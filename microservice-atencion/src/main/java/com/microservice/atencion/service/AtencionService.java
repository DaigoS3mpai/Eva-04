package com.microservice.atencion.service;

import com.microservice.atencion.dto.AtencionDTO;
import com.microservice.atencion.model.Atencion;
import com.microservice.atencion.repository.AtencionRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AtencionService {

    @Autowired
    private AtencionRepository atencionRepository;

    public boolean existeAtencion(int id){
        return atencionRepository.existsById(id);
    }

    public List<Atencion> findAll(){
        return atencionRepository.findAll();
    }

    public Optional<Atencion> getPatientById(int id){
        return atencionRepository.findById(id);
    }

    public Atencion save(AtencionDTO dto){
        Atencion atencion = new Atencion();
        atencion.setFecha_atencion(dto.getFecha_atencion());
        atencion.setHora_atencion(dto.getHora_atencion());
        atencion.setCosto(dto.getCosto());
        atencion.setId_paciente(dto.getId_paciente());
        atencion.setComentario(dto.getComentario());

        return atencionRepository.save(atencion);
    }

    public void delete(int id){
        atencionRepository.deleteById(id);
    }
}
