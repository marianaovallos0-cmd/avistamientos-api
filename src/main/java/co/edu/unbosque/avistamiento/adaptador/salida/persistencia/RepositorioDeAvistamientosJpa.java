package co.edu.unbosque.avistamiento.adaptador.salida.persistencia;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import co.edu.unbosque.avistamiento.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.avistamiento.aplicacion.modelo.ConteoPorEspecie;
import co.edu.unbosque.avistamiento.aplicacion.puerto.salida.RepositorioDeAvistamientos;

@Component
public class RepositorioDeAvistamientosJpa implements RepositorioDeAvistamientos {

    private final AvistamientoJpaRepository jpa;

    public RepositorioDeAvistamientosJpa(AvistamientoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Avistamiento> buscarTodos() {
        return jpa.findAll().stream().map(this::aDominio).toList();
    }

    @Override
    public Optional<Avistamiento> buscarPorId(Long id) {
        return jpa.findById(id).map(this::aDominio);
    }

    @Override
    public Avistamiento guardar(Avistamiento avistamiento) {
        AvistamientoEntidad guardada = jpa.save(aEntidad(avistamiento));
        return aDominio(guardada);
    }

    @Override
    public boolean existe(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<ConteoPorEspecie> contarPorEspecie() {
        return jpa.contarPorEspecie().stream()
                .map(fila -> new ConteoPorEspecie((String) fila[0], (Long) fila[1]))
                .toList();
    }

    private AvistamientoEntidad aEntidad(Avistamiento a) {
        AvistamientoEntidad e = new AvistamientoEntidad();
        e.setId(a.getId());
        e.setEspecie(a.getEspecie());
        e.setLugar(a.getLugar());
        e.setFecha(a.getFecha());
        e.setObservador(a.getObservador());
        return e;
    }

    private Avistamiento aDominio(AvistamientoEntidad e) {
        Avistamiento a = new Avistamiento();
        a.setId(e.getId());
        a.setEspecie(e.getEspecie());
        a.setLugar(e.getLugar());
        a.setFecha(e.getFecha());
        a.setObservador(e.getObservador());
        return a;
    }
}