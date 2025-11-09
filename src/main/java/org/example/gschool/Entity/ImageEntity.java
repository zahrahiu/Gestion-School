package org.example.gschool.Entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class ImageEntity {

    @Id
    private String cne; // CNE comme identifiant

    private String nom;            // Nom de l'étudiant
    private String prenom;         // Prénom de l'étudiant

    private String image;          // Chemin ou URL de l'image

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Date de naissance
    private Date dateNaissance;

    private String email;          // Email de l'étudiant
    private String telephone;      // Numéro de téléphone
    private String adresse;        // Adresse de l'étudiant
    private String filiere;        // Filière de l'étudiant
    private String sexe;           // Sexe de l'étudiant
    private String villeDenaissance; // Ville de naissance de l'étudiant

    @Lob // Pour stocker des données binaires (byte[])
    private byte[] data; // Données de l'image

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filiere_id")  // Cette clé étrangère relie l'étudiant à la filière
    private Filiere filiereEntity; // La filière à laquelle l'étudiant appartient

    // Getters et Setters
    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getVilleDenaissance() {
        return villeDenaissance;
    }

    public void setVilleDenaissance(String villeDenaissance) {
        this.villeDenaissance = villeDenaissance;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Filiere getFiliereEntity() {
        return filiereEntity;
    }

    public void setFiliereEntity(Filiere filiereEntity) {
        this.filiereEntity = filiereEntity;
    }
}
