package com.ensah.springsecurityperson.config;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * To customize the behavior of the Spring Security Framework in case of authentication failure, we can propose
 * an implementation of the AuthenticationFailureHandler interface.
 */

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //write your custom here
        if (exception instanceof DisabledException) {
            response.sendRedirect("showMyLoginPage?error=disabled");
            return;

        }

        else if (exception instanceof LockedException) {
            response.sendRedirect("showMyLoginPage?error=locked");
            return;

        } else if (exception instanceof CredentialsExpiredException) {
            response.sendRedirect("showMyLoginPage?error=expired");
            return;

        } else if (exception instanceof AccountExpiredException) {
            response.sendRedirect("showMyLoginPage?error=expired");
            return;

        } else {
            response.sendRedirect("showMyLoginPage?error=other");

        }

    }
}
