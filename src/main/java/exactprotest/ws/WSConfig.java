package exactprotest.ws;

import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;

public class WSConfig extends ServerEndpointConfig.Configurator {
    
    private final Logger logger =  Logger.getLogger(WSConfig.class);  
    
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,            
            HandshakeRequest request,
            HandshakeResponse response) {
        
    }
    
}
