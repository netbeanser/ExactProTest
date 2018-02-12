package exactprotest.execs;

import exactprotest.config.ExactProConfig;
import exactprotest.web.ExactProResponseHandler;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
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
      
    private static RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(ExactProConfig.getConnectTimeout())
                .setConnectTimeout(ExactProConfig.getConnectTimeout())
                .setConnectionRequestTimeout(ExactProConfig.getConnectTimeout())
                .build();
    
    private static String rateUrl = ExactProConfig.getRateUrl();
       
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
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Integer id : IDs){
            HttpGet httpGet = new HttpGet(rateUrl+id);                    
            httpGet.setConfig(requestConfig);
            
            try (CloseableHttpClient httpClient = HttpClients.createDefault()){                                        
                String respBody = httpClient.execute(httpGet, new ExactProResponseHandler());  
                Pr pr = new Gson().fromJson(respBody, Pr.class);
                pr.setId(id);
                sb.append(pr.toString());
                sb.append(",");      
            } catch (IOException e){
                logger.error("Exception occured: "+e);
            }              
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        ret = sb.toString();
        logger.info("RET="+ret);
        synchronized(this){
            notify();
        }        
    }
    
    private class Pr {
        private float price;
        private int id;
        
        public Pr(){};
        
        public Pr(float price,int id) {
            this.price = price;
            this.id = id;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public void setId(int id) {
            this.id = id;
        }
        
        
        
        @Override
        public String toString(){
            return "{ \"id\":"+id+",\"price\":"+price+" }";
        }
    }
    
    @Override
    public String toString(){
        return "ExactProPrice ids="+IDs.toString();
    }
}
