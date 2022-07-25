package hu.progmatic.spotilive.appconfig;

import hu.progmatic.spotilive.felhasznalo.UserType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/zenekarKarbantartas/**").hasAnyRole(UserType.Roles.ZENEKAR_KEZELES_ROLE)
                .antMatchers("/tag/**").hasAnyRole(UserType.Roles.TAG_KARBANTARTAS_ROLE)
                .antMatchers("/zene/**").hasAnyRole(UserType.Roles.ZENE_KEZELES_ROLE)
                .antMatchers("/felhasznalo/**").hasAnyRole(UserType.Roles.USER_CREATE_ROLE, UserType.Roles.NEWGUEST_CREATE_ROLE)
                .antMatchers("/**").authenticated()
                .and()
                .formLogin()
                .successForwardUrl("/")
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}