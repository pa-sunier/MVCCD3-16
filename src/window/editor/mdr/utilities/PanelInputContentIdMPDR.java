package window.editor.mdr.utilities;

import m.MElement;
import main.MVCCDElement;
import mcd.MCDElement;
import md.MDElement;
import mdr.MDRElement;
import mdr.MDRTable;
import mdr.interfaces.IMDRElementWithSource;
import mldr.interfaces.IMLDRElement;
import mldr.interfaces.IMLDRElementWithSource;
import preferences.Preferences;
import preferences.PreferencesManager;
import utilities.Trace;
import utilities.window.editor.PanelInput;
import utilities.window.editor.PanelInputContent;
import utilities.window.scomponents.SComponent;
import utilities.window.scomponents.STextField;
import utilities.window.services.PanelService;
import window.editor.mdr.table.MDRTableInput;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;

public class PanelInputContentIdMPDR extends PanelInputContent {

    protected JPanel panelId = new JPanel ();

    private JLabel labelName ;
    private STextField fieldName ;
    private JLabel labelSource;
    private STextField fieldSource ;


    //private JComboBox<String> profile = new JComboBox<>();


    public PanelInputContentIdMPDR(PanelInput panelInput)     {
        super(panelInput);
    }

    public PanelInputContentIdMPDR(MVCCDElement element)     {
        super(null);
        elementForCheckInput = element;
    }

    @Override
    public void createContentCustom() {

        labelName = new JLabel("Nom : ");
        fieldName = new STextField(this, labelName);
        fieldName.setPreferredSize((new Dimension(400, Preferences.EDITOR_FIELD_HEIGHT)));

        labelSource = new JLabel("Source :");
        fieldSource = new STextField(this, labelSource);
        fieldSource.setPreferredSize((new Dimension(400, Preferences.EDITOR_FIELD_HEIGHT)));

        super.getSComponents().add(fieldName);
        super.getSComponents().add(fieldSource);

        //createPanelMaster();
    }

    protected GridBagConstraints createPanelId() {
        GridBagConstraints gbc = PanelService.createSubPanelGridBagConstraints(panelId, "Identification");

        panelId.add(labelName, gbc);
        gbc.gridx = 1;
        panelId.add(fieldName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelId.add(labelSource,gbc);
        gbc.gridx++;
        panelId.add(fieldSource, gbc);

        this.add(panelId);
        return gbc;
    }




    protected boolean changeField(DocumentEvent e) {
        boolean ok = true;
        SComponent sComponent = null;

        Document doc = e.getDocument();

        // Autres champs que les champs Id
        return ok;

    }

    @Override
    protected void changeFieldSelected(ItemEvent e) {

    }

    @Override
    protected void changeFieldDeSelected(ItemEvent e) {

    }


    @Override
    public void focusGained(FocusEvent focusEvent) {
        super.focusGained(focusEvent);
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override
    public void loadSimulationChange(MVCCDElement mvccdElementCrt) {
    }

    @Override
    protected void initDatas() {
    }

    @Override
    public void loadDatas(MVCCDElement mvccdElementCrt) {
        MDRElement mdrElement = (MDRElement) mvccdElementCrt;
        fieldName.setText(mdrElement.getName());

        if (mdrElement instanceof IMDRElementWithSource) {
            MDElement mdElementSource = ((IMDRElementWithSource)mdrElement).getMdElementSource();
            String sourceName = mdElementSource.getName();
            if (mdElementSource instanceof MCDElement) {
                sourceName = ((MCDElement) mdElementSource).getNamePath(MElement.SCOPESHORTNAME);
            }
            String sourceClassName = mdElementSource.getClass().getSimpleName();
            fieldSource.setText(sourceClassName + " :  " + sourceName);
        }
    }

    @Override
    protected void saveDatas(MVCCDElement mvccdElement) {
     }

    @Override
    protected void enabledContent() {
        Preferences preferences = PreferencesManager.instance().preferences();
    }

}