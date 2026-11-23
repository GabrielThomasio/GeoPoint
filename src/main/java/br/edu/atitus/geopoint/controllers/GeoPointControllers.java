package br.edu.atitus.geopoint.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helloworld")
public class GeoPointControllers {


    @GetMapping("/{namePath}")
    public ResponseEntity<String> getHelloWorld
            (@RequestParam(required = false) String name,
             @PathVariable String namePath){
        if (name == null || name.isBlank())
            name= namePath == null ? "World" : namePath;
        String response = "Hello " + name + "!!!";
        return ResponseEntity.ok(response);
    }
}