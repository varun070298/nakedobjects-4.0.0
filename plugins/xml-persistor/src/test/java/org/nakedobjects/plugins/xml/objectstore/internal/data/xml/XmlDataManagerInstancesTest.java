package org.nakedobjects.plugins.xml.objectstore.internal.data.xml;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.xml.objectstore.internal.clock.TestClock;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectData;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectDataVector;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;


public class XmlDataManagerInstancesTest extends ProxyJunit3TestCase {
    private static final Logger LOG = Logger.getLogger(XmlDataManagerInstancesTest.class);

    protected XmlDataManager manager;
    protected final int SIZE = 5;

    private SerialOid oids[];
    private ObjectData data[];
    private ObjectData pattern;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(XmlDataManagerInstancesTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        clearTestDirectory();
        manager = new XmlDataManager(new XmlFile(system.getConfiguration(), "tmp/tests"));

        FileVersion.setClock(new TestClock());

        oids = new SerialOid[SIZE];
        data = new ObjectData[SIZE];

        final NakedObjectSpecification type = system.getSpecification(Object.class);
        pattern = new ObjectData(type, null, new FileVersion("user", 13));
        for (int i = 0; i < SIZE; i++) {
            oids[i] = SerialOid.createPersistent(i);
            data[i] = new ObjectData(type, oids[i], new FileVersion("user", 13));
            manager.insertObject(data[i]);
        }
        
        LOG.debug("test starting...");
    }

