package window.editor.mcd.relends;


import utilities.window.editor.PanelInput;
import utilities.window.editor.PanelInputContent;

public class RelEndsInput extends PanelInput {

    public RelEndsInput(RelEndsEditorBtn relEndsEditor) {
        super(relEndsEditor);
    }


    @Override
    protected PanelInputContent createInputContentCustom() {
        return new RelEndsInputContent(this);
    }
}
