package window.editor.preferences.application;

import main.MVCCDElement;
import main.MVCCDElementApplicationPreferences;
import main.MVCCDManager;
import utilities.window.editor.DialogEditor;
import preferences.Preferences;
import utilities.window.editor.PanelButtons;
import utilities.window.editor.PanelInput;

import java.awt.*;
import java.awt.event.WindowEvent;

public class PrefApplicationEditor extends DialogEditor {

    public PrefApplicationEditor(Window owner,
                                 MVCCDElement parent,
                                 MVCCDElementApplicationPreferences applPref,
                                 String mode)  {
        super(owner, parent, applPref, mode, DialogEditor.NOTHING);

        /*
        super.setSize(Preferences.PREFERENCES_WINDOW_WIDTH, Preferences.PREFERENCES_WINDOW_HEIGHT);
        super.setInput(new PrefApplicationInput(this));
        super.setButtons (new PrefApplicationButtons(this));

        super.start();

         */
    }

    @Override
    protected String getPropertyTitleNew() {
        return null;
    }

    @Override
    protected String getPropertyTitleUpdate() {
        return "preferences.application.update";
    }

    @Override
    protected PanelButtons getButtonsCustom() {
        return new PrefApplicationButtons(this);
    }

    @Override
    protected PanelInput getInputCustom() {
        return new PrefApplicationInput(this);
    }

    @Override
    protected Dimension getSizeCustom() {
        return new Dimension(Preferences.PREFERENCES_WINDOW_WIDTH, Preferences.PREFERENCES_WINDOW_HEIGHT);
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {
        MVCCDManager.instance().setDatasProjectEdited(false);
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }


}