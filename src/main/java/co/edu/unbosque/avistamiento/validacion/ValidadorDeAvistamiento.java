package co.edu.unbosque.avistamiento.validacion;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import co.edu.unbosque.avistamiento.model.Avistamiento;

@Component
public class ValidadorDeAvistamiento {

    private final Reloj reloj;

    public ValidadorDeAvistamiento(Reloj reloj) {
        this.reloj = reloj;
    }

    public String validar(Avistamiento avistamiento) {

        String especie = avistamiento.getEspecie();
        if (especie == null || especie.trim().length() < 3 || especie.trim().length() > 50) {
            return "especie: debe tener entre 3 y 50 caracteres";
        }

        String textoFecha = avistamiento.getFecha();
        if (textoFecha == null) {
            return "fecha: es obligatoria con el formato AAAA-MM-DD";
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(textoFecha);
        } catch (DateTimeParseException e) {
            return "fecha: debe tener el formato AAAA-MM-DD";
        }

        if (fecha.isAfter(reloj.hoy())) {
            return "fecha: no puede ser posterior a hoy";
        }

        if (estaVacio(avistamiento.getLugar())) {
            return "lugar: no puede estar vacío";
        }
        if (estaVacio(avistamiento.getObservador())) {
            return "observador: no puede estar vacío";
        }

        return null;
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}