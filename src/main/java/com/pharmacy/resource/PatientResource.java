package com.pharmacy.resource;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.DuplicationException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.model.Patient;
import com.pharmacy.service.PatientService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/patients")
public class PatientResource {

    @Inject
    private PatientService patientService;

    /**
     * Récupère les informations d'un patient spécifique à partir de son ID.
     *
     * @param id L'ID du patient.
     * @return Les détails du patient.
     * @throws NotFoundException Si aucun patient avec cet ID n'est trouvé.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatient(@PathParam("id") Long id) throws NotFoundException {
        Patient patient = patientService.findPatientById(id);
        return Response.ok(patient).build();
    }

    /**
     * Liste tous les patients disponibles dans le système.
     *
     * @return Une liste de tous les patients.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPatients() throws NotFoundException {
        List<Patient> patients = patientService.listPatients();
        return Response.ok(patients).build();
    }

    /**
     * Crée un nouveau patient dans le système.
     *
     * @param patient Les données du nouveau patient.
     * @return Le patient nouvellement créé.
     * @throws BadRequestException Si les données du patient sont invalides.
     * @throws DuplicationException Si un patient avec des informations identiques existe déjà.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPatient(@Valid Patient patient) throws BadRequestException, DuplicationException {
        Patient createdPatient = patientService.addPatient(patient);
        return Response.status(Response.Status.CREATED).entity(createdPatient).build();
    }

    /**
     * Met à jour les informations d'un patient par son ID.
     *
     * @param id L'ID du patient à mettre à jour.
     * @param patient Les nouvelles données à mettre à jour.
     * @return Les informations du patient mises à jour.
     * @throws NotFoundException Si aucun patient avec cet ID n'est trouvé.
     * @throws DuplicationException Si la mise à jour entraîne un conflit (duplication de données).
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePatient(@PathParam("id") Long id, @Valid Patient patient)
            throws NotFoundException, DuplicationException {
        Patient updatedPatient = patientService.updatePatient(patient, id);
        return Response.ok(updatedPatient).build();
    }

    /**
     * Supprime un patient spécifique du système.
     *
     * @param id L'ID du patient à supprimer.
     * @return Une réponse sans contenu si la suppression est réussie.
     * @throws NotFoundException Si le patient avec cet ID est introuvable.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePatient(@PathParam("id") Long id) throws NotFoundException {
        patientService.deletePatient(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countPatients() {
        return Response.ok(patientService.countPatients()).build();
    }
}