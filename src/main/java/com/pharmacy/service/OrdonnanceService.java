package com.pharmacy.service;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.dto.ImageResponseDTO;
import com.pharmacy.ejb.Implimentation.OrdonnanceEJBImpl;
import com.pharmacy.model.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Base64;

@Stateless
public class OrdonnanceService {

    @EJB
    private OrdonnanceEJBImpl ordonnanceEJB;
    @Inject
    private PatientService patientService;
    @Inject
    private PharmacieService pharmacieService;

    private static final int MAX_IMAGE_SIZE = 16777215; // 16MB

    public Ordonnance createOrdonnance(Long patientId, String encodedImage, Long pharmacieId) throws BadRequestException, NotFoundException {
        validateInputs(patientId, encodedImage, pharmacieId);
        
        // Vérifier si une ordonnance existe déjà pour ce patient et cette pharmacie
        List<Ordonnance> existingOrdonnances = ordonnanceEJB.getOrdonnancesByPatientId(patientId);
        boolean exists = existingOrdonnances.stream()
                .anyMatch(o -> o.getPharmacie().getId().equals(pharmacieId));
        
        if (exists) {
            throw new BadRequestException("Une ordonnance existe déjà pour ce patient dans cette pharmacie");
        }
        
        Patient patient = patientService.findPatientById(patientId);
        Pharmacie pharmacie = pharmacieService.findPharmacyById(pharmacieId);

        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setPatient(patient);
        ordonnance.setPharmacie(pharmacie);
        
        // Sauvegarde de l'image
        ImageContent imageContent = new ImageContent();
        imageContent.setContent(encodedImage);
        ordonnance.setImageContent(imageContent);
        ordonnance.setImageUrl("content://" + patient.getId());

        return ordonnanceEJB.addOrdonnance(ordonnance);
    }

    private void validateInputs(Long patientId, String encodedImage, Long pharmacieId) throws BadRequestException {
        if (encodedImage == null || pharmacieId == null || patientId == null) {
            throw new BadRequestException("Tous les champs sont obligatoires");
        }
        if (encodedImage.length() > MAX_IMAGE_SIZE) {
            throw new BadRequestException("L'image doit faire moins de 16MB");
        }
    }

    public ImageResponseDTO getOrdonnanceImage(Long ordonnanceId) throws NotFoundException {
        Ordonnance ordonnance = ordonnanceEJB.findOrdonnanceById(ordonnanceId);
        if (ordonnance == null || ordonnance.getImageContent() == null) {
            throw new NotFoundException("Image non trouvée");
        }
        
        // Get content from ImageContent object
        String encodedImage = ordonnance.getImageContent();
        if (encodedImage == null) {
            throw new NotFoundException("Image content is null");
        }

        return new ImageResponseDTO(
            Base64.getDecoder().decode(extractBase64(encodedImage)),
            determineContentType(encodedImage)
        );
    }

    private String extractBase64(String encodedImage) {
        return encodedImage.contains(",") ? encodedImage.split(",")[1] : encodedImage;
    }

    private String determineContentType(String encodedImage) {
        return encodedImage.contains("data:") ? 
            encodedImage.split(";")[0].replace("data:", "") : 
            "image/jpeg";
    }

    public void deleteOrdonnance(Long id) throws NotFoundException {
        Ordonnance ordonnance = ordonnanceEJB.findOrdonnanceById(id);
        if (ordonnance == null) {
            throw new NotFoundException("Ordonnance with id " + id + " not found");
        }
        ordonnanceEJB.deleteOrdonnance(ordonnance);
    }

    public Ordonnance updateOrdonnance(Ordonnance ordonnance, Long id) throws NotFoundException {
        if (ordonnanceEJB.findOrdonnanceById(id) == null) {
            throw new NotFoundException("Ordonnance with id " + id + " not found");
        }
        ordonnance.setId(id);
        return ordonnanceEJB.updateOrdonnance(ordonnance);
    }

    public Ordonnance updateOrdonnanceStatus(Long ordonnanceId, OrdonnanceStatut statut) throws NotFoundException {
        Ordonnance ordonnance = ordonnanceEJB.findOrdonnanceById(ordonnanceId);
        if (ordonnance == null) {
            throw new NotFoundException("Ordonnance non trouvée");
        }
        ordonnance.setStatut(statut);
        return ordonnanceEJB.updateOrdonnance(ordonnance);
    }

    public List<Ordonnance> getOrdonnancesByPatientId(Long id) throws NotFoundException {
        patientService.findPatientById(id);
        List<Ordonnance> ordonnances = ordonnanceEJB.getOrdonnancesByPatientId(id);
        if(ordonnances.isEmpty()){
            throw new NotFoundException("No ordonnance found for patient with id " + id);
        }
        return ordonnances;
    }

    public Ordonnance findOrdonnanceById(Long ordonnanceId) throws BadRequestException {
        if (ordonnanceId == null) {
            throw new BadRequestException("Ordonnance ID is required");
        }
        return ordonnanceEJB.findOrdonnanceById(ordonnanceId);
    }
}