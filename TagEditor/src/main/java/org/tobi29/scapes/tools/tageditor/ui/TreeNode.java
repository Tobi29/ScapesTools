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
package org.tobi29.scapes.tools.tageditor.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.tobi29.scapes.tools.tageditor.node.Node;

public class TreeNode extends TreeItem {
    public Node node;

    public TreeNode(TreeItem parent, String name, String type) {
        super(parent, SWT.NONE);
        setText(0, name);
        setText(2, type);
    }

    public TreeNode(TreeItem parent, String name, String value, String type) {
        this(parent, name, type);
        setText(1, value);
    }

    public TreeNode(Tree parent, String name, String type) {
        super(parent, SWT.NONE);
        setText(0, name);
        setText(2, type);
    }

    public TreeNode(Tree parent, String name, String value, String type) {
        this(parent, name, type);
        setText(1, value);
    }

    @Override
    protected void checkSubclass() {
    }
}
