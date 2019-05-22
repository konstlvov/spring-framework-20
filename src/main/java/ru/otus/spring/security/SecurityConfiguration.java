package ru.otus.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // По умолчанию SecurityContext хранится в сессии
                // Это необходимо, чтобы он нигде не хранился
                // и данные приходили каждый раз с запросом
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/public").permitAll()
                .and()
                .authorizeRequests().antMatchers("/authenticated", "/success").authenticated()
                // Включает Form-based аутентификацию
                .and()
                .formLogin().successForwardUrl("/success") // надо ставить @PostMapping в контроллере, а то не заработает
                // Включает HTTP-basic
                //.and()
                //.httpBasic()
                //.and()
                // Включает анонимную аутнетифкацию
                // Здесь небольшая ошибка
                //.anonymous().principal("anonymous")
                //.and()
                // Включает Remember-me аутентифкацию
                //.rememberMe().key("someSecret")
        ;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("password").roles("ADMIN");
    }
}
