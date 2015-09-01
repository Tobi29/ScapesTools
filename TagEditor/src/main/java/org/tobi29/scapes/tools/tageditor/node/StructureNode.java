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
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QTextEdit;
import org.tobi29.scapes.engine.qt.util.InputDialog;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

public class StructureNode extends AbstractStructureNode {
    protected final AbstractStructureNode parent;
    protected String name;

    public StructureNode(AbstractStructureNode parent, String name) {
        this(new TreeNode(parent.node, name, "Structure"), parent, name);
    }

    public StructureNode(TagEditorWidget tree, AbstractStructureNode parent,
            String name) {
        this(new TreeNode(tree, name, "Structure"), parent, name);
    }

    private StructureNode(TreeNode node, AbstractStructureNode parent,
            String name) {
        super(node, name);
        this.parent = parent;
        this.name = name;
        node.setIcon(0, node.treeWidget().style()
                .standardIcon(QStyle.StandardPixmap.SP_DirIcon));
    }

    @Override
    public void changed() {
        parent.changed();
    }

    @Override
    public void rightClick(QMenu menu) {
        super.rightClick(menu);
        menu.addAction("Rename", this, "rename()");
        menu.addAction("Delete", this, "delete()");
    }

    private void rename() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Rename...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        nameField.setText(name);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            if (!this.name.equals(name) && checkValidName(name)) {
                parent.tagStructure.move(this.name, name);
                this.name = name;
                node.setText(0, name);
                parent.changed();
            }
        });
    }

    private void delete() {
        parent.tagStructure.remove(name);
        parent.node.removeChild(node);
        parent.changed();
    }

    protected boolean checkValidName(String name) {
        if (parent.tagStructure.has(name)) {
            QMessageBox.warning(node.treeWidget(), "Failed to rename",
                    name + " already exists!");
            return false;
        }
        return true;
    }
}
