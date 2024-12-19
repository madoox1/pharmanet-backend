package com.pharmacy.resource;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.model.Commande;
import com.pharmacy.service.CommandeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/patients/{patientId}/commandes")
public class CommandeResource {

    @Inject
    private CommandeService commandeService;

    /**
     * Récupère l'historique des commandes d'un patient spécifique.
     *
     * @param patientId L'ID du patient.
     * @return Une liste des commandes effectuées par le patient.
     * @throws NotFoundException Si aucune commande n'est trouvée pour ce patient.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommandesHistory(@PathParam("patientId") Long patientId) throws NotFoundException {
        return Response.ok(commandeService.getCommandesByPatientId(patientId)).build();
    }

    /**
     * Vérifie le statut d'une commande spécifique d'un patient.
     *
     * @param patientId L'ID du patient.
     * @param commandeId L'ID de la commande.
     * @return Les détails et le statut de la commande.
     * @throws NotFoundException Si la commande n'est pas trouvée.
     */
    @GET
    @Path("/{commandeId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCommandeStatus(@PathParam("patientId") Long patientId, @PathParam("commandeId") Long commandeId)
            throws NotFoundException {
        Commande commande = commandeService.getCommandeStatus(patientId, commandeId);
        return Response.ok(commande).build();
    }

    @POST
    @Path("/{ordonnanceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCommande(
            @PathParam("patientId") Long patientId,
            @PathParam("ordonnanceId") Long ordonnanceId) throws NotFoundException, BadRequestException {
        return Response.status(Response.Status.CREATED)
                      .entity(commandeService.createCommande(patientId, ordonnanceId))
                      .build();
    }
}