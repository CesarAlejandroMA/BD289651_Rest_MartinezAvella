package edu.ucam.service;
import edu.ucam.dao.HuecoDAO;
import edu.ucam.model.Huecos;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/espacios/{espacioId}/huecos")
@Produces(MediaType.APPLICATION_JSON)
public class HuecoServicio {

    private HuecoDAO dao = new HuecoDAO();
    
    /** GET /api/espacios/{espacioId}/huecos */
    @GET
    public Response getByEspacio(@PathParam("espacioId") int espacioId) {
        try {
            List<Huecos> lista = dao.findByEspacio(espacioId);
            // 200 OK con la lista (vacía si no hay huecos)
            return Response
                      .status(200)
                      .entity(lista)
                      .build();
        } catch (RuntimeException ex) {
            // 500 si la consulta falla
            throw new WebApplicationException(
                "Error al recuperar huecos para espacio " + espacioId,
                500
            );
        }
    }
    
    /** POST /api/espacios/{espacioId}/huecos */
    @POST
    public Response createHueco(
            @PathParam("espacioId") int espacioId,
            Huecos nuevo) {

    	// 1) Validar que fin > inicio
        if (nuevo.getStartTime() == null || nuevo.getEndTime() == null) {
		    Response resp = Response
			        .status(500)
			        .entity("Faltan fechas inicio/fin")
			        .type(MediaType.TEXT_PLAIN)   // o APPLICATION_JSON si prefieres JSON
			        .build();
			    throw new WebApplicationException(resp);
        }
        if (!nuevo.getEndTime().isAfter(nuevo.getStartTime())) {
		    Response resp = Response
			        .status(400)
			        .entity("El tiempo de fin debe ser posterior al de inicio")
			        .type(MediaType.TEXT_PLAIN)   // o APPLICATION_JSON si prefieres JSON
			        .build();
			    throw new WebApplicationException(resp);
        }
        // asocia el espacio
        nuevo.setSpaceId(espacioId);
        
        // 2) Validar solapamiento

		if (dao.hasOverlap(espacioId, nuevo.getStartTime(), nuevo.getEndTime())) {
		    Response resp = Response
		        .status(409)
		        .entity("El hueco se solapa con otro existente")
		        .type(MediaType.TEXT_PLAIN)   // o APPLICATION_JSON si prefieres JSON
		        .build();
		    throw new WebApplicationException(resp);
		}

     // 3) Crear
        try {
            Huecos creado = dao.create(nuevo);
            URI loc = URI.create(
                String.format("/api/espacios/%d/huecos/%d", espacioId, creado.getId())
            );
            return Response.created(loc)
                           .entity(creado)
                           .build();
        } catch (WebApplicationException wae) {
            throw wae;
        } catch (Exception ex) {
            throw new WebApplicationException("Error al crear hueco", 500);
        }
  }
}
