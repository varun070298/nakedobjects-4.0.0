package org.nakedobjects.metamodel.facets.propparam.specification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.nakedobjects.applib.spec.Specification;
import org.nakedobjects.applib.spec.SpecificationNot;


public class SpecificationNotTests {

    private Specification alwaysSatisfied = new SpecificationAlwaysSatisfied();
    private Specification neverSatisfied = new SpecificationNeverSatisfied();

    @Test
    public void notSatisfiedIfUnderlyingIs() {
        class MySpecNot extends SpecificationNot {
            public MySpecNot() {
                super(alwaysSatisfied);
            }
        };
        Specification mySpecOr = new MySpecNot();
        assertThat(mySpecOr.satisfies(null), is(not(nullValue())));
        assertThat(mySpecOr.satisfies(null), is("not satisfied"));
    }


    @Test
    public void satisfiedIfUnderlyingIsNot() {
        class MySpecNot extends SpecificationNot {
            public MySpecNot() {
                super(neverSatisfied);
            }
        };
        Specification mySpecOr = new MySpecNot();
        assertThat(mySpecOr.satisfies(null), is(nullValue()));
    }

}
