package in.gaurav.Servelet;

import in.gaurav.model.User;
import in.gaurav.Service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        Integer id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        if (id == null || email == null ||
                name == null || mobile == null) {
            response.setStatus(400);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\n" +
                            "    \"message\" : \"Some fields are missing\"\n" +
                            "}"
            );
        }

        User user = new User(id, name, email, mobile);

        User createdUser = userService.createUser(user);

        response.setStatus(201);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\n" +
                        "    \"message\" : \"User Added successfully\"\n" +
                        "}"
        );
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        String idParam = request.getParameter("id");

        if(idParam == null) {
            List<User> users = userService.getAllUsers();
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().write(usersToJson(users));
            return;
        }
        Integer id = Integer.parseInt(idParam);

        User userResp = userService.getUserById(id);

        if(userResp == null) {
            response.setStatus(404);
            response.setContentType("application/json");
        }

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write(userToJson(userResp));

    }

    @Override
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.setStatus(400);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{ \"message\" : \"Id is required\" }"
            );
            return;
        }

        Integer id = Integer.parseInt(idParam);
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        User user = new User(id, name, email, mobile);

        userService.UpdateUser(user);

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"message\" : \"User Updated successfully\" }"
        );
    }

    @Override
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().println("{\"message\":\"Id is required\"}");
            return;
        }

        Integer id = Integer.parseInt(idParam);

        User userResp = userService.Deleteuser(id);

        if (userResp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"message\":\"User not found\"}");
            return;
        }

        response.setContentType("application/json");
        response.getWriter().println("{\"message\":\"User deleted successfully\"}");

    }


    private String userToJson(User user) {


        return "{\n" +
                "    \"id\" : " + user.getId() + ",\n" +
                "    \"name\" : " + user.getName() + ",\n" +
                "    \"email\" : " + user.getEmail() + ",\n" +
                "    \"mobile\" : " + user.getMobile() + "\n" +
                "}";
    }

    private String usersToJson(List<User> users) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for(int i = 0; i<users.size(); i++) {
            stringBuilder.append(userToJson(users.get(i)));

            if(i < users.size() - 1) {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}








