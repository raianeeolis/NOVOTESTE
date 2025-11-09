package br.ucb.labbd.ToDoList.controllers;

// Exemplo em Java no seu pacote de controllers (ex: ucb.estudo.controller)

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String homePage() {
        return "Sistema de Tarefas - API está no ar!";
    }
}