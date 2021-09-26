package org.forzm.demo.repository;

import org.forzm.demo.model.Forum;
import org.forzm.demo.model.ForumGameType;
import org.forzm.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {
    Optional<Forum> getForumByName(String name);
    List<Forum> findForumsByUser_Username(String username);
    List<Forum> getAllByOrderByPostsAsc();
    List<Forum> getAllByNameContainingIgnoreCase(String name);
    List<Forum> findAllByNameAndForumGameType(String name, ForumGameType forumGameType);
    List<Forum> findAllByNameContainingAndForumGameType(String name, ForumGameType forumGameType);
    void deleteByName(String name);
    List<Forum> findAllByForumGameType(ForumGameType forumGameType);
    Optional<Forum> findForumsByNameAndUserUsername(String forumName, String username);
    Long countAllByUserUsername(String username);
}
