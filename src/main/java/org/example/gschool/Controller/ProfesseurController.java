package org.example.gschool.Controller;

import org.example.gschool.Entity.ImageEntity;
import org.example.gschool.Entity.Professeur;
import org.example.gschool.Service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
@Controller
@RequestMapping("/professeurs")
public class ProfesseurController {

    @Autowired
    private ProfesseurService professeurService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("professeur", new Professeur());
        return "addProfesseur"; // Page HTML pour ajouter un professeur
    }

    @PostMapping("/add")
    public String addProfesseur(@ModelAttribute Professeur professeur,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            professeur.setImage(imageFile.getBytes()); // Convertir l'image en byte[]
        }
        professeurService.saveProfesseur(professeur);
        return "redirect:/professeurs/list"; // Redirection vers la liste des professeurs
    }

    @GetMapping("/list")
    public String listProfesseurs(Model model) {
        List<Professeur> professeurs = professeurService.getAllProfesseurs();
        model.addAttribute("professeurs", professeurs);
        return "listProfesseurs"; // Page HTML pour afficher la liste des professeurs
    }

    @GetMapping("/edit/{cin}")
    public String showEditForm(@PathVariable String cin, Model model) {
        Professeur professeur = professeurService.getProfesseurByCin(cin);
        model.addAttribute("professeur", professeur);
        return "editProfesseur"; // Page HTML pour modifier un professeur
    }


    @GetMapping(value = "/image/image/{cin}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImageData(@PathVariable String cin) {
        Professeur professeur = professeurService.getProfesseurByCin(cin);
        if (professeur != null && professeur.getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // ou MediaType.IMAGE_JPEG
                    .body(professeur.getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update/{cin}")
    public String updateProfesseur(@PathVariable String cin,
                                   @ModelAttribute Professeur professeur,
                                   @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            professeur.setImage(imageFile.getBytes()); // Mettre à jour l'image
        }
        professeurService.updateProfesseur(cin, professeur);
        return "redirect:/professeurs/list";
    }

    @GetMapping("/delete/{cin}")
    public String deleteProfesseur(@PathVariable String cin) {
        professeurService.deleteProfesseurByCin(cin);
        return "redirect:/professeurs/list";
    }

    @GetMapping("/search")
    public String searchProfesseurs(
            @RequestParam(required = false) String cin,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String login,
            Model model) {

        List<Professeur> professeurs = professeurService.searchProfesseurs(cin, nom, prenom, login);
        model.addAttribute("professeurs", professeurs);
        return "listProfesseurs"; // Retourne la même page avec les résultats de la recherche
    }


    @GetMapping("/downloadExcel")
    public ResponseEntity<InputStreamResource> downloadProfesseursExcel() throws IOException {
        List<Professeur> professeurs = professeurService.getAllProfesseurs();  // Adapte selon ton service

        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Professeurs");

        String[] columns = {"CIN", "Nom", "Prénom", "Adresse", "Téléphone", "Email", "Ville", "Login","password"};
        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (Professeur professeur : professeurs) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(professeur.getCin());
            row.createCell(1).setCellValue(professeur.getNom());
            row.createCell(2).setCellValue(professeur.getPrenom());
            row.createCell(3).setCellValue(professeur.getAdresse());
            row.createCell(4).setCellValue(professeur.getTelephone());
            row.createCell(5).setCellValue(professeur.getEmail());
            row.createCell(6).setCellValue(professeur.getVille());
            row.createCell(7).setCellValue(professeur.getLogin());
            row.createCell(8).setCellValue(professeur.getPassword());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Utilisateurs.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }

}