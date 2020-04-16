package project;

import main.MVCCDElement;
import main.MVCCDManager;
import mcd.MCDContModels;
import mcd.MCDElement;
import mcd.MCDEntity;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class ProjectService {


    public ProjectService() {

    }

    static Project getProjectRoot(ProjectElement projectElement) {
        if (projectElement instanceof Project) {
            return ((Project) projectElement);
        } else {
            return getProjectRoot((ProjectElement) projectElement.getParent());
        }
    }

    public static ProjectElement getElementById(int id) {
        Project project = MVCCDManager.instance().getProject();
        return getElementById(project, id);
    }

    public static ProjectElement getElementById(ProjectElement projectElement, int id) {
        ProjectElement resultat = null;
        for (MVCCDElement mvccdElement : projectElement.getChilds()) {
            if (mvccdElement instanceof ProjectElement) {
                ProjectElement child = (ProjectElement) mvccdElement;
                if (child.getId() == id) {
                    resultat = child;
                } else {
                    if (resultat == null) {
                        resultat = getElementById(child, id);
                    }
                }
            }
        }
        return resultat;
    }

    //TODO-1 Déplacer dans RepositoryService ?
    public static DefaultMutableTreeNode getNodeById(int id) {

        DefaultMutableTreeNode rootProject = MVCCDManager.instance().getRepository().getNodeProject();
        return getNodeById(rootProject, id);
    }

    public static DefaultMutableTreeNode getNodeById(DefaultMutableTreeNode root, int id) {
        DefaultMutableTreeNode resultat = null;
        if (root.getUserObject() instanceof ProjectElement) {
            ProjectElement projectElement = (ProjectElement) root.getUserObject();
            if (projectElement.getId() == id) {
                resultat = root;
            }
        }
        if (resultat == null){
            for (int i = 0; i < root.getChildCount(); i++) {
                if (resultat == null) {
                    resultat = getNodeById((DefaultMutableTreeNode) root.getChildAt(i), id);
                }
            }
        }
        return resultat;
    }


    public static ArrayList<ProjectElement> getAllProjectElements() {
        Project project = MVCCDManager.instance().getProject();
        return  getAllProjectElements(project);
    }

    public static ArrayList<ProjectElement> getAllProjectElements(ProjectElement parentProjectElement) {
        ArrayList<ProjectElement> resultat = new ArrayList<ProjectElement>();
        resultat.add(parentProjectElement);
        for (MVCCDElement mvccdElement : parentProjectElement.getChilds()){
            if (mvccdElement instanceof ProjectElement){
                ProjectElement childProjectElement  = (ProjectElement) mvccdElement;
                resultat.addAll(getAllProjectElements(childProjectElement));
            }
        }
        return resultat;
    }


    public static ArrayList<MCDElement> getAllMCDElements() {
        ArrayList<MCDElement> resultat = new ArrayList<MCDElement>();
        for (ProjectElement projectElement : getAllProjectElements()){
            if (projectElement instanceof MCDElement){
                resultat.add((MCDElement)projectElement);
            }
        }
        return resultat;
    }


    public static ArrayList<MCDElement> getAllMCDElementsByNamePath(int pathMode, String namePath){
        ArrayList<MCDElement>  resultat = new ArrayList<MCDElement>() ;
        for (MCDElement mcdElement : getAllMCDElements()){
            if (mcdElement.getNamePath(pathMode).equals(namePath)){
                resultat.add(mcdElement);
            }
        }
        return resultat;
    }

    public static MCDContModels getMCDContModels() {
        for (MVCCDElement mvccdElement : getAllProjectElements()) {
            if (mvccdElement instanceof MCDContModels) {
                return (MCDContModels) mvccdElement;
            }
        }
        return null;
    }
}
