package com.web.servlets;

import com.app.GameState;
import com.app.Message;
import com.app.Utilisateur;
import com.web.helpers.GameContextManagement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginServlet extends HttpServlet {
    private final Logger LOGGER= Logger.getLogger(String.valueOf(getClass()));
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // On récupére les données envoyées dans le formulaire
        String login=request.getParameter("login");
        String password=request.getParameter("password");
        // On instancie la liste que nous utiliserons pour stocker les messages ,on traite ls messages pour une seule requet pour ca on a cree une list dans doPOST
        // à passer à la vue
        List<Message> messages = new ArrayList<Message>();
        GameContextManagement gameContext=GameContextManagement.getInstance(getServletContext());
        // On recherche l'utilisateur par login
        Utilisateur user=gameContext.getUserByLogin(login);
        // Si un utilisateur existe
        if(user!=null){
            // On vérifie que les mots de passe sont identiques
            if(user.getPassword() != null && user.getPassword().equals(password)) {
                // On stocke l'objet stockant l'état de jeu dans la session
                GameState gameSate = new GameState(user);
                request.getSession().setAttribute("gameState", gameSate);
                // On stocke l'utilisateur authentifié dans la session
                request.getSession().setAttribute("user", user);
                // On envoie une vue qu'est la page home comme résultat
                getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);
                // Fin
                return;
            }
                else{
                    messages.add(new Message("Login ou mot de passe incorrect",Message.WARN));
                    request.setAttribute("messages",messages);
                // on affiche le formulaire d'authentification avec des
                // messages d'erreur
                getServletContext().getRequestDispatcher("/WEB-INF/vues/pages/loginForm.jsp").forward(request, response);

                return;
            }
                }
        else{
            messages.add(new Message("Login ou mot de passe incorrect", Message.WARN));

            request.setAttribute("messages", messages);

            // de meme si l'utilisateur est introuvable avec une recherche par
            // login
            getServletContext().getRequestDispatcher("/WEB-INF/vues/pages/loginForm.jsp").forward(request, response);

            // Fin
            return;

            }

    }
}
