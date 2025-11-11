package com.example.foodshare.controller;

import com.example.foodshare.entity.User;
import com.example.foodshare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TestController {
    
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/test-login")
    public String testLoginPage(Model model) {
        return "test-login";
    }

    @PostMapping("/test-login")
    public String testLogin(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
            model.addAttribute("userFound", true);
            model.addAttribute("passwordMatches", passwordMatches);
            model.addAttribute("userRole", user.getRole());
            model.addAttribute("userName", user.getName());
        } else {
            model.addAttribute("userFound", false);
        }
        return "test-login-result";
    }
}
