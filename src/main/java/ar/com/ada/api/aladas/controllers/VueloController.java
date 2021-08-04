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

@RestController
public class VueloController {

    /*
     * Version SIMPLE
     * 
     * @Autowired VueloService service;
     * 
     * @Autowired AeropuertoService aeropuertoService;
     */

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

        service.crear(vuelo);

        respuesta.isOk = true;
        respuesta.id = vuelo.getVueloId();
        respuesta.message = "Vuelo creado correctamente";

        return ResponseEntity.ok(respuesta);
    }

    /*
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

    @PutMapping("/api/vuelos/{id}")
    public ResponseEntity<GenericResponse> putActualizarEstadoVuelo(@PathVariable Integer id,
            @RequestBody EstadoVueloRequest estadoVuelo) {
        GenericResponse respuesta = new GenericResponse();
        
        Vuelo vuelo = service.buscarPorId(id);
        vuelo.setEstadoVueloId(estadoVuelo.estado);
        service.actualizar(vuelo);

        respuesta.isOk = true;
        respuesta.message = "Actualizado";
        respuesta.id = vuelo.getVueloId();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/api/vuelos/abiertos")
    public ResponseEntity<List<Vuelo>> getVuelosAbiertos() {

        return ResponseEntity.ok(service.traerVuelosAbiertos());
    }

    @DeleteMapping("/api/vuelos/{id}")
    public ResponseEntity<GenericResponse>eliminar (@PathVariable Integer id){

        service.eliminar(id);
        GenericResponse respuesta = new GenericResponse();

        respuesta.isOk = true;
        respuesta.message = "Vuelo eliminado con exito.";

        return ResponseEntity.ok(respuesta);
    }

}
