package hu.zhu.example.mortarflowsetup.paths.world;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

import flow.custom_path.BasePath;
import hu.zhu.example.mortarflowsetup.R;
import hu.zhu.example.mortarflowsetup.application.InjectorService;
import mortar.bundler.Bundler;
import timber.log.Timber;

/**
 * Created by Zhuinden on 2016.02.14..
 */
public class WorldPath extends BasePath {
    public static final String TAG = "WorldPath";

    public WorldPath() {
    }

    @Override
    public int getLayout() {
        return R.layout.path_world;
    }

    WorldComponent worldComponent;

    @Inject
    WorldPresenter worldPresenter;

    @Override
    public Object createAndStoreComponentAndInjectSelfAndPresenter() {
        Timber.v("createAndStoreComponentAndInjectSelfAndPresenter() called");
        if(worldComponent == null) {
            worldComponent = DaggerWorldComponent.builder().applicationComponent(InjectorService.obtain()).build();
            worldComponent.inject(this);
            worldComponent.inject(worldPresenter);
        }
        return worldComponent;
    }

    @Override
    public String getScopeName() {
        return TAG;
    }

    public WorldPath(Parcel in) {
        super(in);
        Timber.v("Load from Parcel");
        worldPresenter.onLoad(in.readBundle());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Timber.v("Save to Parcel");
        Bundle presenterState = new Bundle();
        worldPresenter.onSave(presenterState);
        dest.writeBundle(presenterState);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WorldPath> CREATOR = new Parcelable.Creator<WorldPath>() {
        @Override
        public WorldPath createFromParcel(Parcel in) {
            return new WorldPath(in);
        }

        @Override
        public WorldPath[] newArray(int size) {
            return new WorldPath[size];
        }
    };
}
