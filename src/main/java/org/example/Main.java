package org.example;

import org.example.config.JwtMiddleware;
import org.example.config.TokenManager;
import org.example.di.AppModule;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(7000);

        //app.get("/", ctx -> ctx.result("Hello World"));

        TokenManager tokenManager = new TokenManager();
        JwtMiddleware jwtMiddleware = new JwtMiddleware(tokenManager);

        // Proteger endpoints de usuario
        app.before("/usuario/profile", jwtMiddleware);
        app.before("/usuario/*/update", jwtMiddleware);
        
        // Proteger endpoints de publicación
        app.before("/publicacion", ctx -> {
            String method = ctx.method().toString();
            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE") || method.equals("PATCH")) {
                jwtMiddleware.handle(ctx);
            }
        });
        
        // Proteger endpoints de ventas
        app.before("/venta", jwtMiddleware);
        app.before("/venta/*", jwtMiddleware);
        
        // Proteger endpoints de notificaciones
        app.before("/notificacion", jwtMiddleware);
        app.before("/notificacion/*", jwtMiddleware);
        
        // Proteger endpoints de quejas
        app.before("/queja-usuario", jwtMiddleware);
        app.before("/queja-usuario/*", jwtMiddleware);
        app.before("/queja-venta", jwtMiddleware);
        app.before("/queja-venta/*", jwtMiddleware);
        
        // Proteger endpoints de membresías
        app.before("/usuario-membresia", jwtMiddleware);
        app.before("/usuario-membresia/*", jwtMiddleware);

        AppModule.initRol().register(app);
        AppModule.initUsuario(tokenManager).register(app);
        AppModule.initMembresiaTipo().register(app);
        AppModule.initUsuarioMembresia().register(app);
        AppModule.initPublicacion().register(app);
        AppModule.initVenta().register(app);
        AppModule.initCalificacion().register(app);
        AppModule.initCategoria().register(app);
        AppModule.initNotificacion().register(app);
        AppModule.initUsuarioBaneado().register(app);
        AppModule.initQuejaUsuario().register(app);
        AppModule.initQuejaVenta().register(app);
        AppModule.initHoraEntrega().register(app);
    }
}