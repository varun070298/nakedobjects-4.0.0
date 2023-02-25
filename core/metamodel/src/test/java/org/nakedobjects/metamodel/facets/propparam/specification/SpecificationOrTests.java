package org.nakedobjects.metamodel.facets.propparam.specification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.nakedobjects.applib.spec.Specification;
import org.nakedobjects.applib.spec.SpecificationOr;


public class SpecificationOrTests {

    private Specification alwaysSatisfied = new SpecificationAlwaysSatisfied();
    private Specification neverSatisfied = new SpecificationNeverSatisfied();

    @Test
    public void satisfiedIfNone() {
        class MySpecOr extends SpecificationOr {
            public MySpecOr() {}
        };
        Specification mySpecOr = new MySpecOr();
        assertThat(mySpecOr.satisfies(null), is(nullValue()));
    }

    @Test
    public void satisfiedIfOneAndOkay() {
        class MySpecOr extends SpecificationOr {
            public MySpecOr() {
                super(alwaysSatisfied);
            }
        };
        Specification mySpecOr = new MySpecOr();
        assertThat(mySpecOr.satisfies(null), is(nullValue()));
    }

    @Test
    public void notSatisfiedIfOneAndNotOkay() {
        class MySpecOr extends SpecificationOr {
            public MySpecOr() {
                super(neverSatisfied);
            }
        };
        Specification mySpecOr = new MySpecOr();
        assertThat(mySpecOr.satisfies(null), is(not(nullValue())));
        assertThat(mySpecOr.satisfies(null), is("not satisfied"));
    }

    @Test
    public void satisfiedIfTwoAndOneIsNotOkay() {
        class MySpecOr extends SpecificationOr {
            public MySpecOr() {
                super(alwaysSatisfied, neverSatisfied);
            }
        };
        Specification mySpecOr = new MySpecOr();
        assertThat(mySpecOr.satisfies(null), is(nullValue()));
    }

    @Test
    public void satisfiedIfTwoAndBothAreOkay() {
        class MySpecOr extends SpecificationOr {
            public MySpecOr() {
                super(alwaysSatisfied, alwaysSatisfied);
            }
        };
        Specification mySpecOr = new MySpecOr();
        assertThat(mySpecOr.satisfies(null), is(nullValue()));
    }

    @Test
    public void notSatisfiedIfTwoAndBothAreNotOkayWithConcatenatedReason() {
        class MySpecOr extends SpecificationOr {
            public MySpecOr() {
                super(neverSatisfied, neverSatisfied);
            }
        };
        Specification mySpecOr = new MySpecOr();
        assertThat(mySpecOr.satisfies(null), is(not(nullValue())));
        assertThat(mySpecOr.satisfies(null), is("not satisfied; not satisfied"));
    }

}
