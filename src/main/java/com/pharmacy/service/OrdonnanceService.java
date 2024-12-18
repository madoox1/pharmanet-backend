package com.pharmacy.service;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.OrdonnanceEJBImpl;
import com.pharmacy.model.Ordonnance;
import com.pharmacy.model.Patient;
import com.pharmacy.model.Pharmacie;
import com.pharmacy.model.Pharmacien;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;


import java.util.List;

@Stateless
public class OrdonnanceService {

    @EJB
    private OrdonnanceEJBImpl ordonnanceEJB;
    @Inject
    private PatientService patientService;
    @Inject
    private PharmacieService pharmacieService;


    public Ordonnance createOrdonnance(Ordonnance ordonnance) throws BadRequestException, NotFoundException {
        if(ordonnance == null){
            throw  new BadRequestException("Ordonnance object is missing information");
        }
        Patient patient = patientService.findPatientById(ordonnance.getPatient().getId());
        Pharmacie pharmacie = pharmacieService.findPharmacyById(ordonnance.getPharmacie().getId());
        ordonnance.setPatient(patient);
        ordonnance.setPharmacie(pharmacie);
        return ordonnanceEJB.addOrdonnance(ordonnance);
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