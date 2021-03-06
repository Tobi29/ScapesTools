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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class TagEditorWindow extends Shell {
    public final MenuItem fileOpenFile, fileOpenDirectory;
    public final TagEditorWidget editorWidget;

    public TagEditorWindow(Display display, int style) {
        super(display, style);
        setLayout(new FillLayout());
        editorWidget = new TagEditorWidget(this, SWT.NONE);
        Menu menu = new Menu(this, SWT.BAR);
        setMenuBar(menu);
        MenuItem file = new MenuItem(menu, SWT.CASCADE);
        file.setText("File");
        Menu fileMenu = new Menu(file);
        file.setMenu(fileMenu);
        fileOpenFile = new MenuItem(fileMenu, SWT.PUSH);
        fileOpenFile.setText("Open File...");
        fileOpenDirectory = new MenuItem(fileMenu, SWT.PUSH);
        fileOpenDirectory.setText("Open Directory...");
    }

    @Override
    protected void checkSubclass() {
    }
}
