package flow.utils;

import android.os.Parcelable;

import flow.StateParceler;

/**
 * Created by Owner on 2015.12.29.
 */
public class ParcelableParceler
        implements StateParceler {
    @Override
    public Parcelable wrap(Object instance) {
        return (Parcelable) instance;
    }

    @Override
    public Object unwrap(Parcelable parcelable) {
        return parcelable;
    }
}
