package org.tobi29.scapes.tools.tageditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.tobi29.scapes.engine.swt.util.framework.Document;
import org.tobi29.scapes.engine.swt.util.framework.MultiDocumentApplication;
import org.tobi29.scapes.engine.swt.util.widgets.SmartMenuBar;
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath;
import org.tobi29.scapes.tools.tageditor.node.DirectoryNode;
import org.tobi29.scapes.tools.tageditor.ui.TagEditorWidget;

public class DirectoryDocument extends Document {
    private final FilePath path;

    public DirectoryDocument(FilePath path) {
        this.path = path;
    }

    @Override
    public void forceClose() {
    }

    @Override
    protected String title() {
        return String.valueOf(path);
    }

    @Override
    protected String shortTitle() {
        return String.valueOf(path.getFileName());
    }

    @Override
    protected boolean empty() {
        return false;
    }

    @Override
    protected void populate(Composite composite, SmartMenuBar menu,
            MultiDocumentApplication application) {
        Menu file = menu.menu("File");
        MenuItem fileClose = new MenuItem(file, SWT.PUSH);
        fileClose.setText("Close");
        fileClose.addListener(SWT.Selection,
                event -> application.closeTab(composite));
        composite.setLayout(new FillLayout());
        TagEditorWidget editor = new TagEditorWidget(composite, SWT.NONE);
        new DirectoryNode(editor, path).init();
    }
}
