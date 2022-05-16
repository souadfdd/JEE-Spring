package com.web.servlets;

import com.app.GameState;
import com.app.Message;
import com.app.Utilisateur;
import com.web.helpers.GameContextManagement;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;


public class GameServlet extends HttpServlet {


    /**
     * Méthode permettant à un utilisateur de jouer
     */
    protected void play(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // On récupére la session de l'utilisateur en cours
        HttpSession session = request.getSession();
        // On récupére de la session, les informations du joueur en cours
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        // Pour gérer les données de l'application dans le ServletContext
        GameContextManagement gameContext = GameContextManagement.getInstance(getServletContext());
        // Cette objet déjà inséré dans la session au moment de login
        GameState gameState = (GameState) session.getAttribute("gameState");
        //recupere le numero de des
        String cle = request.getParameter("cle");
        // Lancer un dé (générer un nombre aléatoire dans l'intervalle 1,6)
        Random rand = new Random();
        int resultat = rand.nextInt(6) + 1;

        // recupere la session lie au des
        HashMap<String, Integer> list = (HashMap<String, Integer>) session.getAttribute(user.getLogin());

        //verifie le champ et redirege en cas erreur
        if (cle == null || !Arrays.asList("1", "2", "3").contains(request.getParameter("cle"))) {
            if (!gameState.isGameOver()) {
                // redirect to JSP
                gameState.addMessage(new Message("il faut choisir un valeur entre 1 et 3", Message.WARN));
                getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);
                // and stop execution of the servlet method
                return;

            }

        }
        if (!gameState.isGameOver()) {
            //verifie si le nombre de lancement attient 3
            if (user.getCompteurLancer() == 3) {
                // On ajoute un message d'information
                gameState.addMessage(new Message("Game Over", Message.INFO));
                // On vérifie s'il faut mettre à jour le meilleur score pour ce joueur

                if (list.get("1") < list.get("2") && list.get("2") < list.get("3") ) {
                    // Si oui alors mise à jour des données dans la session
                    user.setScore(list.get("1") + list.get("2") + list.get("3"));
                } else {
                    user.setScore(0);
                    gameContext.updateScore(user);
                }
                // On vérifie s'il faut mettre à jour le meilleur score pour ce joueur
                if (user.getScore() > user.getBestScore()) {
                    user.setBestScore(user.getScore());
                    // Si oui alors mise à jour des données dans la session
                    gameContext.updateScore(user);
                    System.out.println(user);

                }
                // On indique que le jeu est terminé
                gameState.setGameOver(true);
                // On redirige vers la vue par une redirection au niveau du serveur
                getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);

                // On arrete l'execution
                return;

            }
            //gestion de session
            if (list != null) {
                if (list.containsKey(cle)) {
                    //on ajout un message de warning
                    gameState.addMessage(new Message("le dés est deja choisi", Message.WARN));
                    // On ajoute un message d'information
                    gameState.addMessage(new Message("Game Over", Message.INFO));
                    // On indique que le jeu est terminé
                    user.setScore(-1);
                    gameState.setGameOver(true);
                    getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);
                    return;
                }
                list.put(cle, resultat);

            }

            else {
                list = new HashMap<String, Integer>();
                // ajoute le numero de des avec la valeur de
                list.put(cle, resultat);
                // put the list in the session
                session.setAttribute(user.getLogin(), list);
            }
            // on incrémente le nombre de lancer
            user.incrementLance();
            //verifie un condition d'arret du jeu
            if (resultat == 6 && cle.equals("1")) {
                user.setScore(0);
                // On ajoute un message d'information
                gameState.addMessage(new Message("Vous avez gagné 6 pour le dés numero 1", Message.INFO));
                // On ajoute un message d'information
                gameState.addMessage(new Message("Game Over", Message.INFO));
                // On indique que le jeu est terminé
                gameState.setGameOver(true);
                getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);
                return;
            }
            // On ajoute un message indiquant le résultat du lancé
            gameState.addMessage(new Message("Vous avez eu pour le dés numero " + cle + " un valeur" + String.valueOf(resultat), Message.INFO));
            getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);

        }
        getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);


    }




    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        play(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        play(request, response);

    }
}
