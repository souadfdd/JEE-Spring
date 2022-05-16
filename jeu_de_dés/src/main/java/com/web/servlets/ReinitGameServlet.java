package com.web.servlets;

import com.app.GameState;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;

public class ReinitGameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        GameState gs = (GameState) session.getAttribute("gameState");

        if (gs != null) {

            gs.reinit();
        }

        // clear la session
        HashMap<String,Integer> list= (HashMap<String, Integer>) session.getAttribute(gs.getUser().getLogin());
         list.clear();
        getServletContext().getRequestDispatcher("/WEB-INF/vues/back/userHome.jsp").forward(request, response);

        return;

    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
