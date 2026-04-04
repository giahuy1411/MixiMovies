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
    private final dao.CategoryDAO categoryDAO = new dao.CategoryDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String cidStr = req.getParameter("cid");
        List<Series> seriesList;
        entity.Category currentCategory = null;

        if (cidStr != null && !cidStr.isEmpty()) {
            Long cid = Long.parseLong(cidStr);
            currentCategory = categoryDAO.findById(cid);
            String catName = currentCategory != null ? currentCategory.getName() : "";
            seriesList = seriesDao.findByCategory(cid, catName, 1, 100); // Fetch first 100 for now
        } else {
            seriesList = seriesDao.findAllActive();
        }

        List<entity.Category> categoryList = categoryDAO.findAllActive();

        req.setAttribute("seriesList", seriesList);
        req.setAttribute("categoryList", categoryList);
        req.setAttribute("currentCategory", currentCategory);
        req.getRequestDispatcher("/views/index.jsp").forward(req, resp);
    }
}