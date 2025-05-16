package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity zur Repräsentation einer Benutzer-Community.
 *
 * Eine Community besitzt einen Namen, einen führenden Benutzer (Leader) sowie eine Menge an Mitgliedern.
 * Benutzer können gemeinsam auf Speicher und Items zugreifen.
 */
@Getter
@Setter
@Entity
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Community name cannot be empty")
    private String name;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private MyUser leader;

    @ManyToMany
    @JoinTable(
            name = "community_user",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<MyUser> members = new HashSet<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Storage> storages = new HashSet<>();

    // Helper methods
    public boolean isLeader(MyUser user) {
        return leader != null && leader.getId().equals(user.getId());
    }

    public boolean hasMember(MyUser user) {
        return members.stream().anyMatch(u -> u.getId().equals(user.getId()));
    }

    public void addMember(MyUser user) {
        members.add(user);
    }

    public void removeMember(MyUser user) {
        members.remove(user);
    }

    public void transferLeadershipTo(MyUser newLeader) {
        this.leader = newLeader;
    }
}
