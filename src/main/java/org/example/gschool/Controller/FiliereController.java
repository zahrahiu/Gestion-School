package org.example.gschool.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.gschool.Entity.Filiere;
import org.example.gschool.Entity.Module;
import org.example.gschool.Service.FiliereService;
import org.example.gschool.Service.ImageService;
import org.example.gschool.Service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/filieres")
public class FiliereController {

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private ImageService imageService;



    @Autowired
    private ModuleService moduleService;

    @GetMapping
    public String listFilieres(Model model) {
        List<Filiere> filieres = filiereService.getAllFilieres();
        model.addAttribute("filieres", filieres);

        // Récupérer le nombre d'étudiants par filière
        Map<String, Long> etudiantsParFiliere = imageService.countStudentsByFiliere();
        model.addAttribute("etudiantsParFiliere", etudiantsParFiliere);

        // Récupérer les modules par filière
        Map<Filiere, List<Module>> modulesParFiliere = new HashMap<>();
        for (Filiere filiere : filieres) {
            List<Module> modules = moduleService.getModulesByFiliere(filiere.getId());
            modulesParFiliere.put(filiere, modules);
        }
        model.addAttribute("modulesParFiliere", modulesParFiliere);

        return "userList"; // Nom de la page HTML pour la liste des filières
    }

    @GetMapping("/ajouter")
    public String ajouterFiliereForm(Model model) {
        model.addAttribute("filiere", new Filiere());
        return "userForm";
    }

    @PostMapping("/sauvegarder")
    public String saveFiliere(@ModelAttribute Filiere filiere) {
        filiereService.saveFiliere(filiere);
        return "redirect:/filieres";
    }


    @PostMapping("/supprimer")
    public String supprimerFiliere(@RequestParam int id) {
        filiereService.supprimerFiliere(id);
        return "redirect:/filieres"; // Redirection après suppression
    }

    @GetMapping("/modifier/{id}")
    public String modifierFiliere(@PathVariable int id, Model model) {
        Filiere filiere = filiereService.getFiliereById(id);
        if (filiere != null) {
            model.addAttribute("filiere", filiere);
            return "modifier_filiere"; // Formulaire de modification
        }
        return "redirect:/filieres"; // Redirection si la filière n'est pas trouvée
    }

    @PostMapping("/modifier")
    public String saveModifiedFiliere(@ModelAttribute Filiere filiere) {
        filiereService.saveFiliere(filiere); // Mise à jour de la filière
        return "redirect:/filieres"; // Redirection vers la liste après modification
    }

    @GetMapping("/search")
    public String searchFilieres(@RequestParam(required = false) String nom,
                                 @RequestParam(required = false) String code,
                                 @RequestParam(required = false) String responsable,
                                 Model model) {
        List<Filiere> filieres = filiereService.searchFilieres(nom, code, responsable);
        model.addAttribute("filieres", filieres);
        model.addAttribute("nom", nom);
        model.addAttribute("code", code);
        model.addAttribute("responsable", responsable);
        return "userList";
    }


    @GetMapping("/details/{id}")
    public String afficherDetailsFiliere(@PathVariable int id, Model model) {
        Filiere filiere = filiereService.getFiliereById(id);
        if (filiere != null) {
            model.addAttribute("filiere", filiere);
            return "detailsFiliere"; // page dial détail
        }
        return "redirect:/filieres";
    }

    @GetMapping("/dashbort")
    public String dashboard(Model model) {
        List<Filiere> filieres = filiereService.getAllFilieres();
        model.addAttribute("filieres", filieres);

        // Récupérer le nombre d'étudiants par filière
        Map<String, Long> etudiantsParFiliere = imageService.countStudentsByFiliere();
        model.addAttribute("etudiantsParFiliere", etudiantsParFiliere);

        // Calculer le pourcentage d'étudiants par filière
        long totalEtudiants = etudiantsParFiliere.values().stream().mapToLong(Long::longValue).sum();
        Map<String, Double> pourcentageParFiliere = new HashMap<>();
        for (Map.Entry<String, Long> entry : etudiantsParFiliere.entrySet()) {
            double pourcentage = (entry.getValue() * 100.0) / totalEtudiants;
            pourcentageParFiliere.put(entry.getKey(), pourcentage);
        }
        model.addAttribute("pourcentageParFiliere", pourcentageParFiliere);

        return "accueil"; // Nom de la page HTML pour la liste des filières
    }

    @GetMapping("/downloadExcel")
    public ResponseEntity<InputStreamResource> downloadExcel() throws IOException {
        List<Filiere> filieres = filiereService.getAllFilieres();

        // Utilisation de Apache POI
        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Filières");

        // Création de l'en-tête
        String[] columns = {"ID", "Nom", "Code", "Durée d'étude", "Nombre de semestres", "Responsable"};
        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Remplissage des données
        int rowNum = 1;
        for (Filiere filiere : filieres) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(filiere.getId());
            row.createCell(1).setCellValue(filiere.getNom());
            row.createCell(2).setCellValue(filiere.getCode());
            row.createCell(3).setCellValue(filiere.getDureeEtude());
            row.createCell(4).setCellValue(filiere.getNombreSemestres());
            row.createCell(5).setCellValue(filiere.getResponsable());
        }

        // Écrire dans un flux mémoire
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=filieres.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }


}
