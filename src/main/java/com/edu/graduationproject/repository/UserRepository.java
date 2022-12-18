package com.edu.graduationproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.model.EAuthProvider;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users u WHERE u.username = ?1 AND u.is_deleted = 0", nativeQuery = true)
    public Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT ur.user FROM UserRole ur WHERE ur.role.id IN ('USER','ADMIN','STAFF')")
    public List<User> getAdministrators();

    @Query("SELECT u FROM User u WHERE u.reset_pwd_token = ?1")
    public Optional<User> findByResetPasswordToken(String token);

    @Query("SELECT o FROM User o WHERE o.verify_code=?1")
    public User findByVerifyCode(String code);

    // Derived Query - for checking if User exist by email
    public boolean existsUserByEmail(String email);

    // Derived Query - for checking if User exist by id
    public boolean existsUserById(String id);

    // @Query("SELECT o FROM User o WHERE username=?1")
    @Modifying
    @Query("DELETE FROM User u WHERE u.username=?1")
    public void deleteByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.provider = ?2 WHERE u.email = ?1")
    public void updateAuthenticationTypeOAuth(String email, EAuthProvider provider);

    @Modifying
    @Query("UPDATE User u SET u.provider = ?2 WHERE u.username = ?1 AND u.isDeleted = FALSE")
    public void updateAuthenticationTypeDB(String username, EAuthProvider provider);
}
