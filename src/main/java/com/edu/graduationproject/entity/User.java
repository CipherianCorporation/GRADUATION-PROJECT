package com.edu.graduationproject.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.edu.graduationproject.model.EAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private String address;

    // đặt tên là image_url vì khi user login bằng GG,FB thì lưu url ảnh
    // user signup theo dạng database thì chỉ lưu tên ảnh <tên>.jpg
    private String image_url;

    @Enumerated(EnumType.STRING) // Đinh dạng cái enum AuthProvider như là String
    private EAuthProvider provider;
    private String provider_id;
    private Boolean enabled;
    private String verify_code;
    private String reset_pwd_token;
    // bắt buộc dùng @TemporalType.Date cho các class từ java.util.*
    @Temporal(TemporalType.DATE)
    @Column(name="updated_at")
    private Date updatedAt;
    @Temporal(TemporalType.DATE)
    @Column(name="created_at")
    private Date createdAt = new Date();
    @Temporal(TemporalType.DATE)
    @Column(name="deleted_at")
    private Date deletedAt;

    // OneToMany cách mới của Codejava.com - Chạy được nhưng khi update User thì bên
    // UserRole sẽ bị xóa chứ ko cascade

    // @JsonIgnore
    // @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"),
    // inverseJoinColumns = @JoinColumn(name = "role_id"))
    // private Set<Role> authorities = new HashSet<>();

    // OneToMany cách cũ của thầy Nghiệm - HIỆU QUẢ
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> authorities;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;

}
