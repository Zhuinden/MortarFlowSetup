package hu.zhu.example.mortarflowsetup.injection;

import android.graphics.Typeface;

import javax.inject.Named;

import static hu.zhu.example.mortarflowsetup.injection.TypefaceModule.HELVETICA_NEUE;
import static hu.zhu.example.mortarflowsetup.injection.TypefaceModule.OSWALD_BOLD;
import static hu.zhu.example.mortarflowsetup.injection.TypefaceModule.OSWALD_BOOK;
import static hu.zhu.example.mortarflowsetup.injection.TypefaceModule.OSWALD_LIGHT;
import static hu.zhu.example.mortarflowsetup.injection.TypefaceModule.OSWALD_REGULAR;

/**
 * Created by Zhuinden on 2015.11.15..
 */
public interface TypefaceComponent {
    @Named(HELVETICA_NEUE)
    Typeface helveticaNeue();

    @Named(OSWALD_BOLD)
    Typeface oswaldBold();

    @Named(OSWALD_BOOK)
    Typeface oswaldBook();

    @Named(OSWALD_LIGHT)
    Typeface oswaldLight();

    @Named(OSWALD_REGULAR)
    Typeface oswaldRegular();

    TypefaceModule.TypefaceFactory typefaceFactory();
}
