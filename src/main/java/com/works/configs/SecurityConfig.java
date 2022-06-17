package com.works.configs;

import com.works.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    final AuthenticationService authenticationService;
    final JWTFilter jwtFilter;

    public SecurityConfig(AuthenticationService authenticationService, JWTFilter jwtFilter) {
        this.authenticationService = authenticationService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(authenticationService.encoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers(getCustomerRole()).hasRole("customer")
                .antMatchers(getAdminRole()).hasRole("admin")
                .antMatchers(getCustomerAdmin_Role()).hasAnyRole("admin","customer")
                .antMatchers("/admin/register","/admin/auth").permitAll()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private String[] getCustomerRole(){
        String[] customerRole={"/customer/changePassword","/customer/setting",
                "/admin/forgotPassword",
                "/admin/resetPassword","/admin/auth",
                "/basket/save","/basket/delete","/basket/update",
                "/order/save","/order/delete","/order/customerOrder"};
        return customerRole;
    }
    private String[] getAdminRole(){
        String[] adminRole={"/category/save","/category/delete","/category/update",
                "/customer/delete","/admin/list","/admin/changeCustomerEnable",
                "/product/save","/product/delete","/product/update","/basket/list",
                "/order/list","/order/getOrder","/admin/changePassword","/admin/forgotPassword",
                "/admin/resetPassword","/admin/settings"};
        return adminRole;
    }

    private String[] getCustomerAdmin_Role(){
        String[] bothRole={
                "/category/list","/product/list","/product/searchByCategory",
                "/product/search"};
        return bothRole;

    }


}
