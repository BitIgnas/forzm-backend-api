package org.forzm.demo.repository;

import org.forzm.demo.model.Comment;
import org.forzm.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}