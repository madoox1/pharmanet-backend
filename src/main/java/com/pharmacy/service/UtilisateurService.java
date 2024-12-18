package com.pharmacy.service;

import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.UtilisateurEJB;
import com.pharmacy.model.Utilisateur;
import com.pharmacy.model.UtilisateurStatus;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class UtilisateurService {
    @EJB
    UtilisateurEJB utilisateurEJB ;

    public List<Utilisateur> GetAllUsers() throws NotFoundException {
        List<Utilisateur> users = utilisateurEJB.getAllUsers();
        if(users == null) {
            throw new NotFoundException("No user found");
        }
        return users;
    }

    public Utilisateur findUserByEmail(String email) throws NotFoundException {
        Utilisateur user = utilisateurEJB.findUserByEmail(email);
        if(user == null) {
            throw new NotFoundException("No user found with email: " + email);
        }
        return user;
    }


    public Utilisateur changeUserStatus(Long id, String status) throws NotFoundException {
        Utilisateur user = utilisateurEJB.findUserById(id);
        if(user == null) {
            throw new NotFoundException("No user found with id: " + id);
        }
        user.setStatus(UtilisateurStatus.valueOf(status));
        utilisateurEJB.save(user);
        return user;
    }
}
