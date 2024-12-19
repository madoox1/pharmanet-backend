package com.pharmacy.resource;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.ErrorDTO;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.dto.CreateOrdonnanceRequest;
import com.pharmacy.dto.ImageResponseDTO;
import com.pharmacy.model.Ordonnance;
import com.pharmacy.service.OrdonnanceService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

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
            @Valid CreateOrdonnanceRequest request) {
        try {
            Ordonnance ordonnance = ordonnanceService.createOrdonnance(
                patientId,
                request.getEncodedImage(),
                request.getPharmacieId()
            );
            return Response.ok(ordonnance).build();
        } catch (BadRequestException | NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity(new ErrorDTO(e.getMessage()))
                         .build();
        }
    }

    /**
     * Récupère une ordonnance spécifique d'un patient.
     *
     * @param patientId L'ID du patient.
     * @param ordonnanceId L'ID de l'ordonnance.
     * @return Les détails de l'ordonnance.
     * @throws NotFoundException Si l'ordonnance n'est pas trouvée.
     */
    @GET
    @Path("/{ordonnanceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdonnance(@PathParam("patientId") Long patientId, @PathParam("ordonnanceId") Long ordonnanceId)
            throws NotFoundException {
        Ordonnance ordonnance = ordonnanceService.getOrdonnance(patientId, ordonnanceId);
        return Response.ok(ordonnance).build();
    }

    /**
     * Récupère le contenu de l'image d'une ordonnance spécifique.
     *
     * @param ordonnanceId L'ID de l'ordonnance.
     * @return Le contenu de l'image de l'ordonnance.
     * @throws NotFoundException Si l'ordonnance n'est pas trouvée.
     */
    @GET
    @Path("/{ordonnanceId}/image")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getOrdonnanceImage(@PathParam("ordonnanceId") Long ordonnanceId) {
        try {
            ImageResponseDTO image = ordonnanceService.getDecodedImage(ordonnanceId);
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
}