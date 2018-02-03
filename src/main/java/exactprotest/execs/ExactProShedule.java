package exactprotest.execs;


import java.util.ArrayList;
import exactprotest.config.ExactProConfig;
import javax.websocket.Session;
import org.apache.log4j.Logger;


public class ExactProShedule {
    
    private final Logger logger =  Logger.getLogger(ExactProShedule.class);            
        
    private CallRateThread callRateThread;
    
    public CallRateThread getCallRateThread(){
        return callRateThread;
    }
    
    public void callRates (ArrayList<String> ids,Session session) throws InterruptedException {
        callRateThread = new CallRateThread(ids,session);
        callRateThread.start();
        logger.info("Exiting callRates: session"+session.getId()+" ids:"+ids);
    }
    
}
