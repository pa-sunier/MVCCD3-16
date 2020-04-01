package newEditor;

import main.MVCCDElement;
import utilities.window.PanelContent;
import utilities.window.scomponents.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public abstract class PanelInputContent
        extends PanelContent
        implements IAccessDialogEditor, IPanelInputContent,
                    FocusListener, DocumentListener,  ItemListener {

    private PanelInput panelInput;
    private boolean alreadyFocusGained = false;
    private ArrayList<SComponent> sComponents = new ArrayList<SComponent>();
    //private boolean readOnly = false;
    private boolean dataInitialized = false;

    public PanelInputContent(PanelInput panelInput) {
        super(panelInput);
        this.panelInput = panelInput;
    }

    protected abstract boolean checkDatas();

    public abstract boolean checkDatasPreSave(boolean unitaire);

    protected abstract void changeField(DocumentEvent e);


    protected abstract void changeFieldSelected(ItemEvent e);

    protected abstract void changeFieldDeSelected(ItemEvent e);

    public abstract void loadDatas(MVCCDElement mvccdElementCrt);

    //protected abstract void initDatas(MVCCDElement mvccdElementParent);
    protected abstract void initDatas();

    public abstract void saveDatas(MVCCDElement mvccdElement);


    @Override
    public void insertUpdate(DocumentEvent e) {
        changeField(e);
        if (alreadyFocusGained) {
            enabledButtons();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        /*
        if (e.getDocument() instanceof  STextField){
            STextField sTextField = (STextField) e.getDocument();
            sTextField.setColorNormal();
        } */
        changeField(e);
        if (alreadyFocusGained) {
            enabledButtons();
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changeField(e);
        if (alreadyFocusGained) {
            enabledButtons();
        }
    }



    @Override
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            changeFieldSelected(e);
        }
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            changeFieldDeSelected(e);
        }
        if (e.getSource() instanceof SCheckBox) {
                SCheckBox checkBox = (SCheckBox) e.getSource();
                enableSubPanels(checkBox);
        }
        if (e.getSource() instanceof SComboBox) {
        }

        if (alreadyFocusGained) {
                enabledButtons();
        }

    }
    protected void enabledButtons() {
        // Le check doit être fait au début pour remettre l'affichage normal
        // de champs testés ensemble
        boolean datasChecked = checkDatasPreSave(true);
        if (datasChangedNow()) {
            if (datasChecked) {
                getButtonsContent().getBtnOk().setEnabled(true);
                getButtonsContent().getBtnApply().setEnabled(true);
            } else {
                getButtonsContent().getBtnOk().setEnabled(false);
                getButtonsContent().getBtnApply().setEnabled(false);
            }
            getButtonsContent().getBtnUndo().setEnabled(true);
        } else {
            getButtonsContent().getBtnUndo().setEnabled(false);
            getButtonsContent().getBtnOk().setEnabled(false);
            getButtonsContent().getBtnApply().setEnabled(false);
        }

    }

    public boolean checkInput(SComponent field, boolean unitaire, ArrayList<String> messagesErrors) {
        if (unitaire) {
            showCheckResultat(messagesErrors);
        }
        if (messagesErrors.size() == 0) {
            field.setColorNormal();
        } else {
            if (field.isCheckPreSave()) {
                field.setColorError();
            } else{
                field.setColorWarning();
            }
        }
        return messagesErrors.size() == 0;
    }

    public void reInitField(STextField field){
        field.setColorNormal();
    }


    protected void showCheckResultat(ArrayList<String> messagesErrors) {
       if (getEditor().getButtons() != null) {
            PanelButtonsContent buttonsContent = (PanelButtonsContent) getEditor().getButtons().getPanelContent();
            buttonsContent.clearMessages();
            for (String message : messagesErrors) {
                buttonsContent.addIfNotExistMessage(message);
            }
        }
    }


    public DialogEditor getEditor() {
        return panelInput.getEditor();
    }

    public PanelButtons getButtons() {
        return getEditor().getButtons();
    }

    public PanelButtonsContent getButtonsContent() {
        return getButtons().getButtonsContent();
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        // pour remettre la fenêtre au premier plan si l'aide est affichée
        getEditor().focusGained(focusEvent);
        getButtonsContent().clearMessages();
        if (!alreadyFocusGained) {
            checkDatas();
            checkDatasPreSave(true);
            enabledButtons();
            alreadyFocusGained = true;
        }
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        alreadyFocusGained = false;
        getButtonsContent().clearMessages();
    }


    public boolean datasChangedNow() {
        boolean resultat = false;
        for (SComponent sComponent : sComponents) {
            resultat = resultat || sComponent.checkIfUpdated();
        }
        return resultat;
    }

    public void restartChange() {
        for (SComponent sComponent : sComponents) {
            sComponent.restartChange();
        }
    }

    public void resetDatas() {
        for (SComponent sComponent : sComponents) {
            sComponent.reset();
        }
    }

    public ArrayList<SComponent> getsComponents() {
        return sComponents;
    }

    protected void initOrLoadDatas() {
        if (getEditor().getMode().equals(DialogEditor.NEW)) {
            //initDatas(getEditor().getMvccdElementParent());
            initDatas();
        } else {
            loadDatas(getEditor().getMvccdElementCrt());
        }
        dataInitialized = true;
    }

    public boolean isDataInitialized() {
        return dataInitialized;
    }

    protected void enableSubPanels(SCheckBox sCheckBox) {
        if (sCheckBox.getSubPanel() != null) {
            Component[] components = sCheckBox.getSubPanel().getComponents();
            if (components.length > 0) {
                for (int i = 0; i < components.length; i++) {
                    components[i].setEnabled(sCheckBox.isSelected());
                    if (components[i] instanceof SCheckBox) {
                        SCheckBox checkBoxChild = (SCheckBox) components[i];
                        if (checkBoxChild.getSubPanel() != null) {
                            enableSubPanels(checkBoxChild);
                        }
                    }
                }
            }
        }
    }

    protected void enableSubPanels() {
        for (SCheckBox sCheckBox : getSCheckBoxs()){
            if (sCheckBox.isRootSubPanel()){
                enableSubPanels(sCheckBox);
            }
        }
    }


    protected ArrayList<SCheckBox> getSCheckBoxs() {
        ArrayList<SCheckBox> resultat = new ArrayList<SCheckBox>();
        for (SComponent sComponent : sComponents) {
            if (sComponent instanceof SCheckBox) {
                resultat.add((SCheckBox) sComponent);
            }
        }
        return resultat;
    }

    public void setComponentsReadOnly(boolean readOnly) {
        System.out.println(readOnly);
            for (SComponent sComponent : sComponents) {
                sComponent.setReadOnly(readOnly);
            }
    }


}