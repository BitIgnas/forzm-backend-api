package org.forzm.demo.repository;

import org.forzm.demo.model.Forum;
import org.forzm.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {
    Optional<Forum> getForumByName(String name);
    List<Forum> getAllByOrderByPostsDesc();
}
