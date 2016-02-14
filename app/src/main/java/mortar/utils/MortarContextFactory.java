package mortar.utils;

/**
 * Created by Zhuinden on 2015.07.02..
 */
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

import flow.custom_path.BasePath;
import flow.path.Path;
import flow.path.PathContextFactory;
import mortar.MortarScope;

public final class MortarContextFactory implements PathContextFactory {
    private final ScreenScoper screenScoper = new ScreenScoper();

    public MortarContextFactory() {
    }

    @Override public Context setUpContext(Path path, Context parentContext) {
        BasePath basePath = (BasePath) path;
        MortarScope screenScope = screenScoper.getScreenScope(parentContext, basePath.getScopeName(), path);
        return new TearDownContext(parentContext, screenScope);
    }

    @Override public void tearDownContext(Context context) {
        TearDownContext.destroyScope(context);
    }

    static class TearDownContext extends ContextWrapper {
        private static final String SERVICE = "PARENT_HOOK_FOR_MORTAR_SCOPE";
        private final MortarScope parentScope;
        private LayoutInflater inflater;

        static void destroyScope(Context context) {
            MortarScope.getScope(context).destroy();
        }

        public TearDownContext(Context context, MortarScope scope) {
            super(scope.createContext(context));
            this.parentScope = MortarScope.getScope(context);
        }

        @Override public Object getSystemService(String name) {
            if (LAYOUT_INFLATER_SERVICE.equals(name)) {
                if (inflater == null) {
                    inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
                }
                return inflater;
            }

            if (SERVICE.equals(name)) {
                return parentScope;
            }

            return super.getSystemService(name);
        }
    }
}