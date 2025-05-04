package com.foodbuddy.food_buddy_api.UnitTest.model;

import org.junit.jupiter.api.Test;

class CommunityTest {

    @Test
    void shouldCreateCommunityCorrectly() {
        MyUser creator = new MyUser();
        creator.setId(1L);
        creator.setUsername("mk");

        Community community = new Community();
        community.setName("WG Berlin");
        community.setLeader(creator);
        community.addMember(creator);

        assertEquals("WG Berlin", community.getName());
        assertEquals(creator, community.getLeader());
        assertTrue(community.hasMember(creator));
        assertTrue(community.isLeader(creator));
    }

    @Test
    void shouldAddAndRemoveMembers() {
        MyUser user = new MyUser();
        user.setId(1L);

        Community community = new Community();
        community.addMember(user);

        assertTrue(community.hasMember(user));

        community.removeMember(user);
        assertFalse(community.hasMember(user));
    }

    @Test
    void shouldIdentifyLeaderCorrectly() {
        MyUser leader = new MyUser();
        leader.setId(2L);

        Community community = new Community();
        community.setLeader(leader);

        assertTrue(community.isLeader(leader));

        MyUser otherUser = new MyUser();
        otherUser.setId(3L);
        assertFalse(community.isLeader(otherUser));
    }

    @Test
    void shouldTransferLeadership() {
        MyUser oldLeader = new MyUser();
        oldLeader.setId(1L);

        MyUser newLeader = new MyUser();
        newLeader.setId(2L);

        Community community = new Community();
        community.setLeader(oldLeader);
        community.transferLeadershipTo(newLeader);

        assertTrue(community.isLeader(newLeader));
    }
}
