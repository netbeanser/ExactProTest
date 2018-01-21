/**
 * @class ExactProServlet
 * @date Tue Jan 12 15:37 2018 MSK
 * @author dglunts
 * 
 * Description : Servlet
 * 
 * 
 */
package exactprotest.web;

import exactprotest.config.ExactProConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.config.RequestConfig;


import java.io.IOException;
import org.apache.log4j.Logger;



    
    @WebServlet(urlPatterns={"/instrumentList","/rate"})
    public class ExactProServlet extends HttpServlet {
        
        private static final long serialVersionUID = 345L;
        
        private final static Logger logger =  Logger.getLogger(ExactProServlet.class); 
        
        @Override
        public void doPost(HttpServletRequest req,HttpServletResponse resp) 
        throws ServletException, IOException {
            processRequest(req,resp);
        }
    
        @Override
        public void doGet(HttpServletRequest req,HttpServletResponse resp)
        throws ServletException, IOException{
            processRequest(req,resp);
        } 
    
        private void processRequest (HttpServletRequest req,HttpServletResponse resp)
        throws ServletException, IOException {      
                
            String respBody = "";
            
            resp.setContentType("application/json");
            resp.setHeader("Content-Encoding", "utf8");
//!!!!!!!!            resp.setHeader("Access-Control-Allow-Origin", "*");
            PrintWriter out = resp.getWriter();               
            /*
            Берем из конфига, который мы ранее в ExactProContextListener сложили 
            в ServletContext, url списка инструментов, url котировок и проч.
            */
            int connectTimeout = ((ExactProConfig)
                req
                .getServletContext()
                .getAttribute("config"))
                .getConnectTimeout();  
                        
            String instrumentListUrl = ((ExactProConfig)
                req
                .getServletContext()
                .getAttribute("config"))
                .getInstrumentListUrl();            
            
            String rateUrl = ((ExactProConfig)
                req
                .getServletContext()
                .getAttribute("config"))
                .getRateUrl();                        
            //Таймауты
            RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(connectTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .build();
                               
            if ("/instrumentList".equals(req.getRequestURI())) {                                
                
                HttpGet httpGet = new HttpGet(instrumentListUrl);
                    
                httpGet.setConfig(requestConfig);                         
                /*
                Здесь опять же используется try-with-resource
                Т.к. CloseableHttpClient исполняет Closeable, то блок finally не нужен.
                Хотя рекомендуется.
                Как показывает debug output,
                дефолтный PoolingHttpClientConnectionManager освобождает ресурсы сам
                */
                try (CloseableHttpClient httpClient = HttpClients.createDefault()){
                //RetryHandler 
                //KeepAliveStrategy
                //CompletionService
                    respBody = httpClient.execute(httpGet, new ExactProResponseHandler());                                     
                    out.println(respBody);                        
                    out.flush();
                    out.close();
                } catch (Exception e){
                    logger.error("Exception occured: "+e);
                }

            } else {
                if ("/rate".equals(req.getRequestURI())) {
                    if (null == req.getParameterValues("id")) {
                        logger.error("Invalid Request:QueryString="+req.getQueryString());
                        out.println(respBody);

                    } else {
                        String id = req.getParameterValues("id")[0];
                        HttpGet httpGet = new HttpGet(rateUrl+id);                    
                        httpGet.setConfig(requestConfig);                                 
                
                        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
                                        
                        respBody = httpClient.execute(httpGet, new ExactProResponseHandler());                                     
                        out.println(respBody);                        
                        out.flush();
                        out.close();
                        
                        } catch (Exception e){
                            logger.error("Exception occured: "+e);
                        }                        
                    }
                    
                }
            }
    }    

}
    

