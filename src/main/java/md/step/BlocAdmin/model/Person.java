package md.step.BlocAdmin.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(	name = "persons",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
//                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "idnp")
        })
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personid;

    @NotBlank
    @Size(max = 20,min = 3)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "person_roles",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @NotBlank
    @Size(max=30)
    private String name;

    @NotBlank
    @Size(max=30)
    private String surname;

    @Size(min = 10, max = 200)
    private String description;

    @Size(max=20)
    private String phone;

    @Size(max=20)
    private String mobile;

    private LocalDate regDate;

    @NotBlank
    @Size(min = 13,max=13)
    private String idnp;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    public Person() {
    }

    public Person(String username, String email, String password,String description,String name, String surname,String idnp,String mobile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.description=description;
        this.name=name;
        this.surname=surname;
        this.idnp=idnp;
        this.mobile=mobile;
        this.regDate= LocalDate.now();
    }

    public void setPersonid(Integer personid) {
        this.personid = personid;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Integer getPersonid() {
        return personid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public String getIdnp() {
        return idnp;
    }

    public void setIdnp(String idnp) {
        this.idnp = idnp;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personid=" + personid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", regDate=" + regDate +
                ", idnp='" + idnp + '\'' +
                '}';
    }
}
