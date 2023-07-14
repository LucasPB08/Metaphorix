package client.modules;

import client.utils.MessageHandler;
import com.google.inject.AbstractModule;

public class MessageHandlerModule extends AbstractModule {

    /**
     * Configures what to inject.
     */
    @Override
    public void configure(){
        bind(MessageHandler.class);
    }
}
