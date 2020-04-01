package utilities.window.scomponents;

import main.MVCCDManager;
import utilities.window.editor.PanelInputContent;
import utilities.window.scomponents.services.SComponentService;

import javax.swing.*;

public class SComboBox<S> extends JComboBox<S> implements SComponent {

    public static final String LINEDASH = "---";
    public static final String LINEWHITE = "";

    public static final int INDEXOUTBOUND = -1;

    //private S oldSelected = null;
    private int oldIndex = -1 ;
    private boolean checkPreSave = false;

    private boolean readOnly = false;
    private IPanelInputContent panel;


    public SComboBox(IPanelInputContent panel) {
        this.panel = panel ;
        this.setColorNormal();
    }

    public void setSelectedItem(Object item) {
        for (int i = 0; i < getItemCount(); i++) {
            if (item.equals(getItemAt(i))){
                super.setSelectedItem(item);
                setSelectedBase(i);
            }
        }
    }

    public void setSelectedIndex(int index) {
        for (int i = 0; i < getItemCount(); i++) {
            if (index == i ){
                super.setSelectedIndex(index);
                setSelectedBase(i);
            }
        }
    };

    private void setSelectedBase(int i) {
        if (! panel.isDataInitialized()) {
            //oldSelected = getItemAt(i);
            oldIndex = i ;
        }
    }

    public Integer getOldIndex() {
        return oldIndex;
    }

    @Override
    public boolean checkIfUpdated() {
        boolean updated;
        updated = (getSelectedIndex() != oldIndex);

       if (updated) {
            MVCCDManager.instance().datasProjectChangedFromEditor();
        }
        return updated;
    }

    @Override
    public void restartChange() {
        oldIndex = getSelectedIndex();
    }

    @Override
    public void reset() {
        setSelectedIndex(oldIndex);
    }


    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        if (readOnly) {
            super.setEnabled(false);
        }
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    public void setEnabled(boolean enabled){
        if (! isReadOnly()){
            super.setEnabled(enabled);
        } else{
            super.setEnabled(false);
        }
    }

    @Override
    public void setColorError() {
        SComponentService.colorError(this);
    }

    @Override
    public void setColorWarning() {
        SComponentService.colorWarning(this);
    }

    @Override
    public void setColorNormal() {
        SComponentService.colorNormal(this);
    }

    @Override
    public boolean isCheckPreSave() {
        return checkPreSave;
    }

    public boolean isSelectedEmpty(){
        String content = (String) this.getSelectedItem();
        return  (content.equals(LINEDASH) || content.equals(LINEWHITE)) ;
    }

    public int getItemEmptyIndex(){
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemAt(i).equals(LINEWHITE) || getItemAt(i).equals(LINEDASH)){
                return INDEXOUTBOUND;
            }
        }
        return -1 ;
    }

    public void setSelectedEmpty(){
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemAt(i).equals(LINEWHITE) || getItemAt(i).equals(LINEDASH)){
                super.setSelectedIndex(i);
            }
        }
    }

    public String getFirstItem(){
        return (String) super.getItemAt(0);
    }

    public void setSelectedFirst() {
        super.setSelectedIndex(0);
    }
}
