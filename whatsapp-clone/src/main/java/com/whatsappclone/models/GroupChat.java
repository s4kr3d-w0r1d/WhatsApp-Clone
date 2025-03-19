package com.whatsappclone.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "groups")
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Optionally, map the group members (bidirectional mapping)
    @OneToMany(mappedBy = "groupChat")
    @JsonManagedReference  // This side will be serialized
    private List<GroupMember> members;

    @Column
    private String profilePictureUrl;  // Stores URL or Base64 string

    // Convenience method to add member
    public void addMember(GroupMember member) {
        members.add(member);
        member.setGroupChat(this);
    }

    // Convenience method to remove member
    public void removeMember(GroupMember member) {
        members.remove(member);
        member.setGroupChat(null);
    }


}
