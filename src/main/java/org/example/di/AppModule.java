package org.example.di;

import org.example.controllers.CalificacionController;
import org.example.config.TokenManager;
import org.example.controllers.CategoriaController;
import org.example.controllers.MembresiaTipoController;
import org.example.controllers.NotificacionController;
import org.example.controllers.PublicacionController;
import org.example.controllers.QuejaUsuarioController;
import org.example.controllers.QuejaVentaController;
import org.example.controllers.RolController;
import org.example.controllers.UsuarioController;
import org.example.controllers.UsuarioMembresiaController;
import org.example.controllers.UsuarioBaneadoController;
import org.example.controllers.VentaController;
import org.example.repositories.CalificacionRepository;
import org.example.repositories.CategoriaRepository;
import org.example.repositories.MembresiaTipoRepository;
import org.example.repositories.NotificacionRepository;
import org.example.repositories.PublicacionRepository;
import org.example.repositories.QuejaUsuarioRepository;
import org.example.repositories.QuejaVentaRepository;
import org.example.repositories.RolRepository;
import org.example.repositories.UsuarioRepository;
import org.example.repositories.UsuarioMembresiaRepository;
import org.example.repositories.UsuarioBaneadoRepository;
import org.example.repositories.VentaRepository;
import org.example.routers.CalificacionRouter;
import org.example.routers.CategoriaRouter;
import org.example.routers.MembresiaTipoRouter;
import org.example.routers.NotificacionRouter;
import org.example.routers.PublicacionRouter;
import org.example.routers.QuejaUsuarioRouter;
import org.example.routers.QuejaVentaRouter;
import org.example.routers.RolRouter;
import org.example.routers.UsuarioRouter;
import org.example.routers.UsuarioMembresiaRouter;
import org.example.routers.UsuarioBaneadoRouter;
import org.example.routers.VentaRouter;
import org.example.services.CalificacionService;
import org.example.services.CategoriaService;
import org.example.services.MembresiaTipoService;
import org.example.services.NotificacionService;
import org.example.services.PublicacionService;
import org.example.services.QuejaUsuarioService;
import org.example.services.QuejaVentaService;
import org.example.services.RolService;
import org.example.services.UsuarioService;
import org.example.services.UsuarioMembresiaService;
import org.example.services.UsuarioBaneadoService;
import org.example.services.VentaService;

public class AppModule {

    public static RolRouter initRol() {
        RolRepository rolRepository = new RolRepository();
        RolService rolService = new RolService(rolRepository);
        RolController rolController = new RolController(rolService);
        return new RolRouter(rolController);
    }

    public static UsuarioRouter initUsuario(TokenManager tokenManager) {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        UsuarioController usuarioController = new UsuarioController(usuarioService, tokenManager);
        return new UsuarioRouter(usuarioController);
    }

    public static MembresiaTipoRouter initMembresiaTipo() {
        MembresiaTipoRepository membresiaTipoRepository = new MembresiaTipoRepository();
        MembresiaTipoService membresiaTipoService = new MembresiaTipoService(membresiaTipoRepository);
        MembresiaTipoController membresiaTipoController = new MembresiaTipoController(membresiaTipoService);
        return new MembresiaTipoRouter(membresiaTipoController);
    }

    public static UsuarioMembresiaRouter initUsuarioMembresia() {
        UsuarioMembresiaRepository umRepository = new UsuarioMembresiaRepository();
        UsuarioMembresiaService umService = new UsuarioMembresiaService(umRepository);
        UsuarioMembresiaController umController = new UsuarioMembresiaController(umService);
        return new UsuarioMembresiaRouter(umController);
    }

    public static PublicacionRouter initPublicacion() {
        PublicacionRepository publicacionRepository = new PublicacionRepository();
        UsuarioMembresiaRepository umRepository = new UsuarioMembresiaRepository();
        PublicacionService publicacionService = new PublicacionService(publicacionRepository);
        PublicacionController publicacionController = new PublicacionController(publicacionService);
        return new PublicacionRouter(publicacionController);
    }

    public static VentaRouter initVenta() {
        VentaRepository ventaRepository = new VentaRepository();
        PublicacionRepository publicacionRepository = new PublicacionRepository();
        VentaService ventaService = new VentaService(ventaRepository, publicacionRepository);
        VentaController ventaController = new VentaController(ventaService);
        return new VentaRouter(ventaController);
    }

    public static CalificacionRouter initCalificacion() {
        CalificacionRepository calificacionRepository = new CalificacionRepository();
        PublicacionRepository publicacionRepository = new PublicacionRepository();
        CalificacionService calificacionService = new CalificacionService(calificacionRepository, publicacionRepository);
        CalificacionController calificacionController = new CalificacionController(calificacionService);
        return new CalificacionRouter(calificacionController);
    }

    public static CategoriaRouter initCategoria() {
        CategoriaRepository categoriaRepository = new CategoriaRepository();
        CategoriaService categoriaService = new CategoriaService(categoriaRepository);
        CategoriaController categoriaController = new CategoriaController(categoriaService);
        return new CategoriaRouter(categoriaController);
    }

    public static NotificacionRouter initNotificacion() {
        NotificacionRepository notificacionRepository = new NotificacionRepository();
        NotificacionService notificacionService = new NotificacionService(notificacionRepository);
        NotificacionController notificacionController = new NotificacionController(notificacionService);
        return new NotificacionRouter(notificacionController);
    }

    public static UsuarioBaneadoRouter initUsuarioBaneado() {
        UsuarioBaneadoRepository ubRepository = new UsuarioBaneadoRepository();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        UsuarioBaneadoService ubService = new UsuarioBaneadoService(ubRepository, usuarioRepository);
        UsuarioBaneadoController ubController = new UsuarioBaneadoController(ubService);
        return new UsuarioBaneadoRouter(ubController);
    }

    public static QuejaUsuarioRouter initQuejaUsuario() {
        QuejaUsuarioRepository quejaRepository = new QuejaUsuarioRepository();
        QuejaUsuarioService quejaService = new QuejaUsuarioService(quejaRepository);
        QuejaUsuarioController quejaController = new QuejaUsuarioController(quejaService);
        return new QuejaUsuarioRouter(quejaController);
    }

    public static QuejaVentaRouter initQuejaVenta() {
        QuejaVentaRepository quejaVentaRepository = new QuejaVentaRepository();
        VentaRepository ventaRepository = new VentaRepository();
        PublicacionRepository publicacionRepository = new PublicacionRepository();
        QuejaVentaService quejaVentaService = new QuejaVentaService(quejaVentaRepository, ventaRepository, publicacionRepository);
        QuejaVentaController quejaVentaController = new QuejaVentaController(quejaVentaService);
        return new QuejaVentaRouter(quejaVentaController);
    }
}