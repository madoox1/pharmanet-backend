package com.pharmacy.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("admin")
@Data
@NoArgsConstructor
public class Administrateur extends Utilisateur {
    // Additional fields and methods specific to Administrateur can be added here
}