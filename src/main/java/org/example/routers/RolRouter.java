package org.example.routers;

import io.javalin.Javalin;
import org.example.controllers.RolController;

public class RolRouter {
    private final RolController rolController;

    public RolRouter(RolController rolController) {
        this.rolController = rolController;
    }

    public void register(Javalin app) {
        app.post("/rol", rolController::saveRol);
        app.put("/rol/{id}", rolController::updateRol);
        app.delete("/rol/{id}", rolController::deleteRol);
        app.get("/rol/{id}", rolController::getRolById);
        app.get("/rol", rolController::getAllRoles);
    }
}