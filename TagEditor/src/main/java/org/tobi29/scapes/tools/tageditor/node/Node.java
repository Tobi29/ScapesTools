/*
 * Copyright 2012-2015 Tobi29
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
package org.tobi29.scapes.tools.tageditor.node;

import org.tobi29.scapes.engine.swt.util.widgets.SmartMenu;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

public abstract class Node {
    protected final TreeNode node;

    protected Node(TreeNode node) {
        this.node = node;
        node.node = this;
    }

    public abstract void expand();

    public abstract void rightClick(SmartMenu menu);
}
