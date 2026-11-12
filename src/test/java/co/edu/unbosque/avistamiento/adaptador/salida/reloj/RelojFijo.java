package co.edu.unbosque.avistamiento.adaptador.salida.reloj;

import java.time.LocalDate;

import co.edu.unbosque.avistamiento.aplicacion.puerto.salida.Reloj;

public class RelojFijo implements Reloj {

    private final LocalDate fecha;

    public RelojFijo(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public LocalDate hoy() {
        return fecha;
    }
}