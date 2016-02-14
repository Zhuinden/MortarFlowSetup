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

import android.content.Context;
import android.content.ContextWrapper;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import flow.custom_path.BasePath;
import timber.log.Timber;

public class PathContext
        extends ContextWrapper {
  private static final String SERVICE_NAME = "PATH_CONTEXT";
  public final Path path;
  public final Map<Path, Context> contexts;

  public PathContext(Context baseContext, Path path, Map<Path, Context> contexts) {
    super(baseContext);
    Preconditions.checkArgument(baseContext != null, "Leaf context may not be null.");
    try {
      Preconditions.checkArgument(path.elements().size() == contexts.size(),
              "Path and context map are not the same size, path has %d elements and there are %d contexts",
              path.elements().size(),
              contexts.size());
    } catch(Exception e) {
      for(int i = 0; i < path.elements().size(); i++) {
        Timber.i("PATH [#" + i + "] IS [" + path.elements()
                        .get(i) + ", context [" + contexts.containsKey(path.elements().get(i)) + "]");
      }
      throw e;
    }

    if(!path.isRoot()) {
      Path leafPath = path.elements().get(path.elements().size() - 1);
      Preconditions.checkArgument(baseContext == contexts.get(leafPath),
              "For a non-root Path, baseContext must be Path leaf's context.");
    }
    this.path = path;
    this.contexts = contexts;
  }

  public static PathContext root(Context baseContext) {
    Timber.i("Creating ROOT context.");
    return new PathContext(baseContext, Path.ROOT, Collections.singletonMap(Path.ROOT, baseContext));
  }

  public static PathContext create(PathContext previousContext, Path newPath, PathContextFactory factory) {
    if(newPath == Path.ROOT) {
      throw new IllegalArgumentException("Path is empty.");
    }
    List<Path> newPathElements = newPath.elements();
    Map<Path, Context> newContextChain = new LinkedHashMap<>();
    // We walk down the elements, reusing existing contexts for the elements we encounter.  As soon
    // as we encounter an element that doesn't already have a context, we stop.
    // Note: we will always have at least one shared element, the root.
    Context baseContext = null;
    Iterator<Path> pathIterator = newPathElements.iterator();
    Iterator<Path> basePathIterator = previousContext.path.elements().iterator();
    Timber.d(":: Creating Context to [" + ((BasePath) newPath).getScopeName() + "]");
    while(pathIterator.hasNext() && basePathIterator.hasNext()) {
      Path element = pathIterator.next();
      Path basePathElement = basePathIterator.next();
      if(basePathElement.equals(element)) {
        if(!element.isRoot()) {
          Timber.d("Matched new Path to old Path [" + ((BasePath) element).getScopeName() + "], preserving context.");
        } else {
          Timber.d("Matched new Path to old Path [ROOT], preserving context.");
        }

        baseContext = previousContext.contexts.get(element);
        newContextChain.put(element, baseContext);
      } else {
        if(!basePathElement.isRoot() && !element.isRoot()) {
          Timber.d("No match from [" + ((BasePath) basePathElement).getScopeName() + "] to [" + ((BasePath) element)
                          .getScopeName() + "] , creating new context.");
        } else {
          Timber.d("No match from ROOT [" + basePathElement + "] to ROOT [" + element + "] , creating new context.");
        }

        baseContext = factory.setUpContext(element, baseContext);
        newContextChain.put(element, baseContext);
        break;
      }
    }
    // Now we continue walking our new path, creating contexts as we go in case they don't exist.
    while(pathIterator.hasNext()) {
      Path element = pathIterator.next();
      if(!element.isRoot()) {
        Timber.d("Creating new path [" + ((BasePath) element).getScopeName() + "].");
      } else {
        Timber.d("Creating new path [ROOT].");
      }
      baseContext = factory.setUpContext(element, baseContext);
      newContextChain.put(element, baseContext);
    }
    // Finally, we can construct our new PathContext
    return new PathContext(baseContext, newPath, newContextChain);
  }

  /**
   * Finds the tail of this path which is not in the given path, and destroys it.
   */
  public void destroyNotIn(PathContext path, PathContextFactory factory) {
    Iterator<Path> aElements = this.path.elements().iterator();
    Iterator<Path> bElements = path.path.elements().iterator();
    while(aElements.hasNext() && bElements.hasNext()) {
      Path aElement = aElements.next();
      Path bElement = bElements.next();
      if(!aElement.equals(bElement)) {
        BasePath aBasePath = (BasePath) aElement;
        BasePath bBasePath = (BasePath) bElement;
        Timber.d("Destroying [" + aBasePath.getScopeName() + "] on matching with [" + bBasePath.getScopeName() + "]");
        factory.tearDownContext(contexts.get(aElement));
        break;
      }
    }
    while(aElements.hasNext()) {
      Path aElement = aElements.next();
      BasePath aBasePath = (BasePath) aElement;
      Timber.d("Destroying [" + aBasePath.getScopeName() + "] as it is not found in [" + path + "]");
      factory.tearDownContext(contexts.get(aElement));
    }
  }

  @SuppressWarnings("ResourceType")
  public static PathContext get(Context context) {
    return Preconditions.checkNotNull((PathContext) context.getSystemService(SERVICE_NAME),
            "Expected to find a PathContext but did not.");
  }

  @Override
  public Object getSystemService(String name) {
    if(SERVICE_NAME.equals(name)) {
      return this;
    }
    return super.getSystemService(name);
  }
}
