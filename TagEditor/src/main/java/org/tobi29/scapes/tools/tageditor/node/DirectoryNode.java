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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenu;
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath;
import org.tobi29.scapes.engine.utils.io.filesystem.FileUtil;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectoryNode extends Node {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DirectoryNode.class);
    protected final FilePath path;
    protected final List<DirectoryNode> childDirectories = new ArrayList<>();
    protected final List<FileStructureNode> childStructures = new ArrayList<>();
    protected final List<FileArchiveNode> childArchives = new ArrayList<>();

    public DirectoryNode(Node parent, FilePath path) {
        this(new TreeNode(parent.node, String.valueOf(path.getFileName()),
                "Directory"), path);
    }

    public DirectoryNode(TagEditorWidget tree, FilePath path) {
        this(new TreeNode(tree, String.valueOf(path.getFileName()),
                "Directory"), path);
    }

    private DirectoryNode(TreeNode node, FilePath path) {
        super(node);
        this.path = path;
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_DirIcon));
    }

    public void init() {
        try {
            FileUtil.stream(path, stream -> stream.forEach(child -> {
                if (FileUtil.isDirectory(child)) {
                    childDirectories.add(new DirectoryNode(this, child));
                } else if (FileUtil.isRegularFile(child)) {
                    Optional<TagStructure> tagStructure =
                            FileStructureNode.structure(child);
                    if (tagStructure.isPresent()) {
                        childStructures.add(new FileStructureNode(this, child,
                                tagStructure.get()));
                    } else {
                        if (FileArchiveNode.archive(child)) {
                            childArchives.add(new FileArchiveNode(this, child));
                        } else {
                            new FileUnknownNode(this, child);
                        }
                    }
                }
            }));
        } catch (IOException e) {
            LOGGER.error("Failed to expand directory node", e);
        }
    }

    @Override
    public void expand() {
        childDirectories.forEach(DirectoryNode::init);
        childStructures.forEach(FileStructureNode::init);
        childArchives.forEach(FileArchiveNode::init);
        childDirectories.clear();
        childStructures.clear();
        childArchives.clear();
    }

    @Override
    public void rightClick(SmartMenu menu) {
    }
}
