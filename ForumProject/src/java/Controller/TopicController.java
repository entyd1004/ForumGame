/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAL.PlayerDAO;
import DAL.PostsDAO;
import Model.player;
import Model.posts;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DPV
 */
@WebServlet(name = "TopicController", urlPatterns = {"/topic"})
public class TopicController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TopicController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TopicController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PostsDAO daoPosts = new PostsDAO();
        PlayerDAO daoPlayer = new PlayerDAO();

        String id = request.getParameter("id");
        if (id == null) {
            response.sendRedirect("home");
            return;
        }

        int page = 1;
        int page_size = 15;

        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        int countComment = daoPosts.countComment(id);
        int totalPage = countComment / page_size;
        if (countComment % page_size != 0) {
            totalPage += 1;
        }

        daoPosts.updateView(id);
        posts getposts = daoPosts.getposts(id);
        ArrayList<player> listplayer = daoPlayer.getListplayer();
        ArrayList<posts> listComment = daoPosts.getCommentWithPage(id, page, page_size);
        request.getSession().setAttribute("urlPrev", "topic?id=" + id + "&&page=" + page);
        
        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("getposts", getposts);
        request.setAttribute("listplayer", listplayer);
        request.setAttribute("listComment", listComment);
        request.getRequestDispatcher("topic.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PlayerDAO daoPlayer = new PlayerDAO();
        PostsDAO daoPosts = new PostsDAO();

        String topicID = request.getParameter("topicID");
        String userID = request.getParameter("userID");
        String timeCmt = request.getParameter("timeCmt");
        String content = request.getParameter("content");

        daoPosts.createComment(topicID, userID, content, timeCmt);
        daoPlayer.updatePosts(userID);

        response.sendRedirect("topic?id=" + topicID);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
