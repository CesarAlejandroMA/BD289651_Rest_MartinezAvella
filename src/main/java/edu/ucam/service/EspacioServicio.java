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

    /** GET /espacios → 200 OK con lista (aunque esté vacía) */
    @GET
    public Response getAll() {
        try {
            List<Espacio> lista = dao.findAll();
            // Puedes devolver explícitamente 200 si quieres:
            return Response
                      .status(200)
                      .entity(lista)
                      .build();
        } catch (Exception ex) {
            // error no esperado
            throw new WebApplicationException(
                "Error al leer los espacios", 500
            );
        }
    }

    /** GET /espacios/{id} → 200 OK o 404 */
    @GET @Path("{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            Espacio e = dao.findById(id);
            if (e == null) {
                // 404 Not Found
                throw new WebApplicationException(
                    "No existe espacio con id " + id, 404
                );
            }
            return Response
                      .status(200)
                      .entity(e)
                      .build();
        } catch (WebApplicationException wae) {
            // lo volvemos a lanzar para que Jersey devuelva el código correcto
            throw wae;
        } catch (Exception ex) {
            // cualquier otro error
            throw new WebApplicationException(
                "Error al recuperar espacio", 500
            );
        }
    }

    /** POST /espacios → 201 Created o 400 Bad Request */
    @POST
    public Response createEspacio(Espacio nuevo) {
        // validación
        if (nuevo.getName() == null || nuevo.getName().trim().isEmpty()) {
            throw new WebApplicationException(
                "El campo 'name' es obligatorio", 400
            );
        }
        try {
            Espacio creado = dao.create(nuevo);
            URI location = URI.create("/api/espacios/" + creado.getId());
            return Response
                      .created(location)     // 201
                      .entity(creado)
                      .build();
        } catch (WebApplicationException wae) {
            // si en DAO lanzas tus propias WebApplicationException
            throw wae;
        } catch (Exception ex) {
            throw new WebApplicationException(
                "Error al crear espacio", 500
            );
        }
    }
    
    /** PUT /espacios/{id} → 200 OK o 404 si no existe */
    @PUT
    @Path("{id}")
    public Response updateEspacio(@PathParam("id") int id, Espacio actualizado) {
        // Validación mínima
        if (actualizado.getName() == null || actualizado.getName().trim().isEmpty()) {
            throw new WebApplicationException("El campo 'name' es obligatorio", 400);
        }
        actualizado.setId(id);
        try {
            boolean ok = dao.update(actualizado);
            if (!ok) {
                // no existía ese id
                throw new WebApplicationException("No existe espacio con id " + id, 404);
            }
            // 200 OK con el objeto actualizado
            return Response.status(200)
                           .entity(actualizado)
                           .build();
        } catch (WebApplicationException wae) {
            throw wae;  // deja pasar 400/404
        } catch (Exception ex) {
            throw new WebApplicationException("Error al actualizar espacio", 500);
        }
    }
    
    /** DELETE /espacios/{id} → 204 No Content o 404 Not Found */
    @DELETE
    @Path("{id}")
    public Response deleteEspacio(@PathParam("id") int id) {
        try {
            boolean ok = dao.delete(id);
            if (!ok) {
                // 404 si no existía ese id
                throw new WebApplicationException(
                    "No existe espacio con id " + id, 404
                );
            }
            // 204 No Content indica borrado correcto sin cuerpo
            return Response.noContent().build();
        } catch (WebApplicationException wae) {
            throw wae;  // propaga 404
        } catch (Exception ex) {
            // cualquier otro error → 500
            throw new WebApplicationException(
                "Error al eliminar espacio", 500
            );
        }
    }
    
}
