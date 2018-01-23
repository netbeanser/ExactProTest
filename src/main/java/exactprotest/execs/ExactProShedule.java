package exactprotest.execs;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.ArrayList;
import exactprotest.config.ExactProConfig;
import javax.websocket.Session;
import org.apache.log4j.Logger;


public class ExactProShedule {
    
    private final Logger logger =  Logger.getLogger(ExactProShedule.class);    
    
    private static ScheduledThreadPoolExecutor pricePool = 
            new ScheduledThreadPoolExecutor(ExactProConfig.getMaxClients());    
    
    private static int wsInterval = ExactProConfig.getWsInterval();
    
    private ScheduledFuture sf;
    
    private boolean runCall = true;    
    
    public static ScheduledThreadPoolExecutor getScheduledPool(){
        return pricePool;
    } 
    
    public ScheduledFuture getScheduledFuture(){
        return sf;
    }
    
    public void runCall(boolean runCall){
        this.runCall = runCall;
    }
    
    public void callRates (ArrayList ids,Session session) throws InterruptedException {
        ExactProPrice epp = new ExactProPrice(ids);
        ScheduledFuture sf = pricePool.scheduleWithFixedDelay(epp, 1, wsInterval, TimeUnit.SECONDS);
        logger.info("QUEUE SIZE="+pricePool.getQueue().size());
        while (runCall){
            synchronized(epp) {
                epp.wait();
            }
            logger.info("EPP is DONE for session:"+session.getId()+" IDs="+ids.toString());
            logger.info("SESSION ID="+session.getId()+" IS OPEN: "+session.isOpen());        
        
            if (session.isOpen() ) {
                try {
                    session.getBasicRemote().sendText(epp.getRet());
                } catch (Exception e){
                    logger.error("callRates Exception: "+e);
                }
            }
        }
    }
    
}
