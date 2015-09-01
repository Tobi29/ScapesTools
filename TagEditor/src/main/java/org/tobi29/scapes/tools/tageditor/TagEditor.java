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

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QTreeWidgetItem;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.tools.tageditor.node.DirectoryNode;
import org.tobi29.scapes.tools.tageditor.node.FileArchiveNode;
import org.tobi29.scapes.tools.tageditor.node.FileStructureNode;
import org.tobi29.scapes.tools.tageditor.node.FileUnknownNode;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWindow;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class TagEditor {
    private final TagEditorWindow shell;

    public TagEditor() {
        shell = new TagEditorWindow();
        shell.editorWidget.itemExpanded
                .connect(this, "expanded(QTreeWidgetItem)");
        shell.fileOpenFile.triggered.connect(this, "fileOpenFile()");
        shell.fileOpenDirectory.triggered.connect(this, "fileOpenDirectory()");
        shell.resize(640, 480);
    }

    @SuppressWarnings("CallToSystemExit")
    public static void main(String[] args) {
        QApplication.initialize("Scapes Tag Editor", args);
        new TagEditor().run();
        QApplication.shutdown();
        System.exit(0);
    }

    public void run() {
        shell.show();
        QApplication.execStatic();
    }

    public void expanded(QTreeWidgetItem item) {
        ((TreeNode) item).node.expand();
        shell.editorWidget.resizeColumnToContents(0);
    }

    public void fileOpenFile() {
        String file = QFileDialog.getOpenFileName(shell, "Open File...", "",
                new QFileDialog.Filter("Tag Structure (*.stag *.json *.xml)"));
        if (file.isEmpty()) {
            return;
        }
        shell.editorWidget.clear();
        Path path = Paths.get(file);
        Optional<TagStructure> tagStructure = FileStructureNode.structure(path);
        if (tagStructure.isPresent()) {
            new FileStructureNode(shell.editorWidget, path, tagStructure.get())
                    .init();
        } else {
            if (FileArchiveNode.archive(path)) {
                new FileArchiveNode(shell.editorWidget, path).init();
            } else {
                new FileUnknownNode(shell.editorWidget, path);
            }
        }
    }

    public void fileOpenDirectory() {
        String file =
                QFileDialog.getExistingDirectory(shell, "Open Directory...");
        if (file.isEmpty()) {
            return;
        }
        shell.editorWidget.clear();
        Path path = Paths.get(file);
        new DirectoryNode(shell.editorWidget, path).init();
    }
}
