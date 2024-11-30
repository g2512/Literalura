package com.persona2.persona2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persona2.persona2.ClasesDeEntidad.AutorEntidad;
import com.persona2.persona2.ClasesDeEntidad.LibroEntidad;
import com.persona2.persona2.Repositorios.AutorEntidadRepository;
import com.persona2.persona2.Repositorios.LibroEntidadRepository;
import com.persona2.persona2.model.Autor;
import com.persona2.persona2.model.Libro;
import com.persona2.persona2.model.LibrosData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
@SpringBootApplication
public class Persona2Application implements CommandLineRunner {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LibroEntidadRepository libroEntidadRepository;
	@Autowired
	private AutorEntidadRepository autorEntidadRepository;


	private List<Libro> librosGuardados = new ArrayList<>();
	private List<Autor> autoresGuardados = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(Persona2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("**************\n Bienvenido a Literalura ! \n Ingresa el nombre del libro que quieres buscar \n o escribe \n 'salir' para terminar,\n 'mostrar' para ver los libros guardados, \n 'autores' para ver los autores guardados, \n 'contar' para contar y listar los libros por idioma\n 'vivos y el año para buscar los autores vivos en un año'\n**************\n Acá: ");
			String nombreDelLibro = scanner.nextLine();

			switch (nombreDelLibro.toLowerCase()) {
				case "salir":
					System.out.println("Saliendo del programa...");
					return;
				case "mostrar":
					mostrarLibrosGuardados();
					break;
				case "autores":
					mostrarAutoresGuardados();
					break;
				case "contar":
					contarYListarLibrosPorIdioma();
					break;
				case "vivos":
					System.out.print("Ingresa el año: ");
					int año = scanner.nextInt();
					scanner.nextLine(); // Consume el salto de línea
					mostrarAutoresVivosEnAño(año);
					break;
				default:
					buscarLibro(nombreDelLibro);
					break;
			}
		}
	}

	private void buscarLibro(String nombreDelLibro) throws Exception {
		String url = String.format("https://gutendex.com/books/?search=%s", nombreDelLibro.replace(" ", "%20"));
		String jsonResponse = restTemplate.getForObject(url, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		LibrosData librosData = objectMapper.readValue(jsonResponse, LibrosData.class);

		if (!librosData.getResults().isEmpty()) {
			// Obtén solo el primer libro de la lista
			Libro primerLibro = librosData.getResults().get(0);
			System.out.println("ID: " + primerLibro.getId());
			System.out.println("Título: " + primerLibro.getTitulo());
			System.out.println("Número de Descargas: " + primerLibro.getNumeroDescargas());

			// Comprueba si el libro ya existe en la base de datos
			LibroEntidad libroExistente = libroEntidadRepository.findByTitulo(primerLibro.getTitulo());
			if (libroExistente != null) {
				System.out.println("El libro ya existe en la base de datos.");
				return;
			}


			// Obtén el primer idioma
			String primerIdioma = primerLibro.getLanguages().isEmpty() ? "No disponible" : primerLibro.getLanguages().get(0);
			System.out.println("Primer Idioma: " + primerIdioma);

			// Obtén el primer autor
			String primerAutorNombre = "No disponible";
			AutorEntidad autorEntidad = null;
			if (!primerLibro.getAuthors().isEmpty()) {
				Autor primerAutor = primerLibro.getAuthors().get(0);
				primerAutorNombre = primerAutor.getName();
				System.out.println("Primer Autor: " + primerAutorNombre);
				System.out.println("Año de nacimiento: " + primerAutor.getBirth_year());
				System.out.println("Año de defunción: " + primerAutor.getDeath_year());

				// Comprueba si el autor existe en la bd y lo guarda el autor en la base de datos
				autorEntidad = autorEntidadRepository.findByNombre(primerAutor.getName());
				if (autorEntidad == null) {
					autorEntidad = convertirAutor(primerAutor);
					autorEntidadRepository.save(autorEntidad);
				} else {
					System.out.println("El autor ya existe en la base de datos.");
				}
			} else {
				System.out.println("No se encontraron autores para este libro.");
			}


			// Crea la entidad del libro y asigna el primer idioma y autor
			LibroEntidad libroEntidad = new LibroEntidad();
			libroEntidad.setTitulo(primerLibro.getTitulo());
			libroEntidad.setNumeroDescargas(primerLibro.getNumeroDescargas());
			libroEntidad.setLanguage(primerIdioma);
			libroEntidad.setAuthor(primerAutorNombre);

			// Guarda el libro en la base de datos
			libroEntidadRepository.save(libroEntidad);

			// Agrega el libro y el primer autor a las listas guardadas
			librosGuardados.add(primerLibro);
			if (!primerLibro.getAuthors().isEmpty()) {
				autoresGuardados.add(primerLibro.getAuthors().get(0));
			}
		} else {
			System.out.println("No se encontraron libros para la búsqueda.");
		}
	}

	private AutorEntidad convertirAutor(Autor autor) {
		AutorEntidad autorEntidad = new AutorEntidad();
		autorEntidad.setNombre(autor.getName());
		autorEntidad.setAñoNacimiento(autor.getBirth_year());
		autorEntidad.setAñoDefuncion(autor.getDeath_year());
		return autorEntidad;
	}

	private void mostrarLibrosGuardados() {
		if (librosGuardados.isEmpty()) {
			System.out.println("No hay libros guardados.");
		} else {
			System.out.println("Libros guardados:");
			for (Libro libro : librosGuardados) {
				System.out.println("ID: " + libro.getId());
				System.out.println("Título: " + libro.getTitulo());
				System.out.println("Número de Descargas: " + libro.getNumeroDescargas());

				if (!libro.getLanguages().isEmpty()) {
					String primerIdioma = libro.getLanguages().get(0);
					System.out.println("Primer Idioma: " + primerIdioma);
				} else {
					System.out.println("No se encontraron idiomas para este libro.");
				}

				if (!libro.getAuthors().isEmpty()) {
					Autor primerAutor = libro.getAuthors().get(0);
					System.out.println("Primer Autor: " + primerAutor.getName());
					System.out.println("Año de nacimiento: " + primerAutor.getBirth_year());
					System.out.println("Año de defunción: " + primerAutor.getDeath_year());
				} else {
					System.out.println("No se encontraron autores para este libro.");
				}

				System.out.println("----");
			}
		}
	}

	private void mostrarAutoresGuardados() {
		if (autoresGuardados.isEmpty()) {
			System.out.println("No hay autores guardados.");
		} else {
			System.out.println("Autores guardados:");
			for (Autor autor : autoresGuardados) {
				System.out.println("Nombre: " + autor.getName());
				System.out.println("Año de nacimiento: " + autor.getBirth_year());
				System.out.println("Año de defunción: " + autor.getDeath_year());
				System.out.println("----");
			}
		}
	}

	private void contarYListarLibrosPorIdioma() {
		long librosEnIngles = libroEntidadRepository.countByLanguage("en");
		long librosEnEspanol = libroEntidadRepository.countByLanguage("es");

		System.out.println("Número de libros en inglés: " + librosEnIngles);
		System.out.println("Libros en inglés:");
		List<LibroEntidad> librosEnInglesLista = libroEntidadRepository.findByLanguage("en");
		librosEnInglesLista.stream()
				.map(LibroEntidad::getTitulo)
				.forEach(System.out::println);

		System.out.println("Número de libros en español: " + librosEnEspanol);
		System.out.println("Libros en español:");
		List<LibroEntidad> librosEnEspanolLista = libroEntidadRepository.findByLanguage("es");
		librosEnEspanolLista.stream()
				.map(LibroEntidad::getTitulo)
				.forEach(System.out::println);
	}

	private void mostrarAutoresVivosEnAño(int año) {
		List<AutorEntidad> autoresVivos = autorEntidadRepository.findByAñoNacimientoLessThanEqualAndAñoDefuncionGreaterThanEqual(año, año);
		if (autoresVivos.isEmpty()) {
			System.out.println("No se encontraron autores vivos en el año " + año + ".");
		} else {
			System.out.println("Autores vivos en el año " + año + ":");
			for (AutorEntidad autor : autoresVivos) {
				System.out.println("Nombre: " + autor.getNombre());

			}

		}
	}
}