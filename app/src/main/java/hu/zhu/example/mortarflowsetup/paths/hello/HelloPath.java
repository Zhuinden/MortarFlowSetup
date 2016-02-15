package hu.zhu.example.mortarflowsetup.paths.hello;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

import flow.custom_path.BasePath;
import hu.zhu.example.mortarflowsetup.R;
import hu.zhu.example.mortarflowsetup.application.InjectorService;
import timber.log.Timber;

/**
 * Created by Owner on 2016.02.02.
 */
public class HelloPath
        extends BasePath {
    public static final String TAG = "HelloPath";

    public HelloPath() {
    }

    @Override
    public int getLayout() {
        return R.layout.path_hello;
    }

    protected HelloComponent helloComponent;

    @Inject
    HelloPresenter helloPresenter;

    @Override
    public Object createAndStoreComponentAndInjectSelfAndPresenter() {
        Timber.v("createAndStoreComponentAndInjectSelfAndPresenter() called");
        if(helloComponent == null) {
            helloComponent = DaggerHelloComponent.builder().applicationComponent(InjectorService.obtain()).build();
            helloComponent.inject(this);
            helloComponent.inject(helloPresenter);
        }
        return helloComponent;
    }

    @Override
    public String getScopeName() {
        return TAG;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Timber.v("Save to Parcel");
        Bundle bundle = new Bundle();
        helloPresenter.onSave(bundle);
        dest.writeBundle(bundle);
    }

    public HelloPath(Parcel in) {
        super(in);
        Timber.v("Load from Parcel");
        helloPresenter.onLoad(in.readBundle());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HelloPath> CREATOR = new Parcelable.Creator<HelloPath>() {
        @Override
        public HelloPath createFromParcel(Parcel in) {
            return new HelloPath(in);
        }

        @Override
        public HelloPath[] newArray(int size) {
            return new HelloPath[size];
        }
    };
}
