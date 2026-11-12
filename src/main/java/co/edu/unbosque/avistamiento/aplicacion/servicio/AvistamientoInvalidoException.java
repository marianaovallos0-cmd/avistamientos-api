package co.edu.unbosque.avistamiento.aplicacion.servicio;

public class AvistamientoInvalidoException extends RuntimeException {

    public AvistamientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}