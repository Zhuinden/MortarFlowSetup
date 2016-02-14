package flow.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import flow.Flow;
import flow.custom_path.BasePath;
import flow.path.Path;
import flow.path.PathContainer;
import flow.path.PathContext;
import flow.path.PathContextFactory;
import timber.log.Timber;

import static flow.Flow.Direction.REPLACE;

/**
 * Provides basic right-to-left transitions. Saves and restores view state.
 * Uses {@link PathContext} to allow customized sub-containers.
 */
public class SimplePathContainer
        extends PathContainer {
    private static final String TAG = "SimplePathContainer";

    private final PathContextFactory contextFactory;

    public SimplePathContainer(int tagKey, PathContextFactory contextFactory) {
        super(tagKey);
        this.contextFactory = contextFactory;
    }

    @Override
    protected void performTraversal(final ViewGroup containerView, Flow.Traversal traversal, final PathContainer.TraversalState traversalState, final Flow.Direction direction, final Flow.TraversalCallback callback) {
        final PathContext oldPathContext;
        final PathContext newPathContext;
        if(containerView.getChildCount() > 0) {
            Timber.d("Container View Child count was > 0, using view context of path [" + PathContext.get(containerView.getChildAt(
                            0).getContext()).path + "]");
            oldPathContext = PathContext.get(containerView.getChildAt(0).getContext());
        } else {
            Timber.d("Container View Child Count was == 0, using root context");
            oldPathContext = PathContext.root(containerView.getContext());
        }
        Timber.d("Previous Path [" + oldPathContext.path + "]");
        final Path to = traversalState.toPath();
        Timber.d("New Path [" + to + "]");

        View newView;
        newPathContext = PathContext.create(oldPathContext, to, contextFactory);

        int layout = ((BasePath) to).getLayout(); //removed annotation
        newView = LayoutInflater.from(newPathContext.getApplicationContext()) //fixed first path error
                .cloneInContext(newPathContext)
                .inflate(layout, containerView, false);
        View fromView = null;
        if(traversalState.fromPath() != null) {
            fromView = containerView.getChildAt(0);
            traversalState.saveViewState(fromView);
        }
        traversalState.restoreViewState(newView);

        if(fromView == null || direction == REPLACE) {
            containerView.removeAllViews();
            containerView.addView(newView);
            oldPathContext.destroyNotIn(newPathContext, contextFactory);
            callback.onTraversalCompleted();
        } else {
            final View finalFromView = fromView;
            if(direction == Flow.Direction.BACKWARD) {
                containerView.removeView(fromView);
                containerView.addView(newView);
                containerView.addView(finalFromView);
            } else {
                containerView.addView(newView);
            }
            ViewUtils.waitForMeasure(newView, new ViewUtils.OnMeasuredCallback() {
                @Override
                public void onMeasured(View view, int width, int height) {
                    runAnimation(containerView, finalFromView, view, direction, new Flow.TraversalCallback() {
                        @Override
                        public void onTraversalCompleted() {
                            containerView.removeView(finalFromView);
                            oldPathContext.destroyNotIn(newPathContext, contextFactory);
                            callback.onTraversalCompleted();
                        }
                    }, (BasePath) Path.get(oldPathContext), (BasePath) to);
                }
            });
        }
    }

    private void runAnimation(final ViewGroup container, final View from, final View to, Flow.Direction direction, final Flow.TraversalCallback callback, BasePath fromPath, BasePath toPath) {
        Animator animator = createSegue(from, to, direction);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.removeView(from);
                callback.onTraversalCompleted();
            }
        });
        animator.start();
    }

    private Animator createSegue(View from, View to, Flow.Direction direction) {
        boolean backward = direction == Flow.Direction.BACKWARD;
        int fromTranslation = backward ? from.getWidth() : -from.getWidth();
        int toTranslation = backward ? -to.getWidth() : to.getWidth();

        AnimatorSet set = new AnimatorSet();

        set.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_X, fromTranslation));
        set.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, toTranslation, 0));

        return set;
    }
}