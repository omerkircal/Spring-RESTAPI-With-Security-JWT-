package com.works.restcontrollers;


import com.works.entities.Admin;
import com.works.entities.Customer;
import com.works.entities.Login;
import com.works.services.AdminDetailService;
import com.works.services.AuthenticationService;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@RestController
@Validated
@RequestMapping("/admin")
public class AdminRestController {
    final AdminDetailService adminDetailService;
    final AuthenticationService authenticationService;

    public AdminRestController(AdminDetailService adminDetailService, AuthenticationService authenticationService) {
        this.adminDetailService = adminDetailService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody Admin admin){
        return adminDetailService.register(admin);
    }

    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody Login jwtLogin){
        return authenticationService.auth(jwtLogin);
    }

    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam @NotBlank(message = "password can not be blank")  String newPassword){
        return  adminDetailService.changePassword(oldPassword,newPassword);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        return adminDetailService.forgotPassword(email);
    }
    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String resettoken,@RequestParam String password){

        return adminDetailService.resetPassword(resettoken,password);

    }

    @PutMapping("/settings")
    public ResponseEntity settings(@RequestParam String companyName, @RequestParam  @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50) String firstName,
                                  @RequestParam @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50)  String lastName,
                                  @RequestParam @Email(message = "E-mail Format error") String email,
                                  @RequestParam  @Length(message = "telephone must contain min 10 max  5O character.", min = 10, max = 50) String telephone ){
        return adminDetailService.settings(companyName, firstName, lastName,email,telephone);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return adminDetailService.delete(id);
    }


    @PutMapping("/changeCustomerEnable")
    public ResponseEntity changeEnable(@RequestParam Long id,@RequestParam boolean enable){
        return adminDetailService.changeEnableCustomer(id,enable);
    }
    @GetMapping("/list")
    public ResponseEntity list(){

        return adminDetailService.list();
    }





}
