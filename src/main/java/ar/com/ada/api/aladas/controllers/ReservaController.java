package ar.com.ada.api.aladas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.aladas.entities.Reserva;
import ar.com.ada.api.aladas.entities.Usuario;
import ar.com.ada.api.aladas.models.request.InfoReservaNueva;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.models.response.ReservaResponse;
import ar.com.ada.api.aladas.services.ReservaService;
import ar.com.ada.api.aladas.services.UsuarioService;
import ar.com.ada.api.aladas.services.ReservaService.ValidacionReservaDataEnum;

@RestController
public class ReservaController {
    @Autowired
    ReservaService service;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/api/reservas")
    public ResponseEntity<?> generarReserva(@RequestBody InfoReservaNueva infoReserva){
        ReservaResponse respuesta = new ReservaResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorUsername(username);

        ValidacionReservaDataEnum resultado = service.validar(infoReserva.vueloId);

        if (resultado == ValidacionReservaDataEnum.OK) {

            Reserva reserva = service.generarReserva(infoReserva.vueloId, usuario.getPasajero().getPasajeroId());

            respuesta.id = reserva.getReservaId();
            respuesta.fechaDeEmision = reserva.getFechaEmision();
            respuesta.fechaDeVencimiento = reserva.getFechaVencimiento();
            respuesta.message = "Reserva creada con éxito.";

            return ResponseEntity.ok(respuesta);

        } else {
            GenericResponse r = new GenericResponse();
            r.isOk = false;
            r.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(r);
        }
    }

    @GetMapping("/api/reservas")
    public ResponseEntity<List<Reserva>> traerReservas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    
}
