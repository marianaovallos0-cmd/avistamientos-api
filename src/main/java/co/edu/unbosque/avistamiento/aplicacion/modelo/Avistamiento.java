package co.edu.unbosque.avistamiento.aplicacion.modelo;

import lombok.Data;

@Data
public class Avistamiento {

    private Long id;
    private String especie;
    private String lugar;
    private String fecha;
    private String observador;
}