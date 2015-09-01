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
import org.tobi29.scapes.engine.utils.Pair;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStructureNode extends Node {
    protected final String name;
    protected final List<Pair<AbstractStructureNode, TagStructure>>
            childStructures = new ArrayList<>();
    protected final List<Pair<ListNode, TagStructure.StructureList>>
            childLists = new ArrayList<>();
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
                childLists.add(new Pair<>(new ListNode(this, entry.getKey()),
                        (TagStructure.StructureList) value));
            } else {
                new TagNode(this, entry.getKey(), value);
            }
        });
    }

    public abstract void changed();

    @Override
    public void expand() {
        childStructures.forEach(pair -> pair.a.init(pair.b));
        childLists.forEach(pair -> pair.a.init(pair.b));
        childStructures.clear();
        childLists.clear();
    }

    @Override
    public void rightClick(QMenu menu) {
        QMenu addMenu = menu.addMenu("Add");
        addMenu.addAction("Boolean", this, "addBoolean()");
        addMenu.addAction("Byte", this, "addByte()");
        addMenu.addAction("Byte[]", this, "addByteArray()");
        addMenu.addAction("Int16", this, "addInt16()");
        addMenu.addAction("Int32", this, "addInt32()");
        addMenu.addAction("Int64", this, "addInt64()");
        addMenu.addAction("Float32", this, "addFloat32()");
        addMenu.addAction("Float64", this, "addFloat64()");
        addMenu.addAction("String", this, "addString()");
    }

    private void addBoolean() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Boolean...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(0, 1);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            boolean value = valueField.value() > 0;
            tagStructure.setBoolean(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addByte() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Byte...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Byte.MIN_VALUE, Byte.MAX_VALUE);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            byte value = (byte) valueField.value();
            tagStructure.setByte(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addByteArray() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Byte[]...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QTextEdit valueField = dialog.add("Value", new QTextEdit());
        dialog.show(() -> {
            try {
                String name = nameField.toPlainText();
                byte[] value =
                        ArrayUtil.fromHexadecimal(valueField.toPlainText());
                tagStructure.setByteArray(name, value);
                new TagNode(this, name, value);
                changed();
            } catch (IOException e) {
                QMessageBox.warning(node.treeWidget(), "Failed to set",
                        "Invalid byte array:\n" + e.toString());
            }
        });
    }

    private void addInt16() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Int16...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Short.MIN_VALUE, Short.MAX_VALUE);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            short value = (short) valueField.value();
            tagStructure.setShort(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addInt32() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Int32...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            int value = valueField.value();
            tagStructure.setInteger(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addInt64() {
        InputDialog dialog = new InputDialog(node.treeWidget(), "Set Int64...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QSpinBox valueField = dialog.add("Value", new QSpinBox());
        valueField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            long value = valueField.value();
            tagStructure.setLong(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addFloat32() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Float32...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QDoubleSpinBox valueField = dialog.add("Value", new QDoubleSpinBox());
        valueField.setRange(Float.MIN_VALUE, Float.MAX_VALUE);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            float value = (float) valueField.value();
            tagStructure.setFloat(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addFloat64() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set Float64...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QDoubleSpinBox valueField = dialog.add("Value", new QDoubleSpinBox());
        valueField.setRange(Double.MIN_VALUE, Double.MAX_VALUE);
        dialog.show(() -> {
            String name = nameField.toPlainText();
            double value = valueField.value();
            tagStructure.setDouble(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }

    private void addString() {
        InputDialog dialog =
                new InputDialog(node.treeWidget(), "Set String...");
        QTextEdit nameField = dialog.add("Name", new QTextEdit());
        QTextEdit valueField = dialog.add("Value", new QTextEdit());
        dialog.show(() -> {
            String name = nameField.toPlainText();
            String value = valueField.toPlainText();
            tagStructure.setString(name, value);
            new TagNode(this, name, value);
            changed();
        });
    }
}
