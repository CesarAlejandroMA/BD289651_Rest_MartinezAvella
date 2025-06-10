package edu.ucam.service;

import edu.ucam.dao.EspacioDAO;
import edu.ucam.model.Espacio;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/espacios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EspacioServicio {

    private EspacioDAO dao = new EspacioDAO();

    @GET
    public List<Espacio> getAll() {
        return dao.findAll();
    }


}
