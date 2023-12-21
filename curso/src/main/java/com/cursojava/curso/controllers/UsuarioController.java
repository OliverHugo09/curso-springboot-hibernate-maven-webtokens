package com.cursojava.curso.controllers;

import com.cursojava.curso.dao.UsuarioDao;
import com.cursojava.curso.models.Usuario;
import com.cursojava.curso.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// Esta clase es un controlador REST que maneja las solicitudes HTTP
@RestController
public class UsuarioController {

    // Inyección de dependencia: se utiliza para conectar UsuarioDao con este controlador
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    // Este método maneja las solicitudes GET a la ruta "api/usuario/{id}"
    @RequestMapping(value = "api/usuario/{id}", method = RequestMethod.GET)
    public Usuario getUsuario(@PathVariable Long id){
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Oliver");
        usuario.setApellido("Lucero");
        usuario.setEmail("oliverlucero@gmail.com");
        usuario.setPassword("oliver123");
        usuario.setTelefono("+52 984 318 6500");
        return usuario;
    }

    // Este método maneja las solicitudes GET a la ruta "api/usuarios"
    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario> getUsuarios(@RequestHeader(value = "Authorization") String token){
        if (!validarToken(token)){return null;}
        // Devuelve una lista de usuarios obtenida desde UsuarioDao
        return usuarioDao.getUsuarios();

    }

    private boolean validarToken(String token){
        String usuarioId = jwtUtil.getKey(token);
        return (usuarioId != null);
        }

    // Este método maneja las solicitudes POST a la ruta "api/usuarios"
    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuario(@RequestBody Usuario usuario){

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        usuario.setPassword(hash);

        usuarioDao.registrar(usuario);
    }

    @RequestMapping(value = "api/usuario1")
    public Usuario editarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNombre("Oliver");
        usuario.setApellido("Lucero");
        usuario.setEmail("oliverlucero@gmail.com");
        usuario.setPassword("oliver123");
        usuario.setTelefono("+52 984 318 6500");
        return usuario;
    }

    // Este método maneja las solicitudes DELETE a la ruta "api/usuarios/{id}"
    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void eliminarUsuario(@RequestHeader(value = "Authorization") String token,
                                @PathVariable Long id){
        if (!validarToken(token)){return;}
        // Elimina un usuario identificado por el ID utilizando UsuarioDao
        usuarioDao.eliminar(id);
    }

}
