package com.pharmacy.resource;

import com.pharmacy.Exceptions.ErrorDTO;
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
     * Récupère toutes les commandes d'un patient spécifique.
     *
     * @param patientId L'ID du patient.
     * @return Une liste des commandes liées au patient.
     * @throws NotFoundException Si aucune commande n'est trouvée pour ce patient.
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommandesByPatientId(@PathParam("patientId") Long patientId) throws NotFoundException {
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

    /**
     * Récupère une commande spécifique d'un patient.
     *
     * @param patientId L'ID du patient.
     * @param commandeId L'ID de la commande.
     * @return Les détails de la commande.
     * @throws NotFoundException Si la commande n'est pas trouvée.
     */
    @GET
    @Path("/{commandeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommande(@PathParam("patientId") Long patientId, @PathParam("commandeId") Long commandeId)
            throws NotFoundException {
        Commande commande = commandeService.getCommande(patientId, commandeId);
        return Response.ok(commande).build();
    }

    /**
     * Crée une nouvelle commande pour un patient spécifique.
     *
     * @param patientId L'ID du patient.
     * @param ordonnanceId L'ID de l'ordonnance.
     * @return La commande nouvellement créée.
     * @throws NotFoundException Si le patient ou l'ordonnance n'est pas trouvé.
     */
    @POST
    @Path("/{ordonnanceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCommande(
            @PathParam("patientId") Long patientId,
            @PathParam("ordonnanceId") Long ordonnanceId) {
        try {
            Commande commande = commandeService.createCommande(patientId, ordonnanceId);
            return Response.ok(commande).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                         .entity(new ErrorDTO(e.getMessage()))
                         .build();
        }
    }

    /**
     * Met à jour le statut d'une commande spécifique d'un patient.
     *
     * @param patientId L'ID du patient.
     * @param commandeId L'ID de la commande.
     * @param statut Le nouveau statut de la commande.
     * @return Les détails de la commande mise à jour.
     * @throws NotFoundException Si la commande n'est pas trouvée.
     */
    @PUT
    @Path("/{commandeId}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCommandeStatus(
            @PathParam("patientId") Long patientId,
            @PathParam("commandeId") Long commandeId,
            @QueryParam("statut") CommandeStatut statut) {
        try {
            Commande commande = commandeService.updateCommandeStatus(patientId, commandeId, statut);
            return Response.ok(commande).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                         .entity(new ErrorDTO(e.getMessage()))
                         .build();
        }
    }
}