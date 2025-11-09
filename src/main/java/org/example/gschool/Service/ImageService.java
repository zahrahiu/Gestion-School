package org.example.gschool.Service;

import org.example.gschool.Entity.Filiere;
import org.example.gschool.Entity.ImageEntity;

import java.util.List;
import java.util.Map;

public interface ImageService {
    void uploadImage(byte[] imageData, String imageName, String cne, String nom, String prenom,
                     String email, String telephone, String adresse, String filiere,
                     String sexe, String villeDenaissance);
    ImageEntity getImageByCne(String cne);
    List<ImageEntity> getAllStudents();
    void deleteStudentByCne(String cne);
    void updateStudent(String cne, ImageEntity updatedStudent);
    List<ImageEntity> getEtudiantsPaginated(int page, int size);
    List<ImageEntity> searchStudents(String cne, String nom, String prenom, String filiere, String sexe, String villeDenaissance);
    Map<String, Long> countStudentsByFiliere();
}
