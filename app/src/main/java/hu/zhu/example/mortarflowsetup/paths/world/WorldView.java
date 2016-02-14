package hu.zhu.example.mortarflowsetup.paths.world;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import mortar.utils.DaggerService;

/**
 * Created by Zhuinden on 2016.02.14..
 */
public class WorldView extends RelativeLayout {
    public WorldView(Context context) {
        super(context);
    }

    public WorldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WorldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public WorldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Inject
    WorldPresenter worldPresenter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!isInEditMode()) {
            worldPresenter.takeView(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(!isInEditMode()) {
            WorldComponent worldComponent = DaggerService.getComponent(getContext());
            worldComponent.inject(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(!isInEditMode()) {
            worldPresenter.dropView(this);
        }
    }
}
