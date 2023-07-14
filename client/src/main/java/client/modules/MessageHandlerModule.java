package client.modules;

import client.utils.MessageHandler;
import com.google.inject.AbstractModule;

public class MessageHandlerModule extends AbstractModule {

    @Override
    public void configure(){
        bind(MessageHandler.class);
    }
}
