package com.pharmacy.service;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.DuplicationException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.PharmacienEJBImpl;
import com.pharmacy.model.Pharmacie;
import com.pharmacy.model.Pharmacien;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;


@Stateless
public class PharmacienService {

    @EJB
    private PharmacienEJBImpl pharmacienEJB;
    @EJB
    private PharmacieService pharmacieService;
    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public Pharmacien addPharmacien(Pharmacien pharmacien) throws DuplicationException, BadRequestException, NotFoundException {
        if (pharmacien == null) {
            throw new BadRequestException("Pharmacien object is missing information");
        }
        if (pharmacienEJB.findPharmacienByEmail(pharmacien.getEmail()) != null) {
            throw new DuplicationException("A pharmacien with the same email already exists");
        }
        String hashedPassword = hashPassword(pharmacien.getMotDePasse());
        pharmacien.setMotDePasse(hashedPassword);

        Pharmacie pharmacie = pharmacieService.findPharmacyById(pharmacien.getPharmacie().getId());
        pharmacien.setPharmacie(pharmacie);
        try {
            pharmacienEJB.addPharmacien(pharmacien);
            return pharmacien;
        } catch (PersistenceException e) {
            throw new BadRequestException("Error occurred while adding the pharmacien");
        }
    }

    public void deletePharmacien(Long id) throws NotFoundException {
        Pharmacien pharmacien = pharmacienEJB.findPharmacienById(id);

        if (pharmacien == null) {
            throw new NotFoundException("Pharmacien with id " + id + " not found");
        }

        pharmacienEJB.deletePharmacien(id);
    }


    public List<Pharmacien> listPharmaciens() throws NotFoundException {
        List<Pharmacien> pharmaciens = pharmacienEJB.getAllPharmaciens();

        if (pharmaciens.isEmpty()) {
            throw new NotFoundException("No pharmaciens found");
        }

        return pharmaciens;
    }


    public Pharmacien updatePharmacien(Pharmacien pharmacien, Long id) throws NotFoundException, DuplicationException {
        Pharmacien pharmacienToUpdate = findPharmacienById(id);

        if (pharmacienEJB.findPharmacienByEmail(pharmacien.getEmail()) != null && !pharmacienToUpdate.getEmail().equals(pharmacien.getEmail())) {
            throw new DuplicationException("A pharmacien with the same email already exists");
        }

        pharmacienToUpdate.setNom(pharmacien.getNom());
        pharmacienToUpdate.setPrenom(pharmacien.getPrenom());
        pharmacienToUpdate.setEmail(pharmacien.getEmail());
        pharmacienToUpdate.setMotDePasse(pharmacien.getMotDePasse());
        pharmacienToUpdate.setStatus(pharmacien.getStatus());
        String hashedPassword = hashPassword(pharmacien.getMotDePasse());
        pharmacienToUpdate.setMotDePasse(hashedPassword);

        return pharmacienEJB.updatePharmacien(pharmacienToUpdate);
    }


    public Pharmacien findPharmacienById(Long id) throws NotFoundException {
        Pharmacien pharmacien = pharmacienEJB.findPharmacienById(id);

        if (pharmacien == null) {
            throw new NotFoundException("Pharmacien with id " + id + " not found");
        }

        return pharmacien;
    }


    public List<Pharmacien> findPharmaciensByName(String name) throws NotFoundException {
        List<Pharmacien> pharmacien= pharmacienEJB.getPharmaciensByName(name);
        if(pharmacien.isEmpty()){
            throw new NotFoundException("No pharmacien found");
        }
        return pharmacien;
    }

    public Long countPharmaciens() {
        return pharmacienEJB.countPharmaciens();
    }
}