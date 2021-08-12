package org.forzm.demo.repository;

import org.forzm.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);
    Optional<List<Post>> findAllByForumName(String forumName);
    Long countAllByForumName(String forumName);
}
