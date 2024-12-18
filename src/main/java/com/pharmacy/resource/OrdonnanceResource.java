package com.pharmacy.resource;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.model.Ordonnance;
import com.pharmacy.service.OrdonnanceService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/patients/{patientId}/ordonnances")
public class OrdonnanceResource {

    @Inject
    private OrdonnanceService ordonnanceService;

    /**
     * Récupère toutes les ordonnances relatives à un patient donné.
     *
     * @param patientId L'ID du patient.
     * @return Une liste des ordonnances liées au patient.
     * @throws NotFoundException Si aucune ordonnance n'est trouvée pour ce patient.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listOrdonnances(@PathParam("patientId") Long patientId) throws NotFoundException {
        return Response.ok(ordonnanceService.getOrdonnancesByPatientId(patientId)).build();
    }

    /**
     * Crée une nouvelle ordonnance pour un patient spécifique.
     *
     * @param patientId L'ID du patient.
     * @param ordonnance Les données de l'ordonnance à créer.
     * @return L'ordonnance nouvellement créée.
     * @throws BadRequestException Si les données de l'ordonnance sont invalides.
     * @throws NotFoundException Si le patient n'est pas trouvé.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrdonnance(@PathParam("patientId") Long patientId, @Valid Ordonnance ordonnance)
            throws BadRequestException, NotFoundException {
        ordonnance.getPatient().setId(patientId); // Associe l'ordonnance au bon patient
        return Response.ok(ordonnanceService.createOrdonnance(ordonnance)).build();
    }
}