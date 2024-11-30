package com.persona2.persona2.Repositorios;


import com.persona2.persona2.ClasesDeEntidad.LibroEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroEntidadRepository extends JpaRepository<LibroEntidad, Long> {
    LibroEntidad findByTitulo(String titulo); // Método para buscar libros por título
    long countByLanguage(String language);
    List<LibroEntidad> findByLanguage(String language); // Método para encontrar libros por idioma
}


