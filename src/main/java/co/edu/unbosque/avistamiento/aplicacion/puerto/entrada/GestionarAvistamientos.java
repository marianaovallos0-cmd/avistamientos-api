package co.edu.unbosque.avistamiento.aplicacion.puerto.entrada;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.avistamiento.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.avistamiento.aplicacion.modelo.ConteoPorEspecie;

public interface GestionarAvistamientos {

    List<Avistamiento> obtenerAvistamiento();

    Optional<Avistamiento> buscarPorId(Long id);

    Avistamiento agregarAvistamiento(Avistamiento avistamiento);

    Optional<Avistamiento> actualizar(Long id, Avistamiento datosNuevos);

    boolean eliminar(Long id);

    List<ConteoPorEspecie> resumenPorEspecie();
}