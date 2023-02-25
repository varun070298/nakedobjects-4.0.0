package org.nakedobjects.metamodel.facets;

import java.util.List;

import org.nakedobjects.applib.annotation.ActionOrder;
import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.Debug;
import org.nakedobjects.applib.annotation.DescribedAs;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Executed;
import org.nakedobjects.applib.annotation.Exploration;
import org.nakedobjects.applib.annotation.FieldOrder;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Immutable;
import org.nakedobjects.applib.annotation.MultiLine;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.NotPersisted;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.annotation.Plural;
import org.nakedobjects.applib.annotation.TypeOf;
import org.nakedobjects.applib.annotation.When;


@Bounded
@ActionOrder("1, 2, 3")
@FieldOrder("4, 5, 6")
@Immutable(When.ONCE_PERSISTED)
@Named("singular name")
@Plural("plural name")
public class JavaObjectWithAnnotations {

    public void side(@Named("one") @Optional final String param) {}

    @Debug
    public void start() {}

    @Exploration
    public void top() {}

    @Hidden
    public void stop() {}

    @NotPersisted
    public int getOne() {
        return 1;
    }

    public void setOne(final int value) {}

    @Disabled
    public String getTwo() {
        return "";
    }

    @TypeOf(Long.class)
    @DescribedAs("description text")
    @Named("name text")
    public List getCollection() {
        return null;
    }

    @Executed(Executed.Where.LOCALLY)
    public void left() {}

    @Executed(Executed.Where.REMOTELY)
    public void right() {}

    @Disabled
    public void bottom() {}

    public void complete(final String notMultiline, @MultiLine(numberOfLines = 10) final String multiLine) {}

}

// Copyright (c) Naked Objects Group Ltd.
