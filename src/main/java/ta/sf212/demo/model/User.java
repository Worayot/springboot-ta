package ta.sf212.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // avoid conflict with SQL reserved word "user"
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;   // login username
    private String password;   // encrypted password
    private String name;       // first name
    private String surname;    // last name
    
    @jakarta.persistence.Column(updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Grade> gradesGiven = new ArrayList<>();

    // Default constructor
    public User() {}

    public User(String username, String password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gradesGiven = new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        this.dateCreated = LocalDate.now();
    }

    // getters & setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public List<Grade> getGradesGiven() { return gradesGiven; }
    public void setGradesGiven(List<Grade> gradesGiven) { this.gradesGiven = gradesGiven; }

    public LocalDate getDateCreated() { return dateCreated; }

    // ---- UserDetails interface methods ----
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // no roles yet
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
