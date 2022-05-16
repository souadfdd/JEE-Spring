package com.ensah.springsecurityperson.config;


import com.ensah.springsecurityperson.core.services.CustomAuthentificationService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;




@Configuration // Car cette classe contient des beans (annotées par @bean) qui seront crée
// automatiquement par Spring
@EnableWebSecurity // Car c'est notre classe de gestion de sécurité donc on active Spring Security
public class AppSecurityConfig extends WebSecurityConfigurerAdapter { // Il faut hériter de WebSecurityConfigurerAdapter
    //logger
    private Logger LOGGER = Logger.getLogger(getClass().getName());

    public AppSecurityConfig() {

        LOGGER.debug("AppSecurityConfig Initialisé");
    }
    // Authenitifcation Success Handler qui permet d'inidquer à Spring les
    // redirections
    // à faire après le succès de l'authentification
    @Bean
    public  AuthenticationSuccessHandler getAuthenticationSuccessHandler(){
        return new RedirectionAfterAuthenticationSuccessHandler();
    }
    @Bean
    public AuthenticationFailureHandler getCustomAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
    /**
     *
     * Nous avons choisi d'implémenter une gestion personnalisée de
     * l'authentification Ainsi, nous devons indiquer à Spring Security quelle est
     * notre gestionnaire d'authentification. Ce gestionnaire est
     * CustomAuthentificationService
     *
     */
    @Bean
    public CustomAuthentificationService getCustomAuthentificationService() {
        return new CustomAuthentificationService();
    }
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getCustomAuthentificationService());
        // Par défaut on utilise bcrypt
        // TODO : Vous pouvez changer l'algorithme si vous voulez
        authProvider.setPasswordEncoder( passwordEncoder());
        return authProvider;
    }
    // Configuration de l'algorithme de hashage des mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Par défaut on utilise bcrypt
        // TODO : Vous pouvez changer l'algorithme si vous voulez
         return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO : Configurer la securité de votre application
        http.authorizeHttpRequests()
                .antMatchers("/user/**").hasRole("USER")// Le role USER accèdent aux requete commençant
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN") // Le role ADMIN accèdent aux requete commençant par /admin/
        // TODO : Vous pouvez ajouter les configurations nécessaires si vous avez
        // d'autres rôles
                .and().formLogin().loginPage("/showMyLoginPage") // Indiquer le mapping affichant la page de login Form
                .loginProcessingUrl("/authenticateTheUser") // Meme valeur à mettre dans l'attribut action dans le form
                // de login. Ceci redirigera au bon filtre spring qui
                // s'occupe de l'authentification
                .failureHandler(getCustomAuthenticationFailureHandler())
                .successHandler(getAuthenticationSuccessHandler())

                .and().logout() // Configurer le logout
//		.logoutUrl("/perform_logout")   //Nous avons utiliser la valeur par défaut qui est /logout

                .deleteCookies("JSESSIONID") // effacer le cookie de session après deconnection
                .and().exceptionHandling().accessDeniedPage("/access-denied") // Indiquer le mapping que Spring
        // utilisera pour
        // rediriger à la page d'accès non
        // autorisé
        ;

    }





}
