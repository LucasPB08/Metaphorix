package client.modules;

import client.scenes.*;
import com.google.inject.AbstractModule;

public class ControllerModule extends AbstractModule {

    /**
     * Configures what to inject.
     */
    @Override
    public void configure(){
        bind(AddChatsCtrl.class);
        bind(ChatOverviewController.class);
        bind(MainCtrl.class);
        bind(SignInCtrl.class);
        bind(SignUpCtrl.class);
        bind(UserOverviewController.class);
        bind(GroupChatCreationController.class);
        bind(GroupChatOverviewController.class);
    }
}
