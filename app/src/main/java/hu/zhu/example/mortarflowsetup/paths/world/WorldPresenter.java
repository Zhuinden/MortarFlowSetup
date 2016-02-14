package hu.zhu.example.mortarflowsetup.paths.world;

import android.os.Bundle;

import hu.zhu.example.mortarflowsetup.paths.hello.HelloView;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Created by Zhuinden on 2016.02.14..
 */
public class WorldPresenter extends ViewPresenter<WorldView> {
    @Override
    protected void onEnterScope(MortarScope scope) {
        super.onEnterScope(scope);
        Timber.v("onEnterScope() called");
    }

    @Override
    protected void onExitScope() {
        super.onExitScope();
        Timber.v("onExitScope() called");
    }

    @Override
    protected void onSave(Bundle outState) {
        super.onSave(outState);
        Timber.v("onSave() called");
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        Timber.v("onLoad() called");
        if(savedInstanceState != null) {
            if(getView() != null) {
                Timber.i("The view of this presenter exists.");
            } else {
                Timber.i("The view of this presenter does not exist.");
            }
        }
    }

    @Override
    public void takeView(WorldView view) {
        super.takeView(view);
        Timber.v("takeView() called");
    }

    @Override
    public void dropView(WorldView view) {
        super.dropView(view);
        Timber.v("dropView() called");
    }
}
