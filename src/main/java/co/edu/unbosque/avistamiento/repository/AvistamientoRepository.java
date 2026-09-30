package co.edu.unbosque.avistamiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.avistamiento.model.Avistamiento;

@Repository 
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long>{
    
    @Query("SELECT a.especie, COUNT(a) FROM Avistamiento a GROUP BY a.especie")
    List<Object[]> contarPorEspecie();
}
