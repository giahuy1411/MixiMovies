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
        
        // 1. Xử lý tham số trang p
        int currentPage = 1;
        String pStr = req.getParameter("p");
        if (pStr != null && !pStr.isEmpty()) {
            try {
                currentPage = Math.max(1, Integer.parseInt(pStr));
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int pageSize = 4; // Giảm xuống 4 phim/trang để dễ dàng kiểm tra phân trang
        String cidStr = req.getParameter("cid");
        List<Series> seriesList;
        entity.Category currentCategory = null;
        long totalItems = 0;

        if (cidStr != null && !cidStr.isEmpty()) {
            try {
                Long cid = Long.parseLong(cidStr);
                currentCategory = categoryDAO.findById(cid);
                String catName = currentCategory != null ? currentCategory.getName() : "";
                
                totalItems = seriesDao.countByCategory(cid, catName);
                seriesList = seriesDao.findByCategory(cid, catName, currentPage, pageSize);
            } catch (NumberFormatException e) {
                totalItems = seriesDao.countActive();
                seriesList = seriesDao.findAllActive(currentPage, pageSize);
            }
        } else {
            totalItems = seriesDao.countActive();
            seriesList = seriesDao.findAllActive(currentPage, pageSize);
        }

        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        List<entity.Category> categoryList = categoryDAO.findAllActive();

        req.setAttribute("seriesList", seriesList);
        req.setAttribute("categoryList", categoryList);
        req.setAttribute("currentCategory", currentCategory);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalItems", totalItems);

        req.getRequestDispatcher("/views/index.jsp").forward(req, resp);
    }
}