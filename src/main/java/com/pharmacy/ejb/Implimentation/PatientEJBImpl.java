package com.pharmacy.ejb.Implimentation;

import com.pharmacy.model.Patient;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class PatientEJBImpl {

    @PersistenceContext(unitName = "pharmacyPU")
    private EntityManager em;


    public Patient addPatient(Patient patient) {
        em.persist(patient);
        return patient;
    }

    public Patient findPatientById(Long id) {
        return em.find(Patient.class, id);
    }

    public List<Patient> getAllPatients() {
        return em.createQuery("SELECT p FROM Patient p", Patient.class)
                .getResultList();
    }

    public List<Patient> getPatientsByName(String name) {
        return em.createQuery("SELECT p FROM Patient p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :name, '%'))", Patient.class)
                .setParameter("name", name)
                .getResultList();
    }


    public Patient updatePatient(Patient patient) {
        return em.merge(patient);
    }

    public void deletePatient(Long id) {
        Patient existingPatient = em.find(Patient.class, id);
        if (existingPatient != null) {
            em.remove(existingPatient);
        }
    }
    public  Patient findPatientByEmail(String email){
        try{
            return em.createQuery("SELECT p FROM Patient p WHERE p.email = :email", Patient.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }

    public long countPatients() {
        try {
            // On effectue la requête et on retourne le résultat sous forme de long
            return (long) em.createQuery("SELECT COUNT(p) FROM Patient p").getSingleResult();
        } catch (Exception e) {
            return 0; // En cas d'erreur, retourner 0
        }
    }

}