package hu.zhu.example.mortarflowsetup.application;

/**
 * Created by Owner on 2016.02.02.
 */

import android.os.Handler;
import android.os.Looper;

import java.util.Iterator;
import java.util.Vector;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusException;
import timber.log.Timber;

/**
 * Created by Owner on 2016.01.08.
 */
public enum SingletonBus {
    INSTANCE;

    private static String TAG = "SingletonBus";
    private final Vector<Object> eventQueueBuffer = new Vector<>();
    private volatile boolean paused;
    private Handler handler = new Handler(Looper.getMainLooper());

    public <T> void post(final T event) {
        if(paused) {
            eventQueueBuffer.add(event);
        } else {
            EventBus.getDefault().post(event);
        }
    }

    public <T> void delayedPostToMainThread(final T event, final long millis) {
        if(paused) {
            eventQueueBuffer.add(event);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(event);
                }
            }, millis);
        }
    }

    public <T> void register(T subscriber) {
        try {
            EventBus.getDefault().register(subscriber);
        } catch(EventBusException e) {
            Timber.e(e, "The object [" + subscriber + "] was already registered when trying to register!");
        }
    }

    public <T> void unregister(T subscriber) {
        try {
            EventBus.getDefault().unregister(subscriber);
        } catch(EventBusException e) {
            Timber.e(e, "The object [" + subscriber + "] was not registered when trying to unregister!");
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if(!paused) {
            Iterator<Object> eventIterator = eventQueueBuffer.iterator();
            while(eventIterator.hasNext()) {
                Object event = eventIterator.next();
                post(event);
                Timber.d("Posted event: " + event.toString());
                eventIterator.remove();
            }
        }
    }
}
