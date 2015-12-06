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
import org.tobi29.scapes.engine.utils.Pair;
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath;
import org.tobi29.scapes.engine.utils.io.filesystem.FileUtil;
import org.tobi29.scapes.engine.utils.io.tag.TagStructureArchive;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileArchiveNode extends Node {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(FileArchiveNode.class);
    protected final FilePath path;
    protected final List<Pair<ArchiveStructureNode, TagStructureArchive.Entry>>
            childStructures = new ArrayList<>();
    protected TagStructureArchive tagArchive;

    public FileArchiveNode(Node parent, FilePath path) {
        this(new TreeNode(parent.node, path.getFileName().toString(),
                "File -> Archive"), path);
    }

    public FileArchiveNode(TagEditorWidget tree, FilePath path) {
        this(new TreeNode(tree, path.getFileName().toString(),
                "File -> Archive"), path);
    }

    private FileArchiveNode(TreeNode node, FilePath path) {
        super(node);
        this.path = path;
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_DirIcon));
    }

    public static boolean archive(FilePath path) {
        String name = path.getFileName().toString();
        return name.endsWith(".star");
    }

    public void init() {
        try {
            FileUtil.read(path, stream -> {
                TagStructureArchive.readHeader(stream).forEach(
                        entry -> childStructures.add(new Pair<>(
                                new ArchiveStructureNode(this, entry.getName()),
                                entry)));
            });
        } catch (IOException e) {
            LOGGER.warn("Failed to read tag structure header", e);
        }
    }

    public void changed() {
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_DialogSaveButton));
    }

    @Override
    public void expand() {
        tagArchive = new TagStructureArchive();
        try {
            FileUtil.read(path, stream -> {
                tagArchive.read(stream);
                for (Pair<ArchiveStructureNode, TagStructureArchive.Entry> pair : childStructures) {
                    tagArchive.getTagStructure(pair.b.getName())
                            .ifPresent(pair.a::init);
                }
            });
        } catch (IOException e) {
            LOGGER.warn("Failed to load tag structure", e);
        }
        childStructures.clear();
    }

    @Override
    public void rightClick(SmartMenu menu) {
        // TODO: Add right-click menu
    }
}
