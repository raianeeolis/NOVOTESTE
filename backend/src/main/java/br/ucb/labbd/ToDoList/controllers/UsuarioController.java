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
        return usuarioService.findAll(); 
    }

    @PostMapping
    public Usuario criar(@RequestBody Usuario u){
        return usuarioService.createUser(u);
    }
}
