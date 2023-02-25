package org.nakedobjects.applib.spec;

import org.nakedobjects.applib.util.ReasonBuffer;


/**
 * Adapter to make it easy to perform boolean algebra on {@link Specification}s.
 * 
 * <p>
 * Subclasses represent the conjunction of multiple {@link Specification}s.  An 
 * implementation should instantiate the {@link Specification}s to be satisfied 
 * in its constructor.
 * 
 * <p>
 * For example:
 * <pre>
 * public class TeaOrCoffeeSpec extends SpecificationOr {
 *     public TeaOrCoffeeSpec() {
 *         super(
 *             new MustBeTeaSpec(), 
 *             new MustBeCoffeeSpec()
 *         );
 *     }
 * }
 * </pre>
 * 
 * @see SpecificationAnd
 * @see SpecificationNot
 */
public abstract class SpecificationOr implements Specification {

    private final Specification[] specifications;

    public SpecificationOr(Specification... specifications) {
        this.specifications = specifications;
    }
 
    public String satisfies(Object obj) {
        ReasonBuffer buf = new ReasonBuffer();
        for(Specification specification: specifications) {
            String reasonNotSatisfiedIfAny = specification.satisfies(obj);
            if (reasonNotSatisfiedIfAny == null) {
                // at least one is ok, so all is ok.
                return null;
            }
            buf.append(reasonNotSatisfiedIfAny);
        }
        return buf.getReason(); // may be null if all were satisfied.
    }


}
