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
import org.tobi29.scapes.engine.utils.Pair;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends Node {
    protected final List<Pair<ListStructureNode, TagStructure>>
            childStructures = new ArrayList<>();
    protected final AbstractStructureNode parent;
    protected String name;

    public ListNode(AbstractStructureNode parent, String name) {
        this(new TreeNode(parent.node, name, "List"), parent, name);
    }

    public ListNode(TagEditorWidget tree, AbstractStructureNode parent,
            String name) {
        this(new TreeNode(tree, name, "List"), parent, name);
    }

    private ListNode(TreeNode node, AbstractStructureNode parent, String name) {
        super(node);
        this.parent = parent;
        this.name = name;
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_DirIcon));
    }

    public void init() {
        List<TagStructure> tagStructures = parent.tagStructure.getList(name);
        for (int i = 0; i < tagStructures.size(); i++) {
            childStructures.add(new Pair<>(new ListStructureNode(this, i),
                    tagStructures.get(i)));
        }
    }

    public void changed() {
        parent.changed();
    }

    @Override
    public void expand() {
        childStructures.forEach(pair -> pair.a.init(pair.b));
        childStructures.clear();
    }

    @Override
    public void rightClick(SmartMenu menu) {
        // TODO: Add right-click menu
    }
}
