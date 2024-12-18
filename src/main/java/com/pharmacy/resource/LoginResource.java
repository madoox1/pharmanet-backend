package com.pharmacy.resource;

import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.Exceptions.UnauthorizedException;
import com.pharmacy.model.Credentials;
import com.pharmacy.model.Utilisateur;
import com.pharmacy.service.LoginService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/login")
public class LoginResource {

    @Inject
    private LoginService loginService; // Injection du service métier

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid Credentials credentials)
            throws UnauthorizedException, NotFoundException {
        // Authentification et génération du JWT
        Utilisateur utilisateur = loginService.authenticate(credentials.getEmail(), credentials.getMotDePasse());
        return Response.ok().entity(utilisateur).build();
    }
}