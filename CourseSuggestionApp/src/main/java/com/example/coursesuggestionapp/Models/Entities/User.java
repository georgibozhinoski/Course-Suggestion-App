package com.example.coursesuggestionapp.Models.Entities;

import com.example.coursesuggestionapp.Models.ENUM.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.coursesuggestionapp.Models.Entities.UserCourse.UserCourse;
import java.util.*;

@AllArgsConstructor
@Entity
@Data
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;;

    @ManyToOne
    @JoinColumn(name = "major", nullable = false)
    private StudyMajor studyMajor;

    @OneToMany(mappedBy = "sys_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendationList> lists = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CheatSheet> sheets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserCourse> passedCourses = new ArrayList<>();

    @ManyToMany(mappedBy = "likedByUsers")
    private Set<Comment> likedComments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_has_interest",
            joinColumns = @JoinColumn(name = "interest_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<Interest> interests = new HashSet<>();

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
