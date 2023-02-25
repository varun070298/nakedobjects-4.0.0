package org.nakedobjects.example.expenses.recordedAction.impl;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.recordedAction.RecordedActionContext;

import java.util.List;


/**
 * Defines methods retrieving the actions associated with a given context.
 * 
 */
public interface RecordedActionRepository {

    final static int MAX_RECORDED_ACTIONS = 10;

    /**
     * Returns the RecordedActions for a given context. Note: This is a rather naive implementation. In a real
     * application, objects might eventually accumulate too many recorded actions to be retrieved in one go,
     * so it would be more appropriate to specify a method that retrieved only the most recent 10, say, plus a
     * separate method for retrieving RecordedActions that match specified parameters and/or date range.
     * 
     * @return
     */
    @Hidden
    List<RecordedAction> allRecordedActions(RecordedActionContext context);

}
