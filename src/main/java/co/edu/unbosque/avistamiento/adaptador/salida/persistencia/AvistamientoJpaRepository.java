package co.edu.unbosque.avistamiento.adaptador.salida.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AvistamientoJpaRepository extends JpaRepository<AvistamientoEntidad, Long> {

    @Query("SELECT a.especie, COUNT(a) FROM AvistamientoEntidad a GROUP BY a.especie")
    List<Object[]> contarPorEspecie();
}