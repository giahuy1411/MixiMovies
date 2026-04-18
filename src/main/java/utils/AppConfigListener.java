package utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        java.util.Properties env = new java.util.Properties();
        try (java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("env.properties")) {
            if (is != null) {
                env.load(is);
                sce.getServletContext().setAttribute("sepAccountName", env.getProperty("SEP_ACCOUNT_NAME", ""));
            }
        } catch (Exception e) {
            sce.getServletContext().setAttribute("sepAccountName", "");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}