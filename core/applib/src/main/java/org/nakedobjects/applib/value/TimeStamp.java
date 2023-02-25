package org.nakedobjects.applib.value;

import org.nakedobjects.applib.annotation.Value;
import org.nakedobjects.applib.clock.Clock;


/**
 * Value object representing a date/time value marking a point in time This is a user facing date/time value,
 * more a marker used to indicate the temporal relationship between two objects.
 * 
 * @see DateTime
 */
@Value(semanticsProviderName = "org.nakedobjects.metamodel.value.TimeStampValueSemanticsProvider")
public class TimeStamp extends Magnitude {
    private static final long serialVersionUID = 1L;
    private final long time;

    /**
     * Create a TimeStamp object for storing a timeStamp set to the current time.
     */
    public TimeStamp() {
        time = Clock.getTime();
    }

    public TimeStamp(final long time) {
        this.time = time;
    }

    /**
     * returns true if the time stamp of this object has the same value as the specified timeStamp
     */
    @Override
    public boolean isEqualTo(final Magnitude timeStamp) {
        if (timeStamp instanceof TimeStamp) {
            return this.time == ((TimeStamp) timeStamp).time;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    /**
     * returns true if the timeStamp of this object is earlier than the specified timeStamp
     */
    @Override
    public boolean isLessThan(final Magnitude timeStamp) {
        if (timeStamp instanceof TimeStamp) {
            return time < ((TimeStamp) timeStamp).time;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    public long longValue() {
        return time;
    }

    @Override
    public String toString() {
        return "Time Stamp " + longValue();
    }
}
// Copyright (c) Naked Objects Group Ltd.
