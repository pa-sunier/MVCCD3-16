package mcd;

import mcd.interfaces.IMPathOnlyRepositoryTree;
import project.ProjectElement;

public class MCDContDiagrams extends MCDElement implements IMPathOnlyRepositoryTree {

    private static final long serialVersionUID = 1000;

    public MCDContDiagrams(ProjectElement parent, int id) {
        super(parent, id);
    }

    public MCDContDiagrams(ProjectElement parent, String name) {
        super(parent, name);
    }

    public MCDContDiagrams(ProjectElement parent) {
        super (parent);
    }


}
