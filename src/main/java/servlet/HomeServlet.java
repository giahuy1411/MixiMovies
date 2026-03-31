package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SeriesDAO;
import dao.SeriesDAOImpl;
import entity.Series;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final SeriesDAO seriesDao = new SeriesDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        List<Series> seriesList = seriesDao.findAllActive();
        req.setAttribute("seriesList", seriesList);
        req.getRequestDispatcher("/views/index.jsp").forward(req, resp);
    }
}