package org.nakedobjects.applib.spec;

import org.nakedobjects.applib.util.ReasonBuffer;


/**
 * Adapter to make it easy to perform boolean algebra on {@link Specification}s.
 * 
 * <p>
 * Subclasses represent the intersection of multiple {@link Specification}s.  An 
 * implementation should instantiate the {@link Specification}s to be satisfied 
 * in its constructor.
 * 
 * <p>
 * For example:
 * <pre>
 * public class MilkAndSugarSpec extends SpecificationAnd {
 *     public MilkAndSugarSpec() {
 *         super(
 *             new MustBeMilkySpec(), 
 *             new TwoLumpsOfSugarSpec()
 *         );
 *     }
 * }
 * </pre>
 * 
 * @see SpecificationOr
 * @see SpecificationNot
 */
public abstract class SpecificationAnd implements Specification {

    private final Specification[] specifications;

    public SpecificationAnd(Specification... specifications) {
        this.specifications = specifications;
    }
 
    public String satisfies(Object obj) {
        ReasonBuffer buf = new ReasonBuffer();
        for(Specification specification: specifications) {
            String reasonNotSatisfiedIfAny = specification.satisfies(obj);
            buf.append(reasonNotSatisfiedIfAny);
        }
        return buf.getReason(); // may be null if all were satisfied.
    }


}
