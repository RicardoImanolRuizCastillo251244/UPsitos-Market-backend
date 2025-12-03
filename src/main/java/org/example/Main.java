package org.example;

import io.javalin.Javalin;
import org.example.config.JwtMiddleware;
import org.example.config.TokenManager;
import org.example.di.AppModule;
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

        app.before("/usuario/profile", jwtMiddleware);

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