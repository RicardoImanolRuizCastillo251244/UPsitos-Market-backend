package org.example.controllers;

import io.javalin.http.Context;
import org.example.config.TokenManager;
import org.example.models.Usuario;
import org.example.models.LoginDTO;
import org.example.services.UsuarioService;

import javax.security.auth.login.LoginException;
import java.util.Map;

public class UsuarioController {
    private final UsuarioService usuarioService;
    private final TokenManager tokenManager;

    public UsuarioController(UsuarioService usuarioService, TokenManager tokenManager) {
        this.usuarioService = usuarioService;
        this.tokenManager = tokenManager;
    }

    public void register(Context ctx) {
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            String plainPassword = usuario.getContrasena();
            int rol = usuario.getId_rol();
            System.out.println(rol);
            Usuario savedUsuario = usuarioService.registerUsuario(usuario, plainPassword);

            ctx.status(201).json(Map.of("message", "Usuario registrado con éxito.", "userId", savedUsuario.getId_usuario()));
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public void getUsuariosConMembresiaPremium(Context ctx) {
        try {
            ctx.json(usuarioService.getUsuariosConMembresiaPremium());
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(404).result(e.getMessage());
        }
    }

    public void getUsuariosConMembresiaSemiPremium(Context ctx) {
        try {
            ctx.json(usuarioService.getUsuariosConMembresiaSemiPremium());
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(404).result(e.getMessage());
        }
    }


    public void updateUsuario(Context ctx) {
        try {
            String userIdFromToken = ctx.header("User-Id");
            int id = Integer.parseInt(userIdFromToken);

            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuario.setId_usuario(id);

            String plainPassword = usuario.getContrasena() != null ? new String(usuario.getContrasena()) : null;

            usuarioService.update(usuario, plainPassword);
            ctx.status(200).json(Map.of("message", "Usuario actualizado correctamente."));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).result("ID de usuario inválido.");
        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    public void login(Context ctx) {
        try {
            LoginDTO loginData = ctx.bodyAsClass(LoginDTO.class);
            Usuario usuario = usuarioService.loginUsuario(loginData.getCorreo_usuario(), loginData.getContrasena());
            String token = tokenManager.issueToken(String.valueOf(usuario.getId_usuario()));
            int id_rol = usuario.getId_rol();

            ctx.status(200).json(Map.of("token", token, "userId", usuario.getId_usuario(), "rol", usuario.getId_rol()));
        } catch (LoginException e) {
            ctx.status(401).result(e.getMessage()); // 401 Unauthorized
        } catch (Exception e) {
            ctx.status(500).result("Error durante el inicio de sesión: " + e.getMessage());
        }
    }

    public void deleteUsuario(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            usuarioService.deleteById(id);
            ctx.status(204);
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de usuario inválido.");
        } catch (Exception e) {
            ctx.status(500).result("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    public void getUsuarioById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
            usuarioService.findById(id)
                    .ifPresentOrElse(
                            usuario -> {
                                usuario.setContrasena(null);
                                ctx.status(200).json(usuario);
                            },
                            () -> ctx.status(404).result("Usuario no encontrado.")
                    );
        } catch (NumberFormatException e) {
            ctx.status(400).result("ID de usuario inválido.");
        } catch (Exception e) {
            ctx.status(500).result("Error al buscar el usuario: " + e.getMessage());
        }
    }

    public void getProfile(Context ctx) {
        try {
            String userId = ctx.header("User-Id");
            int id = Integer.parseInt(userId);
            usuarioService.findById(id)
                    .ifPresentOrElse(
                            usuario -> {
                                usuario.setContrasena(null); // No exponer hash
                                ctx.status(200).json(usuario);
                            },
                            () -> ctx.status(404).result("Perfil de usuario no encontrado.")
                    );
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener el perfil: " + e.getMessage());
        }
    }

    public void getAllUsuarios(Context ctx) {
        try {
            ctx.status(200).json(usuarioService.findAll());
        } catch (Exception e) {
            ctx.status(500).result("Error al obtener los usuarios: " + e.getMessage());
        }
    }
    public void updateUsuarioGeneral(Context ctx) {
        try {
            int targetUserId = Integer.parseInt(ctx.pathParam("id"));
            org.example.models.UpdateUsuarioDTO dto = ctx.bodyAsClass(org.example.models.UpdateUsuarioDTO.class);

            usuarioService.updateUsuarioParcial(targetUserId, dto);

            ctx.status(200).json(Map.of("message", "Perfil actualizado correctamente."));

        } catch (IllegalArgumentException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("Error al actualizar perfil: " + e.getMessage());
        }
    }
}