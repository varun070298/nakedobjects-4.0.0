package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.fixtures.UserProfileFixture;
import org.nakedobjects.applib.profiles.Perspective;
import org.nakedobjects.applib.profiles.Profile;
import org.nakedobjects.example.expenses.claims.Claims;
import org.nakedobjects.example.expenses.employee.Employees;
import org.nakedobjects.example.expenses.recordedAction.impl.RecordActionServiceImpl;


public class ExplorationUserProfileFixture extends UserProfileFixture {

    @Override
    protected void installProfiles() {

        Profile templateProfile = newUserProfile();
        Perspective perspective = templateProfile.newPerspective("Expenses");
        perspective.addToServices(Claims.class);
        perspective.addToServices(Employees.class);
        saveAsDefault(templateProfile);

        Profile svenProfile = newUserProfile();
        Perspective claimsPerspective = svenProfile.newPerspective("Claims");
        claimsPerspective.addToServices(Claims.class);
        claimsPerspective.addToServices(Employees.class);
        saveForUser("sven", svenProfile);

        saveForUser("bob", newUserProfile(svenProfile));
        saveForUser("joe", newUserProfile(svenProfile));

        Profile dickProfile = newUserProfile();
        Perspective supervisorPerspective = dickProfile.newPerspective("Supervisor");
        supervisorPerspective.addToServices(Claims.class);
        supervisorPerspective.addToServices(Employees.class);
        supervisorPerspective.addToServices(RecordActionServiceImpl.class);

        saveForUser("dick", dickProfile);

    }
}
