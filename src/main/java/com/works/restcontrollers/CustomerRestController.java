package com.works.restcontrollers;

import com.works.entities.Customer;
import com.works.entities.Login;
import com.works.services.AuthenticationService;
import com.works.services.CustomerDetailService;
import com.works.utils.passwordvalidation.ValidPassword;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/customer")
@Validated
public class CustomerRestController {
    final CustomerDetailService customerDetailService;
    final AuthenticationService authenticationService;
    public CustomerRestController(CustomerDetailService customerDetailService, AuthenticationService authenticationService) {
        this.customerDetailService = customerDetailService;
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody Customer customer){
        return customerDetailService.register(customer);
    }

    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody Login jwtLogin){
        return authenticationService.auth(jwtLogin);
    }

    @PutMapping("/changePassword")
    public ResponseEntity changePassword(@RequestParam String oldPassword, @RequestParam @NotBlank(message = "password can not be blank")  String newPassword){
        return  customerDetailService.changePassword(oldPassword,newPassword);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        return customerDetailService.forgotPassword(email);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestParam String resettoken,@RequestParam String password){

        return customerDetailService.resetPassword(resettoken,password);

    }

    @PutMapping("/setting")
    public ResponseEntity setting(@RequestParam  @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50) String firstName,
                                  @RequestParam @Length(message = "firstName  must contain min 2 max  50 character.", min = 2, max = 50)  String lastName,
                                  @RequestParam @Email(message = "E-mail Format error") String email,
                                  @RequestParam  @Length(message = "telephone must contain min 10 max  5O character.", min = 10, max = 50) String telephone ){
        return customerDetailService.setting(firstName, lastName,email,telephone);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return customerDetailService.delete(id);
    }



}
