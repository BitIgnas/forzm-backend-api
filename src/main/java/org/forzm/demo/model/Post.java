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
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Lob
    private String content;
    @Lob
    private String contentMarkup;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    private String postImageUrl;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id")
    private Forum forum;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    private Instant created;
}
