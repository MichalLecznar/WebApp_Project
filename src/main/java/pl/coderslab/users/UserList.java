package pl.coderslab.users;

import pl.coderslab.utils.DbUtil;
import pl.coderslab.utils.User;
import pl.coderslab.utils.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/user/list")
public class UserList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();

        UserDao userDao = new UserDao();

//        User [] users = userDao.findAll();
//        System.out.println("lista.length: "+users.length);
//        System.out.println(Arrays.toString(users));
//        User user1 = new User("imie", "email", "pass");
//        List<User> usersList = new ArrayList<User>(Arrays.asList(users));
        request.setAttribute("users", userDao.findAll());


        getServletContext().getRequestDispatcher("/users/list.jsp")
                .forward(request, response);

    }
}
   