package ar.com.ada.api.aladas.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.aladas.entities.Vuelo;
import ar.com.ada.api.aladas.entities.Vuelo.EstadoVueloEnum;
import ar.com.ada.api.aladas.models.request.EstadoVueloRequest;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.services.AeropuertoService;
import ar.com.ada.api.aladas.services.VueloService;
import ar.com.ada.api.aladas.services.VueloService.ValidacionVueloDataEnum;

@RestController
public class VueloController {

    // Version mas "pro"
    private VueloService service;

    private AeropuertoService aeropuertoService;

    public VueloController(VueloService service, AeropuertoService aeropuertoService) {
        this.service = service;
        this.aeropuertoService = aeropuertoService;
    }

    @PostMapping("/api/vuelos")
    public ResponseEntity<GenericResponse> postCrearVuelo(@RequestBody Vuelo vuelo) {
        GenericResponse respuesta = new GenericResponse();

        ValidacionVueloDataEnum resultadoValidacion = service.validar(vuelo);
        if (resultadoValidacion == ValidacionVueloDataEnum.OK) {
            service.crear(vuelo);

            respuesta.isOk = true;
            respuesta.id = vuelo.getVueloId();
            respuesta.message = "Vuelo creado correctamente";

            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.isOk = false;
            respuesta.message = "Error(" + resultadoValidacion.toString() + ")";

            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    /*
     * Version 2 Post
     * 
     * @PostMapping("/api/v2/vuelos") public ResponseEntity<GenericResponse>
     * postCrearVueloV2(@RequestBody Vuelo vuelo) { GenericResponse respuesta = new
     * GenericResponse();
     * 
     * Aeropuerto ao = aeropuertoService.b
     * 
     * Vuelo vueloCreado = service.crear(vuelo.getFecha(), vuelo.getCapacidad(),
     * vuelo.getAeropuertoOrigen(), vuelo.getAeropuertoDestino(), vuelo.getPrecio(),
     * vuelo.getCodigoMoneda());
     * 
     * respuesta.isOk = true; respuesta.id = vueloCreado.getVueloId();
     * respuesta.message = "Vuelo creado correctamente";
     * 
     * return ResponseEntity.ok(respuesta); }
     */

    @PutMapping("/api/vuelos/{id}/estados")
    public ResponseEntity<GenericResponse> putActualizarEstadoVuelo(@PathVariable Integer id,
            @RequestBody EstadoVueloRequest estadoVuelo) {
        GenericResponse respuesta = new GenericResponse();

        // Pasos:
        // 1- buscar vuelo por Id y lo asignamos a una variable ->(vuelo)
        Vuelo vuelo = service.buscarPorId(id);
        // 2- setearle el nuevo estado, que vino en estadoVuelo
        vuelo.setEstadoVueloId(estadoVuelo.estado);
        // 3- grabar el vuelo en la base de datos
        service.actualizar(vuelo);

        // 4- devuelve el status final
        respuesta.isOk = true;
        respuesta.message = "El estado del vuelo ha sido actualizado.";
        respuesta.id = vuelo.getVueloId();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/api/vuelos/abiertos")
    public ResponseEntity<List<Vuelo>> getVuelosAbiertos() {

        return ResponseEntity.ok(service.traerVuelosAbiertos());
    }

    @GetMapping("/api/vuelos")
    public ResponseEntity<List<Vuelo>> getTodosVuelos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }


}
