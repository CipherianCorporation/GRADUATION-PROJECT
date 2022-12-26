package com.edu.graduationproject.controller;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.service.ForgotPasswordService;
import com.edu.graduationproject.service.UserService;
import com.edu.graduationproject.utils.CommonUtils;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    ForgotPasswordService forgotPasswordService;

    @GetMapping("/account/signup")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "account/signup";
    }

    @GetMapping("/account/editprofile")
    public String editprofile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> loggedinUser = userService.findByUsername(auth.getName());
        model.addAttribute("user", loggedinUser.orElse(new User()));
        return "account/edit_profile";
    }

    @PostMapping("/account/register")
    public String register(Model model, @ModelAttribute User user, HttpServletRequest req) throws MessagingException {
        Optional<User> existUserByEmail = userService.findByEmail(user.getEmail());
        Optional<User> existUserByUsername = userService.findByUsername(user.getUsername());
        if (existUserByEmail.isPresent()) {
            if (existUserByEmail.get().getIsDeleted() == false) {
                model.addAttribute("message", "Email " + user.getEmail() + " đã tồn tại");
                return "account/signup";
            }
        }
        if (existUserByUsername.isPresent()) {
            if (existUserByUsername.get().getIsDeleted() == false) {
                model.addAttribute("message", "Tên đăng nhập " + user.getUsername() + " đã tồn tại");
                return "account/signup";
            }
        }
        userService.register(user, CommonUtils.getSiteURL(req));
        model.addAttribute("message", "Vui lòng kiểm tra email để xác nhận tài khoản.");
        return "account/signup";
    }

    @GetMapping("/verify")
    public String verifyAcc(@RequestParam String code) {
        if (userService.verify(code)) {
            return "account/verify-success";
        } else {
            return "account/verify-fail";
        }
    }

    @GetMapping("/account/change_password")
    public String change_password() {
        return "account/change_password";
    }

    @PostMapping("/account/change_password")
    public String showResetPasswordForm(Model model, Authentication authentication,
            @RequestParam("password") String password,
            RedirectAttributes redirAttrs) {
        Optional<User> loggedinUser = userService.findByUsername(authentication.getName());
        if (loggedinUser.isPresent()) {
            forgotPasswordService.updatePassword(loggedinUser.get(), password);
        }
        model.addAttribute("message", "Đổi mật khẩu thành công");
        return "redirect:/security/logoff";
    }

}