    protected static void clearTestDirectory() {
        final File directory = new File("tmp" + File.separator + "tests");
        final String[] files = directory.list(new FilenameFilter() {
            public boolean accept(final File arg0, final String name) {
                return name.endsWith(".xml");
            }
        });

        if (files != null) {
            for (int f = 0; f < files.length; f++) {
                new File(directory, files[f]).delete();
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        system.shutdown();
        super.tearDown();
    }

    public void testNumberOfInstances() {
        assertEquals(SIZE, manager.numberOfInstances(pattern));
    }

    public void testRemove() throws Exception {
        final SerialOid oid = oids[2];
        manager.remove(oid);

        assertEquals(SIZE - 1, manager.numberOfInstances(pattern));

        final ObjectDataVector instances = manager.getInstances(pattern);
        for (int i = 0; i < instances.size(); i++) {
            assertFalse(instances.element(i) == data[2]);
        }

        assertNull(((ObjectData) manager.loadData(oid)));
    }

    public void testSaveObject() throws Exception {
        data[2].set("Person", SerialOid.createPersistent(231));
        data[2].set("Name", "Fred");
        manager.save(data[2]);

        assertTrue(manager.getInstances(pattern).contains(data[2]));
        final ObjectData read = (ObjectData) manager.loadData(oids[2]);
        assertEquals(data[2], read);
        assertEquals(data[2].get("Name"), read.get("Name"));
        assertEquals(data[2].get("Person"), read.get("Person"));
    }

    /*
     * public void xxxtestInsertValues() throws ObjectStoreException { NakedObjectSpecification type =
     * NakedObjects.getSpecificationLoader().loadSpecification(ValueObjectExample.class.getName()); SerialOid
     * oid = new SerialOid(99); ObjectData data = new ObjectData(type, oid);
     * 
     * 
     * Date date1 = new Date(); date1.add(1,2,3); data.saveValue("Date", date1);
     * 
     * FloatingPointNumber floatingPoint1 = new FloatingPointNumber(); floatingPoint1.setValue(3.145);
     * data.saveValue("Floating Point", floatingPoint1);
     * 
     * Label label1 = new Label(); label1.setValue("Labelled"); data.saveValue("Label", label1);
     * 
     * Logical logical1 = new Logical(); logical1.setValue(true); data.saveValue("Logical", logical1);
     * 
     * Money money1 = new Money(); money1.setValue(1233.45); data.saveValue("Money", money1);
     * 
     * Option option1 = new Option(new String[] {"Fred", "Sam", "joe"}, 1); data.saveValue("Option", option1);
     * 
     * Percentage percentage1 = new Percentage(); percentage1.setValue(95); data.saveValue("Percentage",
     * percentage1);
     * 
     * TextString textString1 = new TextString("Fred"); data.saveValue("Text String", textString1);
     * 
     * DateTime timestamp1 = new DateTime(); timestamp1.add(1,2,3); data.saveValue("Time Stamp", timestamp1);
     * 
     * Time time1 = new Time(); time1.add(1,30); data.saveValue("Time", time1);
     * 
     * URLString urlString1 = new URLString("http://nakedobjects.org/"); data.saveValue("Url String",
     * urlString1);
     * 
     * WholeNumber number1 = new WholeNumber(); number1.setValue(435422); data.saveValue("Whole Number",
     * number1);
     * 
     * 
     * manager.insert(data);
     * 
     * 
     * 
     * ObjectData object = manager.loadObjectData(oid);
     * 
     * Date date2 = new Date(); object.restoreValue("Date", date2); assertEquals(date1, date2);
     * 
     * FloatingPointNumber floatingPoint2 = new FloatingPointNumber(); object.restoreValue("Floating Point",
     * floatingPoint2); assertEquals(floatingPoint1, floatingPoint2);
     * 
     * Label label2 = new Label(); object.restoreValue("Label", label2); assertEquals(label1, label2);
     * 
     * Logical logical2 = new Logical(); object.restoreValue("Logical", logical2); assertEquals(logical1,
     * logical2);
     * 
     * Money money2 = new Money(); object.restoreValue("Money", money2); assertEquals(money1, money2);
     * 
     * Option option2 = new Option(new String [] {"Fred", "Sam", "joe"}); object.restoreValue("Option",
     * option2); assertEquals(option1, option2);
     * 
     * Percentage percentage2 = new Percentage(); object.restoreValue("Percentage", percentage2);
     * assertEquals(percentage1, percentage2);
     * 
     * Time time2 = new Time(); object.restoreValue("Time", time2); assertEquals(time1, time2);
     * 
     * DateTime timestamp2 = new DateTime(); object.restoreValue("Time Stamp", timestamp2);
     * assertEquals(timestamp1, timestamp2);
     * 
     * TextString textString2 = new TextString(); object.restoreValue("Text String", textString2);
     * assertEquals(textString1, textString2);
     * 
     * URLString urlString2 = new URLString(); object.restoreValue("Url String", urlString2);
     * assertEquals(urlString1, urlString2);
     * 
     * WholeNumber number2 = new WholeNumber(); object.restoreValue("Whole Number", number2);
     * assertEquals(number1, number2); }
     * 
     * public void xxxtestSaveValues() throws ObjectStoreException { NakedObjectSpecification type =
     * NakedObjects.getSpecificationLoader().loadSpecification(ValueObjectExample.class.getName()); SerialOid
     * oid = new SerialOid(99); ObjectData data = new ObjectData(type, oid);
     * 
     * manager.insert(data);
     * 
     * 
     * Date date1 = new Date(); date1.add(1,2,3); data.saveValue("Date", date1);
     * 
     * FloatingPointNumber floatingPoint1 = new FloatingPointNumber(); floatingPoint1.setValue(3.145);
     * data.saveValue("Floating Point", floatingPoint1);
     * 
     * Label label1 = new Label(); label1.setValue("Labelled"); data.saveValue("Label", label1);
     * 
     * Logical logical1 = new Logical(); logical1.setValue(true); data.saveValue("Logical", logical1);
     * 
     * Money money1 = new Money(); money1.setValue(1233.45); data.saveValue("Money", money1);
     * 
     * Option option1 = new Option(new String[] {"Fred", "Sam", "joe"}, 1); data.saveValue("Option", option1);
     * 
     * Percentage percentage1 = new Percentage(); percentage1.setValue(95); data.saveValue("Percentage",
     * percentage1);
     * 
     * TextString textString1 = new TextString("Fred"); data.saveValue("Text String", textString1);
     * 
     * DateTime timestamp1 = new DateTime(); timestamp1.add(1,2,3); data.saveValue("Time Stamp", timestamp1);
     * 
     * Time time1 = new Time(); time1.add(1,30); data.saveValue("Time", time1);
     * 
     * URLString urlString1 = new URLString("http://nakedobjects.org/"); data.saveValue("Url String",
     * urlString1);
     * 
     * WholeNumber number1 = new WholeNumber(); number1.setValue(435422); data.saveValue("Whole Number",
     * number1);
     * 
     * 
     * manager.save(data);
     * 
     * 
     * 
     * ObjectData object = manager.loadObjectData(oid);
     * 
     * Date date2 = new Date(); object.restoreValue("Date", date2); assertEquals(date1, date2);
     * 
     * FloatingPointNumber floatingPoint2 = new FloatingPointNumber(); object.restoreValue("Floating Point",
     * floatingPoint2); assertEquals(floatingPoint1, floatingPoint2);
     * 
     * Label label2 = new Label(); object.restoreValue("Label", label2); assertEquals(label1, label2);
     * 
     * Logical logical2 = new Logical(); object.restoreValue("Logical", logical2); assertEquals(logical1,
     * logical2);
     * 
     * Money money2 = new Money(); object.restoreValue("Money", money2); assertEquals(money1, money2);
     * 
     * Option option2 = new Option(new String [] {"Fred", "Sam", "joe"}); object.restoreValue("Option",
     * option2); assertEquals(option1, option2);
     * 
     * Percentage percentage2 = new Percentage(); object.restoreValue("Percentage", percentage2);
     * assertEquals(percentage1, percentage2);
     * 
     * Time time2 = new Time(); object.restoreValue("Time", time2); assertEquals(time1, time2);
     * 
     * DateTime timestamp2 = new DateTime(); object.restoreValue("Time Stamp", timestamp2);
     * assertEquals(timestamp1, timestamp2);
     * 
     * TextString textString2 = new TextString(); object.restoreValue("Text String", textString2);
     * assertEquals(textString1, textString2);
     * 
     * URLString urlString2 = new URLString(); object.restoreValue("Url String", urlString2);
     * assertEquals(urlString1, urlString2);
     * 
     * WholeNumber number2 = new WholeNumber(); object.restoreValue("Whole Number", number2);
     * assertEquals(number1, number2);
     * 
     *  }
     * 
     */
}
// Copyright (c) Naked Objects Group Ltd.
