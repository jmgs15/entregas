package com.urjc.books.controllers.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ExistingEntitiesAssociatedException extends Exception {

    public ExistingEntitiesAssociatedException(String associatedEntity) {
        super("The entity you are trying to delete has " + associatedEntity + " associated.");
    }
}
