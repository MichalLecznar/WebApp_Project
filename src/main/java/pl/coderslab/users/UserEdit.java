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

@WebServlet("/user/edit")
public class UserEdit extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // pobieramy id użytkownika i wysyłamy żądanie żeby formularz wypełnił sie danymi tego właśnie użytkownika
        String id = request.getParameter("id");
        UserDao userDao = new UserDao();
        User userRead = userDao.read(Integer.parseInt(id));
        request.setAttribute("user", userRead);
        getServletContext().getRequestDispatcher("/users/edit.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //odbieramy wartości wpisane przez użytkownika tak jak w metodzie tworzenia nowego jednak na końcu ustawiamy
        //metode update żeby nadpisac istniejące dane
        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("id")));
        user.setUserName(request.getParameter("userName"));
        user.setEmail(request.getParameter("mail"));
        user.setPassword(request.getParameter("password"));
        UserDao userDao = new UserDao();
        userDao.update(user);
        response.sendRedirect("/user/list");
    }
}
   