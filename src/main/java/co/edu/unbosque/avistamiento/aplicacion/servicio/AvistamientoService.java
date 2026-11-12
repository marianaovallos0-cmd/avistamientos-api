package co.edu.unbosque.avistamiento.aplicacion.servicio;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.avistamiento.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.avistamiento.aplicacion.modelo.ConteoPorEspecie;
import co.edu.unbosque.avistamiento.aplicacion.puerto.entrada.GestionarAvistamientos;
import co.edu.unbosque.avistamiento.aplicacion.puerto.salida.RepositorioDeAvistamientos;

public class AvistamientoService implements GestionarAvistamientos {

    private final RepositorioDeAvistamientos repositorio;
    private final ValidadorDeAvistamiento validador;

    public AvistamientoService(RepositorioDeAvistamientos repositorio, ValidadorDeAvistamiento validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }

    @Override
    public List<Avistamiento> obtenerAvistamiento() {
        return repositorio.buscarTodos();
    }

    @Override
    public Optional<Avistamiento> buscarPorId(Long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public Avistamiento agregarAvistamiento(Avistamiento avistamiento) {
        validar(avistamiento);
        return repositorio.guardar(avistamiento);
    }

    @Override
    public Optional<Avistamiento> actualizar(Long id, Avistamiento datosNuevos) {
        validar(datosNuevos);
        return repositorio.buscarPorId(id).map(a -> {
            a.setEspecie(datosNuevos.getEspecie());
            a.setFecha(datosNuevos.getFecha());
            a.setLugar(datosNuevos.getLugar());
            a.setObservador(datosNuevos.getObservador());
            return repositorio.guardar(a);
        });
    }

    @Override
    public boolean eliminar(Long id) {
        if (repositorio.existe(id)) {
            repositorio.eliminar(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ConteoPorEspecie> resumenPorEspecie() {
        return repositorio.contarPorEspecie();
    }

    private void validar(Avistamiento avistamiento) {
        String error = validador.validar(avistamiento);
        if (error != null) {
            throw new AvistamientoInvalidoException(error);
        }
    }
}