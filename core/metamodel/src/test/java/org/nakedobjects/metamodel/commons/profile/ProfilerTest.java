package org.nakedobjects.metamodel.commons.profile;

import junit.framework.TestCase;


public class ProfilerTest extends TestCase {
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ProfilerTest.class);
    }

    private Profiler profiler;

    @Override
    public void setUp() {
        Profiler.setProfilerSystem(new ProfilerTestSystem());
        profiler = new Profiler("name");
    }

    public void testFreeMemory() {
        assertEquals("20,300 bytes", Profiler.memoryLog());
    }

    public void testMemoryUsage() {
        assertEquals(10300, profiler.getMemoryUsage());
        assertEquals(20000, profiler.getMemoryUsage());
    }

    public void testMemoryUsageLog() {
        assertEquals("10,300 bytes", profiler.memoryUsageLog());
    }

    public void testTiming() {
        profiler.start();
        profiler.stop();
        assertEquals(100, profiler.getElapsedTime());
    }

    public void testTimingLog() {
        profiler.start();
        profiler.stop();
        assertEquals("0.1 secs", profiler.timeLog());
    }

    public void testContinueWithStartAndStopPausesTiming() {
        profiler.start();
        profiler.stop();

        profiler.start();
        profiler.stop();
        assertEquals(400, profiler.getElapsedTime());
    }

    public void testResetDuringTiming() {
        profiler.start();

        profiler.reset();
        assertEquals(200, profiler.getElapsedTime());
    }

    public void testResetAfterStopResetsToZero() {
        profiler.start();
        profiler.stop();

        profiler.reset();
        assertEquals(0, profiler.getElapsedTime());

        profiler.start();
        profiler.stop();
        assertEquals(400, profiler.getElapsedTime());
    }

    public void testZero() {
        assertEquals(0, profiler.getElapsedTime());
    }

    public void testRepeatedElapseTimeAfterStopGivesSameTime() {
        profiler.start();
        profiler.stop();
        assertEquals(100, profiler.getElapsedTime());
        assertEquals(100, profiler.getElapsedTime());
        assertEquals(100, profiler.getElapsedTime());
    }

    public void testRepeatedElapseTimeGivesLatestTime() {
        profiler.start();
        assertEquals(100, profiler.getElapsedTime());
        assertEquals(300, profiler.getElapsedTime());
        assertEquals(600, profiler.getElapsedTime());
    }

}
// Copyright (c) Naked Objects Group Ltd.
