/**
 * Внедрение в WebSocket Handshake.
 * Служит исключительно для отладки
 */
package exactprotest.ws;

import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import org.apache.log4j.Logger;

public class WSConfig extends ServerEndpointConfig.Configurator {
    
    private final Logger logger =  Logger.getLogger(WSConfig.class);  
    
    @Override
    public void modifyHandshake(ServerEndpointConfig sec,            
            HandshakeRequest request,
            HandshakeResponse response) {
        
    }
    
}
