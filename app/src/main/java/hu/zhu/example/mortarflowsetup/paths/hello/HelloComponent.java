package hu.zhu.example.mortarflowsetup.paths.hello;

import dagger.Component;
import hu.zhu.example.mortarflowsetup.injection.ApplicationComponent;
import mortar.utils.ViewScope;

/**
 * Created by Owner on 2016.02.02.
 */
@Component(dependencies = {ApplicationComponent.class}, modules = HelloModule.class)
@ViewScope
public interface HelloComponent {
    HelloPresenter helloPresenter();

    void inject(HelloView helloView);
    void inject(HelloPresenter helloPresenter);
    void inject(HelloPath helloPath);
}
