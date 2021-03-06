package window.editor.mcd.operation.constraint.constraints;

import utilities.window.editor.PanelButtons;
import utilities.window.editor.PanelButtonsContent;

public class ConstraintsButtons extends PanelButtons {


    public ConstraintsButtons(ConstraintsEditorBtn constraintsEditor) {
        super(constraintsEditor);
    }

    @Override
    protected PanelButtonsContent createButtonsContentCustom() {
        return new ConstraintsButtonsContent(this);
    }
}
