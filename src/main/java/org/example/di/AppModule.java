package org.example.di;

import org.example.controllers.*;
import org.example.config.TokenManager;
import org.example.repositories.*;
import org.example.routers.*;
import org.example.services.*;

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

    public static HoraEntregaRouter initHoraEntrega() {
        HoraEntregaRepository horaEntregaRepository = new HoraEntregaRepository();
        HoraEntregaService horaEntregaService = new HoraEntregaService(horaEntregaRepository);
        HoraEntregaController horaEntregaController  = new HoraEntregaController(horaEntregaService);
        HoraEntregaRouter horaEntregaRouter  = new HoraEntregaRouter(horaEntregaController);
        return horaEntregaRouter;
    }
    // En AppModule.java
    public static PublicacionRouter initPublicacion() {
        PublicacionRepository publicacionRepository = new PublicacionRepository();


        UsuarioMembresiaRepository umRepository = new UsuarioMembresiaRepository();

        PublicacionService publicacionService = new PublicacionService(publicacionRepository, umRepository);

        PublicacionController publicacionController = new PublicacionController(publicacionService);
        return new PublicacionRouter(publicacionController);
    }

}