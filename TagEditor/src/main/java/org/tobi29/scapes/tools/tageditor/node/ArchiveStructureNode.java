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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobi29.scapes.engine.swt.util.widgets.Dialogs;
import org.tobi29.scapes.engine.swt.util.widgets.InputDialog;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.io.IOException;

public class ArchiveStructureNode extends AbstractStructureNode {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ArchiveStructureNode.class);
    private final FileArchiveNode parent;
    protected String name;

    public ArchiveStructureNode(FileArchiveNode parent, String name) {
        this(new TreeNode(parent.node, name, "Structure"), parent, name);
    }

    public ArchiveStructureNode(TagEditorWidget tree, FileArchiveNode parent,
            String name) {
        this(new TreeNode(tree, name, "Structure"), parent, name);
    }

    private ArchiveStructureNode(TreeNode node, FileArchiveNode parent,
            String name) {
        super(node, name);
        this.parent = parent;
        this.name = name;
    }

    @Override
    public void changed() {
        parent.changed();
        try {
            parent.tagArchive.setTagStructure(name, tagStructure);
        } catch (IOException e) {
            Dialogs.openMessage(node.getParent().getShell(), SWT.ICON_ERROR,
                    "Failed to update archive",
                    "Failed to update archive entry:\n" + e.getMessage());
        }
    }

    @Override
    public void rightClick(Menu menu) {
        super.rightClick(menu);
        MenuItem rename = new MenuItem(menu, SWT.PUSH);
        rename.setText("Rename");
        rename.addListener(SWT.Selection, event -> rename());
        MenuItem delete = new MenuItem(menu, SWT.PUSH);
        delete.setText("Delete");
        delete.addListener(SWT.Selection, event -> delete());
    }

    private void rename() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Rename...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        nameField.setText(name);
        dialog.open(() -> {
            String name = nameField.getText();
            if (!this.name.equals(name) && checkValidName(name)) {
                parent.tagArchive.moveTagStructure(this.name, name);
                this.name = name;
                node.setText(0, name);
                parent.changed();
            }
        });
    }

    private void delete() {
        parent.tagArchive.removeTagStructure(name);
        node.dispose();
        parent.changed();
    }

    protected boolean checkValidName(String name) {
        if (parent.tagArchive.hasTagStructure(name)) {
            Dialogs.openMessage(node.getParent().getShell(), SWT.ICON_WARNING,
                    "Failed to rename", name + " already exists!");
            return false;
        }
        return true;
    }
}
