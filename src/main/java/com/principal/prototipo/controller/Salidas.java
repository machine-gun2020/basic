package com.principal.prototipo.controller;

import com.arjuna.common.logging.commonLogger;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/prueba")
public class Salidas{
    private static final Logger log = LoggerFactory.getLogger(Salidas.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String salida() {
        log.info("Salida");
        return "Salidas metodo get público, Marco y paso";
    }
}
