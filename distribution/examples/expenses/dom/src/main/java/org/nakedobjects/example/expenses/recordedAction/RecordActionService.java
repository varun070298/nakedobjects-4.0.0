package org.nakedobjects.example.expenses.recordedAction;

/**
 * An implementation of this Service will be injected into any RecordedActionContext, thereby allowing that
 * context to record an action.
 * 
 * @author Richard
 * 
 */
public interface RecordActionService {

    /**
     * Creates and persists a RecordedAction object. Details field is optional.
     */
    public void recordMenuAction(RecordedActionContext context, String action, String details);

    public void recordFieldChange(RecordedActionContext context, String fieldName, Object previousContents, Object newContents);

}
