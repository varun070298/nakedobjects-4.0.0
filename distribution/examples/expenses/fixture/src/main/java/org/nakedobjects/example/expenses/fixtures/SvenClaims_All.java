package org.nakedobjects.example.expenses.fixtures;

public class SvenClaims_All extends AbstractClaimFixture {

    public SvenClaims_All() {
        addFixture(new SvenClaim1NewStatus());
        addFixture(new SvenClaim2Submitted());
        addFixture(new SvenClaim5New());
        addFixture(new SvenClaim3Returned());
        addFixture(new SvenClaim4Approved());
    }

    @Override
    public void install() {

    }

}
