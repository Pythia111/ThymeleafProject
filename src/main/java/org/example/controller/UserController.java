package org.example.controller;

import org.example.entity.User;
import org.example.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/game";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, Model model) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/game";
        } else {
            model.addAttribute("error", true);
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           HttpSession session, Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);

        session.setAttribute("user", user);
        return "redirect:/game";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
