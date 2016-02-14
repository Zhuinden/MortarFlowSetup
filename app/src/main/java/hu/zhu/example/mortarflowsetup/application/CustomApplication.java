package hu.zhu.example.mortarflowsetup.application;

import android.app.Application;

import hu.zhu.example.mortarflowsetup.AppConfig;
import mortar.MortarScope;
import timber.log.Timber;

/**
 * Created by Owner on 2016.02.02.
 */
public class CustomApplication extends Application {
    public static final String TAG = "CustomApplication";

    private MortarScope rootScope;

    private AppConfig appConfig;

    public static CustomApplication get() {
        return ApplicationHolder.INSTANCE.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHolder.INSTANCE.setApplication(this);
        InjectorService.obtain().inject(this);
        appConfig = new AppConfig();
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public Object getSystemService(String name) {
        if(rootScope == null) {
            rootScope = MortarScope.buildRootScope()
                    .withService(InjectorService.TAG, new InjectorService(this))
                    .build("Root");
        }
        if(rootScope.hasService(name)) { // if the additional "Context" service is within Mortar
            return rootScope.getService(name);
        }
        return super.getSystemService(name); // otherwise return application level context system service
    }
}
