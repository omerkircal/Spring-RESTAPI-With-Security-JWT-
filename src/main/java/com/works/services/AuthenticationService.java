package com.works.services;

import com.works.configs.JWTUtil;
import com.works.entities.Admin;
import com.works.entities.Customer;
import com.works.entities.Login;
import com.works.entities.Roles;
import com.works.repositories.AdminRepository;
import com.works.repositories.CustomerRepository;
import com.works.utils.REnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class AuthenticationService implements UserDetailsService {
    final AdminRepository adminRepository;
    final CustomerRepository customerRepository;
    final AuthenticationManager authenticationManager;
    final JWTUtil jwtUtil;
    final HttpSession httpSession;

    public AuthenticationService(AdminRepository adminRepository, CustomerRepository customerRepository, @Lazy AuthenticationManager authenticationManager, JWTUtil jwtUtil, HttpSession httpSession) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.httpSession = httpSession;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> optionalAdmin = adminRepository.findByEmailEqualsIgnoreCase(username);
        Optional<Customer> optionalCustomer = customerRepository.findByEmailEqualsIgnoreCase(username);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            UserDetails userDetails = new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.isEnabled(),
                    admin.isTokenExpired(),
                    true,
                    true,
                    roles(admin.getRoles())
            );
            httpSession.setAttribute("admin",admin);
            return userDetails;
        }if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            UserDetails userDetails = new User(
                    customer.getEmail(),
                    customer.getPassword(),
                    customer.isEnabled(),
                    customer.isTokenExpired(),
                    true,
                    true,
                    roles(customer.getRoles())
            );
            httpSession.setAttribute("customer",customer);
            return userDetails;
        }
        else {
            throw new UsernameNotFoundException("No such user found.");
        }
    }
    public Collection roles(List<Roles> rolex) {
        List<GrantedAuthority> ls = new ArrayList<>();
        for (Roles roles : rolex) {
            ls.add(new SimpleGrantedAuthority(roles.getName()));
        }
        return ls;
    }
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public ResponseEntity auth(Login login) {
        Map<REnum,Object> hm = new LinkedHashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getUsername(),login.getPassword()
            ));
            UserDetails userDetails = loadUserByUsername(login.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            hm.put(REnum.status, true);
            hm.put(REnum.jwt, jwt);
            return new ResponseEntity(hm, HttpStatus.OK);
        }catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.error, ex.getMessage());
            return new ResponseEntity(hm,HttpStatus.NOT_ACCEPTABLE);
        }
    }
    public ResponseEntity info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<Customer> optionalCustomer = customerRepository.findByEmailEqualsIgnoreCase(userName);
        Map<REnum,Object> hm = new LinkedHashMap<>();
        if (optionalCustomer.isPresent()) {
            hm.put(REnum.status,true);
            hm.put(REnum.result,optionalCustomer.get());
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }else {
            hm.put(REnum.status,false);
            hm.put(REnum.message, "Kullanıcı bulunumadı");
            return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE   );
        }
    }

}
