package hu.zhu.example.mortarflowsetup.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import javax.inject.Inject;

import hu.zhu.example.mortarflowsetup.R;
import hu.zhu.example.mortarflowsetup.application.InjectorService;
import hu.zhu.example.mortarflowsetup.injection.TypefaceModule;

public class TypefaceTextView
        extends TextView {

    private static final String VIEW_ATTR_FONT_DEFAULT_VALUE = "Oswald-Book.otf";
    private String font;

    @Inject
    TypefaceModule.TypefaceFactory typefaceFactory;

    public TypefaceTextView(Context context) {
        this(context, null, 0);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        if (!isInEditMode()) {
            InjectorService.obtain().inject(this);
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceView,
                    defStyle, 0);
            font = typedArray.getString(R.styleable.TypefaceView_typeface);

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

    public void setTypeface(String typeface) {
        font = typeface;
        Typeface tf = typefaceFactory.getTypeface(font);
        if(tf != null) {
            setTypeface(tf);
        }
        setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        invalidate();
    }
}
