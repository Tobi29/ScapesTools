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

import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

public class ListStructureNode extends AbstractStructureNode {
    private final ListNode parent;
    private int index;

    public ListStructureNode(ListNode parent, int index) {
        this(new TreeNode(parent.node, String.valueOf(index), "Structure"),
                parent, index);
    }

    public ListStructureNode(TagEditorWidget tree, ListNode parent, int index) {
        this(new TreeNode(tree, String.valueOf(index), "Structure"), parent,
                index);
    }

    private ListStructureNode(TreeNode node, ListNode parent, int index) {
        super(node, String.valueOf(index));
        this.parent = parent;
        this.index = index;
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_DirIcon));
    }

    @Override
    public void changed() {
        parent.changed();
    }
}
