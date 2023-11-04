//package de.marshal.bankapp.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
////@Configuration // необязательна, так как включена в @EnableWebSecurity
//@EnableWebSecurity
//public class WebSecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults());
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    //  в тесте аннотация -> @WithMockUser  -  позволяет создать фиктивного (мок)
//    //  аутентифицированного пользователя в контексте теста. Она обычно используется
//    //  для предоставления пользователя, когда вы хотите протестировать методы,
//    //  требующие аутентификации.
//
//    //  вместо прописывания методов в коде на user и password - (  .with(hhtpBasic("user","password"))
//
//    // +  .with(csrf()) ->  включаете защиту от подделки межсайтовых запросов (CSRF - Cross-Site Request Forgery).
//}
