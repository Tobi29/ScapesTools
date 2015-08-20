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

import org.eclipse.swt.widgets.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobi29.scapes.engine.utils.io.ReadableByteStream;
import org.tobi29.scapes.engine.utils.io.filesystem.FileUtil;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.engine.utils.io.tag.TagStructureBinary;
import org.tobi29.scapes.engine.utils.io.tag.TagStructureJSON;
import org.tobi29.scapes.engine.utils.io.tag.TagStructureXML;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;
import org.tobi29.scapes.tools.tageditor.ui.TreeNode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class FileStructureNode extends AbstractStructureNode {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(FileStructureNode.class);
    protected final Path path;

    public FileStructureNode(Node parent, Path path,
            TagStructure tagStructure) {
        this(new TreeNode(parent.node, path.getFileName().toString(),
                "File -> Structure"), path, tagStructure);
    }

    public FileStructureNode(TagEditorWidget tree, Path path,
            TagStructure tagStructure) {
        this(new TreeNode(tree, path.getFileName().toString(),
                "File -> Structure"), path, tagStructure);
    }

    private FileStructureNode(TreeNode node, Path path,
            TagStructure tagStructure) {
        super(node, path.getFileName().toString());
        this.path = path;
        this.tagStructure = tagStructure;
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_DirIcon));
    }

    public static Optional<TagStructure> structure(Path path) {
        String name = path.getFileName().toString();
        TagStructureReader reader;
        if (name.endsWith(".stag")) {
            reader = TagStructureBinary::read;
        } else if (name.endsWith(".json")) {
            reader = TagStructureJSON::read;
        } else if (name.endsWith(".xml")) {
            reader = TagStructureXML::read;
        } else {
            return Optional.empty();
        }
        try {
            return FileUtil.readReturn(path,
                    stream -> Optional.of(reader.read(stream)));
        } catch (IOException e) {
            LOGGER.error("Failed to load structure file: {}", e.toString());
        }
        return Optional.empty();
    }

    public void init() {
        init(tagStructure);
    }

    @Override
    public void changed() {
        //node.setIcon(0, node.treeWidget().style()
        //        .standardIcon(QStyle.StandardPixmap.SP_DialogSaveButton));
    }

    @Override
    public void rightClick(Menu menu) {
        super.rightClick(menu);
        // TODO: Add right-click menu
    }

    @FunctionalInterface
    private interface TagStructureReader {
        TagStructure read(ReadableByteStream stream) throws IOException;
    }
}
