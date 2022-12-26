package com.edu.graduationproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class SecurityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "security/login";
        }
        return "forward:/";
    }

    @RequestMapping("/security/login/form")
    public String loginForm(Model model) {
        // model.addAttribute("message", "Please login!");
        return "security/login";
    }

    @RequestMapping("/security/login/success")
    public String loginSuccess(Model model, Authentication auth) {
        boolean isShipper = auth.getAuthorities().stream()
                .map(role -> role.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList())
                .contains("SHIPPER");
        if (isShipper == true) {
            return "redirect:/order/shipper";
        }
        model.addAttribute("message", "Đăng nhập thành công!");
        return "redirect:/";
    }

    @RequestMapping("/security/login/error")
    public String loginError(Model model) {
        model.addAttribute("message", "Sai thông tin hoặc tài khoản chưa được kích hoạt!");
        return "security/login";
    }

    @RequestMapping("/security/unauthorized")
    public String unauthoried(Model model) {
        model.addAttribute("message", "Từ chối truy cập!");
        return "security/login";
    }

    @RequestMapping("/security/logoff/success")
    public String logoffSuccess(Model model, RedirectAttributes redirAttrs) {
        model.addAttribute("message", "Bạn đã đăng xuất!");
        return "security/login";
    }

    // OAuth2

    @RequestMapping("/oauth2/login/success")
    public String success(OAuth2AuthenticationToken oToken) {
        return "forward:/security/login/success";
    }
}
