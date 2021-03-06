package window.editor.mcd.operation.parameter;

import mcd.MCDOperation;
import mcd.MCDParameter;
import repository.editingTreat.EditingTreat;
import utilities.window.editor.PanelButtons;

import java.awt.*;

public class ParameterTransientEditor extends ParameterEditor {

    public static final int UNIQUE = 1 ;
    public static final int NID = 2 ;


    public ParameterTransientEditor(Window owner,
                                    MCDOperation mcdOperation,
                                    MCDParameter mcdParameter,
                                    String mode,
                                    int scope,
                                    EditingTreat editingTreat)  {
        super(owner, mcdOperation, mcdParameter, mode, scope, editingTreat);

    }

    @Override
    protected PanelButtons getButtonsCustom() {
        return new ParameterTransientButtons(this);
    }


}
