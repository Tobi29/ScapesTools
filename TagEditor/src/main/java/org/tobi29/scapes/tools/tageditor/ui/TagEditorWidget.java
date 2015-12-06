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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenu;

import java.util.Arrays;

public class TagEditorWidget extends Tree {
    public TagEditorWidget(Composite parent, int style) {
        super(parent, style);
        setHeaderVisible(true);
        TreeColumn name = new TreeColumn(this, SWT.LEFT);
        name.setText("Name");
        TreeColumn value = new TreeColumn(this, SWT.LEFT);
        value.setText("Value");
        TreeColumn type = new TreeColumn(this, SWT.LEFT);
        type.setText("Type");
        SmartMenu menu = new SmartMenu(this);
        setMenu(menu);
        menu.addMenuListener(new MenuAdapter() {
            @Override
            public void menuShown(MenuEvent e) {
                Arrays.stream(menu.getItems()).forEach(MenuItem::dispose);
                TreeItem[] items = getSelection();
                if (items.length > 0) {
                    TreeItem item = items[0];
                    assert item instanceof TreeNode;
                    ((TreeNode) item).node.rightClick(menu);
                }
            }
        });
        // TODO: Better column resizing
        addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Rectangle area = getClientArea();
                name.setWidth(area.width - 400);
                value.setWidth(200);
                type.setWidth(200);
            }
        });
        addListener(SWT.Expand, event -> expanded(event.item));
    }

    public void expanded(Widget item) {
        ((TreeNode) item).node.expand();
    }

    @Override
    protected void checkSubclass() {
    }
}
