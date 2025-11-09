package org.example.gschool.Controller;

import org.example.gschool.Service.FiliereService;
import org.example.gschool.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class LoginController {

    private final UserService userService;
    @Autowired
    private FiliereService filiereService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        if (userService.authenticate(email, password)) {
            model.addAttribute("filieres", filiereService.getAllFilieres());
            return "redirect:/filieres/dashbort";
        } else {
            model.addAttribute("error", "Email ou mot de passe incorrect !");
            return "login";
        }
    }

    @GetMapping ("/logout")
    public String logout() {

        return "login";  // ou la page de votre choix
    }

    @GetMapping ("/Emploi")
    public String emploi() {

        return "listeEtudiants";  // ou la page de votre choix
    }



}
