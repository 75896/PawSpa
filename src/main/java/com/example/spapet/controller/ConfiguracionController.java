package com.example.spapet.controller;

import com.example.spapet.dto.ConfiguracionDTO;
import com.example.spapet.service.ConfiguracionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    @GetMapping
    public ResponseEntity<List<ConfiguracionDTO>> obtenerTodos() {

        return ResponseEntity.ok(
                configuracionService.obtenerTodos());
    }

    @GetMapping("/{clave}")
    public ResponseEntity<ConfiguracionDTO> obtenerPorClave(
            @PathVariable String clave) {

        return ResponseEntity.ok(
                configuracionService.obtenerPorClave(clave));
    }

    @PostMapping
    public ResponseEntity<ConfiguracionDTO> crear(
            @RequestBody ConfiguracionDTO configuracionDTO) {

        ConfiguracionDTO nuevaConfiguracion = configuracionService.crear(configuracionDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaConfiguracion);
    }

    @PutMapping("/{clave}")
    public ResponseEntity<ConfiguracionDTO> actualizar(
            @PathVariable String clave,
            @RequestBody ConfiguracionDTO configuracionDTO) {

        return ResponseEntity.ok(
                configuracionService.actualizar(clave, configuracionDTO));
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(
            @PathVariable String clave) {

        configuracionService.eliminar(clave);

        return ResponseEntity.noContent().build();
    }
}