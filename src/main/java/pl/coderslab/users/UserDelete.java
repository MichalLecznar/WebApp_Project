package pl.coderslab.users;

import pl.coderslab.utils.User;
import pl.coderslab.utils.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user/delete")
public class UserDelete extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        UserDao userDao = new UserDao();
        User userDelete = userDao.read(Integer.parseInt(id));
        request.setAttribute("user", userDelete);
        getServletContext().getRequestDispatcher("/users/delete.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("yes") != null){
            String id = request.getParameter("id");
            UserDao userDao = new UserDao();
            userDao.delete(Integer.parseInt(id));
            response.sendRedirect("/user/list");
        }
        if(request.getParameter("no") != null){
            response.sendRedirect("/user/list");
        }
    }
}
   