package com.edu.graduationproject.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.Role;
import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.entity.UserRole;
import com.edu.graduationproject.model.EAuthProvider;
import com.edu.graduationproject.model.MailInfo;
import com.edu.graduationproject.repository.RoleRepository;
import com.edu.graduationproject.repository.UserRepository;
import com.edu.graduationproject.repository.UserRoleRepository;
import com.edu.graduationproject.service.MailerService;
import com.edu.graduationproject.service.UserService;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    UserRoleRepository userRoleRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MailerService mailer;

    @Override
    public Optional<User> findById(Integer id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User save(User user) {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    @Override
    public User update(User user) {
        Optional<User> findUser = userRepo.findByUsername(user.getUsername());
        if (findUser.isPresent()) {
            if (user.getEnabled() == null) {
                user.setEnabled(true);
            }
            if (findUser.get().getProvider().equals(EAuthProvider.FACEBOOK)) {
                user.setPassword(null);
                user.setProvider(EAuthProvider.FACEBOOK);
            } else if (findUser.get().getProvider().equals(EAuthProvider.GOOGLE)) {
                user.setPassword(null);
                user.setProvider(EAuthProvider.GOOGLE);
            } else if (findUser.get().getProvider().equals(EAuthProvider.DATABASE)) {
                user.setProvider(EAuthProvider.DATABASE);
            } else if (findUser.get().getProvider() == null || user.getProvider() == null) {
                user.setProvider(EAuthProvider.DATABASE);
            }
            if (findUser.get().getPassword() != null || findUser.get().getPassword() != "") {
                user.setPassword((findUser.get().getPassword()));
            }
            user.setUpdatedAt(new Date());
        }
        return userRepo.save(user);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepo.deleteByUsername(username);

    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public List<User> getAdministators() {
        return userRepo.getAdministrators();
    }

    @Override
    public void register(User user, String url) throws MessagingException {
        String encodedPassword = encoder.encode(user.getPassword());
        String randomCode = RandomString.make(64);
        user.setPassword(encodedPassword);
        user.setVerify_code(randomCode);
        user.setEnabled(false);
        user.setProvider(EAuthProvider.DATABASE);
        User save = userRepo.save(user);

        // set role USER cho user vì nếu là người dùng bình thường đăng ký thì chỉ set
        // role là USER
        Optional<Role> role = roleRepo.findById("USER");
        userRoleRepo.save(new UserRole(user, role.get()));
        sendVerifyEmail(user, url);
    }

    @Override
    public void sendVerifyEmail(User user, String url) throws MessagingException {
        MailInfo mail = new MailInfo();
        mail.setTo(user.getEmail());
        mail.setSubject("ShoeShy - Verify your email");
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>";

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = url + "/verify?code=" + user.getVerify_code();
        content = content.replace("[[URL]]", verifyURL);
        mail.setBody(content);
        mailer.queue(mail);
    }

    @Override
    public boolean verify(String verifyCode) {
        User user = userRepo.findByVerifyCode(verifyCode);
        if (user == null || user.getEnabled()) {
            return false;
        } else {
            user.setVerify_code("0");
            user.setEnabled(true);
            userRepo.save(user);
            return true;
        }
    }

    @Override
    public void processOAuthPostLogin(String username, String email, String image, String oauth2ClientName) {
        Optional<User> existAcc = userRepo.findByEmail(email);
        if (!existAcc.isPresent()) {
            User newAcc = new User();
            EAuthProvider authProvider = EAuthProvider.valueOf(oauth2ClientName.toUpperCase());
            newAcc.setUsername(username);
            newAcc.setEmail(email);
            newAcc.setProvider(authProvider);
            newAcc.setImage_url(image);
            newAcc.setEnabled(true);
            System.out.println(newAcc.toString());
            userRepo.save(newAcc);
        }
    }

    @Override
    public void updateAuthenticationTypeOAuth(String username, String oauth2ClientName) {
        EAuthProvider authProvider = EAuthProvider.valueOf(oauth2ClientName.toUpperCase());
        userRepo.updateAuthenticationTypeOAuth(username, authProvider);
    }

    @Override
    public void updateAuthenticationTypeDB(String username, String oauth2ClientName) {
        EAuthProvider authProvider = EAuthProvider.valueOf(oauth2ClientName.toUpperCase());
        userRepo.updateAuthenticationTypeDB(username, authProvider);
    }

}
