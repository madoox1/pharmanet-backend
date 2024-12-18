package com.pharmacy.resource;

import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.model.Utilisateur;
import com.pharmacy.service.UtilisateurService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Administrateur-specific functionality.
 */
@Path("/admin")
public class AdministrateurResource {

    @Inject
    private UtilisateurService utilisateurService;

    /**
     * Liste tous les utilisateurs (patients, pharmaciens, etc.), fusionnés pour l'administrateur.
     *
     * @return Une liste d'utilisateurs.
     * @throws NotFoundException Si aucun utilisateur n'est trouvé.
     */
    @GET
    @Path("/utilisateurs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUtilisateurs() throws NotFoundException {
        // Centralized functionality for listing users (patients, pharmacists, etc.)
        return Response.ok(utilisateurService.GetAllUsers()).build();
    }

    /**
     * Modifie le statut d'un utilisateur (activé/désactivé).
     *
     * @param id     L'ID de l'utilisateur.
     * @param status Le nouveau statut de l'utilisateur.
     * @return L'utilisateur avec le statut mis à jour.
     * @throws NotFoundException Si l'utilisateur n'est pas trouvé.
     */
    @PUT
    @Path("/utilisateur/{id}/status/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserStatus(@PathParam("id") Long id, @PathParam("status") String status) throws NotFoundException {
        Utilisateur updatedUser = utilisateurService.changeUserStatus(id, status);
        return Response.ok(updatedUser).build();
    }

}