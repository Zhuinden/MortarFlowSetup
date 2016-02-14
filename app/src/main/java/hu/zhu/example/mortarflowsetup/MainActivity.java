package hu.zhu.example.mortarflowsetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.StateParceler;
import flow.custom_path.BasePath;
import flow.path.PathContext;
import flow.utils.HandlesBack;
import flow.utils.ParcelableParceler;
import hu.zhu.example.mortarflowsetup.application.InjectorService;
import hu.zhu.example.mortarflowsetup.application.SingletonBus;
import hu.zhu.example.mortarflowsetup.paths.hello.HelloPath;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortar.utils.MortarScreenSwitcherFrame;

public class MainActivity
        extends AppCompatActivity
        implements Flow.Dispatcher {

    private static final String TAG = "MainActivity";

    protected BasePath currentPath;

    protected HandlesBack handlesBack;

    private FlowDelegate flowSupport;

    protected MortarScope activityScope;

    private RelativeLayout rootContainer;

    @Bind(R.id.progress)
    ProgressWheel progressWheel;

    @Bind(R.id.overlay)
    RelativeLayout overlay;

    @Bind(R.id.main_path_container)
    MortarScreenSwitcherFrame framePathContainerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonBus.INSTANCE.register(this);

        //MORTAR INIT
        MortarScope parentScope = MortarScope.getScope(getApplication());

        activityScope = parentScope.findChild(TAG);
        if(activityScope == null) {
            activityScope = parentScope.buildChild().withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner()).build(TAG);
        }
        InjectorService.get(this).getInjector().inject(this); // MORTAR + DAGGER

        //FLOW PATH INIT
        PathContext pathContext = PathContext.root(this);
        rootContainer = (RelativeLayout) LayoutInflater.from(this).cloneInContext(pathContext).inflate(R.layout.activity_main, null);
        setContentView(rootContainer);
        ButterKnife.bind(this);

        // VIEWS INIT
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        toolbar.setContentInsetsAbsolute(0, 0);

        //FLOW INIT
        StateParceler parceler = new ParcelableParceler();
        FlowDelegate.NonConfigurationInstance nonConfig = (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        handlesBack = (HandlesBack) framePathContainerView;

        flowSupport = FlowDelegate.onCreate(nonConfig,
                getIntent(),
                savedInstanceState,
                parceler,
                History.emptyBuilder().push(new HelloPath()).build(),
                this);

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState); // MORTAR
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        flowSupport.onSaveInstanceState(outState); //FLOW
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState); // MORTAR
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return flowSupport.onRetainNonConfigurationInstance(); // FLOW
    }

    @Override
    protected void onPause() {
        flowSupport.onPause(); // FLOW
        SingletonBus.INSTANCE.setPaused(true);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowSupport.onResume(); // FLOW
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SingletonBus.INSTANCE.setPaused(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowSupport.onNewIntent(intent); // FLOW
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        SingletonBus.INSTANCE.unregister(this);
        // activityScope may be null in case isWrongInstance() returned true in onCreate()
        if(isFinishing() && activityScope != null) {
            activityScope.destroy();
            activityScope = null;
        }
        super.onDestroy();
    }

    @Override
    public Object getSystemService(String name) {
        // MORTAR
        if(activityScope != null && activityScope.hasService(name)) {
            return activityScope.getService(name);
        }

        //FLOW
        if(flowSupport != null) {
            Object flowService = flowSupport.getSystemService(name);
            if(flowService != null) {
                return flowService;
            }
        }

        //DEFAULT
        return super.getSystemService(name);
    }

    @Override
    public void onBackPressed() {
        if(currentPath.onBackPressed()) {
            return;
        }

        if(getOverlay().getVisibility() != View.VISIBLE) {
            if(handlesBack.onBackPressed()) {
                return;
            }
            if(flowSupport.onBackPressed()) {
                return;
            }
            super.onBackPressed();
        }
    }

    public RelativeLayout getOverlay() {
        return overlay;
    }

    @Override
    public void dispatch(final Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        for(Object objectPath : traversal.origin) {
            BasePath basePath = (BasePath) objectPath;
            basePath.unbindActivity();
        }
        for(Object objectPath : traversal.destination) {
            BasePath basePath = (BasePath) objectPath;
            basePath.bindActivity(this);
        }
        currentPath = traversal.destination.top();

        //boolean canGoBack = traversal.destination.size() > 1;
        //actionBar.setDisplayHomeAsUpEnabled(canGoBack);
        //actionBar.setHomeButtonEnabled(canGoBack);
        framePathContainerView.dispatch(traversal, new Flow.TraversalCallback() {
            @Override
            public void onTraversalCompleted() {
                invalidateOptionsMenu();
                callback.onTraversalCompleted();
            }
        });
    }

    public static class DummyEvent {
    }

    public void onEventMainThread(DummyEvent dummyEvent) {
    }
}
