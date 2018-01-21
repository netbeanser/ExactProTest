/**
 * @class ExactProConfig
 * @author dglunts
 * @date Tue Jan 12 19:08 2018 MSK
 * 
 * Description: configuration
 */
package exactprotest.config;

//Этот класс должен быть неизменяемым (immutable), поэтому нет
//дефолтного конструктора и есть только getters 
public class ExactProConfig {
    
    private static   String instrumentListUrl;
    private static   String rateUrl;
    private static   int rateInterval;
    private static   int connectTimeout;
    private static   int maxClients;
    private static   int wsInterval;
   
    public ExactProConfig(String instrumentListUrl,
            String rateUrl,
            int rateInterval,
            int connectTimeout,
            int maxClients,
            int wsInterval){
        this.instrumentListUrl = instrumentListUrl;
        this.rateUrl = rateUrl;
        this.rateInterval = rateInterval;
        this.connectTimeout = connectTimeout;
        this.maxClients = maxClients;
        this.wsInterval = wsInterval;
    }
    
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
    
    @Override
    public String toString(){
        return "InstrumentListUrl="+instrumentListUrl
                + ";rateUrl="+rateUrl
                +";rateInterval="+rateInterval
                +";connectTimeout="+connectTimeout;
    }
    
}
