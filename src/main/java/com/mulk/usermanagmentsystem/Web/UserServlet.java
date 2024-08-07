package com.mulk.usermanagmentsystem.Web;

import com.mulk.usermanagmentsystem.Dao.UserDao;
import com.mulk.usermanagmentsystem.Model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/")
public class UserServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UserServlet.class);

    private UserDao dao;

    public UserServlet(){
        this.dao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error handling action: " + action, e);
            throw new ServletException(e);
        }
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String country = request.getParameter("country");

            User theUser = new User(fullName, email, country);
            dao.registerUSer(theUser);
            response.sendRedirect("list");
            logger.info("User inserted successfully: " + theUser);
        } catch (Exception e) {
            logger.error("Error inserting user", e);
            throw new ServletException(e);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            User theUser = new User(userId);
            dao.deleteUser(theUser);
            response.sendRedirect("list");
            logger.info("User deleted successfully with ID: " + userId);
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            throw new ServletException(e);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User theUser = new User(userId);
            User existingUser = dao.findUserById(theUser);
            RequestDispatcher dispatcher = request.getRequestDispatcher("user_form.jsp");
            request.setAttribute("user", existingUser);
            dispatcher.forward(request, response);
            logger.info("Showing edit form for user ID: " + userId);
        } catch (Exception e) {
            logger.error("Error showing edit form", e);
            throw new ServletException(e);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String country = request.getParameter("country");

            User theUser = new User(userId, fullName, email, country);
            dao.updateUser(theUser);
            response.sendRedirect("list");
            logger.info("User updated successfully: " + theUser);
        } catch (Exception e) {
            logger.error("Error updating user", e);
            throw new ServletException(e);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<User> userList = dao.retrieveAllUser();
            request.setAttribute("listuser", userList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("user_list.jsp");
            dispatcher.forward(request, response);
            logger.info("Listing all users");
        } catch (Exception e) {
            logger.error("Error listing users", e);
            throw new ServletException(e);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher("user_form.jsp");
            dispatcher.forward(request, response);
            logger.info("Showing new user form");
        } catch (Exception e) {
            logger.error("Error showing new user form", e);
            throw new ServletException(e);
        }
    }
}
