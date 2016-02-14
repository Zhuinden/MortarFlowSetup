package hu.zhu.example.mortarflowsetup.injection;

import dagger.Module;
import dagger.Provides;
import hu.zhu.example.mortarflowsetup.application.CustomApplication;

/**
 * Created by Owner on 2016.02.02.
 */
@Module
public class AppContextModule {
    private CustomApplication customApplication;

    public AppContextModule(CustomApplication customApplication) {
        this.customApplication = customApplication;
    }

    @Provides
    public CustomApplication customApplication() {
        return customApplication;
    }
}
