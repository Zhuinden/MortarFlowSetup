package mortar.utils;

import android.content.Context;

import flow.custom_path.BasePath;
import mortar.MortarScope;

/**
 * Creates {@link MortarScope}s for screens.
 */
public class ScreenScoper {
    public MortarScope getScreenScope(Context context, String name, Object screen) {
        MortarScope parentScope = MortarScope.getScope(context);
        return getScreenScope(parentScope, name, screen);
    }

    /**
     * Finds or creates the scope for the given screen.
     */
    public MortarScope getScreenScope(MortarScope parentScope, final String name, final Object screen) {
        MortarScope childScope = parentScope.findChild(name);
        if (childScope == null) {
            BasePath basePath = (BasePath) screen;
            childScope = parentScope.buildChild()
                    .withService(DaggerService.TAG, basePath.createAndStoreComponentAndInjectSelfAndPresenter())
                    .build(name);
        }
        return childScope;
    }
}