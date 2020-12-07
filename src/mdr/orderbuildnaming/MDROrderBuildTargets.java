package mdr.orderbuildnaming;

import messages.MessagesBuilder;
import preferences.Preferences;

public enum MDROrderBuildTargets {
    TABLE (Preferences.MDR_NAMING_TABLE, "tables"),
    TABLENN (Preferences.MDR_NAMING_TABLE_NN, "mdr.of.tables.nn"),
    TABLENNINDICE (Preferences.MDR_NAMING_TABLE_NN, "mdr.of.tables.nn"),
    COLUMNATTR (Preferences.MDR_NAMING_COLUMN_ATTR, "mdr.of.columns.attr"),
    COLUMNPK (Preferences.MDR_NAMING_COLUMN_PK, "mdr.of.columns.pk"),
    COLUMNFK (Preferences.MDR_NAMING_COLUMN_FK, "mdr.of.columns.fk"),
    PK (Preferences.MDR_NAMING_PK, "mdr.of.constraints.pk"),
    FK (Preferences.MDR_NAMING_FK, "mdr.of.constraints.fk");

    private final String name;
    private final String propertyOfTarget;

    MDROrderBuildTargets(String name, String propertyOfTarget) {
        this.name = name;
        this.propertyOfTarget = propertyOfTarget;
   }

    public String getName() {
        return name;
    }


    public String getText() {
        return MessagesBuilder.getMessagesProperty(name);
    }

    public static MDROrderBuildTargets findByText(String text){
        for (MDROrderBuildTargets element: MDROrderBuildTargets.values()){
            if (element.getText().equals(text)) {
                return element;
            }
        }
        return null;
    }

    public String getPropertyOfTarget() {
        return propertyOfTarget;
    }

    public String getMessageOfTarget() {
        return MessagesBuilder.getMessagesProperty(propertyOfTarget);
    }
}
