package org.nakedobjects.metamodel.facets.propparam.specification;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.nakedobjects.applib.spec.Specification;
import org.nakedobjects.applib.spec.SpecificationAnd;


public class SpecificationAndTests {

    private Specification alwaysSatisfied = new SpecificationAlwaysSatisfied();
    private Specification neverSatisfied = new SpecificationNeverSatisfied();

    @Test
    public void satisfiedIfNone() {
        class MySpecAnd extends SpecificationAnd {
            public MySpecAnd() {}
        };
        Specification mySpecAnd = new MySpecAnd();
        assertThat(mySpecAnd.satisfies(null), is(nullValue()));
    }

    @Test
    public void satisfiedIfOneAndOkay() {
        class MySpecAnd extends SpecificationAnd {
            public MySpecAnd() {
                super(alwaysSatisfied);
            }
        };
        Specification mySpecAnd = new MySpecAnd();
        assertThat(mySpecAnd.satisfies(null), is(nullValue()));
    }

    @Test
    public void notSatisfiedIfOneAndNotOkay() {
        class MySpecAnd extends SpecificationAnd {
            public MySpecAnd() {
                super(neverSatisfied);
            }
        };
        Specification mySpecAnd = new MySpecAnd();
        assertThat(mySpecAnd.satisfies(null), is(not(nullValue())));
        assertThat(mySpecAnd.satisfies(null), is("not satisfied"));
    }

    @Test
    public void notSatisfiedIfTwoAndOneIsNotOkay() {
        class MySpecAnd extends SpecificationAnd {
            public MySpecAnd() {
                super(alwaysSatisfied, neverSatisfied);
            }
        };
        Specification mySpecAnd = new MySpecAnd();
        assertThat(mySpecAnd.satisfies(null), is(not(nullValue())));
        assertThat(mySpecAnd.satisfies(null), is("not satisfied"));
    }

    @Test
    public void satisfiedIfTwoAndBothAreOkay() {
        class MySpecAnd extends SpecificationAnd {
            public MySpecAnd() {
                super(alwaysSatisfied, alwaysSatisfied);
            }
        };
        Specification mySpecAnd = new MySpecAnd();
        assertThat(mySpecAnd.satisfies(null), is(nullValue()));
    }

    @Test
    public void notSatisfiedIfTwoAndBothAreNotOkayWithConcatenatedReason() {
        class MySpecAnd extends SpecificationAnd {
            public MySpecAnd() {
                super(neverSatisfied, neverSatisfied);
            }
        };
        Specification mySpecAnd = new MySpecAnd();
        assertThat(mySpecAnd.satisfies(null), is(not(nullValue())));
        assertThat(mySpecAnd.satisfies(null), is("not satisfied; not satisfied"));
    }

}
