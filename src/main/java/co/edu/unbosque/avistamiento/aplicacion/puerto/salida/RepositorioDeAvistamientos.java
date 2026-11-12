package co.edu.unbosque.avistamiento.aplicacion.puerto.salida;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.avistamiento.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.avistamiento.aplicacion.modelo.ConteoPorEspecie;

public interface RepositorioDeAvistamientos {

    List<Avistamiento> buscarTodos();

    Optional<Avistamiento> buscarPorId(Long id);

    Avistamiento guardar(Avistamiento avistamiento);

    boolean existe(Long id);

    void eliminar(Long id);

    List<ConteoPorEspecie> contarPorEspecie();
}