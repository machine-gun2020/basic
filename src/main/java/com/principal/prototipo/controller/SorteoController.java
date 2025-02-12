package com.principal.prototipo.controller;

import com.principal.prototipo.model.Sorteo;
import com.principal.prototipo.service.SorteoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/api/sorteo")
public class SorteoController {

    private static final Logger log = LoggerFactory.getLogger(SorteoController.class);

    @Inject
    SorteoService sorteoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sorteo> getSorteo() {
        List<Sorteo> cuales = sorteoService.getAllSorteo();
        log.info("Salida, cantidad de sorteos: {}", cuales.size());
        return cuales;
    }

    @GET
    @Path("/probabilidad")
    public Map<Integer, Double> getProbabilidadSorteo() {
        List<Sorteo> cuales = sorteoService.getAllSorteo();
        log.info("Salida, probabilidad: {}", cuales.size());

        return sorteoService.calcularProbabilidad(cuales,7);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Sorteo createPerson(Sorteo sorteo) {
        return sorteoService.createSorteo(sorteo);
    }
}
