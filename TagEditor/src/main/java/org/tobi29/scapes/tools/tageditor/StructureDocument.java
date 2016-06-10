package org.tobi29.scapes.tools.tageditor;

import java8.util.Optional;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.tobi29.scapes.engine.swt.util.framework.Document;
import org.tobi29.scapes.engine.swt.util.framework.MultiDocumentApplication;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenuBar;
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.tools.tageditor.node.FileStructureNode;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;

public class StructureDocument implements Document {
    private final Optional<FilePath> path;
    private final TagStructure tagStructure;

    public StructureDocument() {
        this(Optional.empty());
    }

    public StructureDocument(FilePath path) {
        this(Optional.of(path));
    }

    public StructureDocument(Optional<FilePath> path) {
        this(path, tagStructure(path));
    }

    public StructureDocument(Optional<FilePath> path,
            TagStructure tagStructure) {
        this.path = path;
        this.tagStructure = tagStructure;
    }

    private static TagStructure tagStructure(Optional<FilePath> path) {
        if (path.isPresent()) {
            Optional<TagStructure> tagStructure =
                    FileStructureNode.structure(path.get());
            if (tagStructure.isPresent()) {
                return tagStructure.get();
            }
        }
        return new TagStructure();
    }

    @Override
    public void forceClose() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String title() {
        return path.map(String::valueOf).orElse("Untitled");
    }

    @Override
    public String shortTitle() {
        return path.map(FilePath::getFileName).map(String::valueOf)
                .orElse("Untitled");
    }

    @Override
    public boolean empty() {
        return !path.isPresent();
    }

    @Override
    public void populate(Composite composite, SmartMenuBar menu,
            MultiDocumentApplication application) {
        Menu file = menu.menu("File");
        MenuItem fileClose = new MenuItem(file, SWT.PUSH);
        fileClose.setText("Close");
        fileClose.addListener(SWT.Selection,
                event -> application.closeTab(composite));
        composite.setLayout(new FillLayout());
        TagEditorWidget editor = new TagEditorWidget(composite, SWT.NONE);
        new FileStructureNode(editor, path, tagStructure).init();
    }
}
