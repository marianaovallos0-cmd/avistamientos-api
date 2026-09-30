package co.edu.unbosque.avistamiento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.unbosque.avistamiento.model.Avistamiento;
import co.edu.unbosque.avistamiento.repository.AvistamientoRepository;

@Service 
public class AvistamientoService {

    private AvistamientoRepository avistamientoRepository;


    public AvistamientoService(AvistamientoRepository avistamientoRepository) {
        this.avistamientoRepository = avistamientoRepository;
    }

    public List<Avistamiento> obtenerAvistamiento(){
        return avistamientoRepository.findAll();
        
    }

    public Optional<Avistamiento> buscarPorId(Long id){
        return avistamientoRepository.findById(id);
    }

    public Avistamiento agregarAvistamiento(Avistamiento avistamiento){
        return avistamientoRepository.save(avistamiento);

    }

    public Optional<Avistamiento> actualizar (Long id, Avistamiento datosNuevos){
        return avistamientoRepository.findById(id).map(a -> {
            a.setEspecie(datosNuevos.getEspecie());
            a.setFecha(datosNuevos.getFecha());
            a.setLugar(datosNuevos.getLugar());
            a.setObservador(datosNuevos.getObservador());
            return avistamientoRepository.save(a);
        });
    }

    public boolean eliminar(Long id){
        if (avistamientoRepository.existsById(id)) {
            avistamientoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Object[]> resumenPorEspecie(){
        return avistamientoRepository.contarPorEspecie();
    }
           
    }
    

