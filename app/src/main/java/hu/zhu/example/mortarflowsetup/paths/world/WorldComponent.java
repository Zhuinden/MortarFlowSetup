package hu.zhu.example.mortarflowsetup.paths.world;

import dagger.Component;
import hu.zhu.example.mortarflowsetup.injection.ApplicationComponent;
import mortar.utils.ViewScope;

/**
 * Created by Zhuinden on 2016.02.14..
 */
@ViewScope
@Component(dependencies = ApplicationComponent.class, modules = WorldModule.class)
public interface WorldComponent {
    WorldPresenter worldPresenter();

    void inject(WorldPresenter worldPresenter);
    void inject(WorldPath worldPath);
    void inject(WorldView worldView);
}
