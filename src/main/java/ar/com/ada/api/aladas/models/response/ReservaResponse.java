package ar.com.ada.api.aladas.models.response;

import java.util.Date;

import ar.com.ada.api.aladas.entities.Reserva;

public class ReservaResponse {
    
    public Integer id;
    public Date fechaDeEmision;
    public Date fechaDeVencimiento;
    public String message;
}
