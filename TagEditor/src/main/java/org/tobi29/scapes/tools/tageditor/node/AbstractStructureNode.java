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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.tobi29.scapes.engine.swt.util.widgets.Dialogs;
import org.tobi29.scapes.engine.swt.util.widgets.InputDialog;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenu;
import org.tobi29.scapes.engine.utils.ArrayUtil;
import org.tobi29.scapes.engine.utils.Pair;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractStructureNode extends Node {
    protected final String name;
    protected final List<Pair<AbstractStructureNode, TagStructure>>
            childStructures = new ArrayList<>();
    protected final List<ListNode> childLists = new ArrayList<>();
    protected TagStructure tagStructure;

    protected AbstractStructureNode(TreeNode node, String name) {
        super(node);
        this.name = name;
    }

    public void init(TagStructure tagStructure) {
        this.tagStructure = tagStructure;
        tagStructure.getTagEntrySet().forEach(entry -> {
            Object value = entry.getValue();
            if (value instanceof TagStructure) {
                childStructures
                        .add(new Pair<>(new StructureNode(this, entry.getKey()),
                                (TagStructure) value));
            } else if (value instanceof TagStructure.StructureList) {
                childLists.add(new ListNode(this, entry.getKey()));
            } else {
                new TagNode(this, entry.getKey(), value);
            }
        });
    }

    public abstract void changed();

    @Override
    public void expand() {
        childStructures.forEach(pair -> pair.a.init(pair.b));
        childLists.forEach(ListNode::init);
        childStructures.clear();
        childLists.clear();
    }

    @Override
    public void rightClick(SmartMenu menu) {
        SmartMenu add = menu.menu("Add");
        add.action("Structure", this::addStructure);
        add.action("List", this::addList);
        add.action("Boolean...", this::addBoolean);
        add.action("Byte...", this::addByte);
        add.action("Byte[]...", this::addByteArray);
        add.action("Int16...", this::addInt16);
        add.action("Int32...", this::addInt32);
        add.action("Int64...", this::addInt64);
        add.action("Float32...", this::addFloat32);
        add.action("Float64...", this::addFloat64);
        add.action("String...", this::addString);
    }

    private void addStructure() {
        InputDialog dialog = new InputDialog(node.getParent().getShell(),
                "Add Structure...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        dialog.open(() -> {
            String name = nameField.getText();
            if (!checkValidAdd(name)) {
                return;
            }
            tagStructure.getStructure(name);
            new StructureNode(this, name).init(tagStructure);
            changed();
        });
    }

    private void addList() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add List...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        dialog.open(() -> {
            String name = nameField.getText();
            if (!checkValidAdd(name)) {
                return;
            }
            tagStructure.setList(name, Collections.emptyList());
            new ListNode(this, name).init();
            changed();
        });
    }

    private void addBoolean() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Boolean...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(0);
        valueField.setMaximum(1);
        dialog.open(() -> {
            String name = nameField.getText();
            if (!checkValidAdd(name)) {
                return;
            }
            boolean value = valueField.getSelection() != 0;
            tagStructure.setBoolean(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addByte() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Byte...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(Byte.MIN_VALUE);
        valueField.setMaximum(Byte.MAX_VALUE);
        dialog.open(() -> {
            String name = nameField.getText();
            if (!checkValidAdd(name)) {
                return;
            }
            byte value = (byte) valueField.getSelection();
            tagStructure.setByte(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addByteArray() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Byte[]...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        dialog.open(() -> {
            try {
                String name = nameField.getText();
                if (!checkValidAdd(name)) {
                    return;
                }
                byte[] value = ArrayUtil.fromHexadecimal(valueField.getText());
                tagStructure.setByteArray(name, value);
                new TagNode(this, name, value);
                changed();
            } catch (IOException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse array:\n" + e.getMessage());
            }
        });
    }

    private void addInt16() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Int16...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(Short.MIN_VALUE);
        valueField.setMaximum(Short.MAX_VALUE);
        dialog.open(() -> {
            String name = nameField.getText();
            if (!checkValidAdd(name)) {
                return;
            }
            short value = (short) valueField.getSelection();
            tagStructure.setShort(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addInt32() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Int32...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(Integer.MIN_VALUE);
        valueField.setMaximum(Integer.MAX_VALUE);
        dialog.open(() -> {
            String name = nameField.getText();
            if (!checkValidAdd(name)) {
                return;
            }
            int value = valueField.getSelection();
            tagStructure.setInteger(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addInt64() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Int64...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        dialog.open(() -> {
            try {
                String name = nameField.getText();
                if (!checkValidAdd(name)) {
                    return;
                }
                long value = Long.parseLong(valueField.getText());
                tagStructure.setLong(name, value);
                new TagNode(this, name, value);
                changed();
            } catch (NumberFormatException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse array:\n" + e.getMessage());
            }
        });
    }

    private void addFloat32() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Float32...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        dialog.open(() -> {
            try {
                String name = nameField.getText();
                if (!checkValidAdd(name)) {
                    return;
                }
                float value = Float.parseFloat(valueField.getText());
                tagStructure.setFloat(name, value);
                new TagNode(this, name, value);
                changed();
            } catch (NumberFormatException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse number:\n" + e.getMessage());
            }
        });
    }

    private void addFloat64() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add Float64...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        dialog.open(() -> {
            try {
                String name = nameField.getText();
                if (!checkValidAdd(name)) {
                    return;
                }
                double value = Double.parseDouble(valueField.getText());
                tagStructure.setDouble(name, value);
                new TagNode(this, name, value);
                changed();
            } catch (NumberFormatException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse number:\n" + e.getMessage());
            }
        });
    }

    private void addString() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Add String...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        dialog.open(() -> {
            String name = nameField.getText();
            if (!checkValidAdd(name)) {
                return;
            }
            String value = valueField.getText();
            tagStructure.setString(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    protected boolean checkValidAdd(String name) {
        if (tagStructure.has(name)) {
            Dialogs.openMessage(node.getParent().getShell(), SWT.ICON_WARNING,
                    "Failed to add", name + " already exists!");
            return false;
        }
        return true;
    }
}
