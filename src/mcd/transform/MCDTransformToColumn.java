package mcd.transform;

import datatypes.MLDRDatatype;
import exceptions.orderbuildnaming.OrderBuildNameException;
import exceptions.TransformMCDException;
import main.MVCCDElement;
import main.MVCCDElementConvert;
import main.MVCCDManager;
import mcd.*;
import mdr.*;
import mdr.orderbuildnaming.MDROrderBuildNaming;
import mdr.orderbuildnaming.MDROrderBuildTargets;
import messages.MessagesBuilder;
import mldr.MLDRColumn;
import mldr.MLDRModel;
import mldr.MLDRTable;
import org.apache.commons.lang.StringUtils;
import preferences.Preferences;
import preferences.PreferencesManager;
import utilities.TransformService;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCDTransformToColumn {

    //private MCDEntity mcdEntity ;
    //private MLDRTable mldrTable ;


    public void fromAttributes(MCDEntity mcdEntity, MLDRTable mldrTable) {
        for (MCDAttribute mcdAttribute : mcdEntity.getMCDAttributes()) {
            fromAttribute(mcdAttribute, mldrTable);
        }
    }

    public void fromAttribute(MCDAttribute mcdAttribute, MLDRTable mldrTable) {

        MLDRColumn mldrColumn = mldrTable.getMLDRColumnByMCDElementSource(mcdAttribute);

        if (mldrColumn == null) {
            mldrColumn = mldrTable.createColumn(mcdAttribute);
            MVCCDManager.instance().addNewMVCCDElementInRepository(mldrColumn);
        }
        modifyColumn(mldrColumn, mcdAttribute);
    }


    public MLDRColumn fromRelEndParent(MLDRTable mldrTable, MCDRelEnd mcdRelEndParent, MLDRTable mldrTableParent, MLDRColumn mldrColumnPK, MDRFKNature fkNature) {
        MLDRColumn mldrColumnFK = mldrTable.getMLDRColumnFKByMCDRelationAndMLDRColumnPK(
                mcdRelEndParent.getMcdRelation(), mldrColumnPK);
        if (mldrColumnFK == null){
            mldrColumnFK = mldrTable.createColumnFK(mcdRelEndParent.getMcdRelation(), mldrColumnPK);
            MVCCDManager.instance().addNewMVCCDElementInRepository(mldrColumnFK);
        }
        modifyColumnFK(mldrColumnFK, mcdRelEndParent, mldrColumnPK, fkNature);
        return mldrColumnFK;
    }

    public void modifyColumn(MLDRColumn mldrColumn, MCDAttribute mcdAttribute){

        // Nom
        MLDRModel mldrModel = (MLDRModel) mldrColumn.getMDRTableParent().getMDRModelParent();
        TransformService.name(mldrColumn, buildNameColumnAttr(mcdAttribute), mldrModel.getNamingLenth());


        // Datatype
        MLDRDatatype mldrDatatypeNew = MCDTransformToMLDRDatatype.fromMCDDatatype(mcdAttribute.getDatatypeLienProg());
        String mldrDatatypeLienProgNew = mldrDatatypeNew.getLienProg();
        if (mldrColumn.getDatatypeLienProg() != null) {
            if (!(mldrColumn.getDatatypeLienProg().equals(mldrDatatypeLienProgNew))) {
                mldrColumn.setDatatypeLienProg(mldrDatatypeLienProgNew);
            }
        } else {
            mldrColumn.setDatatypeLienProg(mldrDatatypeLienProgNew);
        }

        // Datatype contrainte
        // Les préférences (nom et lienProg) des contraintes datatype sont les mêmes que celles de datratypes !
        if (mldrColumn.getDatatypeConstraintLienProg() != null) {
            if (!(mldrColumn.getDatatypeConstraintLienProg().equals(mcdAttribute.getDatatypeLienProg()))) {
                mldrColumn.setDatatypeConstraintLienProg(mcdAttribute.getDatatypeLienProg());
            }
        } else {
            mldrColumn.setDatatypeConstraintLienProg(mcdAttribute.getDatatypeLienProg());
        }

        // Datatype size
        if (mldrColumn.getSize() != null) {
            if (mldrColumn.getSize().intValue() != mcdAttribute.getSize().intValue()) {
                mldrColumn.setSize(mcdAttribute.getSize());
            }
        } else {
            mldrColumn.setSize(mcdAttribute.getSize());
        }

        // Datatype scale
        if (mldrColumn.getScale() != null) {
            if (mldrColumn.getScale().intValue() != mcdAttribute.getScale().intValue()) {
                mldrColumn.setDatatypeLienProg(mldrDatatypeLienProgNew);
            }
        } else {
            mldrColumn.setScale(mcdAttribute.getScale());
        }

        // Mandatory
        if (mldrColumn.isMandatory() != mcdAttribute.isMandatory()){
            mldrColumn.setMandatory(mcdAttribute.isMandatory());
        }

        // Frozen
        if (mldrColumn.isFrozen() != mcdAttribute.isFrozen()){
            mldrColumn.setFrozen(mcdAttribute.isFrozen());
        }

        // Uppercase
        if (mldrColumn.isUppercase() != mcdAttribute.isUppercase()){
            mldrColumn.setUppercase(mcdAttribute.isUppercase());
        }

        // Init Value
        String mldrInitValue= mcdAttribute.getInitValue();
        if (mldrColumn.getInitValue() != null) {
            if (! mldrColumn.getInitValue().equals(mcdAttribute.getInitValue())) {
                mldrColumn.setInitValue(mldrInitValue);
            }
        } else {
            mldrColumn.setInitValue(mldrInitValue);
        }

        // Derived Value
        String mldrDefaultValue= mcdAttribute.getInitValue();
        if (mldrColumn.getDerivedValue() != null) {
            if (! mldrColumn.getDerivedValue().equals(mcdAttribute.getDerivedValue())) {
                mldrColumn.setDerivedValue(mldrDefaultValue);
            }
        } else {
            mldrColumn.setDerivedValue(mldrDefaultValue);
        }
    }

    public void modifyColumnPKorFK(MLDRColumn mldrColumn) {

        // Datatype
        MLDRDatatype mldrDatatypeNew  = MCDTransformToMLDRDatatype.fromMCDDatatype(
                PreferencesManager.instance().preferences().getMCD_AID_DATATYPE_LIENPROG());
        String mldrDatatypeLienProgNew = mldrDatatypeNew.getLienProg();

        if (mldrColumn.getDatatypeLienProg() != null) {
            if (!(mldrColumn.getDatatypeLienProg().equals(mldrDatatypeLienProgNew))) {
                mldrColumn.setDatatypeLienProg(mldrDatatypeLienProgNew);
            }
        } else {
            mldrColumn.setDatatypeLienProg(mldrDatatypeLienProgNew);
        }

        // Datatype contrainte
        // Les préférences (nom et lienProg) des contraintes datatype sont les mêmes que celles de datratypes !
        String mldrDatatypeConstraintLienProgNew = PreferencesManager.instance().preferences().getMCD_AID_DATATYPE_LIENPROG();
        if (mldrColumn.getDatatypeConstraintLienProg() != null) {
            if (!(mldrColumn.getDatatypeConstraintLienProg().equals(mldrDatatypeConstraintLienProgNew))) {
                mldrColumn.setDatatypeConstraintLienProg(mldrDatatypeConstraintLienProgNew);
            }
        } else {
            mldrColumn.setDatatypeConstraintLienProg(mldrDatatypeConstraintLienProgNew);
        }

        // Datatype size
        // TODO-1 A rendre paramétrable
        Integer mldrDatatypeSizeNew = PreferencesManager.instance().preferences().MCDDOMAIN_AID_SIZEDEFAULT;

        if (mldrColumn.getSize() != null) {
            if (mldrColumn.getSize().intValue() != mldrDatatypeSizeNew) {
                mldrColumn.setSize(mldrDatatypeSizeNew);
            }
        } else {
            mldrColumn.setSize(mldrDatatypeSizeNew);
        }

    }


    public void modifyColumnPK(MCDEntity mcdEntity, MLDRColumn mldrColumnPK) {

        // Nom
        MLDRModel mldrModel = (MLDRModel) mldrColumnPK.getMDRTableParent().getMDRModelParent();
        TransformService.name(mldrColumnPK, buildNameColumnPK(mcdEntity), mldrModel.getNamingLenth());

        modifyColumnPKorFK(mldrColumnPK);
    }

    public void modifyColumnFK(MLDRColumn mldrColumnFK, MCDRelEnd mcdRelEndParent, MLDRColumn mldrColumnPK, MDRFKNature fkNature) {

        // Nom
        MLDRModel mldrModel = (MLDRModel) mldrColumnFK.getMDRTableParent().getMDRModelParent();
        TransformService.name(mldrColumnFK, buildNameColumnFK(mldrColumnFK, mcdRelEndParent, mldrColumnPK), mldrModel.getNamingLenth());

        modifyColumnPKorFK(mldrColumnFK);
    }


    private static  MDRElementNames buildNameColumnAttr(MCDAttribute mcdAttribute){
        Preferences preferences = PreferencesManager.instance().preferences();
        MDRElementNames names = new MDRElementNames();
        for (MDRNamingLength element: MDRNamingLength.values()) {

            MDROrderBuildNaming orderBuild = new MDROrderBuildNaming(element);
            MCDEntity mcdEntityAccueil = mcdAttribute.getEntityAccueil();

            orderBuild.setFormat(preferences.getMDR_COLUMN_ATTR_NAME_FORMAT());
            orderBuild.setFormatUserMarkerLengthMax(Preferences.MDR_MARKER_CUSTOM_COLUMN_NAME_LENGTH);
            orderBuild.setTargetNaming(MDROrderBuildTargets.COLUMNATTR);

            orderBuild.getAttrName().setValue(mcdAttribute.getName());

            orderBuild.getColDerived().setValue(
                    mcdAttribute.isDerived() ? preferences.getMDR_COLUMN_DERIVED_MARKER() : "");

            if (mcdEntityAccueil.getNature() == MCDEntityNature.PSEUDOENTASS) {
                orderBuild.getPea().setValue(mcdEntityAccueil.getShortName());
                orderBuild.getPeaSep().setValue(preferences.getMDR_PEA_SEP_FORMAT());
            } else {
                orderBuild.getPea().setValue("");
                orderBuild.getPeaSep().setValue("");
            }

            String name;

            try {
                name = orderBuild.buildNaming();
            } catch (OrderBuildNameException e) {
                String message = "";
                if (StringUtils.isNotEmpty(e.getMessage())) {
                    message = e.getMessage();
                } else {
                    message = MessagesBuilder.getMessagesProperty("mdrcolumn.build.name.attribute.error",
                            new String[]{mcdEntityAccueil.getName(), mcdAttribute.getName()});
                }
                throw new TransformMCDException(message, e);
            }
            names.setElementName(name, element);
        }
        return names;
    }

    private static  MDRElementNames buildNameColumnPK(MCDEntity mcdEntity){
        Preferences preferences = PreferencesManager.instance().preferences();

        MDRElementNames names = new MDRElementNames();
        for (MDRNamingLength element: MDRNamingLength.values()) {

            MDROrderBuildNaming orderBuild = new MDROrderBuildNaming(element);
            orderBuild.setFormat(preferences.getMDR_COLUMN_PK_NAME_FORMAT());
            orderBuild.setFormatUserMarkerLengthMax(Preferences.MDR_MARKER_CUSTOM_COLUMN_NAME_LENGTH);
            orderBuild.setTargetNaming(MDROrderBuildTargets.PK);

            orderBuild.getAttrName().setValue(mcdEntity);

            String name;

            try {
                name = orderBuild.buildNaming();
            } catch (OrderBuildNameException e) {
                String message = "";
                if (StringUtils.isNotEmpty(e.getMessage())) {
                    message = e.getMessage();
                } else {
                    message = MessagesBuilder.getMessagesProperty("mdrcolumn.build.name.pk.error",
                            new String[]{mcdEntity.getName()});
                }
                throw new TransformMCDException(message, e);
            }
            names.setElementName(name, element);
        }
        return names;

    }

    private static  MDRElementNames buildNameColumnFK(MLDRColumn mldrColumnFK, MCDRelEnd mcdRelEndParent, MLDRColumn mldrColumnPK){
        Preferences preferences = PreferencesManager.instance().preferences();

        MDRElementNames names = new MDRElementNames();
        for (MDRNamingLength element: MDRNamingLength.values()) {

            MDROrderBuildNaming orderBuild = new MDROrderBuildNaming(element);
            orderBuild.setFormat(preferences.getMDR_COLUMN_FK_NAME_FORMAT());
            orderBuild.setFormatUserMarkerLengthMax(Preferences.MDR_MARKER_CUSTOM_COLUMN_NAME_LENGTH);
            orderBuild.setTargetNaming(MDROrderBuildTargets.FK);

            String namePK = mldrColumnPK.getName();
            orderBuild.getColName().setValue(namePK);
            if (mldrColumnPK.isFk() && preferences.getMDR_PREF_COLUMN_FK_ONE_ANCESTOR()) {
                orderBuild.getTableShortNameParent().setValue("");
                orderBuild.getTableSep().setValue("");
                orderBuild.getRoleShortNameParent().setValue("");
                orderBuild.getRoleSep().setValue("");
                ArrayList<MVCCDElement> brothers = MVCCDElementConvert.to(
                        mldrColumnFK.getMDRTableParent().getMDRColumns());
                orderBuild.getIndColFK().setValue(namePK, brothers);
            } else {
                orderBuild.getTableShortNameParent().setValue((MCDEntity) mcdRelEndParent.getMcdElement());
                orderBuild.getTableSep().setValue();
                String roleParent = mcdRelEndParent.getNameNoFreeOrNameRelation();
                orderBuild.getRoleShortNameParent().setValue(mcdRelEndParent);
                if (StringUtils.isNotEmpty(orderBuild.getRoleShortNameParent().getValue())) {
                    orderBuild.getRoleSep().setValue();
                } else {
                    orderBuild.getRoleSep().setValue("");
                }
                orderBuild.getIndColFK().setValue("");
            }

            String name;

            try {
                name = orderBuild.buildNaming();
            } catch (OrderBuildNameException e) {

                String message = "";
                if (StringUtils.isNotEmpty(e.getMessage())) {
                    message = e.getMessage();
                } else {
                    message = MessagesBuilder.getMessagesProperty("mdrcolfk.build.name.error",
                            new String[]{mldrColumnPK.getMDRTableParent().getName(),
                                    mldrColumnPK.getName(),
                                    mldrColumnFK.getMDRTableParent().getName(),
                                    mcdRelEndParent.getNameNoFreeOrNameRelation()});
                }
                throw new TransformMCDException(message, e);

            }
            names.setElementName(name, element);

        }
        return names;
    }


    private static String extractRoot(String nameColumn, String differenciation){
        return nameColumn.substring(0, nameColumn.length()- differenciation.length());

    }


    private static int nbColumnFKByRoot(String rootNew, MDRTable mdrTableParent){
        int resultat = 0;
        Pattern pattern = Pattern.compile(Preferences.MDR_INDICE_REGEXPR);
        for (MDRColumn col : mdrTableParent.getMDRColumns()){
            Matcher matcher = pattern.matcher(col.getName());
            if (matcher.find()){
                String differentiation = matcher.group();
                String rootCol = extractRoot(col.getName(), differentiation);
                if (rootCol.equals(rootNew)) resultat++;
            } else {
                if (col.getName().equals(rootNew)) resultat++;
            }
        }
        return resultat;
    }



}
