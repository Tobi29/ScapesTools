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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.tobi29.scapes.engine.swt.util.widgets.Dialogs;
import org.tobi29.scapes.engine.swt.util.widgets.InputDialog;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenu;
import org.tobi29.scapes.engine.utils.ArrayUtil;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.io.IOException;

public class TagNode extends Node {
    protected final AbstractStructureNode parent;
    protected String name;

    public TagNode(AbstractStructureNode parent, String name, Object value) {
        this(new TreeNode(parent.node, name, type(value)), parent, name, value);
    }

    public TagNode(TagEditorWidget tree, AbstractStructureNode parent,
            String name, Object value) {
        this(new TreeNode(tree, name, type(value)), parent, name, value);
    }

    private TagNode(TreeNode node, AbstractStructureNode parent, String name,
            Object value) {
        super(node);
        this.parent = parent;
        this.name = name;
        node.setText(1, value.toString());
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_FileIcon));
    }

    protected static String type(Object value) {
        if (value instanceof Boolean) {
            return "Tag -> Boolean";
        } else if (value instanceof Byte) {
            return "Tag -> Byte";
        } else if (value instanceof byte[]) {
            return "Tag -> Byte[]";
        } else if (value instanceof Short) {
            return "Tag -> Int16";
        } else if (value instanceof Integer) {
            return "Tag -> Int32";
        } else if (value instanceof Long) {
            return "Tag -> Int64";
        } else if (value instanceof Float) {
            return "Tag -> Float32";
        } else if (value instanceof Double) {
            return "Tag -> Float64";
        } else if (value instanceof String) {
            return "Tag -> String";
        }
        return "Tag";
    }

    @Override
    public void expand() {
    }

    @Override
    public void rightClick(SmartMenu menu) {
        MenuItem set = new MenuItem(menu, SWT.CASCADE);
        set.setText("Set");
        Menu setMenu = new Menu(set);
        set.setMenu(setMenu);
        MenuItem setBoolean = new MenuItem(setMenu, SWT.PUSH);
        setBoolean.setText("Boolean");
        setBoolean.addListener(SWT.Selection, event -> setBoolean());
        MenuItem setByte = new MenuItem(setMenu, SWT.PUSH);
        setByte.setText("Byte...");
        setByte.addListener(SWT.Selection, event -> setByte());
        MenuItem setByteArray = new MenuItem(setMenu, SWT.PUSH);
        setByteArray.setText("Byte[]...");
        setByteArray.addListener(SWT.Selection, event -> setByteArray());
        MenuItem setInt16 = new MenuItem(setMenu, SWT.PUSH);
        setInt16.setText("Int16");
        setInt16.addListener(SWT.Selection, event -> setInt16());
        MenuItem setInt32 = new MenuItem(setMenu, SWT.PUSH);
        setInt32.setText("Int32");
        setInt32.addListener(SWT.Selection, event -> setInt32());
        MenuItem setInt64 = new MenuItem(setMenu, SWT.PUSH);
        setInt64.setText("Int64");
        setInt64.addListener(SWT.Selection, event -> setInt64());
        MenuItem setFloat32 = new MenuItem(setMenu, SWT.PUSH);
        setFloat32.setText("Float32");
        setFloat32.addListener(SWT.Selection, event -> setFloat32());
        MenuItem setFloat64 = new MenuItem(setMenu, SWT.PUSH);
        setFloat64.setText("Float64");
        setFloat64.addListener(SWT.Selection, event -> setFloat64());
        MenuItem setString = new MenuItem(setMenu, SWT.PUSH);
        setString.setText("String");
        setString.addListener(SWT.Selection, event -> setString());
        MenuItem rename = new MenuItem(menu, SWT.PUSH);
        rename.setText("Rename");
        rename.addListener(SWT.Selection, event -> rename());
        MenuItem delete = new MenuItem(menu, SWT.PUSH);
        delete.setText("Delete");
        delete.addListener(SWT.Selection, event -> delete());
    }

    private void setBoolean() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Boolean...");
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(0);
        valueField.setMaximum(1);
        valueField.setSelection(parent.tagStructure.getBoolean(name) ? 1 : 0);
        dialog.open(() -> {
            boolean value = valueField.getSelection() != 0;
            parent.tagStructure.setBoolean(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setByte() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Byte...");
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(Byte.MIN_VALUE);
        valueField.setMaximum(Byte.MAX_VALUE);
        valueField.setSelection(parent.tagStructure.getByte(name));
        dialog.open(() -> {
            byte value = (byte) valueField.getSelection();
            parent.tagStructure.setByte(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setByteArray() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Byte[]...");
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        valueField.setText(ArrayUtil
                .toHexadecimal(1, parent.tagStructure.getByteArray(name)));
        dialog.open(() -> {
            try {
                byte[] value = ArrayUtil.fromHexadecimal(valueField.getText());
                parent.tagStructure.setByteArray(name, value);
                setValue(value);
                parent.changed();
            } catch (IOException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse array:\n" + e.getMessage());
            }
        });
    }

    private void setInt16() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Int16...");
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(Short.MIN_VALUE);
        valueField.setMaximum(Short.MAX_VALUE);
        valueField.setSelection(parent.tagStructure.getShort(name));
        dialog.open(() -> {
            short value = (short) valueField.getSelection();
            parent.tagStructure.setShort(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setInt32() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Int32...");
        Spinner valueField = dialog.add("Value", p -> new Spinner(p, SWT.NONE));
        valueField.setMinimum(Integer.MIN_VALUE);
        valueField.setMaximum(Integer.MAX_VALUE);
        valueField.setSelection(parent.tagStructure.getInteger(name));
        dialog.open(() -> {
            int value = valueField.getSelection();
            parent.tagStructure.setInteger(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setInt64() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Int64...");
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        valueField.setText(Long.toString(parent.tagStructure.getLong(name)));
        dialog.open(() -> {
            try {
                long value = Long.parseLong(valueField.getText());
                parent.tagStructure.setLong(name, value);
                setValue(value);
                parent.changed();
            } catch (NumberFormatException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse array:\n" + e.getMessage());
            }
        });
    }

    private void setFloat32() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Float32...");
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        valueField.setText(Float.toString(parent.tagStructure.getFloat(name)));
        dialog.open(() -> {
            try {
                float value = Float.parseFloat(valueField.getText());
                parent.tagStructure.setFloat(name, value);
                setValue(value);
                parent.changed();
            } catch (NumberFormatException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse number:\n" + e.getMessage());
            }
        });
    }

    private void setFloat64() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set Float64...");
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        valueField
                .setText(Double.toString(parent.tagStructure.getDouble(name)));
        dialog.open(() -> {
            try {
                double value = Double.parseDouble(valueField.getText());
                parent.tagStructure.setDouble(name, value);
                setValue(value);
                parent.changed();
            } catch (NumberFormatException e) {
                Dialogs.openMessage(node.getParent().getShell(),
                        SWT.ICON_WARNING, "Failed to set value",
                        "Unable to parse number:\n" + e.getMessage());
            }
        });
    }

    private void setString() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Set String...");
        Text valueField = dialog.add("Value", p -> new Text(p, SWT.BORDER));
        valueField.setText(parent.tagStructure.getString(name));
        dialog.open(() -> {
            String value = valueField.getText();
            parent.tagStructure.setString(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void rename() {
        InputDialog dialog =
                new InputDialog(node.getParent().getShell(), "Rename...");
        Text nameField = dialog.add("Name", p -> new Text(p, SWT.BORDER));
        nameField.setText(name);
        dialog.open(() -> {
            String name = nameField.getText();
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
        node.dispose();
        parent.changed();
    }

    protected void setValue(Object value) {
        node.setText(1, value.toString());
        node.setText(2, type(value));
    }

    protected boolean checkValidName(String name) {
        if (parent.tagStructure.has(name)) {
            Dialogs.openMessage(node.getParent().getShell(), SWT.ICON_WARNING,
                    "Failed to rename", name + " already exists!");
            return false;
        }
        return true;
    }
}
