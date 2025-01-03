package com.pharmacy.resource;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.ErrorDTO;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.dto.CreateOrdonnanceRequest;
import com.pharmacy.dto.ImageResponseDTO;
import com.pharmacy.model.Ordonnance;
import com.pharmacy.model.OrdonnanceStatut;
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
     * @param request Les données de l'ordonnance à créer.
     * @return L'ordonnance nouvellement créée.
     * @throws BadRequestException Si les données de l'ordonnance sont invalides.
     * @throws NotFoundException Si le patient n'est pas trouvé.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrdonnance(
            @PathParam("patientId") Long patientId,
            @Valid CreateOrdonnanceRequest request)
            throws BadRequestException, NotFoundException {
        try {
            return Response.ok(ordonnanceService.createOrdonnance(
                    patientId,
                    request.getEncodedImage(),
                    request.getPharmacieId()
            )).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{ordonnanceId}/image")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getOrdonnanceImage(@PathParam("ordonnanceId") Long ordonnanceId) {
        try {
            ImageResponseDTO image = ordonnanceService.getOrdonnanceImage(ordonnanceId);
            return Response.ok(image.getImageData())
                         .header("Content-Type", image.getContentType())
                         .header("Content-Disposition", "inline; filename=\"ordonnance.jpg\"")
                         .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                         .entity(new ErrorDTO("Image non trouvée"))
                         .build();
        }
    }

    @PATCH
    @Path("/{ordonnanceId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrdonnanceStatus(
            @PathParam("ordonnanceId") Long ordonnanceId,
            @QueryParam("statut") OrdonnanceStatut statut) {
        try {
            return Response.ok(ordonnanceService.updateOrdonnanceStatus(ordonnanceId, statut)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                         .entity(new ErrorDTO("Ordonnance non trouvée"))
                         .build();
        }
    }
}