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
package org.tobi29.scapes.tools.tageditor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.tobi29.scapes.engine.swt.util.Shortcut;
import org.tobi29.scapes.engine.swt.util.framework.MultiDocumentApplication;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenu;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenuBar;
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath;
import org.tobi29.scapes.engine.utils.io.filesystem.FileUtil;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.tools.tageditor.node.FileStructureNode;

import java.util.Optional;

public class TagEditor extends MultiDocumentApplication {
    public TagEditor() {
        super("Scapes Tag Editor", "TagEditor", "0.0.0_1");
    }

    public static void main(String[] args) {
        new TagEditor().run();
    }

    @Override
    protected void init() {
        openTab(new StructureDocument());
    }

    @Override
    protected void dispose() {
    }

    public void fileOpenFile(Composite composite) {
        FileDialog dialog = new FileDialog(composite.getShell());
        dialog.setText("Open File...");
        dialog.setFilterExtensions(
                new String[]{"*.stag;*.json;*.xml", "*.star"});
        dialog.setFilterNames(
                new String[]{"Tag Structure", "Tag Structure Archive"});
        String file = dialog.open();
        if (file == null) {
            return;
        }
        FilePath path = FileUtil.path(file);
        Optional<TagStructure> tagStructure = FileStructureNode.structure(path);
        if (tagStructure.isPresent()) {
            openNewShell(composite, new StructureDocument(path));
        } else {
            //if (FileArchiveNode.archive(path)) {
            //    new FileArchiveNode(shell.editorWidget, path).init();
            //} else {
            //new FileUnknownNode(shell.editorWidget, path);
            //}
        }
    }

    public void fileOpenDirectory(Composite composite) {
        DirectoryDialog dialog = new DirectoryDialog(composite.getShell());
        dialog.setText("Open Directory...");
        String file = dialog.open();
        if (file == null) {
            return;
        }
        FilePath path = FileUtil.path(file);
        openNewShell(composite, new DirectoryDocument(path));
    }

    @Override
    protected void populate(Composite composite, SmartMenuBar menu) {
        SmartMenu file = menu.menu("File");
        file.action("Open File...", () -> fileOpenFile(composite),
                Shortcut.get("File.OpenFile", 'O', Shortcut.Modifier.CONTROL));
        file.action("Open Directory...", () -> fileOpenDirectory(composite),
                Shortcut.get("File.OpenDirectory", 'O',
                        Shortcut.Modifier.CONTROL, Shortcut.Modifier.SHIFT));
    }
}
