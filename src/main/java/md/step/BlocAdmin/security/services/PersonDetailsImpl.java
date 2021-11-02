package md.step.BlocAdmin.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import md.step.BlocAdmin.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonDetailsImpl implements UserDetails{
    private static final long serialVersionUID = 1L;

    private Integer personid;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public PersonDetailsImpl(Integer personid, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.personid = personid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static PersonDetailsImpl build(Person person) {
        List<GrantedAuthority> authorities = person.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new PersonDetailsImpl(
                person.getPersonid(),
                person.getUsername(),
                person.getEmail(),
                person.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getPersonId() {
        return personid;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PersonDetailsImpl person = (PersonDetailsImpl) o;
        return Objects.equals(personid, person.personid);
    }
}
