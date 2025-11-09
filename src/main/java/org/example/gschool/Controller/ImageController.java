package org.example.gschool.Controller;

import org.example.gschool.Entity.Filiere;
import org.example.gschool.Entity.ImageEntity;
import org.example.gschool.Service.FiliereService;
import org.example.gschool.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private FiliereService filiereService; // Injectez le service FiliereService

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        // Récupérer la liste des filières depuis le service
        List<Filiere> filieres = filiereService.getAllFilieres();
        // Ajouter la liste des filières au modèle
        model.addAttribute("filieres", filieres);
        return "uploadForm"; // Page HTML pour formulaire
    }

    @PostMapping("/upload")
    public String handleImageUpload(@RequestParam("image") MultipartFile file,
                                    @RequestParam("cne") String cne,
                                    @RequestParam("nom") String nom,
                                    @RequestParam("prenom") String prenom,
                                    @RequestParam("email") String email,
                                    @RequestParam("telephone") String telephone,
                                    @RequestParam("adresse") String adresse,
                                    @RequestParam("filiere") String filiere,
                                    @RequestParam("sexe") String sexe,
                                    @RequestParam("villeDenaissance") String villeDenaissance) {
        try {
            byte[] imageData = file.getBytes();
            String imageName = file.getOriginalFilename();
            imageService.uploadImage(imageData, imageName, cne, nom, prenom, email, telephone, adresse, filiere, sexe, villeDenaissance);
            return "uploadForm"; // Redirection vers la page d'affichage
        } catch (IOException e) {
            return "redirect:/error"; // Page d'erreur
        }
    }




    @GetMapping("/image/{cne}")
    public String displayImage(@PathVariable String cne, Model model) {
        ImageEntity image = imageService.getImageByCne(cne); // Recherche de l'image par CNE
        if (image != null) {
            model.addAttribute("image", image); // Ajout de l'image au modèle
            model.addAttribute("student", image); // Ajout de l'étudiant au modèle
            return "displayImage"; // Vue affichant les détails de l'étudiant
        } else {
            return "error"; // Page d'erreur si l'image n'est pas trouvée
        }
    }


    @GetMapping(value = "/image/data/{cne}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getImageData(@PathVariable String cne) {
        ImageEntity image = imageService.getImageByCne(cne);
        if (image != null) {
            return ResponseEntity.ok().body(image.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/students")
    public String listStudents(Model model) {
        List<ImageEntity> students = imageService.getAllStudents();
        model.addAttribute("students", students);



        return "listEtudiants";
    }

    @PostMapping("/students/delete/{cne}")
    public String deleteStudent(@PathVariable String cne) {
        System.out.println("Tentative de suppression de l'étudiant avec CNE : " + cne);
        imageService.deleteStudentByCne(cne);  // Appel du service pour supprimer l'étudiant
        return "redirect:/students";  // Redirection vers la liste des étudiants
    }

    @GetMapping("/students/edit/{cne}")
    public String showEditForm(@PathVariable String cne, Model model) {
        ImageEntity student = imageService.getImageByCne(cne);
        if (student != null) {
            model.addAttribute("student", student);
            return "editStudent"; // Nom de la page HTML pour la modification
        } else {
            return "redirect:/students"; // Redirige si l'étudiant n'est pas trouvé
        }
    }

    @PostMapping("/students/update/{cne}")
    public String updateStudent(@PathVariable String cne, @ModelAttribute ImageEntity student) {
        imageService.updateStudent(cne, student);
        return "redirect:/students";
    }

    @GetMapping("/students/search")
    public String searchStudents(
            @RequestParam(required = false) String cne,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String sexe,
            @RequestParam(required = false) String villeDenaissance,
            Model model) {

        List<ImageEntity> students = imageService.searchStudents(cne, nom, prenom, filiere, sexe, villeDenaissance);
        model.addAttribute("students", students);
        return "listEtudiants"; // Retourne la même page avec les résultats de la recherche
    }

    @GetMapping("/etudiants/downloadExcel")
    public ResponseEntity<InputStreamResource> downloadEtudiantsExcel() throws IOException {
        List<ImageEntity> etudiants = imageService.getAllStudents();  // Adapte selon ton service

        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Etudiants");

        String[] columns = {"CNE", "Nom", "Prénom", "Date de naissance", "Email", "Téléphone", "Adresse", "Filière", "Sexe", "Ville de naissance"};
        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (ImageEntity etudiant : etudiants) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(etudiant.getCne());
            row.createCell(1).setCellValue(etudiant.getNom());
            row.createCell(2).setCellValue(etudiant.getPrenom());
            row.createCell(3).setCellValue(etudiant.getDateNaissance() != null ? etudiant.getDateNaissance().toString() : "");
            row.createCell(4).setCellValue(etudiant.getEmail());
            row.createCell(5).setCellValue(etudiant.getTelephone());
            row.createCell(6).setCellValue(etudiant.getAdresse());
            row.createCell(7).setCellValue(etudiant.getFiliere());
            row.createCell(8).setCellValue(etudiant.getSexe());
            row.createCell(9).setCellValue(etudiant.getVilleDenaissance());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=etudiants.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }




}
