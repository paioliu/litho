/*
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho.sections;

import static com.facebook.litho.sections.SectionLifecycle.StateUpdate;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentsLogger;
import com.facebook.litho.EventHandler;
import com.facebook.litho.TreeProps;
import java.lang.ref.WeakReference;

public class SectionContext extends ComponentContext {

  private SectionTree mSectionTree;
  private WeakReference<Section> mScope;
  private EventHandler<LoadingEvent> mTreeLoadingEventHandler;
  private KeyHandler mKeyHandler;

  public SectionContext(Context context) {
    this(context, null, null);
  }

  public SectionContext(ComponentContext context) {
    this(context.getBaseContext(), context.getLogTag(), context.getLogger());
  }

  public SectionContext(Context context, String logTag, ComponentsLogger logger) {
    super(context, logTag, logger);
    mKeyHandler = new KeyHandler();
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
  public static SectionContext withSectionTree(SectionContext context, SectionTree sectionTree) {
    SectionContext sectionContext = new SectionContext(context);
    sectionContext.mSectionTree = sectionTree;
    sectionContext.mTreeLoadingEventHandler = new SectionTreeLoadingEventHandler(sectionTree);

    return sectionContext;
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
  public static SectionContext withScope(SectionContext context, Section scope) {
    SectionContext sectionContext = new SectionContext(context);
    sectionContext.mSectionTree = context.mSectionTree;
    sectionContext.mTreeLoadingEventHandler = context.mTreeLoadingEventHandler;
    sectionContext.mScope = new WeakReference<>(scope);

    return sectionContext;
  }

  /**
   * Notify the {@link SectionTree} that it needs to synchronously perform a state update.
   *
   * @param stateUpdate state update to perform
   */
  public void updateStateSync(StateUpdate stateUpdate) {
    final Section section = mScope.get();
    final SectionTree sectionTree = mSectionTree;
    if (sectionTree == null || section == null) {
      return;
    }

    sectionTree.updateState(section.getGlobalKey(), stateUpdate);
  }

  public void updateStateLazy(StateUpdate stateUpdate) {
    final SectionTree sectionTree = mSectionTree;
    final Section section = mScope.get();

    sectionTree.updateStateLazy(section.getGlobalKey(), stateUpdate);
  }

  /**
   * Notify the {@link SectionTree} that it needs to asynchronously perform a state update.
   * @param stateUpdate state update to perform
   */
  public void updateStateAsync(StateUpdate stateUpdate) {
    final Section section = mScope.get();
    final SectionTree sectionTree = mSectionTree;
    if (sectionTree == null || section == null) {
      return;
    }

    sectionTree.updateStateAsync(section.getGlobalKey(), stateUpdate);
  }

  <E> EventHandler<E> newEventHandler(String name, int id, Object[] params) {
    final Section section = mScope.get();
    if (section == null) {
      throw new IllegalStateException("Called newEventHandler on a released Section");
    }

    return new EventHandler<E>(section, name, id, params);
  }

  public Section getSectionScope() {
    final Section section = mScope.get();

    return section;
  }

  KeyHandler getKeyHandler() {
    return mKeyHandler;
  }

  @Nullable SectionTree getSectionTree() {
    return mSectionTree;
  }

  EventHandler<LoadingEvent> getTreeLoadingEventHandler() {
    return mTreeLoadingEventHandler;
  }

  @Override
  protected void setTreeProps(TreeProps treeProps) {
    super.setTreeProps(treeProps);
  }

  @Override
  protected @Nullable TreeProps getTreeProps() {
    return super.getTreeProps();
  }
}
