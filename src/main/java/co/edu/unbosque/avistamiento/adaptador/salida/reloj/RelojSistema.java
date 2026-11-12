package co.edu.unbosque.avistamiento.adaptador.salida.reloj;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import co.edu.unbosque.avistamiento.aplicacion.puerto.salida.Reloj;

@Component
public class RelojSistema implements Reloj {

    @Override
    public LocalDate hoy() {
        return LocalDate.now();
    }
}