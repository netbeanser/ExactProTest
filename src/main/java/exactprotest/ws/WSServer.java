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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap; 
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import exactprotest.execs.ExactProShedule;
 
@ServerEndpoint(value = "/realtime",configurator = WSConfig.class,subprotocols = {"json"}) 
public class WSServer {
    
    private final Logger logger =  Logger.getLogger(WSServer.class);
    
    private final CopyOnWriteArrayList<Session> wsSessions = new CopyOnWriteArrayList<>();
    
    private final ConcurrentHashMap<String,ExactProShedule> wsSessParams = new ConcurrentHashMap<>();
    
    public WSServer(){};

    @OnOpen
    public void onOpen(Session session){
        logger.info("WS Client connected: "+session.getId());
        
        wsSessions.add(session);
/*        try {
            session.getBasicRemote().sendText("On Line");
        } catch (IOException ioe) {
            logger.error("IOExcetion onOpen: "+ioe);            
        }
        
*/        
    }
     
    @OnClose
    public void onClose(Session session,CloseReason reason){
        wsSessions.remove(session);
        wsSessParams.remove(session.getId());
        logger.info("WS Client disconnected: "+ session.getId()+" Reason: "+reason);        
    }

    @OnMessage
    public void onMessage(String message,Session session){
        logger.info("Message from client: sessionId="+session.getId()+" Msg: "+ message);
        ArrayList ids = new Gson().fromJson(message, ArrayList.class);
        logger.info("IDDDDDSSS="+ids.toString());
        ExactProShedule eps = wsSessParams.get(session.getId());
        if (eps != null) {
            eps.getScheduledFuture().cancel(true);
            logger.info("Canceled callRates for session: "+ session.getId());
        }           
        eps = new ExactProShedule();
        wsSessParams.put(session.getId(), eps);
        try {
            eps.callRates(ids, session);
        } catch (InterruptedException ie){
           logger.error(ie);
        }        
    }

    @OnError
    public void onError(Throwable e){
        logger.error("Connection error: "+ e);
        e.printStackTrace();
    }    
        
}
