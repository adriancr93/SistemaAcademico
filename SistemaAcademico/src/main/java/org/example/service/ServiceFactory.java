package org.example.service;

import org.example.service.impl.EstudianteServiceImpl;
import org.example.service.impl.ProfesorServiceImpl;
import org.example.service.impl.CursoServiceImpl;
import org.example.service.impl.GrupoServiceImpl;
import org.example.service.impl.GrupoCursoServiceImpl;

/**
 * ServiceFactory - Fábrica centralizada de servicios (Factory Method + Singleton).
 *
 * Antes cada vista creaba su servicio directamente:
 *   this.estudianteService = new EstudianteServiceImpl();
 *
 * Ahora se obtiene desde aquí. Si se cambia la implementación solo se toca esta clase.
 */
public class ServiceFactory {

    private static ServiceFactory instance;

    private final EstudianteService estudianteService;
    private final ProfesorService profesorService;
    private final CursoService cursoService;
    private final GrupoService grupoService;
    private final GrupoCursoService grupoCursoService;

    private ServiceFactory() {
        this.estudianteService = new EstudianteServiceImpl();
        this.profesorService = new ProfesorServiceImpl();
        this.cursoService = new CursoServiceImpl();
        this.grupoService = new GrupoServiceImpl();
        this.grupoCursoService = new GrupoCursoServiceImpl();
    }

    // Singleton: una sola instancia en toda la app
    public static ServiceFactory getInstance() {
        if (instance == null) {
            synchronized (ServiceFactory.class) {
                if (instance == null) {
                    instance = new ServiceFactory();
                }
            }
        }
        return instance;
    }

    // Métodos de fábrica para obtener cada servicio

    public EstudianteService getEstudianteService() {
        return estudianteService;
    }

    public ProfesorService getProfesorService() {
        return profesorService;
    }

    public CursoService getCursoService() {
        return cursoService;
    }

    public GrupoService getGrupoService() {
        return grupoService;
    }

    public GrupoCursoService getGrupoCursoService() {
        return grupoCursoService;
    }
}
