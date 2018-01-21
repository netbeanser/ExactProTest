/**
 * @class ExactProResponseHandler
 * @author dglunts
 * @date Tue Jan 13 11:13 2018 MSK
 * 
 * Description: ResponseHandler
 */
package exactprotest.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import java.io.IOException;

public class ExactProResponseHandler implements ResponseHandler<String>{
    
    private final Logger logger =  Logger.getLogger(ExactProServlet.class); 

    
    @Override
    public String handleResponse(final HttpResponse httpResponse) 
        throws ClientProtocolException {
        String respBody = "";
        
        String contentType = httpResponse
            .getEntity()
            .getContentType()
            .getValue();
            
            if (!"application/json".equals(contentType)) {
                logger.error("Content-Type is not JSON, but must be. Actual: "+contentType);
                throw new ClientProtocolException("Content-Type is not JSON, but must be. Actual: "+contentType);
            }
            try {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status >=200 && status < 300){
                    HttpEntity entity = httpResponse.getEntity();
                    
                    long len = entity.getContentLength();
                    if (len != -1 && len < 2048) {
                        respBody = entity != null ? EntityUtils.toString(entity) : "";
                    }
                } else {
                    throw new ClientProtocolException("Противный статус код: "+status);
                }    
            } catch (IOException ioe) {
                logger.error("IOException in ResponseHandler:"+ioe);
            }
        return respBody;
    }
}