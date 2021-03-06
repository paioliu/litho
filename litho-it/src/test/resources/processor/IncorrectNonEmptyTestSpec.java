/*
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho.processor.integration.resources;

import com.facebook.litho.annotations.TestSpec;
import java.util.List;

/* error: @TestSpecs interfaces must have no members. */
@TestSpec(com.facebook.litho.processor.integration.resources.BasicLayoutSpec.class)
public interface IncorrectNonEmptyTestSpec {
  void test();
  List<Integer> list();
}