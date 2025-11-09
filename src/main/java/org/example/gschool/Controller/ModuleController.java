package org.example.gschool.Controller;

import org.example.gschool.Entity.Filiere;
import org.example.gschool.Entity.Module;
import org.example.gschool.Service.FiliereService;
import org.example.gschool.Service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private ModuleService moduleService;

    @GetMapping("/emploi")
    public String formAjous(Model model) {

        return "listeEtudiants";
    }

    @GetMapping("/ajouter")
    public String formAjouterModules(Model model) {
        List<Filiere> filieres = filiereService.getAllFilieres();
        model.addAttribute("filieres", filieres);
        model.addAttribute("modules", new ArrayList<Module>());
        return "addModule";
    }

    @PostMapping("/sauvegarder")
    public String sauvegarderModules(@RequestParam("filiereId") int filiereId,
                                     @RequestParam("nomsModules") List<String> nomsModules) {
        Filiere filiere = filiereService.getFiliereById(filiereId);
        for (String nom : nomsModules) {
            if (nom != null && !nom.trim().isEmpty()) {
                Module module = new Module();
                module.setNom(nom);
                module.setFiliere(filiere);
                moduleService.saveModule(module);
            }
        }
        return "redirect:/modules/liste";
    }

    @GetMapping("/liste")
    public String listeModulesParFiliere(Model model) {
        // Récupérer toutes les filières
        List<Filiere> filieres = filiereService.getAllFilieres();

        // Pour chaque filière, récupérer ses modules
        Map<Filiere, List<Module>> filieresAvecModules = new HashMap<>();
        for (Filiere filiere : filieres) {
            List<Module> modules = moduleService.getModulesByFiliere(filiere.getId());
            filieresAvecModules.put(filiere, modules);
        }

        // Ajouter les données au modèle
        model.addAttribute("filieresAvecModules", filieresAvecModules);
        return "listeModules"; // Nom de la page HTML pour afficher les modules par filière
    }
}
