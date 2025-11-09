package org.example.gschool.Service;

import org.example.gschool.Dao.ImageDAO;
import org.example.gschool.Entity.Filiere;
import org.example.gschool.Entity.ImageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDAO imageDao;

    @Override
    public void uploadImage(byte[] imageData, String imageName, String cne, String nom, String prenom,
                            String email, String telephone, String adresse, String filiere,
                            String sexe, String villeDenaissance) {
        ImageEntity image = new ImageEntity();
        image.setData(imageData);
        image.setImage(imageName);
        image.setCne(cne);
        image.setNom(nom);
        image.setPrenom(prenom);
        image.setEmail(email);
        image.setTelephone(telephone);
        image.setAdresse(adresse);
        image.setFiliere(filiere);
        image.setSexe(sexe);
        image.setVilleDenaissance(villeDenaissance);
        imageDao.save(image);
    }

    @Override
    public ImageEntity getImageByCne(String cne) {
        return imageDao.findByCne(cne); // Utilisation de la méthode pour retrouver l'image par CNE
    }

    @Override
    public List<ImageEntity> getAllStudents() {
        return imageDao.findAll(); // Récupérer tous les étudiants
    }

    @Override
    @Transactional
    public void deleteStudentByCne(String cne) {
        System.out.println("Recherche de l'étudiant avec CNE : " + cne);
        ImageEntity student = imageDao.findByCne(cne);  // Recherche de l'étudiant par CNE
        if (student != null) {
            System.out.println("Étudiant trouvé : " + student.getNom());
            imageDao.delete(student);  // Suppression de l'étudiant
            System.out.println("Étudiant supprimé avec succès.");
        } else {
            System.out.println("Aucun étudiant trouvé avec CNE : " + cne);
        }
    }
    @Override
    @Transactional
    public void updateStudent(String cne, ImageEntity updatedStudent) {
        ImageEntity student = imageDao.findByCne(cne);
        if (student != null) {
            student.setNom(updatedStudent.getNom());
            student.setPrenom(updatedStudent.getPrenom());
            student.setEmail(updatedStudent.getEmail());
            student.setTelephone(updatedStudent.getTelephone());
            student.setAdresse(updatedStudent.getAdresse());
            student.setFiliere(updatedStudent.getFiliere());
            student.setSexe(updatedStudent.getSexe());
            student.setVilleDenaissance(updatedStudent.getVilleDenaissance());
            imageDao.save(student); // Met à jour l'étudiant dans la base de données
        }
    }

    @Override
    public List<ImageEntity> getEtudiantsPaginated(int page, int size) {
        return imageDao.getEtudiantsP(page, size);
    }
    @Override
    public List<ImageEntity> searchStudents(String cne, String nom, String prenom, String filiere, String sexe, String villeDenaissance) {
        return imageDao.searchStudents(cne, nom, prenom, filiere, sexe, villeDenaissance);
    }

    @Override
    public Map<String, Long> countStudentsByFiliere() {
        return imageDao.countStudentsByFiliere();
    }



}
