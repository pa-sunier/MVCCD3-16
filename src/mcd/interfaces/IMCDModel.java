package mcd.interfaces;

import main.MVCCDElement;
import mdr.MDRModel;
import mldr.MLDRModel;

import java.util.ArrayList;

public interface IMCDModel {

    public ArrayList<MVCCDElement> getChilds();

    public MLDRModel getLastTransformedMLDRModel();
    public void setLastTransformedMLDRModel(MLDRModel getLastTransformedMLDRModel) ;
    public String getName();

}
