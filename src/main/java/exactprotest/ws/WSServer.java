/**
 * @class ExactProResponseHandler
 * @author dglunts
 * @date Tue Jan 15 21:30 2018 MSK
 * 
 * Description: WebSocketServer
 */package exactprotest.ws;

import com.google.gson.Gson;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import javax.websocket.CloseReason;
import java.util.concurrent.ConcurrentHashMap; 
import org.apache.log4j.Logger;
import java.util.ArrayList;
import exactprotest.execs.ExactProShedule;
 
@ServerEndpoint(value = "/realtime",configurator = WSConfig.class,subprotocols = {"json"}) 
public class WSServer {
    
    private final Logger logger =  Logger.getLogger(WSServer.class);
      
    private final ConcurrentHashMap<String,ExactProShedule> wsSessParams = new ConcurrentHashMap<>();
    
    public WSServer(){};

    @OnOpen
    public void onOpen(Session session){
        logger.info("WS Client connected. SessionId="+session.getId());
              
    }
     
    @OnClose
    public void onClose(Session session,CloseReason reason){

        ExactProShedule eps = wsSessParams.get(session.getId());
        if (eps != null) {
            eps.getCallRateThread().doStop();
            logger.info("Canceled onClose callRates for session: "+ session.getId());
        }               
        wsSessParams.remove(session.getId());
        logger.info("WS Client disconnected: "+ session.getId()+" Reason: "+reason);        
    }

    @OnMessage
    public void onMessage(String message,Session session){
        logger.info("Message from client: sessionId="+session.getId()+" Msg: "+ message);
        ArrayList ids = new Gson().fromJson(message, ArrayList.class);
        ExactProShedule eps = wsSessParams.get(session.getId());
        if (eps != null) {
            eps.getCallRateThread().doStop(); //Завершает поток, утечки памяти не будет
            logger.info("Canceled onMessage callRates for session: "+ session.getId());
        }           
        eps = new ExactProShedule();
        wsSessParams.put(session.getId(), eps);
        logger.info("New sessparams put: "+ session.getId()+" Message:"+message);
        try {
            eps.callRates(ids, session);
        } catch (InterruptedException ie){
           logger.error(ie);
        }        
    }

    @OnError
    public void onError(Throwable e){
        logger.error("WS onError: "+ e);
        e.printStackTrace();
    }    
        
}
