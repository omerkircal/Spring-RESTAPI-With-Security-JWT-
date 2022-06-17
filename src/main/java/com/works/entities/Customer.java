package com.works.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.works.utils.passwordvalidation.ValidPassword;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class Customer extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(message = "Your first name cannot exceed 50 characters.", max = 50)
    @NotBlank(message = "Please Enter First Name")
    private String firstName;
    @NotBlank(message = "Please Enter Last Name")
    @Length(message = "Your last  name cannot exceed 50 characters.", min = 0, max = 50)
    private String lastName;
    @Length(message = "Your e-mail cannot exceed 50 characters.", max = 60)
    @NotBlank(message = "Please Enter E-Mail")
    @Email(message = "E-mail Format Exception")
    private String email;
    @NotBlank(message = "Please Enter Telephone")
    @Length(message = "telephone must contain min 10 max  5O character.", min = 10, max = 50)
    private String telephone;
    @ValidPassword
    @NotBlank(message = "Please Enter Password")
    private String password;
    private boolean enabled;
    private boolean tokenExpired;

    private String verificationCode;
    //@JsonIgnore
    @ManyToMany
    @JoinTable(name = "customer_role",joinColumns = @JoinColumn(name = "customer_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private List<Roles> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "customer",cascade={CascadeType.PERSIST, CascadeType.DETACH})
    private List<Orders> orders;
}
