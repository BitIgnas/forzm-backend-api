package org.forzm.demo.repository;

import org.forzm.demo.model.Forum;
import org.forzm.demo.model.Post;
import org.forzm.demo.model.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostByTitleAndId(String title, Long id);
    List<Post> findAllByUserUsername(String username);
    List<Post> findAllByForum(Forum forum);
    List<Post> findTop5ByUserUsernameOrderByCreatedDesc(String username);
    List<Post> findAllByForumNameAndPostType(String forumName, PostType postType);
    void deleteAllByForum(Forum forum);
    Long countAllByForumName(String forumName);
    Long countAllByUserUsername(String username);
}
