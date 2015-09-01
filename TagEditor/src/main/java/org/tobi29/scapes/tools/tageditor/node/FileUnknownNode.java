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

import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QStyle;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.nio.file.Path;

public class FileUnknownNode extends Node {
    protected final Path path;

    public FileUnknownNode(Node parent, Path path) {
        this(new TreeNode(parent.node, path.getFileName().toString(), "File"),
                path);
    }

    public FileUnknownNode(TagEditorWidget tree, Path path) {
        this(new TreeNode(tree, path.getFileName().toString(), "File"), path);
    }

    private FileUnknownNode(TreeNode node, Path path) {
        super(node);
        this.path = path;
        node.setIcon(0, node.treeWidget().style()
                .standardIcon(QStyle.StandardPixmap.SP_FileIcon));
    }

    @Override
    public void expand() {
    }

    @Override
    public void rightClick(QMenu menu) {
    }
}
