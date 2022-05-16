package com.web.servlets;

import com.app.Utilisateur;
import com.web.helpers.GameContextManagement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class BestScoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GameContextManagement gameContext = GameContextManagement.getInstance(getServletContext());
        // On récupére tous les utilisateurs
        List<Utilisateur> users=gameContext.getAllUsers();

        // On stocke dans la requete (comme attribut) les utilisateurs. Cette
        // liste a une durée de vie = à la durée de vie de la requete. Donc elle
        // n'existera plus à la fin du cycle de vie de la requete
        request.setAttribute("users", users);

        // On redirige vers la vue (redirection coté serveur)
        getServletContext().getRequestDispatcher("/WEB-INF/vues/back/bestScore.jsp").forward(request, response);



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
