package org.forzm.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "forum")
public class Forum {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Lob
    private String description;
    @Enumerated(EnumType.STRING)
    private ForumGameType forumGameType;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "forum", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Post> posts;
    private Instant created;
}
