package co.edu.unbosque.avistamiento.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.unbosque.avistamiento.aplicacion.puerto.entrada.GestionarAvistamientos;
import co.edu.unbosque.avistamiento.aplicacion.puerto.salida.Reloj;
import co.edu.unbosque.avistamiento.aplicacion.puerto.salida.RepositorioDeAvistamientos;
import co.edu.unbosque.avistamiento.aplicacion.servicio.AvistamientoService;
import co.edu.unbosque.avistamiento.aplicacion.servicio.ValidadorDeAvistamiento;

@Configuration
public class ConfiguracionAplicacion {

    @Bean
    public ValidadorDeAvistamiento validadorDeAvistamiento(Reloj reloj) {
        return new ValidadorDeAvistamiento(reloj);
    }

    @Bean
    public GestionarAvistamientos gestionarAvistamientos(RepositorioDeAvistamientos repositorio,
            ValidadorDeAvistamiento validador) {
        return new AvistamientoService(repositorio, validador);
    }
}