package com.foodbuddy.food_buddy_api.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(optional = false)
    private MyUser leader;

    @ManyToMany
    private Set<MyUser> members = new HashSet<>();

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingListItem> items;

    public void addMember(MyUser user) {
        members.add(user);
    }

    public void removeMember(MyUser user) {
        members.remove(user);
    }

    public boolean isLeader(MyUser user) {
        return leader != null && leader.equals(user);
    }

    public boolean hasMember(MyUser user) {
        return members.contains(user) || isLeader(user);
    }
}
