package org.nakedobjects.examples.claims.service.claim;

import java.util.List;

import org.nakedobjects.applib.AbstractFactoryAndRepository;

import org.nakedobjects.examples.claims.dom.claim.Claim;
import org.nakedobjects.examples.claims.dom.claim.ClaimRepository;
import org.nakedobjects.examples.claims.dom.claim.Claimant;


public class ClaimRepositoryInMemory extends AbstractFactoryAndRepository implements ClaimRepository {

	// {{ Id, iconName
    public String getId() {
        return "claims";
    }
    public String iconName() {
        return "ClaimRepository";
    }
    // }}

    
    // {{ action: allClaims
    public List<Claim> allClaims() {
        return allInstances(Claim.class);
    }
    // }}

    
    // {{ action: findClaims
    public List<Claim> findClaims(String description
    		) {
        return allMatches(Claim.class, description);
    }
    // }}

    
    // {{ action: claimsFor
    public List<Claim> claimsFor(Claimant claimant) {
        Claim pattern = newTransientInstance(Claim.class);
        pattern.setStatus(null);
        pattern.setDate(null);
        pattern.setClaimant(claimant);
        return allMatches(Claim.class, pattern);
    }
    // }}
    
    // {{ action: newClaim
	public Claim newClaim(Claimant claimant) {
        Claim claim = newTransientInstance(Claim.class);
        if (claimant != null) {
	        claim.setClaimant(claimant);
	        claim.setApprover(claimant.getApprover());
        }
		return claim;
	}
	// }}

    
}
