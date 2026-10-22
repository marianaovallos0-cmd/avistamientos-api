package co.edu.unbosque.avistamiento.validacion;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class RelojSistema implements Reloj {

    @Override
    public LocalDate hoy() {
        return LocalDate.now();
    }
}