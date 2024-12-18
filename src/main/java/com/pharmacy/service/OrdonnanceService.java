package com.pharmacy.service;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.OrdonnanceEJBImpl;
import com.pharmacy.model.Ordonnance;
import com.pharmacy.model.Patient;
import com.pharmacy.model.Pharmacie;
import com.pharmacy.model.Pharmacien;
import com.pharmacy.model.ImageContent;
import com.pharmacy.dto.ImageResponseDTO;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.Base64;
import java.util.List;

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
        // Validations
        validateInputs(patientId, encodedImage, pharmacieId);
        
        // Création de l'ordonnance
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setPatient(patientService.findPatientById(patientId));
        ordonnance.setPharmacie(pharmacieService.findPharmacyById(pharmacieId));
        
        // Gestion de l'image
        saveImage(ordonnance, encodedImage);

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

    private void saveImage(Ordonnance ordonnance, String encodedImage) {
        ImageContent imageContent = new ImageContent();
        imageContent.setContent(encodedImage);
        ordonnance.setImageContent(imageContent);
        ordonnance.setImageUrl("content://" + ordonnance.getPatient().getId());
    }

    public ImageResponseDTO getDecodedImage(Long ordonnanceId) throws NotFoundException {
        String encodedImage = getOrdonnanceImage(ordonnanceId);
        if (encodedImage == null) {
            throw new NotFoundException("Image non trouvée");
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

    // Add method to retrieve image content
    public String getOrdonnanceImage(Long ordonnanceId) throws NotFoundException {
        Ordonnance ordonnance = ordonnanceEJB.findOrdonnanceById(ordonnanceId);
        if (ordonnance == null) {
            throw new NotFoundException("Ordonnance not found");
        }
        
        return ordonnance.getImageContent() != null ? 
               ordonnance.getImageContent().getContent() : 
               ordonnance.getImageUrl();
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

    public List<Ordonnance> getOrdonnancesByPatientId(Long id) throws NotFoundException {
        patientService.findPatientById(id);
        List<Ordonnance> ordonnances = ordonnanceEJB.getOrdonnancesByPatientId(id);
        if(ordonnances.isEmpty()){
            throw new NotFoundException("No ordonnance found for patient with id " + id);
        }
        return ordonnances;
    }

}