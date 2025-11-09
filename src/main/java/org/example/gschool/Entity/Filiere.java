package org.example.gschool.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "filiere")
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "duree_etude", nullable = false)
    private int dureeEtude;

    @Column(name = "nombre_semestres", nullable = false)
    private int nombreSemestres;

    @Column(name = "responsable", nullable = false, length = 100)
    private String responsable;

    @OneToMany(mappedBy = "filiereEntity", fetch = FetchType.LAZY)
    private List<ImageEntity> students;
    // Liste des étudiants associés à cette filière

    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL)
    private List<Module> modules;


    // Constructeurs
    public Filiere() {}

    public Filiere(String nom, String code, String description, int dureeEtude, int nombreSemestres, String responsable) {
        this.nom = nom;
        this.code = code;
        this.description = description;
        this.dureeEtude = dureeEtude;
        this.nombreSemestres = nombreSemestres;
        this.responsable = responsable;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDureeEtude() {
        return dureeEtude;
    }

    public void setDureeEtude(int dureeEtude) {
        this.dureeEtude = dureeEtude;
    }

    public int getNombreSemestres() {
        return nombreSemestres;
    }

    public void setNombreSemestres(int nombreSemestres) {
        this.nombreSemestres = nombreSemestres;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public List<ImageEntity> getStudents() {
        return students;
    }

    public void setStudents(List<ImageEntity> students) {
        this.students = students;
    }
}
