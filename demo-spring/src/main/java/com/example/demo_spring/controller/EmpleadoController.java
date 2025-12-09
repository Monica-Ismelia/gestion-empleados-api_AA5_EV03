package com.example.demo_spring.controller; // src/main/java/com/example/demo_spring/controller/EmpleadoController.java

import com.example.demo_spring.model.Empleado;
import com.example.demo_spring.service.EmpleadoService;
import com.example.demo_spring.repository.EmpleadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository repository;
    // Bloque 403 de ejemplo para reutilizar
    private final String ERROR_403_JSON = "{\"status\":403,\"error\":\"No autorizado\",\"message\":\"No tiene permisos para acceder a este recurso\",\"timestamp\":\"2024-01-01T10:20:30\"}";
    private final String ERROR_404_JSON = "{\"status\":404,\"error\":\"NOT_FOUND\",\"message\":\"Empleado no encontrado\",\"timestamp\":\"2024-01-01T10:20:30\"}";
    private final String ERROR_400_JSON = "{\"status\":400,\"error\":\"BAD_REQUEST\",\"message\":\"Datos de entrada inválidos (ej. correo duplicado)\",\"timestamp\":\"2024-01-01T10:20:30\"}";
    // ---------------------------------------------------------
    // GET → Todos
    // ---------------------------------------------------------
    @Operation(summary = "Obtener todos los empleados")
        @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de empleados recuperada", // Texto de la descripción
        content = @Content(schema = @Schema(implementation = Empleado.class)) // <-- ¡Esto llena el recuadro!
    ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: Se requiere autenticación válida",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<Empleado>> getAll() {
        return ResponseEntity.ok(empleadoService.listarTodos());
    }

    // ---------------------------------------------------------
    // GET → Por ID
    @Operation(summary = "Obtener un empleado por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado encontrado", // Texto de la descripción
        content = @Content(schema = @Schema(implementation = Empleado.class)) // <-- ¡Esto llena el recuadro!
    ),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_404_JSON)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: Se requiere autenticación válida", // 403 AGREGADO
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        try {
            Empleado empleado = empleadoService.findById(id);
            return ResponseEntity.ok(empleado);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        }
    }

    // ---------------------------------------------------------
    // POST → Crear
    // ---------------------------------------------------------
    @Operation(summary = "Registrar un nuevo empleado")
        @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Empleado registrado con éxito",
                    content = @Content(schema = @Schema(implementation = Empleado.class))
                     ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_400_JSON)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: Se requiere autenticación válida", // 403 AGREGADO
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Empleado empleado) {

        try {
            Empleado nuevo = empleadoService.guardar(empleado);
            return ResponseEntity.status(201).body(nuevo);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // PUT → Actualización completa
    // ---------------------------------------------------------
  @Operation(
    summary = "Actualizar completamente un empleado (PUT)",
    requestBody = @RequestBody(
        description = "Objeto Empleado completo requerido para la actualización total.",
        required = true,
        content = @Content(
            schema = @Schema(implementation = Empleado.class),
            examples = @ExampleObject(
                name = "Ejemplo de Empleado completo",
                value = "{\"id\": 5, \"nombre\": \"Elena Garcías\", \"correo\": \"elena.g@ejemplo.com\", \"salario\": 55000.00, \"fechaIngreso\": \"2023-08-15\"}"
            )
        )
    )
) // <--- Cierre de @Operation--------------------------------------------------------------------

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado actualizado", // Texto de la descripción
        content = @Content(schema = @Schema(implementation = Empleado.class)) // <-- ¡Esto llena el recuadro!
    ),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_404_JSON)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_400_JSON)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: Se requiere autenticación válida", // 403 AGREGADO
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Empleado datos) {

        try {
            Empleado actualizado = empleadoService.actualizar(id, datos);
            return ResponseEntity.ok(actualizado);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // PATCH → Parcial
    // ---------------------------------------------------------
    @Operation(
    summary = "Actualizar parcialmente un empleado (PATCH)",
    requestBody = @RequestBody( // <-- El RequestBody se mueve dentro de @Operation
        description = "Campos a modificar. El correo debe ser único.",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "Ejemplo de modificación de nombre, salario y correo",
                value = "{\"nombre\": \"Maria\", \"salario\": 45000.00, \"correo\": \"nuevo.email@ejemplo.com\"}"
            )
        )
    )
) // <--- Cierre de @Operation
        @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado actualizado parcialmente", // Texto de la descripción
        content = @Content(schema = @Schema(implementation = Empleado.class)) // <-- ¡Esto llena el recuadro!
    ),        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o campo no permitido",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_400_JSON)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: Se requiere autenticación válida", // 403 AGREGADO
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
        })
    
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> cambios) {

        Empleado empleado = empleadoService.findById(id);

        for (String campo : cambios.keySet()) {

            Object v = cambios.get(campo);

            switch (campo) {
                case "nombre" -> empleado.setNombre(v.toString());

                case "correo" -> {
                    if (repository.existsByCorreo(v.toString()) &&
                       !v.toString().equals(empleado.getCorreo())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "El correo ya está registrado");
                    }
                    empleado.setCorreo(v.toString());
                }

                case "salario" -> empleado.setSalario(new BigDecimal(v.toString()));

                case "fechaIngreso" -> empleado.setFechaIngreso(LocalDate.parse(v.toString()));

                default ->
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Campo no permitido: " + campo);
            }
        }

        return ResponseEntity.ok(repository.save(empleado));
    }

    // ---------------------------------------------------------
    // DELETE → Eliminar
    // ---------------------------------------------------------
    
    @Operation(summary = "Eliminar un empleado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empleado eliminado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"mensaje\":\"Empleado eliminado\"}")
            )
        ),
        @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_404_JSON)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: Se requiere autenticación válida", // 403 AGREGADO
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = ERROR_403_JSON)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        try {
            empleadoService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Empleado eliminado"));

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
