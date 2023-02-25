package org.nakedobjects.example.expenses.services.inmemory;

import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.example.expenses.recordedAction.RecordedActionContext;
import org.nakedobjects.example.expenses.recordedAction.impl.RecordedAction;
import org.nakedobjects.example.expenses.recordedAction.impl.RecordedActionRepositoryAbstract;

import java.util.List;


public class RecordedActionRepositoryInMemory extends RecordedActionRepositoryAbstract {

    @Hidden
    public List<RecordedAction> allRecordedActions(final RecordedActionContext context) {
        final RecordedAction pattern = newTransientInstance(RecordedAction.class);
        pattern.setContext(context);
        return allMatches(RecordedAction.class, pattern);
    }

}
