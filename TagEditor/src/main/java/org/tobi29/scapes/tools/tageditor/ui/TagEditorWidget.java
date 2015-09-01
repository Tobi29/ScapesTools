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

import com.trolltech.qt.gui.QContextMenuEvent;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;

import java.util.Arrays;
import java.util.List;

public class TagEditorWidget extends QTreeWidget {
    public TagEditorWidget() {
        setColumnCount(3);
        setHeaderLabels(Arrays.asList("Name", "Value", "Type"));
    }

    @Override
    protected void contextMenuEvent(QContextMenuEvent arg__1) {
        List<QTreeWidgetItem> items = selectedItems();
        if (items.isEmpty()) {
            return;
        }
        QTreeWidgetItem item = items.get(0);
        if (item instanceof TreeNode) {
            QMenu menu = new QMenu();
            ((TreeNode) item).node.rightClick(menu);
            menu.exec(arg__1.globalPos());
        }
    }
}
