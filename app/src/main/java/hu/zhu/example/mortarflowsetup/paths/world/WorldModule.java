package hu.zhu.example.mortarflowsetup.paths.world;

import dagger.Module;
import dagger.Provides;
import mortar.utils.ViewScope;

/**
 * Created by Zhuinden on 2016.02.14..
 */
@Module
public class WorldModule {
    @Provides
    @ViewScope
    public WorldPresenter worldPresenter() {
        return new WorldPresenter();
    }
}
