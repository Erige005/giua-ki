package com.example.full_stack_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Trả về file login.html
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home"; // Trả về file home.html
    }

    @GetMapping("/user")
    public String user() {
        return "user"; // Trả về file user.html
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin"; // Trả về file admin.html
    }
}