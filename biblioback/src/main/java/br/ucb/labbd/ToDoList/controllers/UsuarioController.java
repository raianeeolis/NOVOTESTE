package br.ucb.labbd.ToDoList.controllers;

import org.springframework.web.bind.annotation.*;
import br.ucb.labbd.ToDoList.services.UsuarioService;
import br.ucb.labbd.ToDoList.entities.Usuario;
import br.ucb.labbd.ToDoList.repositories.GrupoUsuarioRepository;
import br.ucb.labbd.ToDoList.entities.GrupoUsuarios;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final GrupoUsuarioRepository grupoRepo;

    public UsuarioController(UsuarioService usuarioService, GrupoUsuarioRepository grupoRepo){
        this.usuarioService = usuarioService;
        this.grupoRepo = grupoRepo;
    }

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.findAll(); // you can implement findAll in service
    }

    @PostMapping
    public Usuario criar(@RequestBody Usuario u){
        // Expect front to provide id via DB function, or we can call a native query. Simpler: persist as-is.
        return usuarioService.createUser(u);
    }
}
