package exactprotest.execs;

import exactprotest.config.ExactProConfig;
import exactprotest.web.ExactProResponseHandler;
import java.util.List;
import java.util.ArrayList;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import com.google.gson.Gson;

public class ExactProPrice implements Runnable {       

    private final static Logger logger =  Logger.getLogger(ExactProPrice.class);     
    
    private final List<Integer> IDs = new ArrayList<>();
    
    private String ret = "";
    
    private volatile boolean done = false;
    
    private static RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(ExactProConfig.getConnectTimeout())
                .setConnectTimeout(ExactProConfig.getConnectTimeout())
                .setConnectionRequestTimeout(ExactProConfig.getConnectTimeout())
                .build();
    
    private static String rateUrl = ExactProConfig.getRateUrl();
    
    public boolean isDone(){
        return done;
    }
    
    public ExactProPrice(List<String> IDs){
        for (String s: IDs){            
            this.IDs.add(Integer.parseInt(s));
        }
    }
    
    public String getRet(){
        return ret;
    }
    
    @Override
    public void run () {
        done = false;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Integer id : IDs){
            HttpGet httpGet = new HttpGet(rateUrl+id);                    
            httpGet.setConfig(requestConfig);
            
            try (CloseableHttpClient httpClient = HttpClients.createDefault()){                                        
                String respBody = httpClient.execute(httpGet, new ExactProResponseHandler());  
                sb.append(id.toString());
                sb.append(":");
                sb.append(respBody);
                sb.append(",");   
                logger.info("SB="+sb.toString());
            } catch (Exception e){
                logger.error("Exception occured: "+e);
            }              
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("}");
        ret = sb.toString();
        logger.info("RET="+ret);
        done = true;
        
    }
    
}
