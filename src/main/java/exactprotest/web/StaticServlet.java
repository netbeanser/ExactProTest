/**
 * @class ExactProServlet
 * @date Tue Jan 12 15:37 2018 MSK
 * @author dglunts
 * 
 * Description : StaticServlet
 */
package exactprotest.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(urlPatterns={"/exact"})
public class StaticServlet extends HttpServlet {
    
    private static final long serialVersionUID = 567L;
    
    private final static Logger logger =  Logger.getLogger(StaticServlet.class); 
        
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) 
    throws ServletException, IOException {
            processRequest(req,resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)
        throws ServletException, IOException{
         processRequest(req,resp);
    } 
    
    private void processRequest (HttpServletRequest req,HttpServletResponse resp)
        throws ServletException, IOException {      
        String reqUri = req.getRequestURI();
        logger.info("Request URI="+reqUri);
        if ((reqUri == null) || (reqUri.equals("/exact"))){
           resp.setContentType("text/html; charset=UTF-8");
           resp.sendRedirect(req.getContextPath()+"pages/index.jsp");
        }         


    }
        
}
