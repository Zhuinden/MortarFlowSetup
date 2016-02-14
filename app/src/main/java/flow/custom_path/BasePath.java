package flow.custom_path;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import flow.path.Path;
import hu.zhu.example.mortarflowsetup.MainActivity;

/**
 * Created by Owner on 2016.02.02.
 */
public abstract class BasePath
        extends Path
        implements Parcelable {

    public BasePath() {
        initSelf();
    }

    private MainActivity activity;

    public abstract int getLayout();

    public abstract Object createAndStoreComponentAndInjectSelfAndPresenter();

    public abstract String getScopeName();

    public boolean onBackPressed() {
        return false;
    }

    public void bindActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void unbindActivity() {
        this.activity = null;
    }

    @Nullable
    public MainActivity getActivity() {
        return activity;
    }

    private void initSelf() {
        createAndStoreComponentAndInjectSelfAndPresenter();
        init();
    }

    protected void init() {
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(o == null) {
            return false;
        } else if(o instanceof BasePath) {
            BasePath basePath = (BasePath) o;
            return basePath.getScopeName().equals(getScopeName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getScopeName().hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected BasePath(Parcel in) {
        initSelf();
    }
}