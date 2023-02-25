package org.nakedobjects.metamodel.facets.ordering.memberorder;

import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.nakedobjects.metamodel.facets.ordering.OrderSet;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectMemberPeer;


public class DeweyOrderSetTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DeweyOrderSetTest.class));
    }

    private final MemberPeerStub lastNameMember = new MemberPeerStub("LastName");
    private final MemberPeerStub firstNameMember = new MemberPeerStub("FirstName");
    private final MemberPeerStub houseNumberMember = new MemberPeerStub("HouseNumber");
    private final MemberPeerStub streetNameMember = new MemberPeerStub("StreetName");
    private final MemberPeerStub postalTownMember = new MemberPeerStub("PostalTown");
    private final NakedObjectMemberPeer[] lastNameAndFirstName = new MemberPeerStub[] { lastNameMember, firstNameMember };
    private final NakedObjectMemberPeer[] nameAndAddressMembers = new MemberPeerStub[] { lastNameMember, firstNameMember,
            houseNumberMember, streetNameMember, postalTownMember };
    private final NakedObjectMemberPeer[] lastNameFirstNameAndPostalTown = new MemberPeerStub[] { lastNameMember,
            firstNameMember, postalTownMember };

    @Override
    protected void setUp() {
        LogManager.getLoggerRepository().setThreshold(Level.OFF);

    }

    @Override
    protected void tearDown() throws Exception {}

    public void testDefaultGroup() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameAndFirstName);
        assertEquals("", orderSet.getGroupName());
        assertEquals("", orderSet.getGroupFullName());
        assertEquals("", orderSet.getGroupPath());
    }

    public void testDefaultGroupSize() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameAndFirstName);
        assertEquals(2, orderSet.size());
        assertEquals(2, orderSet.elementList().size());
        assertEquals(0, orderSet.children().size());
    }

    public void testDefaultGroupTwoMembersSorted() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameAndFirstName);
        assertEquals(lastNameMember, orderSet.elementList().get(0));
        assertEquals(firstNameMember, orderSet.elementList().get(1));
    }

    public void testTwoMembersAtDefaultGroupOtherWay() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", firstNameMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameAndFirstName);
        assertEquals(firstNameMember, orderSet.elementList().get(0));
        assertEquals(lastNameMember, orderSet.elementList().get(1));
    }

    public void testWithChildGroupDefaultGroupName() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));
        houseNumberMember.addFacet(new MemberOrderFacetAnnotation("address", "1", houseNumberMember));
        streetNameMember.addFacet(new MemberOrderFacetAnnotation("address", "2", streetNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("address", "3", postalTownMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(nameAndAddressMembers);
        assertEquals("", orderSet.getGroupName());
        assertEquals("", orderSet.getGroupFullName());
        assertEquals("", orderSet.getGroupPath());
    }

    public void testWithChildGroupSize() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));
        houseNumberMember.addFacet(new MemberOrderFacetAnnotation("address", "1", houseNumberMember));
        streetNameMember.addFacet(new MemberOrderFacetAnnotation("address", "2", streetNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("address", "3", postalTownMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(nameAndAddressMembers);
        assertEquals(1, orderSet.children().size());
        assertEquals(3, orderSet.size());
    }

    public void testWithChildGroupChildsGroupName() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));
        houseNumberMember.addFacet(new MemberOrderFacetAnnotation("address", "1", houseNumberMember));
        streetNameMember.addFacet(new MemberOrderFacetAnnotation("address", "2", streetNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("address", "3", postalTownMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(nameAndAddressMembers);
        final List children = orderSet.children();
        final OrderSet childOrderSet = (OrderSet) children.get(0);
        assertEquals("Address", childOrderSet.getGroupName());
        assertEquals("address", childOrderSet.getGroupFullName());
        assertEquals("", childOrderSet.getGroupPath());
    }

    public void testWithChildGroupChildsGroupSize() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));
        houseNumberMember.addFacet(new MemberOrderFacetAnnotation("address", "1", houseNumberMember));
        streetNameMember.addFacet(new MemberOrderFacetAnnotation("address", "2", streetNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("address", "3", postalTownMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(nameAndAddressMembers);
        final OrderSet childOrderSet = (OrderSet) orderSet.children().get(0);
        assertEquals(3, childOrderSet.size());
        assertEquals(0, childOrderSet.children().size());
    }

    public void testWithChildGroupChildsGroupElementOrdering() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));
        houseNumberMember.addFacet(new MemberOrderFacetAnnotation("address", "6", houseNumberMember));
        streetNameMember.addFacet(new MemberOrderFacetAnnotation("address", "5", streetNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("address", "4", postalTownMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(nameAndAddressMembers);
        final OrderSet childOrderSet = (OrderSet) orderSet.children().get(0);
        assertEquals(postalTownMember, childOrderSet.elementList().get(0));
        assertEquals(streetNameMember, childOrderSet.elementList().get(1));
        assertEquals(houseNumberMember, childOrderSet.elementList().get(2));
    }

    public void testWithChildGroupOrderedAtEnd() {
        houseNumberMember.addFacet(new MemberOrderFacetAnnotation("address", "6", houseNumberMember));
        streetNameMember.addFacet(new MemberOrderFacetAnnotation("address", "5", streetNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("address", "4", postalTownMember));
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "3", lastNameMember));
        firstNameMember.addFacet(new MemberOrderFacetAnnotation("", "2", firstNameMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(nameAndAddressMembers);
        assertEquals(firstNameMember, orderSet.elementList().get(0));
        assertEquals(lastNameMember, orderSet.elementList().get(1));
        assertTrue(orderSet.elementList().get(2) instanceof OrderSet);
    }

    public void testDefaultGroupNeitherAnnotatedSize() {
        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameAndFirstName);
        assertEquals(2, orderSet.elementList().size());
    }

    public void testDefaultGroupNeitherAnnotatedOrderedByName() {
        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameAndFirstName);
        assertEquals(firstNameMember, orderSet.elementList().get(0));
        assertEquals(lastNameMember, orderSet.elementList().get(1));
    }

    public void testDefaultGroupMixOfAnnotatedAndNotSize() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("address", "2", postalTownMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameFirstNameAndPostalTown);
        assertEquals(3, orderSet.elementList().size());
    }

    public void testDefaultGroupMixOfAnnotatedAndNotOrderedWithAnnotatedFirst() {
        lastNameMember.addFacet(new MemberOrderFacetAnnotation("", "1", lastNameMember));
        postalTownMember.addFacet(new MemberOrderFacetAnnotation("", "2", postalTownMember));

        final DeweyOrderSet orderSet = DeweyOrderSet.createOrderSet(lastNameFirstNameAndPostalTown);

        assertEquals(lastNameMember, orderSet.elementList().get(0));
        assertEquals(postalTownMember, orderSet.elementList().get(1));
        assertEquals(firstNameMember, orderSet.elementList().get(2));
    }

}
// Copyright (c) Naked Objects Group Ltd.
