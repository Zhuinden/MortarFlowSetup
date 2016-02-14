/*
 * Copyright 2014 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package flow.path;

import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;

import flow.Flow;
import flow.ViewState;
import timber.log.Timber;

/**
 * Handles swapping paths within a container view, as well as flow mechanics, allowing supported
 * container views to be largely declarative.
 */
public abstract class PathContainer {

  /**
   * Provides information about the current or most recent Traversal handled by the container.
   */
  protected static final class TraversalState {
    private Path fromPath;
    private ViewState fromViewState;
    private Path toPath;
    private ViewState toViewState;

    public void setNextEntry(Path path, ViewState viewState) {
      this.fromPath = this.toPath;
      this.fromViewState = this.toViewState;
      this.toPath = path;
      this.toViewState = viewState;
    }

    public Path fromPath() {
      return fromPath;
    }

    public Path toPath() {
      return toPath;
    }

    public void saveViewState(View view) {
      fromViewState.save(view);
    }

    public void restoreViewState(View view) {
      toViewState.restore(view);
    }
  }

  private final int tagKey;

  protected PathContainer(int tagKey) {
    this.tagKey = tagKey;
  }

  public final void executeTraversal(PathContainerView view, Flow.Traversal traversal, final Flow.TraversalCallback callback) {
    final View oldChild = view.getCurrentChild();
    Path path = traversal.destination.top();
    int i;
    if(traversal.direction == Flow.Direction.FORWARD) {
      Iterator<Object> _destinationPaths = traversal.destination.reverseIterator();
      i = 1; // 0 is ROOT in PathContext! DO NOT CHANGE TO 0
      while(_destinationPaths.hasNext()) {
        Path destinationPath = (Path) _destinationPaths.next();
        Timber.d("Traversing [#" + i + "] of path [" + destinationPath + "]");
        if(destinationPath != path) {
          path.elements().add(i, destinationPath);
        }
        i++;
      }
    }
    ViewState viewState = traversal.destination.currentViewState();
    Path oldPath;
    ViewGroup containerView = view.getContainerView();
    TraversalState traversalState = ensureTag(containerView);

    // See if we already have the direct child we want, and if so short circuit the traversal.
    if(oldChild != null) {
      oldPath = Preconditions.checkNotNull(traversalState.toPath,
              "Container view has child %s with no path",
              oldChild.toString());
      if(oldPath.equals(path)) {
        callback.onTraversalCompleted();
        return;
      }
    }

    traversalState.setNextEntry(path, viewState);
    Iterator<Object> originPaths = traversal.origin.reverseIterator();
    i = 0;
    while(originPaths.hasNext()) {
      Timber.d("[Origin Path #" + i++ + "]: [" + originPaths.next() + "]");
    }
    Iterator<Object> destinationPaths = traversal.destination.reverseIterator();
    i = 0;
    while(destinationPaths.hasNext()) {
      Timber.d("[Destination Path #" + i++ + "]: [" + destinationPaths.next() + "]");
    }
    performTraversal(containerView, traversal, traversalState, traversal.direction, callback);
  }

  protected abstract void performTraversal(ViewGroup container, Flow.Traversal traversal, TraversalState traversalState, Flow.Direction direction, Flow.TraversalCallback callback);

  private TraversalState ensureTag(ViewGroup container) {
    TraversalState traversalState = (TraversalState) container.getTag(tagKey);
    if(traversalState == null) {
      traversalState = new TraversalState();
      container.setTag(tagKey, traversalState);
    }
    return traversalState;
  }
}
