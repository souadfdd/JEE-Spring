package com.ensah.springsecurityperson.core.services;

import com.ensah.springsecurityperson.core.bo.UserAccount;
import com.ensah.springsecurityperson.core.bo.UserPrincipal;
import com.ensah.springsecurityperson.core.dao.IUserAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.apache.log4j.Logger;

import java.util.Date;

/**
 * To customize the User Details Service, we proposed a new class that implements
 * the UserDetailsService interface of Spring Security, this interface has only one method:
 *
 * UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
 *
 * This method checks if a user account with the username passed in parameter exists or not. If it exists,
 * it returns it otherwise; it triggers an exception of type UsernameNotFoundException.
 * The method also triggers this exception if the account has no role.
 */

public class CustomAuthentificationService implements UserDetailsService {
    @Autowired
    private IUserAccountDao userRepository;//le dao gere lees utilisateurs
    /**utilise poir la journalisation**/
    private Logger LOGGER= Logger.getLogger(getClass().getName());

    public CustomAuthentificationService() {
    }

    // Implémentation de la méthode de vérification de l'existence d'un compte
    // utilisateur

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user =null;
        // On récupère le compte avec son username
        user =userRepository.getUserByUsername(username);
        if(user==null){
            String msg="Utilisateur " + username + " introuvable ";
            //on ecrit dans le journal un message de warning
            LOGGER.warn(msg);
            // Cette exception informe Spring Security que l'utilisateur n'existe pas
            // et donc il n a pas le droit de se connecter
            throw new UsernameNotFoundException(msg);

        }
        if (user.getRole() == null) {
            String msg = "Utilisateur " + username + " n'ayant aucune autorisation ";

            // On écrit dans le journal un message de warning
            LOGGER.warn(msg);

            // Cette exception informe Spring Security que l'utilisateur n'a aucune
            // autorisation
            throw new UsernameNotFoundException(msg);
        }


        user.setLastAccessDate(new Date());

        // Embaler l'objet de type UserAccount dans un objet de type UserPrincipal qui
        // lui même
        // implémente UserDetails
        return new UserPrincipal(user);
    }
}
