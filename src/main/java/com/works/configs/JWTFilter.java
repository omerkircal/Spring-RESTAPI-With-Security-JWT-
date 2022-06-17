package com.works.configs;

import com.works.services.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    final AuthenticationService authenticationService;
    final JWTUtil jwtUtil;
    public JWTFilter(AuthenticationService authenticationService, JWTUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String usernameAdmin = null;
        String usernameCustomer = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            usernameAdmin = jwtUtil.extractUsername(jwt);
            usernameCustomer=jwtUtil.extractUsername(jwt);
        }
        if (usernameAdmin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetailsAdmin = authenticationService.loadUserByUsername(usernameAdmin);
            if (jwtUtil.validateToken(jwt, userDetailsAdmin)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetailsAdmin, null, userDetailsAdmin.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }else if(usernameCustomer != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetailsCustomer = authenticationService.loadUserByUsername(usernameCustomer);
            if (jwtUtil.validateToken(jwt, userDetailsCustomer)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetailsCustomer, null, userDetailsCustomer.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
