package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.VideoDAO;
import dao.VideoDAOImpl;
import entity.Video;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private VideoDAO videoDao = new VideoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Lấy danh sách video active
        List<Video> videos = videoDao.findAllActive();
        req.setAttribute("videos", videos);
        req.getRequestDispatcher("/views/index.jsp").forward(req, resp);
    }
}