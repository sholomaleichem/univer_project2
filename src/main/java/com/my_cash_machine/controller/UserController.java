package com.my_cash_machine.controller;

import com.my_cash_machine.domen.User;
import com.my_cash_machine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.my_cash_machine.utils.PassEncoding;

import java.util.Collection;


@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;




    @RequestMapping({"/", "/login" })
    public String root(Model model) {
       // model.addAttribute("reqUser", new User());
        logger.info("root");
        return "login";
    }


    @RequestMapping("/home")
    public String home(Model model) {
//        List<Product> products = productService.getAllProducts();
//        model.addAttribute("products", products);

        logger.info("home");
        return "home";
    }

    @RequestMapping("/admin")
    public String helloAdmin() {
        logger.info("admin");
        return "admin";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("reqUser", new User());
        logger.info("register");
        return "register";
    }

    //ToDO Add optional!!!!!!!!!!!!!!!!!!!1
    @RequestMapping(value = {"/user/register"}, method = RequestMethod.POST)
    public String register(@ModelAttribute("reqUser") User reqUser,
                           final RedirectAttributes redirectAttributes) {

        logger.info("/user/register");
        User user = userService.findByEmail(reqUser.getEmail());
        if (user != null) {
            redirectAttributes.addFlashAttribute("saveUser", "exist-name");
            return "redirect:/register";
        }


        user = userService.findByEmail(reqUser.getEmail());

        if (user != null) {
            redirectAttributes.addFlashAttribute("saveUser", "exist-email");
            return "redirect:/register";
        }

        reqUser.setPassword(PassEncoding.getInstance().passwordEncoder.encode(reqUser.getPassword()));
        reqUser.setRole(2);

        if (userService.save(reqUser) != null) {
            redirectAttributes.addFlashAttribute("saveUser", "success");
        } else {
            redirectAttributes.addFlashAttribute("saveUser", "fail");
        }

        return "redirect:/register";
    }

    @PostMapping("info")
    public String info(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "info";
    }

    @PostMapping("info/save")
    public String saveInfo(Model model, @RequestParam(name = "namee") String name, @RequestParam(name = "surnamee") String surname) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        user.setName(name);
        user.setSurname(surname);
        userService.save(user);
        model.addAttribute("user", user);
        return "info";
    }


}
