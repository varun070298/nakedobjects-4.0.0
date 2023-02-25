package org.nakedobjects.applib.spec;



/**
 * Adapter to make it easy to perform boolean algebra on {@link Specification}s.
 * 
 * <p>
 * <p>
 * Subclasses represent the logical inverse of a {@link Specification}s.  An 
 * implementation should instantiate the {@link Specification}s to be satisfied 
 * in its constructor.
 * 
 * <p>
 * For example:
 * <pre>
 * public class NoSugarThanksSpec extends SpecificationNot {
 *     public NoSugarThanksSpec() {
 *         super(
 *             new TwoLumpsOfSugarSpec(), 
 *         );
 *     }
 * }
 * </pre>
 * 
 * @see SpecificationAnd
 * @see SpecificationOr
 */
public abstract class SpecificationNot implements Specification {

    private final Specification specification;

    public SpecificationNot(Specification specification) {
        this.specification = specification;
    }
 
    public String satisfies(Object obj) {
        String satisfies = specification.satisfies(obj);
        return satisfies != null? null: "not satisfied";
    }
}
