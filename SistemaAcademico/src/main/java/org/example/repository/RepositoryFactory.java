package org.example.repository;

import org.example.repository.impl.EstudianteRepositoryMongoDB;
import org.example.repository.impl.ProfesorRepositoryMongoDB;
import org.example.repository.impl.CursoRepositoryMongoDB;
import org.example.repository.impl.GrupoRepositoryMongoDB;
import org.example.repository.impl.GrupoCursoRepositoryMongoDB;

/**
 * RepositoryFactory - Fábrica centralizada de repositorios (Factory Method + Singleton).
 *
 * Antes cada servicio creaba su repositorio directamente:
 *   this.estudianteRepository = new EstudianteRepositoryMongoDB();
 *
 * Ahora se obtiene desde aquí. Si se migra de MongoDB a otra BD solo se toca esta clase.
 */
public class RepositoryFactory {

    private static RepositoryFactory instance;

    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final CursoRepository cursoRepository;
    private final GrupoRepository grupoRepository;
    private final GrupoCursoRepository grupoCursoRepository;

    private RepositoryFactory() {
        this.estudianteRepository = new EstudianteRepositoryMongoDB();
        this.profesorRepository = new ProfesorRepositoryMongoDB();
        this.cursoRepository = new CursoRepositoryMongoDB();
        this.grupoRepository = new GrupoRepositoryMongoDB();
        this.grupoCursoRepository = new GrupoCursoRepositoryMongoDB();
    }

    // Singleton: una sola instancia en toda la app
    public static RepositoryFactory getInstance() {
        if (instance == null) {
            synchronized (RepositoryFactory.class) {
                if (instance == null) {
                    instance = new RepositoryFactory();
                }
            }
        }
        return instance;
    }

    // Métodos de fábrica para obtener cada repositorio
    public EstudianteRepository getEstudianteRepository() {
        return estudianteRepository;
    }

    public ProfesorRepository getProfesorRepository() {
        return profesorRepository;
    }

    public CursoRepository getCursoRepository() {
        return cursoRepository;
    }

    public GrupoRepository getGrupoRepository() {
        return grupoRepository;
    }

    public GrupoCursoRepository getGrupoCursoRepository() {
        return grupoCursoRepository;
    }
}
