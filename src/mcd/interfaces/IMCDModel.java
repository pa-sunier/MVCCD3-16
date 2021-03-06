package mcd.interfaces;

import main.MVCCDElement;
import mcd.MCDElement;
import mldr.MLDRModel;
import resultat.Resultat;

import java.util.ArrayList;

public interface IMCDModel {

    public ArrayList<MVCCDElement> getChilds();

    public MLDRModel getLastTransformedMLDRModel();
    public void setLastTransformedMLDRModel(MLDRModel getLastTransformedMLDRModel) ;
    public String getName();
    public ArrayList<MCDElement> getMCDDescendants();
    public Resultat treatCompliant();
    public Resultat treatTransform();
}
