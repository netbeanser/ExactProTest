/**
 * @class ExactProConfig
 * @author dglunts
 * @date Tue Jan 12 19:08 2018 MSK
 * 
 * Description: configuration
 */
package exactprotest.config;

import exactprotest.web.ExactProContextListener;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

//Этот класс должен быть неизменяемым (immutable), поэтому нет
//дефолтного конструктора и есть только getters 
public class ExactProConfig {
    
    private static final Logger LOGGER =  Logger.getLogger(ExactProContextListener.class);
    
    private static   String instrumentListUrl;
    private static   String rateUrl;
    private static   int rateInterval;
    private static   int connectTimeout;
    private static   int maxClients;
    private static   int wsInterval; 
    
    static {
        String configPath = System.getProperty("cfgfile");        
        Properties props = new Properties();
        boolean fallBack = false; 
        
        if (configPath == null){          
            fallBack = true;
            LOGGER.info("Argument -Dcfgfile not specified. Falling back to calsspath");
        } else {
    /*
    Здесь и ниже используется конструкция try-with-resource,
    InputStream исполняет интерфейс AutoClosable, так что блок finally не нужен.
    InputStream'овский метод close() будет вызван автоматически    
    */ 
            try (InputStream ins = new FileInputStream(configPath)){                                                               
                props.load(ins);                        
            }
            catch (IOException e){
                LOGGER.info("Config file error: "+configPath+", falling back to classpath");
                fallBack = true;        
            }
        }
        
        if (fallBack){
            try (final InputStream ins = ExactProConfig
                .class
                .getClassLoader()
                .getResourceAsStream("application.properties")){            
                props.load(ins);
                LOGGER.info("Configuration: resources/application.properties");
            } catch (IOException e){
                LOGGER.error("Configuration error for classpath fallback "+e);
                throw new RuntimeException(e);                        
            }        
        
                instrumentListUrl = props.getProperty("instrumentListUrl", "http://priceticker.exactpro.com/RestInstruments/json/instrumentList");
                rateUrl = props.getProperty("rateUrl","http://pricetickehr.exactpro.com/RestInstruments/json/price?id=");        
                rateInterval = Integer.parseInt(props.getProperty("rateInterval","30"),10);
                connectTimeout = Integer.parseInt(props.getProperty("connectTimeout","5000"),10);
                maxClients = Integer.parseInt(props.getProperty("maxClients","10"),10);
                wsInterval = Integer.parseInt(props.getProperty("wsInterval","37"),10);                 
        }                       
        
    }
   
    public ExactProConfig() {};
    
    public static String getInstrumentListUrl(){
        return instrumentListUrl;
    }
    
    public static String getRateUrl(){
        return rateUrl;
    }
    
    public static int setRateInterval(){
        return rateInterval;
    }  
    
    public static int getConnectTimeout(){
        return connectTimeout;
    }
    
    public static int getMaxClients(){
        return maxClients;
    }
    
    public static int getWsInterval(){
        return wsInterval;
    }    
    
    public static String getConfig(){
        return "InstrumentListUrl="+instrumentListUrl
                + ";rateUrl="+rateUrl
                +";rateInterval="+rateInterval
                +";connectTimeout="+connectTimeout;        
    }
    
}
