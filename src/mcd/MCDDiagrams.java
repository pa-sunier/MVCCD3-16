package mcd;

import project.ProjectElement;

public class MCDDiagrams extends MCDElement {

    private static final long serialVersionUID = 1000;
    public MCDDiagrams(ProjectElement parent, String name) {
        super(parent,name);
    }

    public MCDDiagrams(ProjectElement parent) {
        super (parent);
    }


    @Override
    public String getNameTree() {
        return null;
    }

}
