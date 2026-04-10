package servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import entity.User;
import utils.Mailer;
import utils.PasswordUtil;
import utils.XJPA;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/views/register.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");

        EntityManager em = XJPA.getEntityManager();

        try {
            // Check trùng username
            TypedQuery<Long> q1 =
                em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.id = :id",
                    Long.class);

            q1.setParameter("id", id);

            if (q1.getSingleResult() > 0) {
                req.setAttribute("error", "Tên đăng nhập đã tồn tại!");
                req.getRequestDispatcher("/views/register.jsp")
                   .forward(req, resp);
                return;
            }

            // Check trùng email
            TypedQuery<Long> q2 =
                em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.email = :email",
                    Long.class);

            q2.setParameter("email", email);

            if (q2.getSingleResult() > 0) {
                req.setAttribute("error", "Email đã được sử dụng!");
                req.getRequestDispatcher("/views/register.jsp")
                   .forward(req, resp);
                return;
            }

            // Tạo user
            User user = new User();
            user.setId(id);
            user.setPassword(PasswordUtil.hash(password));
            user.setEmail(email);
            user.setFullname(fullname);
            user.setAdmin(false);

            // Lưu DB
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            // Gửi mail (sau khi commit thành công)
            try {
                String subject = "🎉 Chào mừng bạn đến với MixiMovies";
                String body = "Xin chào " + fullname + ",\n\n"
                        + "Cảm ơn bạn đã đăng ký tài khoản tại MixiMovies.\n"
                        + "Tên đăng nhập của bạn: " + id + "\n\n"
                        + "Chúc bạn xem phim vui vẻ!\n\n"
                        + "— MixiMovies Team";

                Mailer.send(
                        "miximovies.contact@gmail.com",
                        email,
                        subject,
                        body
                );
            } catch (Exception mailEx) {
                System.out.println("⚠️ Gửi mail thất bại: "
                        + mailEx.getMessage());
            }

            resp.sendRedirect(req.getContextPath() + "/login");

        } catch (Exception e) {

            // Chỉ rollback khi transaction đang chạy
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new ServletException(e);

        } finally {
            em.close(); // Luôn đóng
        }
    }
}