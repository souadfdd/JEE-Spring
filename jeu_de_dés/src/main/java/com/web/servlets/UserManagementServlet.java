package com.web.servlets;

import com.app.Message;
import com.app.Utilisateur;
import com.web.helpers.GameContextManagement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManagementServlet extends HttpServlet {

    private final Logger LOGGER = Logger.getLogger(String.valueOf(getClass()));


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String createUserFormPage = "/WEB-INF/vues/pages/formInscription.jsp";
        ServletContext cntx = getServletContext();
        // On affiche le formulaire d'ajout
        if (request.getParameter("create") != null) {
            LOGGER.debug("Redirection vers le formulaire de création des utilisateurs");
            cntx.getRequestDispatcher(createUserFormPage).forward(request, response);
            // fin
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorPage = "/WEB-INF/vues/pages/error.jsp";
        String okPage = "/WEB-INF/vues/pages/operationOK.jsp";
        String loginForm = "/WEB-INF/vues/pages/loginForm.jsp";
        ServletContext cntx = getServletContext();
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        GameContextManagement gameContext =GameContextManagement.getInstance(cntx);
        Utilisateur user = new Utilisateur(nom, prenom, login, password, 0, 0);
        List<Message> messages = new ArrayList<Message>();
        try{
            //test si l'utilisateur existe dans le contexe
            if(gameContext.getUserByLogin(login)!=null){
                messages.add(new Message("utilisateur exist",Message.WARN));
                request.setAttribute("messages", messages);
                //redirection n'arret execution
                cntx.getRequestDispatcher(loginForm).forward(request,response);
                return;
            }
            // On ajoute l'utilisateur
            gameContext.insertUser(user);


            // On redirige vers la page login avec un message de succès
            messages.add(new Message("Utilisateur correctement ajouté", Message.INFO));
            // On enregistre la liste des messages comme attributs de requete
            request.setAttribute("messages", messages);

            // On redirige vers la vue
            cntx.getRequestDispatcher(loginForm).forward(request, response);

            // Arret
            return;

        } catch (Exception ex) {
            LOGGER.error("Erreur à cause de :  ", ex);
            // En cas d'erreur on redirige vers la vue qui va afficher les erreurs
            messages.add(new Message("erreur est survenue",Message.ERROR));
            cntx.getRequestDispatcher(errorPage).forward(request, response);

            return;
        }





    }
}
