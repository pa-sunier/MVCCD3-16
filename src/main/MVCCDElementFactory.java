package main;

import mcd.*;
import mcd.MCDContAttributes;
import messages.MessagesBuilder;
import preferences.Preferences;
import preferences.PreferencesManager;
import project.Project;
import project.ProjectElement;

public class MVCCDElementFactory {

    private static MVCCDElementFactory instance;


    public static synchronized MVCCDElementFactory instance() {
        if (instance == null) {
            instance = new MVCCDElementFactory();
        }

        return instance;
    }


    public Project createProject(String name){

        Project project = new Project(name);

        Preferences preferences = MVCCDElementFactory.instance().createPreferences(project, Preferences.REPOSITORY_PREFERENCES_NAME);
        MCDModels mcdModels = MVCCDElementFactory.instance().createMCDModels(project, Preferences.REPOSITORY_MCD_MODELS_NAME);

        // Les préférences de projets ne sont pas encore mises à jour...
        if (!PreferencesManager.instance().getApplicationPref().getREPOSITORY_MCD_MODELS_MANY()) {
                createContentModel(mcdModels);
        }
        return project;
    }


    public MCDModels createMCDModels(Project project, String name){
        return new MCDModels(project, name);
    }

    public MCDModel createMCDModel(MCDModels mcdModels){
        MCDModel mcdModel = new MCDModel(mcdModels);
        createContentModel(mcdModel);
        return mcdModel;
    }

    public MCDPackage createMCDPackage(ProjectElement parent){
        MCDPackage mcdPackage = new MCDPackage(parent);
        createContentPackage(mcdPackage);
        return mcdPackage;
    }

    public Preferences createPreferences(ProjectElement parent, String name) {
        return new Preferences(parent, name);
    }

    public MCDDiagrams createMCDDiagrams(ProjectElement parent, String name){
        return new MCDDiagrams(parent, name);
    }

    public MCDEntities createMCDEntities(ProjectElement parent, String name){
        MCDEntities mcdEntities = new MCDEntities(parent, name);
        return mcdEntities;
    }

    public MCDRelations createMCDRelations(ProjectElement parent, String name){
        MCDRelations mcdRelations = new MCDRelations(parent, name);
        return mcdRelations;
    }

    public MCDEntity createMCDEntity(ProjectElement parent){
        MCDEntity mcdEntity = new MCDEntity(parent);
        MCDContAttributes mcdContAttributes = MVCCDElementFactory.instance().createMCDAttributes(mcdEntity,
                Preferences.REPOSITORY_MCD_ATTRIBUTES_NAME);
        MCDContEndRels mcdContEndRels = MVCCDElementFactory.instance().createMCDRelations(mcdEntity,
                Preferences.REPOSITORY_MCD_RELATIONS_NAME);

        return mcdEntity;
    }

    public MCDContAttributes createMCDAttributes(MCDEntity parent, String name){
        return new MCDContAttributes(parent, name);
    }

    public MCDContEndRels createMCDRelations(MCDEntity parent, String name){
        return new MCDContEndRels(parent, name);
    }
    public MCDAttribute createMCDAttribute(MCDContAttributes parent){

        return new MCDAttribute(parent);
    }


    public MCDAssociation createMCDAssociation( MCDRelations mcdRelations,
                MCDContEndRels mcdContEndRelsFrom, MCDContEndRels mcdContEndRelsTo) {

        MCDAssociation mcdAssociation = new MCDAssociation(mcdRelations) ;

        MCDEntity mcdEntityFrom = (MCDEntity) mcdContEndRelsFrom.getParent();
        MCDAssEnd mcdAssEndFrom = new MCDAssEnd(mcdEntityFrom) ;

        MCDEntity mcdEntityTo = (MCDEntity) mcdContEndRelsTo.getParent();
        MCDAssEnd mcdAssEndTo = new MCDAssEnd(mcdEntityTo) ;

        mcdAssociation.setFrom(mcdAssEndFrom);
        mcdAssociation.setTo(mcdAssEndTo);

        mcdAssEndFrom.setMcdEntity(mcdEntityFrom);
        mcdAssEndFrom.setMcdAssociation(mcdAssociation);

        mcdAssEndTo.setMcdEntity(mcdEntityTo);
        mcdAssEndTo.setMcdAssociation(mcdAssociation);

        return mcdAssociation;
    }


    private void createContentModel(MCDElement parent) {
        createContentPackage(parent);
    }

    private void createContentPackage(MCDElement parent) {
        MCDDiagrams mcdDiagrams = MVCCDElementFactory.instance().createMCDDiagrams(parent,Preferences.REPOSITORY_MCD_DIAGRAMS_NAME);
        MCDEntities mcdEntities = MVCCDElementFactory.instance().createMCDEntities(parent,Preferences.REPOSITORY_MCD_ENTITIES_NAME);
        MCDRelations mcdRelations = MVCCDElementFactory.instance().createMCDRelations(parent,Preferences.REPOSITORY_MCD_RELATIONS_NAME);
    }


    public ProjectElement createMVCCDElementFromXML(String baliseName, ProjectElement ancestor){
        if (baliseName.equals(Preferences.XML_BALISE_PROJECT)) {
            return new Project(null);
        } else if (baliseName.equals(Preferences.XML_BALISE_MODELS)) {
            return new MCDModels(ancestor);
        } else if (baliseName.equals(Preferences.XML_BALISE_DIAGRAMS)) {
            return new MCDDiagrams(ancestor);
        } else if (baliseName.equals(Preferences.XML_BALISE_ENTITIES)) {
            return new MCDEntities(ancestor);
        } else if (baliseName.equals(Preferences.XML_BALISE_ENTITY)) {
            return new MCDEntity(ancestor);
        } else {
            String message = MessagesBuilder.getMessagesProperty("load.xml.balise.unknow", new String[] {
                    baliseName, ancestor.getName(), ancestor.getClass().getName()});
            MVCCDManager.instance().getConsole().printMessage(message);
            return null;
        }
    }
}