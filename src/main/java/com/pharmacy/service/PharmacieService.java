package com.pharmacy.service;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.PharmacieEJBImpl;
import com.pharmacy.model.Pharmacie;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class PharmacieService {
    @EJB
    private PharmacieEJBImpl pharmacieEJB;


    public Pharmacie addPharmacie(Pharmacie pharmacie) throws BadRequestException {
        if(pharmacie == null){
            throw new BadRequestException("Pharmacie object is missing information");
        }
        if(pharmacieEJB.findPharmacieByNomAndAdresse(pharmacie.getNom(), pharmacie.getAdresse()) != null){
            throw new BadRequestException("Pharmacie with name " + pharmacie.getNom() + " and address " + pharmacie.getAdresse() + " already exists");
        }
        return pharmacieEJB.addPharmacie(pharmacie);
    }


    public Pharmacie findPharmacyById(Long id) throws NotFoundException {
        Pharmacie pharmacie = pharmacieEJB.findPharmacieById(id);
        if (pharmacie == null) {
            throw new NotFoundException("Pharmacie with id " + id + " not found");
        }
        return pharmacie;
    }

    public Pharmacie changerStatusPharmacie(Long id) throws NotFoundException {
        findPharmacyById(id);
        return pharmacieEJB.changerStatusPharmacie(id);
    }

    public List<Pharmacie> listPharmacies() throws NotFoundException {
       List<Pharmacie> pharmacies= pharmacieEJB.listPharmacies();
       if(pharmacies.isEmpty()){
           throw new NotFoundException("No pharmacies found");
       }
       return pharmacies;
    }

    public void deletePharmacie(Long id) throws NotFoundException {
        Pharmacie pharmacie = pharmacieEJB.findPharmacieById(id);
        if (pharmacie == null) {
            throw new NotFoundException("Pharmacie with id " + id + " not found");
        }
        pharmacieEJB.deletePharmacie(pharmacie);
    }

    public long countPharmacies() {
        return pharmacieEJB.countPharmacies();
    }

    public Pharmacie updatePharmacie(Pharmacie pharmacie, Long id) throws NotFoundException {
        Pharmacie pharmacieToUpdate = findPharmacyById(id);
        pharmacieToUpdate.setNom(pharmacie.getNom());
        pharmacieToUpdate.setAdresse(pharmacie.getAdresse());
        pharmacieToUpdate.setLatitude(pharmacie.getLatitude());
        pharmacieToUpdate.setLongitude(pharmacie.getLongitude());
        pharmacieEJB.updatePharmacie(pharmacieToUpdate);
        return pharmacieToUpdate;
    }
}