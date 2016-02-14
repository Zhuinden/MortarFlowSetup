package mortar.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Owner on 2016.02.02.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewScope {
}
