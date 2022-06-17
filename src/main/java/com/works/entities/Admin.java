package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.works.utils.passwordvalidation.ValidPassword;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
public class Admin extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(message = "Your company name cannot exceed 50 characters.", max = 50)
    @NotBlank(message = "Please Enter Company Name")
    private String companyName;
    @Length(message = "Your first name cannot exceed 50 characters.", max = 50)
    @NotBlank(message = "Please Enter First Name")
    private String firstName;
    @NotBlank(message = "Please Enter Last Name")
    @Length(message = "Your last  name cannot exceed 50 characters.", min = 0, max = 50)
    private String lastName;
    @Length(message = "Your e-mail cannot exceed 60 characters.", max = 60)
    @NotBlank(message = "Please Enter E-Mail")
    @Email(message = "E-mail Format Exception")
    private String email;
    @ValidPassword
    @NotBlank(message = "Please Enter Password")
    private String password;
    private boolean enabled;
    private boolean tokenExpired;
    @NotBlank(message = "Please Enter Telephone")
    @Length(message = "telephone must contain min 10 max  5O character.", min = 10, max = 50)
    private String telephone;

    private String verificationCode;

    //@JsonIgnore
    @ManyToMany
    @JoinTable(name = "admin_role",joinColumns = @JoinColumn(name = "admin_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private List<Roles> roles;
}
