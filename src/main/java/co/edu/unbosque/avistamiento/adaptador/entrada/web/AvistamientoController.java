package co.edu.unbosque.avistamiento.adaptador.entrada.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.avistamiento.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.avistamiento.aplicacion.puerto.entrada.GestionarAvistamientos;
import co.edu.unbosque.avistamiento.aplicacion.servicio.AvistamientoInvalidoException;

@RestController
@RequestMapping("/avistamientos")
public class AvistamientoController {

    private final GestionarAvistamientos avistamientos;

    public AvistamientoController(GestionarAvistamientos avistamientos) {
        this.avistamientos = avistamientos;
    }

    @GetMapping
    public ResponseEntity<List<Avistamiento>> obtenerTodos() {
        return ResponseEntity.ok(avistamientos.obtenerAvistamiento());
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<Object[]>> resumen() {
        List<Object[]> filas = avistamientos.resumenPorEspecie().stream()
                .map(c -> new Object[] { c.especie(), c.cantidad() })
                .toList();
        return ResponseEntity.ok(filas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> buscarPorId(@PathVariable Long id) {
        return avistamientos.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Avistamiento> crear(@RequestBody Avistamiento avistamiento) {
        Avistamiento creado = avistamientos.agregarAvistamiento(avistamiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avistamiento> actualizar(@PathVariable Long id, @RequestBody Avistamiento datosNuevos) {
        return avistamientos.actualizar(id, datosNuevos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (avistamientos.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AvistamientoInvalidoException.class)
    public ResponseEntity<Map<String, String>> avistamientoInvalido(AvistamientoInvalidoException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}