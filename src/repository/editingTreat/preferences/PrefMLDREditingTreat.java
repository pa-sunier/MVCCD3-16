package repository.editingTreat.preferences;

import main.MVCCDElement;
import preferences.Preferences;
import repository.editingTreat.EditingTreat;
import utilities.window.editor.DialogEditor;
import utilities.window.editor.PanelInputContent;
import window.editor.preferences.project.mldr.PrefMLDREditor;

import java.awt.*;

public class PrefMLDREditingTreat extends EditingTreat {


    @Override
    protected PanelInputContent getPanelInputContent(MVCCDElement element) {
        return null;
    }

    @Override
    protected DialogEditor getDialogEditor(Window owner, MVCCDElement parent, MVCCDElement element, String mode) {
        return new PrefMLDREditor(owner , null, (Preferences) element,
                mode, new PrefMLDREditingTreat());
    }

    @Override
    protected String getPropertyTheElement() {
        return null;
    }

}
