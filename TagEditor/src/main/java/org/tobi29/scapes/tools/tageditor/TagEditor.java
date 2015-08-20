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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.tobi29.scapes.tools.tageditor.node.DirectoryNode;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWindow;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TagEditor {
    private final TagEditorWindow shell;

    public TagEditor(Display display) {
        shell = new TagEditorWindow(display, SWT.SHELL_TRIM);
        shell.editorWidget
                .addListener(SWT.Expand, event -> expanded(event.item));
        //shell.fileOpenFile.addListener(SWT.Selection, event -> fileOpenFile());
        shell.fileOpenDirectory
                .addListener(SWT.Selection, event -> fileOpenDirectory());
    }

    @SuppressWarnings("CallToSystemExit")
    public static void main(String[] args) {
        Display.setAppName("Scapes Tag Editor");
        Display display = Display.getDefault();
        new TagEditor(display).run();
    }

    public void run() {
        shell.open();
        Display display = shell.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public void expanded(Widget item) {
        ((TreeNode) item).node.expand();
    }

    /*public void fileOpenFile() {
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
    }*/

    public void fileOpenDirectory() {
        DirectoryDialog dialog = new DirectoryDialog(shell);
        dialog.setMessage("Open Directory...");
        String file = dialog.open();
        if (file == null) {
            return;
        }
        shell.editorWidget.removeAll();
        Path path = Paths.get(file);
        new DirectoryNode(shell.editorWidget, path).init();
    }
}
