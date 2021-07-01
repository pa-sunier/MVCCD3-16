package test.entites.onglets.contenus.general;

import test.entites.EntiteOnglets;
import utilities.window.editor.PanelInput;
import utilities.window.editor.PanelInputContent;

public class NewGeneraliteInput extends PanelInput {
    final String name = "Généralités";

    public NewGeneraliteInput(EntiteOnglets entiteOnglets) {
        super(entiteOnglets);
    }

    @Override
    protected PanelInputContent createInputContentCustom() {
        return new NewGeneraliteInputContent(this);
    }

    @Override
    public String getName() {
        return name;
    }
}
