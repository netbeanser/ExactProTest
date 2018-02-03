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
import org.apache.log4j.Logger;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

import exactprotest.config.ExactProConfig;
import exactprotest.execs.ExactProScheduledPool;

@WebListener
public class ExactProContextListener implements ServletContextListener{
    
    private final static Logger logger =  Logger.getLogger(ExactProContextListener.class);
    
    /*Выкидываем исключения, которые образовались в других классах именно здесь,
    чтобы, в случае ошибки, приложение wouldn't be deployed.
    Для этого нужно выкидывать RuntimeException.
    */    
    
    @Override
    public void contextInitialized(ServletContextEvent evt){  

        String configPath = System.getProperty("cfgfile");
                        
        Properties props = new Properties();
        
        boolean fallBack = false;
        
        if (configPath == null){            
            logger.info("Argument -Dcfgfile not specified. Falling back to calsspath");
            fallBack = true;
        } else {            
    /*
    Здесь и ниже используется конструкция try-with-resource,
    InputStream исполняет интерфейс AutoClosable, так что блок finally не нужен.
    InputStream'овский метод close() будет вызван автоматически    
    */        
            
            try (InputStream ins = new FileInputStream(configPath)){                                                               
                props.load(ins);                        
            }
            catch (Exception e){
                logger.error("Config file error: "+e.getStackTrace());
                logger.info("Config file error: "+configPath+", falling back to classpath");
                fallBack = true;        
            }            
        } 
        if (fallBack){
            try (final InputStream ins = this
                    .getClass()
                    .getClassLoader()
                    .getResourceAsStream("application.properties")){            
                props.load(ins);
                logger.info("Configuration: resources/application.properties");
            } catch (Exception e){
                logger.error("Configuration error for ExactProTest "+e.getStackTrace());
                throw new RuntimeException("Configuration error");                        
           }        
        
        }   
        
        ExactProConfig exactProConfig = new ExactProConfig(
        props.getProperty("instrumentListUrl", "http://priceticker.exactpro.com/RestInstruments/json/instrumentList"),
        props.getProperty("rateUrl","http://pricetickehr.exactpro.com/RestInstruments/json/price?id="),        
        Integer.parseInt(props.getProperty("rateInterval","30"),10),
        Integer.parseInt(props.getProperty("connectTimeout","5000"),10),
        Integer.parseInt(props.getProperty("maxClients","10"),10),
        Integer.parseInt(props.getProperty("wsInterval","37"),10));
        
        //Толкаем конфигурацию в ServletContex, чтобы ее видели все, кому надо
    //    evt.getServletContext().setAttribute("config", exactProConfig);
        
        logger.info("ExactProConfig: "+exactProConfig);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent evt){
        ExactProScheduledPool.getPricePool().shutdownNow();
     }       
    
}
