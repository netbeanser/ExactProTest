/**
 * @class ExactProContextListener
 * @author dglunts
 * @date Tue Jan 12 15:54 2018 MSK
 * 
 * Description: Intercepts the startup of the application and
 * initializes configuration stuff
 */
package exactprotest.web;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import exactprotest.execs.ExactProScheduledPool;
import exactprotest.config.ExactProConfig;
import org.apache.log4j.Logger;

@WebListener
public class ExactProContextListener implements ServletContextListener{
    
    private static final Logger logger =  Logger.getLogger(ExactProContextListener.class);    
    /*Выкидываем исключения, которые образовались в других классах именно здесь,
    чтобы, в случае ошибки, приложение wouldn't be deployed.
    Для этого нужно выкидывать RuntimeException.
    */    
    
    @Override
    public void contextInitialized(ServletContextEvent evt){  
        try {
            this.getClass().getClassLoader().loadClass("exactprotest.config.ExactProConfig");
            logger.info("Config: "+ExactProConfig.getConfig());
        } catch (ClassNotFoundException cnf){
            //Fond!
        }
//        logg
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent evt){
        ExactProScheduledPool.getPricePool().shutdownNow();
     }       
    
}
