package com.pharmacy.service;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.DuplicationException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.PatientEJBImpl;
import com.pharmacy.model.Patient;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;


@Stateless
public class PatientService {

    @EJB
    private PatientEJBImpl patientEJB;

    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public Patient addPatient(Patient patient) throws DuplicationException, BadRequestException {
        if(patient ==null)
        {
            throw new BadRequestException("Patient object is missing information");
        }
        if(patientEJB.findPatientByEmail(patient.getEmail())!=null){
            throw new DuplicationException("user with same email exist");
        }
        String hashedPassword = hashPassword(patient.getMotDePasse());
        patient.setMotDePasse(hashedPassword);
        patientEJB.addPatient(patient);
        return patient;
    }


    public void deletePatient(Long id) throws NotFoundException {
        Patient patient = patientEJB.findPatientById(id);
        if(patient == null){
            throw new NotFoundException("Patient with id " + id + " not found");
        }
        patientEJB.deletePatient(patient.getId());
    }

    public List<Patient> listPatients() throws NotFoundException {
        List<Patient> patients = patientEJB.getAllPatients();
        if(patients.isEmpty()){
            throw new NotFoundException("No patient found");
        }
        return patients;
    }

    public Patient updatePatient(Patient patient, Long id) throws NotFoundException, DuplicationException {
        Patient patientToUpdate= findPatientById(id);
        if (patientEJB.findPatientByEmail(patient.getEmail()) != null) {
            throw new DuplicationException("Un utilisateur avec cet email existe déjà");
        }
        patientToUpdate.setNom(patient.getNom());
        patientToUpdate.setPrenom(patient.getPrenom());
        patientToUpdate.setEmail(patient.getEmail());
        patientToUpdate.setAdresse(patient.getAdresse());
        patientToUpdate.setMotDePasse(patient.getMotDePasse());
        patientToUpdate.setStatus(patient.getStatus());
        String hashedPassword = hashPassword(patient.getMotDePasse());
        patientToUpdate.setMotDePasse(hashedPassword);
        return patientEJB.updatePatient(patientToUpdate);
    }


    public Patient findPatientById(Long id) throws NotFoundException {
        Patient patient = patientEJB.findPatientById(id);
        if (patient == null) {
            throw new NotFoundException("Patient with id " + id + " not found");
        }
        return patient;
    }

    public Long countPatients() {
        return patientEJB.countPatients();
    }
}
