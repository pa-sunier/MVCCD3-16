package repository.editingTreat.preferences;

import main.MVCCDElement;
import main.MVCCDElementApplicationPreferences;
import main.MVCCDManager;
import repository.editingTreat.EditingTreat;
import utilities.window.editor.DialogEditor;
import utilities.window.editor.PanelInputContent;
import window.editor.preferences.application.PrefApplicationEditor;

import java.awt.*;
import java.util.ArrayList;

public class PrefApplEditingTreat extends EditingTreat {


    @Override
    protected PanelInputContent getPanelInputContent(MVCCDElement element) {
        return null;
    }
    @Override
    protected DialogEditor getDialogEditor(Window owner, MVCCDElement parent, MVCCDElement element, String mode) {
        return new PrefApplicationEditor(owner , null,
                ( MVCCDElementApplicationPreferences) element, DialogEditor.UPDATE);

    }

    @Override
    protected String getPropertyTheElement() {
        return null;
    }


    @Override
    protected ArrayList<String> checkCompliant(MVCCDElement mvccdElement) {
        ArrayList<String> resultat = new ArrayList<String>();
        return resultat;
    }
}