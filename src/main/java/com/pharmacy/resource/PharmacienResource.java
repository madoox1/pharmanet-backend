package com.pharmacy.resource;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.DuplicationException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.model.Pharmacien;
import com.pharmacy.service.PharmacienService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pharmaciens")
public class PharmacienResource {

    @Inject
    private PharmacienService pharmacienService;

    /**
     * Crée un nouveau pharmacien dans le système.
     *
     * @param pharmacien Les informations du pharmacien à ajouter.
     * @return Les informations du pharmacien créé.
     * @throws BadRequestException Si les informations fournies sont invalides.
     * @throws DuplicationException Si un pharmacien avec les mêmes informations existe déjà.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPharmacien(@Valid Pharmacien pharmacien)
            throws BadRequestException, DuplicationException, NotFoundException {
        Pharmacien createdPharmacien = pharmacienService.addPharmacien(pharmacien);
        return Response.status(Response.Status.CREATED).entity(createdPharmacien).build();
    }

    /**
     * Liste tous les pharmaciens disponibles dans le système.
     *
     * @return La liste des pharmaciens.
     * @throws NotFoundException Si aucun pharmacien n'est trouvé.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPharmaciens() throws NotFoundException {
        return Response.ok(pharmacienService.listPharmaciens()).build();
    }

    /**
     * Met à jour les informations d’un pharmacien.
     *
     * @param id L'ID du pharmacien à mettre à jour.
     * @param pharmacien Les informations à mettre à jour.
     * @return Les informations du pharmacien mises à jour.
     * @throws NotFoundException Si le pharmacien n'est pas trouvé.
     * @throws DuplicationException Si la mise à jour crée une duplication.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePharmacien(@PathParam("id") Long id, @Valid Pharmacien pharmacien)
            throws NotFoundException, DuplicationException {
        Pharmacien updatedPharmacien = pharmacienService.updatePharmacien(pharmacien, id);
        return Response.ok(updatedPharmacien).build();
    }

    /**
     * Supprime un pharmacien spécifique.
     *
     * @param id L'ID du pharmacien à supprimer.
     * @return Une réponse sans contenu (204) si la suppression est réussie.
     * @throws NotFoundException Si le pharmacien n'existe pas.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePharmacien(@PathParam("id") Long id) throws NotFoundException {
        pharmacienService.deletePharmacien(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPharmacien(@PathParam("id") Long id) throws NotFoundException {
        Pharmacien pharmacien = pharmacienService.findPharmacienById(id);
        return Response.ok(pharmacien).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countPharmaciens() {
        return Response.ok(pharmacienService.countPharmaciens()).build();
    }


}