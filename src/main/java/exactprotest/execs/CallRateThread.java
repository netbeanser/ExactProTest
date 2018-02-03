/**
 * @class CallRateThread
 * @date Tue Jan 14 23:45 2018 MSK
 * @author dglunts
 * 
 * Description : Thread retrieves rates
 */
package exactprotest.execs;

import java.util.concurrent.TimeUnit;
import exactprotest.config.ExactProConfig;
import java.util.ArrayList;
import javax.websocket.Session;
import org.apache.log4j.Logger;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class CallRateThread extends Thread {
    
    private ArrayList<String> ids;
    
    private Session session;
    
    public CallRateThread(ArrayList ids,Session session){
        this.session = session;
        this.ids = ids;
        
    }
    
    private final Logger logger =  Logger.getLogger(CallRateThread.class);            
    
    private ScheduledFuture sf;    
    
    private boolean runCall = true;
    
    public void doStop(){
        this.runCall = false;
        sf.cancel(true);
        logger.info("In doStop sessionId "+session.getId()+" ids: "+ids);
    }
       
    
    public void run(){
        logger.info("Thread started: "+Thread.currentThread().toString());
        ExactProPrice epp = new ExactProPrice(ids);
        sf = ExactProScheduledPool.getPricePool().scheduleWithFixedDelay(epp, 1, ExactProConfig.getWsInterval(), TimeUnit.SECONDS);
        logger.info("QUEUE SIZE="+ExactProScheduledPool.getPricePool().getQueue().size());
        while (runCall){
            synchronized(epp) {
                try {
                    epp.wait();
                } catch (InterruptedException ie){
                    logger.info("Interrupted: "+epp.toString()+" ex:"+ ie);
                }
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
