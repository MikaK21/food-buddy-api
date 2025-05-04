package com.foodbuddy.food_buddy_api.application.service;

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

    public CommunityService(CommunityRepository communityRepository, MyUserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
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
        Community community = getCommunityOrThrow(communityId);
        MyUser leader = getUserOrThrow(leaderUsername);
        MyUser newMember = getUserOrThrow(newMemberUsername);

        if (!community.isLeader(leader)) {
            throw new RuntimeException("Only the leader can add members.");
        }

        community.addMember(newMember);
        communityRepository.save(community);
    }

    @Transactional
    public void removeMember(Long communityId, String leaderUsername, String memberUsername) {
        Community community = getCommunityOrThrow(communityId);
        MyUser leader = getUserOrThrow(leaderUsername);
        MyUser member = getUserOrThrow(memberUsername);

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
        Community community = getCommunityOrThrow(communityId);
        MyUser currentLeader = getUserOrThrow(currentLeaderUsername);
        MyUser newLeader = getUserOrThrow(newLeaderUsername);

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
        Community community = getCommunityOrThrow(communityId);
        MyUser user = getUserOrThrow(username);

        if (community.isLeader(user)) {
            throw new RuntimeException("Leader must transfer leadership before leaving.");
        }

        community.removeMember(user);
        communityRepository.save(community);
    }

    @Transactional
    public void deleteCommunity(Long communityId, String leaderUsername) {
        Community community = getCommunityOrThrow(communityId);
        MyUser leader = getUserOrThrow(leaderUsername);

        if (!community.isLeader(leader)) {
            throw new RuntimeException("Only the leader can delete the community.");
        }

        communityRepository.delete(community);
    }

    public List<Community> getCommunitiesForUser(String username) {
        MyUser user = getUserOrThrow(username);
        return communityRepository.findAll().stream()
                .filter(c -> c.hasMember(user))
                .toList();
    }

    private Community getCommunityOrThrow(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

    private MyUser getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
