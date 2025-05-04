package com.foodbuddy.food_buddy_api.application.service;

import com.foodbuddy.food_buddy_api.application.helper.DomainLookupService;
import com.foodbuddy.food_buddy_api.domain.model.Community;
import com.foodbuddy.food_buddy_api.domain.model.MyUser;
import com.foodbuddy.food_buddy_api.domain.repository.CommunityRepository;
import com.foodbuddy.food_buddy_api.domain.repository.MyUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final MyUserRepository userRepository;
    private final DomainLookupService domainLookupService;

    public CommunityService(CommunityRepository communityRepository, MyUserRepository userRepository, DomainLookupService domainLookupService) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.domainLookupService = domainLookupService;
    }

    @Transactional
    public Community createCommunity(String name, String username) {
        MyUser creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Community community = new Community();
        community.setName(name);
        community.setLeader(creator);
        community.addMember(creator);

        return communityRepository.save(community);
    }

    @Transactional
    public void addMember(Long communityId, String leaderUsername, String newMemberUsername) {
        Community community = domainLookupService.getCommunityOrThrow(communityId);
        MyUser leader = domainLookupService.getUserOrThrow(leaderUsername);
        MyUser newMember = domainLookupService.getUserOrThrow(newMemberUsername);

        if (!community.isLeader(leader)) {
            throw new RuntimeException("Only the leader can add members.");
        }

        community.addMember(newMember);
        communityRepository.save(community);
    }

    @Transactional
    public void removeMember(Long communityId, String leaderUsername, String memberUsername) {
        Community community = domainLookupService.getCommunityOrThrow(communityId);
        MyUser leader = domainLookupService.getUserOrThrow(memberUsername);
        MyUser member = domainLookupService.getUserOrThrow(memberUsername);

        if (!community.isLeader(leader)) {
            throw new RuntimeException("Only the leader can remove members.");
        }

        if (leader.getUsername().equals(member.getUsername())) {
            throw new RuntimeException("Leader cannot remove themselves.");
        }

        community.removeMember(member);
        communityRepository.save(community);
    }

    @Transactional
    public void transferLeadership(Long communityId, String currentLeaderUsername, String newLeaderUsername) {
        Community community = domainLookupService.getCommunityOrThrow(communityId);
        MyUser currentLeader = domainLookupService.getUserOrThrow(currentLeaderUsername);
        MyUser newLeader = domainLookupService.getUserOrThrow(newLeaderUsername);

        if (!community.isLeader(currentLeader)) {
            throw new RuntimeException("Only the current leader can transfer leadership.");
        }

        if (!community.hasMember(newLeader)) {
            throw new RuntimeException("New leader must already be a member of the community.");
        }

        community.transferLeadershipTo(newLeader);
        communityRepository.save(community);
    }

    @Transactional
    public void leaveCommunity(Long communityId, String username) {
        Community community = domainLookupService.getCommunityOrThrow(communityId);
        MyUser user = domainLookupService.getUserOrThrow(username);

        if (community.isLeader(user)) {
            throw new RuntimeException("Leader must transfer leadership before leaving.");
        }

        community.removeMember(user);
        communityRepository.save(community);
    }

    @Transactional
    public void deleteCommunity(Long communityId, String leaderUsername) {
        Community community = domainLookupService.getCommunityOrThrow(communityId);
        MyUser leader = domainLookupService.getUserOrThrow(leaderUsername);

        if (!community.isLeader(leader)) {
            throw new RuntimeException("Only the leader can delete the community.");
        }

        communityRepository.delete(community);
    }

    // Helper methods
    public List<Community> getCommunitiesForUser(String username) {
        MyUser user = domainLookupService.getUserOrThrow(username);
        return communityRepository.findAll().stream()
                .filter(c -> c.hasMember(user))
                .toList();
    }
}
