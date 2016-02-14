package hu.zhu.example.mortarflowsetup.application;

import android.content.Context;

import hu.zhu.example.mortarflowsetup.injection.AppContextModule;
import hu.zhu.example.mortarflowsetup.injection.ApplicationComponent;
import hu.zhu.example.mortarflowsetup.injection.DaggerApplicationComponent;
import mortar.MortarScope;

/**
 * Created by Owner on 2016.02.02.
 */
public class InjectorService {
    public static final String TAG = "InjectorService";

    private ApplicationComponent applicationComponent; //dagger2 app level component

    InjectorService(CustomApplication customApplication) {
        AppContextModule appContextModule = new AppContextModule(customApplication);
        applicationComponent = DaggerApplicationComponent.builder()
                .appContextModule(appContextModule)
                .build();
    }

    public ApplicationComponent getInjector() { //return the app component to inject `this` with it
        return applicationComponent;
    }

    public static InjectorService get(Context context) {
        //this is needed otherwise the compiler is whining. -_-
        //noinspection ResourceType
        return (InjectorService) context.getSystemService(TAG);
    }

    public static ApplicationComponent obtain() {
        return ((InjectorService) MortarScope.getScope(ApplicationHolder.INSTANCE.getApplication())
                .getService(TAG)).getInjector();
    }
}
