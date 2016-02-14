package hu.zhu.example.mortarflowsetup.injection;

import android.graphics.Typeface;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.zhu.example.mortarflowsetup.BuildConfig;
import hu.zhu.example.mortarflowsetup.application.CustomApplication;
import hu.zhu.example.mortarflowsetup.application.InjectorService;
import timber.log.Timber;

/**
 * Created by Zhuinden on 2015.11.15..
 */
@Module
public class TypefaceModule {
    public static final String HELVETICA_NEUE = "HelveticaNeue.ttf";
    public static final String OSWALD_BOLD = "Oswald-Bold.otf";
    public static final String OSWALD_BOOK = "Oswald-Book.otf";
    public static final String OSWALD_LIGHT = "Oswald-Light.otf";
    public static final String OSWALD_REGULAR = "Oswald-Regular.otf";

    private Typeface getTypeface(String font) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(CustomApplication.get().getAssets(), "fonts/" + font);
        } catch(RuntimeException e) {
            try {
                tf = Typeface.createFromAsset(CustomApplication.get().getAssets(), font);
            } catch(RuntimeException e2) {
                Timber.e(e2, "Failed creating typeface");
            }
        }
        return tf;
    }

    @Provides
    @Singleton
    @Named(HELVETICA_NEUE)
    public Typeface helveticaNeue() {
        return getTypeface(HELVETICA_NEUE);
    }

    @Provides
    @Singleton
    @Named(OSWALD_BOLD)
    public Typeface oswaldBold() {
        return getTypeface(OSWALD_BOLD);
    }

    @Provides
    @Singleton
    @Named(OSWALD_BOOK)
    public Typeface oswaldBook() {
        return getTypeface(OSWALD_BOOK);
    }

    @Provides
    @Singleton
    @Named(OSWALD_LIGHT)
    public Typeface oswaldLight() {
        return getTypeface(OSWALD_LIGHT);
    }

    @Provides
    @Singleton
    @Named(OSWALD_REGULAR)
    public Typeface oswaldRegular() {
        return getTypeface(OSWALD_REGULAR);
    }

    @Provides
    @Singleton
    public TypefaceFactory typefaceFactory() {
        return new TypefaceFactory();
    }

    public static class TypefaceFactory {
        public Typeface getTypeface(String font) {
            if(HELVETICA_NEUE.equals(font)) {
                return InjectorService.obtain().helveticaNeue();
            }
            if(OSWALD_BOLD.equals(font)) {
                return InjectorService.obtain().oswaldBold();
            }
            if(OSWALD_BOOK.equals(font)) {
                return InjectorService.obtain().oswaldBook();
            }
            if(OSWALD_LIGHT.equals(font)) {
                return InjectorService.obtain().oswaldLight();
            }
            if(OSWALD_REGULAR.equals(font)) {
                return InjectorService.obtain().oswaldRegular();
            }
            if(BuildConfig.DEBUG) {
                throw new IllegalArgumentException("Invalid font [" + font + "]");
            } else {
                return null;
            }
        }
    }
}
