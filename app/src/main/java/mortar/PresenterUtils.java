package mortar;

import android.view.View;

import flow.custom_path.BasePath;
import flow.path.Path;
import mortar.utils.DaggerService;

/**
 * Created by Owner on 2015.10.28.
 */
public class PresenterUtils {
    @SuppressWarnings("unchecked")
    public static <T extends BasePath, V extends View> T getPath(ViewPresenter<V> viewPresenter) {
        return (T) Path.get(viewPresenter.getView().getContext());
    }

    public static <T, V extends View> T getComponent(ViewPresenter<V> viewPresenter) {
        return DaggerService.getComponent(viewPresenter.getView().getContext());
    }
}
