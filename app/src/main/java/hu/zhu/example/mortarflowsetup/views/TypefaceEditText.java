package hu.zhu.example.mortarflowsetup.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import javax.inject.Inject;

import hu.zhu.example.mortarflowsetup.R;
import hu.zhu.example.mortarflowsetup.application.InjectorService;
import hu.zhu.example.mortarflowsetup.injection.TypefaceModule;

public class TypefaceEditText
        extends EditText {

    @Inject
    TypefaceModule.TypefaceFactory typefaceFactory;

    private static final String VIEW_ATTR_FONT_DEFAULT_VALUE = "Oswald-Book.otf";

    public TypefaceEditText(Context context) {
        this(context, null, 0);
    }

    public TypefaceEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        if (!isInEditMode()) {
            InjectorService.obtain().inject(this);
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceView,
                    defStyle, 0);
            String font = typedArray.getString(R.styleable.TypefaceView_typeface);

            if ("".equals(font) || font == null) {
                font = VIEW_ATTR_FONT_DEFAULT_VALUE;
            }
            Typeface tf = typefaceFactory.getTypeface(font);
            if(tf != null) {
                setTypeface(tf);
            }
            typedArray.recycle();

            setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
