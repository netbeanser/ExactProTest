/**
 * @class CallRateThread
 * @date Tue Jan 12 10:05 2018 MSK
 * @author dglunts
 * 
 * Description : SheduledThreadPool
 */
package exactprotest.execs;

import exactprotest.config.ExactProConfig;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ExactProScheduledPool {
    
    private static ScheduledThreadPoolExecutor pricePool = 
            new ScheduledThreadPoolExecutor(ExactProConfig.getMaxClients());    
    
    public static ScheduledThreadPoolExecutor getPricePool(){
        return pricePool;
    }
    
}
