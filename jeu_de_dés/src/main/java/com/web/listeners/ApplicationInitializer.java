package com.web.listeners;

import com.app.Utilisateur;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Application Lifecycle Listener implementation class ApplicationInitializer
 *
 */
@WebListener
public class ApplicationInitializer implements ServletContextListener {

    /**
     * Default constructor.
     */
    public ApplicationInitializer() {

    }

     public void contextInitialized(ServletContextEvent sce) {
        // contextInitialized is invoked when application is deployed on the server.

     	ServletContext ctx = sce.getServletContext();
         //The synchronizedList() method of java.util.Collections class is used to return a synchronized (thread-safe) list backed by the specified list
    	List<Utilisateur>  userList = Collections.synchronizedList(new ArrayList<Utilisateur>());

    	ctx.setAttribute("users", userList);

     }

	
}
