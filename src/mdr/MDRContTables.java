package mdr;

import mdr.services.MDRContTablesService;
import project.ProjectElement;

import java.util.ArrayList;

public class MDRContTables extends MDRElement {

    private  static final long serialVersionUID = 1000;
    public MDRContTables(ProjectElement parent, String name) {
        super(parent, name);
    }


    public ArrayList<MDRTable> getMDRTables(){
        return MDRContTablesService.getMDRTables(this);
    }



}