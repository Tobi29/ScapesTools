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

import com.trolltech.qt.gui.*;
import org.tobi29.scapes.engine.qt.util.InputDialog;
import org.tobi29.scapes.engine.utils.ArrayUtil;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

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
        node.setIcon(0, node.treeWidget().style()
                .standardIcon(QStyle.StandardPixmap.SP_FileIcon));
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
    public void rightClick(QMenu menu) {
        QMenu setMenu = menu.addMenu("Set");
        setMenu.addAction("Boolean", this, "setBoolean()");
        setMenu.addAction("Byte", this, "setByte()");
        setMenu.addAction("Byte[]", this, "setByteArray()");
        setMenu.addAction("Int16", this, "setInt16()");
        setMenu.addAction("Int32", this, "setInt32()");
        setMenu.addAction("Int64", this, "setInt64()");
        setMenu.addAction("Float32", this, "setFloat32()");
        setMenu.addAction("Float64", this, "setFloat64()");
        setMenu.addAction("String", this, "setString()");
        menu.addAction("Rename", this, "rename()");
        menu.addAction("Delete", this, "delete()");
    }

    private void setBoolean() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Boolean...");
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(0, 1);
        valueField.setValue(parent.tagStructure.getBoolean(name) ? 1 : 0);
        dialog.show(() -> {
            boolean value = valueField.value() > 0;
            parent.tagStructure.setBoolean(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setByte() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Byte...");
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Byte.MIN_VALUE, Byte.MAX_VALUE);
        valueField.setValue(parent.tagStructure.getByte(name));
        dialog.show(() -> {
            byte value = (byte) valueField.value();
            parent.tagStructure.setByte(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setByteArray() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Byte[]...");
        QTextEdit valueField = dialog.add("Value", new QTextEdit());
        valueField.setText(ArrayUtil
                .toHexadecimal(1, parent.tagStructure.getByteArray(name)));
        dialog.show(() -> {
            String value = valueField.toPlainText();
            parent.tagStructure.setString(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setInt16() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Int16...");
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Short.MIN_VALUE, Short.MAX_VALUE);
        valueField.setValue(parent.tagStructure.getShort(name));
        dialog.show(() -> {
            short value = (short) valueField.value();
            parent.tagStructure.setShort(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setInt32() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Int32...");
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        valueField.setValue(parent.tagStructure.getInteger(name));
        dialog.show(() -> {
            int value = valueField.value();
            parent.tagStructure.setInteger(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setInt64() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Int64...");
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        valueField.setValue((int) parent.tagStructure.getLong(name));
        dialog.show(() -> {
            long value = valueField.value();
            parent.tagStructure.setLong(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setFloat32() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Float32...");
        QDoubleSpinBox valueField = dialog.add("Value", new QDoubleSpinBox());
        valueField.setRange(Float.MIN_VALUE, Float.MAX_VALUE);
        valueField.setValue(parent.tagStructure.getFloat(name));
        dialog.show(() -> {
            float value = (float) valueField.value();
            parent.tagStructure.setFloat(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setFloat64() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Float64...");
        QDoubleSpinBox valueField = dialog.add("Value", new QDoubleSpinBox());
        valueField.setRange(Double.MIN_VALUE, Double.MAX_VALUE);
        valueField.setValue(parent.tagStructure.getDouble(name));
        dialog.show(() -> {
            double value = valueField.value();
            parent.tagStructure.setDouble(name, value);
            setValue(value);
            parent.changed();
        });
    }

    private void setString() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set String...");
        QTextEdit valueField = dialog.add("Value", new QTextEdit());
        valueField.setText(parent.tagStructure.getString(name));
        dialog.show(() -> {
            String value = valueField.toPlainText();
            parent.tagStructure.setString(name, value);
            setValue(value);
            parent.changed();
        });
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

    protected void setValue(Object value) {
        node.setText(1, value.toString());
        node.setText(2, type(value));
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
