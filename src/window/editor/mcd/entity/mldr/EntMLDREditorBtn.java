package window.editor.mcd.entity.mldr;

import mcd.MCDContEntities;
import mcd.MCDEntity;
import preferences.PreferencesManager;
import repository.editingTreat.EditingTreat;
import utilities.window.editor.DialogEditorNavBtn;
import utilities.window.editor.PanelButtons;
import utilities.window.editor.PanelInput;
import utilities.window.editor.PanelNavBtn;

import java.awt.*;

public class EntMLDREditorBtn extends DialogEditorNavBtn {


    public EntMLDREditorBtn(Window owner,
                            MCDContEntities parent,
                            MCDEntity mcdEntity,
                            String mode,
                            EditingTreat editingTreat)  {
        super(owner, parent, mcdEntity, mode, editingTreat);

    }

    @Override
    protected PanelButtons getButtonsCustom() {
        return new EntMLDRButtons(this);
    }

    @Override
    protected PanelInput getInputCustom() {
        return new EntMLDRInput(this);
    }

    @Override
    protected PanelNavBtn getNavCustom() {
        return new EntMLDRNavBtn(this);
    }

    @Override
    protected Dimension getSizeCustom() {
        return PreferencesManager.instance().preferences().getENTITY_WINDOW_SIZE_CUSTOM();
        //return new Dimension(Preferences.ENTITY_WINDOW_WIDTH, Preferences.ENTITY_WINDOW_HEIGHT);
    }

    @Override
    protected void setSizeCustom(Dimension dimension) {
        PreferencesManager.instance().preferences().setENTITY_WINDOW_SIZE_CUSTOM(getSize());
    }

    @Override
    protected Point getLocationCustom() {
        return PreferencesManager.instance().preferences().getENTITY_WINDOW_LOCATION_ONSCREEN();

    }

    @Override
    protected void setLocationCustom(Point point) {
        PreferencesManager.instance().preferences().setENTITY_WINDOW_LOCATION_ONSCREEN(getLocationOnScreen());
    }

    @Override
    protected String getPropertyTitleNew() {
        return "";
    }

    @Override
    protected String getPropertyTitleUpdate() {
        return "editor.entity.update";
    }

    @Override
    protected String getPropertyTitleRead() {
        return "editor.entity.read";
    }


}
