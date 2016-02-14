package hu.zhu.example.mortarflowsetup.paths.hello;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import flow.Flow;
import hu.zhu.example.mortarflowsetup.R;
import hu.zhu.example.mortarflowsetup.paths.world.WorldPath;
import mortar.utils.DaggerService;
import timber.log.Timber;

/**
 * Created by Owner on 2016.02.02.
 */
public class HelloView
        extends RelativeLayout {
    public HelloView(Context context) {
        super(context);
    }

    public HelloView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelloView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public HelloView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Inject
    HelloPresenter helloPresenter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Timber.v("onAttachedToWindow() called");
        if(!isInEditMode()) {
            helloPresenter.takeView(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Timber.v("onFinishInflate() called");
        if(!isInEditMode()) {
            HelloComponent helloComponent = DaggerService.getComponent(getContext());
            helloComponent.inject(this);
        }
        findViewById(R.id.hello_go_to_next_screen).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Flow.get(getContext()).set(new WorldPath());
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Timber.v("onDetachedFromWindow() called");
        if(!isInEditMode()) {
            helloPresenter.dropView(this);
        }
    }
}
