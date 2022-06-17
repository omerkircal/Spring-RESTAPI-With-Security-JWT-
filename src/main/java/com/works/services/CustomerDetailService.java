package com.works.services;

import com.works.entities.Customer;
import com.works.repositories.CustomerRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Transactional
@Service
public class CustomerDetailService {
    final CustomerRepository customerRepository;
    final PasswordEncoder passwordEncoder;
    final JavaMailSender emailSender;
    final AuthenticationService authenticationService;

    public CustomerDetailService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JavaMailSender emailSender, AuthenticationService authenticationService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
        this.authenticationService = authenticationService;
    }


    public ResponseEntity register(Customer customer) {
        Optional<Customer> optionalJWTUser = customerRepository.findByEmailEqualsIgnoreCase(customer.getEmail());
        Map<REnum, Object> hm = new LinkedHashMap();
        if (!optionalJWTUser.isPresent()) {;
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            Customer customerNew = customerRepository.save(customer);
            hm.put(REnum.status, true);
            hm.put(REnum.result, customerNew);
            return new ResponseEntity(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "This e-mail is already registered");
            hm.put(REnum.result, customer);
            return new ResponseEntity(hm, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public ResponseEntity changePassword(String oldPassword, String newPassword) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<Customer> optionalCustomer=customerRepository.findByEmailEqualsIgnoreCase(userName);

        Customer customer = optionalCustomer.get();
        if (this.passwordEncoder.matches(oldPassword, customer.getPassword())) {
            customer.setPassword(passwordEncoder.encode(newPassword));
            Customer updatedCustomer = customerRepository.save(customer);
            hm.put(REnum.status, "true");
            hm.put(REnum.result, updatedCustomer);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.message, "Please check again current password");
            hm.put(REnum.status, "false");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity forgotPassword(String email) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer = customerRepository.findByEmailEqualsIgnoreCase(email);
        Customer customer = optionalCustomer.get();
        if (optionalCustomer.isPresent()) {
            UUID uuid = UUID.randomUUID();
            String verifyCode = uuid.toString();
            customer.setVerificationCode(uuid.toString());
            customerRepository.save(customer);
            String resetPasswordLink = "http://localhost:8092/customer/resetPassword?resettoken=" + verifyCode;
            try {
                sendSimpleMessage("omerkircal@outlook.com", "Password Reset Link", resetPasswordLink);
                hm.put(REnum.status, "true");
                hm.put(REnum.result, resetPasswordLink);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            } catch (Exception exception) {
                System.out.println("mail Error" + exception);
                hm.put(REnum.status, false);
                hm.put(REnum.error, exception);
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        } else {
            hm.put(REnum.status, "false");
            hm.put(REnum.status, "Invalid customer id");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity resetPassword(String verificationCode,  String password) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer = customerRepository.findByVerificationCodeEquals(verificationCode);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setPassword(passwordEncoder.encode(password));
            customer.setVerificationCode(null);
            customerRepository.save(customer);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);

        } else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "Invalid verification code");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("javaproject.sendmail@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    public ResponseEntity delete(Long id) {
        Map<REnum, Object> hm = new LinkedHashMap();
        try {
            customerRepository.deleteById(id);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } catch (Exception exception) {
            hm.put(REnum.status, false);
            System.out.println(exception);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity setting( String firstName, String lastName, String email, String telephone) {
        Map<REnum, Object> hm = new LinkedHashMap();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            Optional<Customer> optionalCustomer1=customerRepository.findByEmailEqualsIgnoreCase(userName);
            Optional<Customer> customer1 = customerRepository.findByEmailEqualsIgnoreCase(email);
            if (optionalCustomer1.isPresent()) {
                Customer oldCustomer = optionalCustomer1.get();
                if ((oldCustomer.getEmail().equals(email)) || !customer1.isPresent()) {
                    oldCustomer.setFirstName(firstName);
                    oldCustomer.setLastName(lastName);
                    oldCustomer.setEmail(email);
                    oldCustomer.setTelephone(telephone);
                    customerRepository.saveAndFlush(oldCustomer);
                    hm.put(REnum.status, true);
                    hm.put(REnum.result, oldCustomer);
                    return new ResponseEntity<>(hm, HttpStatus.OK);
                } else {
                    hm.put(REnum.status, false);
                    hm.put(REnum.message, "This email already registered");
                    return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
                }

            } else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "Invalid customer id");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            hm.put(REnum.status, false);
            System.out.println(exception);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }

    }


}
