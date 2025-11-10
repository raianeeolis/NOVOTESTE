package br.ucb.labbd.ToDoList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ToDoListApplication {

	public static void main(String[] args) {
                System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("1234"));
		SpringApplication.run(ToDoListApplication.class, args);
	}

}
