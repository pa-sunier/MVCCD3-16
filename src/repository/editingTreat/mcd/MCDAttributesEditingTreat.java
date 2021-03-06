package repository.editingTreat.mcd;

import main.MVCCDElement;
import mcd.MCDContAttributes;
import repository.editingTreat.EditingTreat;
import utilities.window.editor.DialogEditor;
import utilities.window.editor.PanelInputContent;
import window.editor.mcd.attributes.AttributesEditorBtn;

import java.awt.*;

public class MCDAttributesEditingTreat extends EditingTreat {

    @Override
    protected PanelInputContent getPanelInputContent(MVCCDElement element) {
        return null;
    }

    @Override
    protected DialogEditor getDialogEditor(Window owner, MVCCDElement parent, MVCCDElement element, String mode) {
        return new AttributesEditorBtn(owner , parent, (MCDContAttributes)element, mode,
                new MCDAttributesEditingTreat());
    }

    @Override
    protected String getPropertyTheElement() {
        return null;
    }

 }
