package com.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("pharmacist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Pharmacien extends Utilisateur {


    @ManyToOne(optional = false) // Pharmacien must belong to a Pharmacie
    @JoinColumn(name = "pharmacie_id", nullable = false)
    @JsonBackReference
    private Pharmacie pharmacie;
}