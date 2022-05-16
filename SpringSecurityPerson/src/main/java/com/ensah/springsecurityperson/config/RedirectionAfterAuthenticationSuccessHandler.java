package com.ensah.springsecurityperson.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * To customize the behavior of the Spring Security Framework after successful authentication,*
 * we can provide an implementation of the AuthenticationSuccessHandler interface:
 */
public class RedirectionAfterAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    // Logger
    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl="";
        Map<String,String> roleTarhetUrlMap=new HashMap<>();
        roleTarhetUrlMap.put("ROLE_USER", "/user/showUserHome"); // L'utilisateur de type USER sera rediriger vers "/user/showUserHome
        roleTarhetUrlMap.put("ROLE_ADMIN", "/admin/showAdminHome"); // L'utilisateur de type USER sera rediriger vers
        // "/admin/showAdminHome"

        // On récupère les rôles de l'utilisateur, et en fonction
        // de son rôle on détérmine l'action à executer parmis les actions déclarées
        // dans roleTargetUrlMap
        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        boolean found=false;
        for(GrantedAuthority grantedAuthority:authorities){
            String authorityName=grantedAuthority.getAuthority();
            //si touve
            if(roleTarhetUrlMap.containsKey(authorityName)){
                targetUrl = roleTarhetUrlMap.get(authorityName);
                found = true;
                break;
            }
        }
        // Si le rôle de l'utilisateur est introuvable dans le dictionnaire
        // roleTargetUrlMap
        if (!found)

        {
            throw new IllegalStateException();

        }

        // redirection vers la page d'acceuil

        if (response.isCommitted()) {
            LOGGER.debug("Impossible de rediriger car la réponse a été déjà envoyé " + targetUrl);
            return;
        }

        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        redirectStrategy.sendRedirect(request, response, targetUrl);

    }
}
