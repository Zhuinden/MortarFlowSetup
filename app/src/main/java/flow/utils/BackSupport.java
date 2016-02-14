package flow.utils;

import android.view.View;
import flow.Flow;

/**
 * Support for {@link HandlesBack}.
 */
public class BackSupport {

    public static boolean onBackPressed(View childView) {
        if (childView instanceof HandlesBack) {
            if (((HandlesBack) childView).onBackPressed()) {
                return true;
            }
        }
        return Flow.get(childView).goBack();
    }

    private BackSupport() {
    }
}