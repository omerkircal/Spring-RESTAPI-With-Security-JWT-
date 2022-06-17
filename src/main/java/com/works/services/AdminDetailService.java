package com.works.services;

import com.works.entities.Admin;
import com.works.entities.Customer;
import com.works.repositories.AdminRepository;
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

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class AdminDetailService {
    final AdminRepository adminRepository;
    final CustomerRepository customerRepository;
    final PasswordEncoder passwordEncoder;
    final JavaMailSender emailSender;
    final HttpSession session;

    public AdminDetailService(AdminRepository adminRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JavaMailSender emailSender, HttpSession session) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
        this.session = session;
    }


    public ResponseEntity register(Admin admin) {
        Optional<Admin> optionalJWTUser = adminRepository.findByEmailEqualsIgnoreCase(admin.getEmail());
        Map<REnum, Object> hm = new LinkedHashMap();
        if (!optionalJWTUser.isPresent()) {;
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            Admin adminNew = adminRepository.save(admin);
            hm.put(REnum.status, true);
            hm.put(REnum.result, adminNew);
            return new ResponseEntity(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "This e-mail is already registered");
            hm.put(REnum.result, admin);
            return new ResponseEntity(hm, HttpStatus.NOT_ACCEPTABLE);
        }


    }

    public ResponseEntity changePassword(String oldPassword, String newPassword) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<Admin> optionalCustomer=adminRepository.findByEmailEqualsIgnoreCase(userName);

        Admin admin = optionalCustomer.get();
        if (this.passwordEncoder.matches(oldPassword, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            Admin updatedAdmin = adminRepository.save(admin);
            hm.put(REnum.status, "true");
            hm.put(REnum.result, updatedAdmin);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } else {
            hm.put(REnum.message, "Please check again current password");
            hm.put(REnum.status, "false");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity forgotPassword(String email) {
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Admin> optionalAdmin = adminRepository.findByEmailEqualsIgnoreCase(email);
        Admin admin = optionalAdmin.get();
        if (optionalAdmin.isPresent()) {
            UUID uuid = UUID.randomUUID();
            String verifyCode = uuid.toString();
            admin.setVerificationCode(uuid.toString());
            adminRepository.save(admin);
            String resetPasswordLink = "http://localhost:8092/customer/resetPassword?resettoken=" + verifyCode;
            try {
                sendSimpleMessage("test.omerkircal@gmail.com", "Password Reset Link", resetPasswordLink);
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
        Optional<Admin> optionalAdmin = adminRepository.findByVerificationCodeEquals(verificationCode);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            admin.setPassword(passwordEncoder.encode(password));
            admin.setVerificationCode(null);
            adminRepository.save(admin);
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
            adminRepository.deleteById(id);
            hm.put(REnum.status, true);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        } catch (Exception exception) {
            hm.put(REnum.status, false);
            System.out.println(exception);
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity settings(String companyName, String firstName, String lastName, String email, String phone) {
        Map<REnum, Object> hm = new LinkedHashMap();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            Optional<Admin> optionalAdmin=adminRepository.findByEmailEqualsIgnoreCase(userName);
            Optional<Admin> admin = adminRepository.findByEmailEqualsIgnoreCase(email);
            if (optionalAdmin.isPresent() ) {
                Admin oldAdmin = optionalAdmin.get();
                if ((oldAdmin.getEmail().equals(email)) || !admin.isPresent()) {
                    //System.out.println(oldAdmin.getEmail());
                    oldAdmin.setCompanyName(companyName);
                    oldAdmin.setFirstName(firstName);
                    oldAdmin.setLastName(lastName);
                    oldAdmin.setEmail(email);
                    oldAdmin.setTelephone(phone);
                    adminRepository.saveAndFlush(oldAdmin);
                    hm.put(REnum.status, true);
                    hm.put(REnum.result, oldAdmin);
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


    public ResponseEntity changeEnableCustomer(Long id,boolean enable){
        Map<REnum, Object> hm = new LinkedHashMap();
        Optional<Customer> optionalCustomer=customerRepository.findById(id);
        if(optionalCustomer.isPresent()){
            Customer customer=optionalCustomer.get();
            customer.setEnabled(enable);
            customerRepository.save(customer);
            hm.put(REnum.status,true);
            hm.put(REnum.result,customer);
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }
        else {
            hm.put(REnum.status, false);
            hm.put(REnum.message,"Invalid customer id");
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity list() {
        Map<REnum, Object> hm = new HashMap<>();
        List<Customer> customerList = customerRepository.findAll();
        hm.put(REnum.status, true);
        hm.put(REnum.result, customerList);
        return new ResponseEntity<>(hm, HttpStatus.OK);

    }
}
