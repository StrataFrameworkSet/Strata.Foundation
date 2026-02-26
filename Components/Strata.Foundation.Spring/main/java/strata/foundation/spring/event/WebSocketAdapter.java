/// ///////////////////////////////////////////////////////////////////////////
// WebSocketAdapter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.spring.event;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

public
class WebSocketAdapter<E>
    implements WebSocketHandler
{
    @Override
    public Mono<Void>
    handle(WebSocketSession session)
    {
        return null;
    }
}

//////////////////////////////////////////////////////////////////////////////
