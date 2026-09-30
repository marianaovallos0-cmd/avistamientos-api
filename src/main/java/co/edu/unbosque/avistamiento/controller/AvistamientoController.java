package co.edu.unbosque.avistamiento.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.avistamiento.model.Avistamiento;
import co.edu.unbosque.avistamiento.service.AvistamientoService;

@RestController
@RequestMapping("/avistamientos")
public class AvistamientoController {

    private final AvistamientoService service;

    public AvistamientoController(AvistamientoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Avistamiento>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerAvistamiento());
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<Object[]>> resumen() {
        return ResponseEntity.ok(service.resumenPorEspecie());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Avistamiento> crear(@RequestBody Avistamiento avistamiento) {
        if (avistamiento.getEspecie() == null || avistamiento.getEspecie().isBlank() ||
            avistamiento.getLugar() == null || avistamiento.getLugar().isBlank() ||
            avistamiento.getFecha() == null || avistamiento.getFecha().isBlank() ||
            avistamiento.getObservador() == null || avistamiento.getObservador().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Avistamiento creado = service.agregarAvistamiento(avistamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avistamiento> actualizar(@PathVariable Long id, @RequestBody Avistamiento datosNuevos) {
        return service.actualizar(id, datosNuevos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();  
        }
        return ResponseEntity.notFound().build();        
    }

    
}