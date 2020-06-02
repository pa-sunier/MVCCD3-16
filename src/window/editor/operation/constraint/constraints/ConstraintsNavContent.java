package window.editor.operation.constraint.constraints;

import mcd.MCDContAttributes;
import mcd.MCDEntity;
import repository.editingTreat.mcd.MCDEntityEditingTreat;
import utilities.window.editor.PanelNavContent;
import utilities.window.scomponents.SButton;
import window.editor.entity.EntityNavContentPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ConstraintsNavContent extends EntityNavContentPanel implements ActionListener {


    public ConstraintsNavContent(ConstraintsNav constraintsNav) {

        super(constraintsNav);
    }

    @Override
    protected void createContentCustom() {
        super.createContentCustom();
        btnConstraints.setEnabled(false);
        btsEnabled.remove(btnConstraints);
    }

}
