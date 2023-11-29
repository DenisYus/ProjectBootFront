package ru.denis.katacourse.ProjectBoot.controller;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.denis.katacourse.ProjectBoot.model.User;
import ru.denis.katacourse.ProjectBoot.service.RoleService;
import ru.denis.katacourse.ProjectBoot.service.UserService;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public AuthController(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {


        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      @RequestParam(value = "checkedRegistration") String[] selectResult,
                                      BindingResult bindingResult) {
        for (String s : selectResult) {
            user.addRole(roleService.getRole("ROLE_" + s));
        }
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/auth/login";
    }

}
