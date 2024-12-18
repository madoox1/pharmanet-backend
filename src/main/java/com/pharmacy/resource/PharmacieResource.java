package com.pharmacy.resource;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.model.Pharmacie;
import com.pharmacy.service.PharmacieService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pharmacies")
public class PharmacieResource {

    @Inject
    private PharmacieService pharmacieService;

    /**
     * Liste toutes les pharmacies disponibles dans le système.
     *
     * @return Une liste des pharmacies.
     * @throws NotFoundException Si aucune pharmacie n'est trouvée.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPharmacies() throws NotFoundException {
        return Response.ok(pharmacieService.listPharmacies()).build();
    }

    /**
     * Crée une nouvelle pharmacie.
     *
     * @param pharmacie Les données de la pharmacie à créer.
     * @return La pharmacie nouvellement créée.
     * @throws BadRequestException Si les données sont invalides.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPharmacie(@Valid Pharmacie pharmacie) throws BadRequestException {
        Pharmacie createdPharmacie = pharmacieService.addPharmacie(pharmacie);
        return Response.status(Response.Status.CREATED).entity(createdPharmacie).build();
    }

    /**
     * Modifie le statut d'une pharmacie.
     *
     * @param id L'ID de la pharmacie.
     * @return La pharmacie mise à jour avec son nouveau statut.
     * @throws NotFoundException Si la pharmacie n'est pas trouvée.
     */
    @PUT
    @Path("/{id}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePharmacieStatus(@PathParam("id") Long id) throws NotFoundException {
        Pharmacie updatedPharmacie = pharmacieService.changerStatusPharmacie(id);
        return Response.ok(updatedPharmacie).build();
    }

    /**
     * Supprime une pharmacie via son ID.
     *
     * @param id L'ID de la pharmacie.
     * @return Une réponse sans contenu (204) si la suppression est réussie.
     * @throws NotFoundException Si la pharmacie n'existe pas.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePharmacie(@PathParam("id") Long id) throws NotFoundException {
        pharmacieService.deletePharmacie(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPharmacie(@PathParam("id") Long id) throws NotFoundException {
        Pharmacie pharmacie = pharmacieService.findPharmacyById(id);
        return Response.ok(pharmacie).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countPharmacies() {
        return Response.ok(pharmacieService.countPharmacies()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePharmacie(@PathParam("id") Long id, @Valid Pharmacie pharmacie)
            throws NotFoundException {
        Pharmacie updatedPharmacie = pharmacieService.updatePharmacie(pharmacie, id);
        return Response.ok(updatedPharmacie).build();
    }
}