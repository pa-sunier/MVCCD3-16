package main;

import messages.MessagesBuilder;

public class MVCCDElementRepositoryGlobal extends MVCCDElement {


    public MVCCDElementRepositoryGlobal(MVCCDElement parent) {
        super(parent, MessagesBuilder.getMessagesProperty("repository.global.name"));
    }

    @Override
    public String baliseXMLBegin() {
        return null;
    }

    @Override
    public String baliseXMLEnd() {
        return null;
    }
}
