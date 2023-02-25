package org.nakedobjects.example.expenses.recordedAction.impl;

import org.nakedobjects.applib.AbstractService;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.example.expenses.recordedAction.RecordedActionContext;

import java.util.List;


/**
 * Defines user actions made available on objects implementing RecordedActionContext
 * 
 */
@Named("Recorded Actions")
public class RecordedActionContributedActions extends AbstractService {

    // {{ Injected Services
    /*
     * This region contains references to the services (Repositories, Factories or other Services) used by
     * this domain object. The references are injected by the application container.
     */

    // {{ Injected: RecordedActionRepository
    private RecordedActionRepository recordedActionRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected RecordedActionRepository getRecordedActionRepository() {
        return this.recordedActionRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setRecordedActionRepository(final RecordedActionRepository recordedActionRepository) {
        this.recordedActionRepository = recordedActionRepository;
    }

    // }}

    // }}

    /**
     * Returns the most recently-recorded actions on a context, up to a system-defined maximum of, say 10.
     */
    public List<RecordedAction> allRecordedActions(final RecordedActionContext context) {
        return recordedActionRepository.allRecordedActions(context);
    }

}
