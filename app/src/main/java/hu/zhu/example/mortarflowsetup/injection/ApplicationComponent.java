package hu.zhu.example.mortarflowsetup.injection;

import javax.inject.Singleton;

import dagger.Component;
import hu.zhu.example.mortarflowsetup.MainActivity;
import hu.zhu.example.mortarflowsetup.application.CustomApplication;
import hu.zhu.example.mortarflowsetup.views.TypefaceEditText;
import hu.zhu.example.mortarflowsetup.views.TypefaceTextView;

/**
 * Created by Owner on 2016.02.02.
 */
@Component(modules = {AppContextModule.class, TypefaceModule.class})
@Singleton
public interface ApplicationComponent extends TypefaceComponent {
    CustomApplication customApplication();

    void inject(CustomApplication customApplication);
    void inject(MainActivity mainActivity);

    void inject(TypefaceEditText typefaceEditText);

    void inject(TypefaceTextView typefaceTextView);
}
