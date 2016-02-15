package hu.zhu.example.mortarflowsetup.paths.hello;

import dagger.Module;
import dagger.Provides;
import mortar.utils.ViewScope;

/**
 * Created by Owner on 2016.02.02.
 */
@Module
public class HelloModule {
    @Provides
    @ViewScope
    public HelloPresenter helloPresenter() {
        return new HelloPresenter();
    }
}
