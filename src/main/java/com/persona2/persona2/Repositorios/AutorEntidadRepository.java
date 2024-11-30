package com.persona2.persona2.Repositorios;


import com.persona2.persona2.ClasesDeEntidad.AutorEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorEntidadRepository extends JpaRepository<AutorEntidad, Long> {
    AutorEntidad findByNombre(String nombre); // Método para buscar autores por nombre
    List<AutorEntidad> findByAñoNacimientoLessThanEqualAndAñoDefuncionGreaterThanEqual(int año, int añoFin); // Método para buscar autores vivos en un año específico

}
