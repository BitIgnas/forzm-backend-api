package org.forzm.demo.repository;

import org.forzm.demo.model.Forum;
import org.forzm.demo.model.ForumGameType;
import org.forzm.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ForumRepositoryTest {

    @Autowired
    private ForumRepository forumRepository;

    @Test
    void shouldSaveForum() {
        Forum forum = new Forum(null, "FORUM", "FORUM DESC", ForumGameType.FPS, null, null, null, null);
        Forum savedForum = forumRepository.save(forum);

        assertThat(savedForum).isNotNull();
        assertThat(savedForum).isInstanceOf(Forum.class);
        assertThat(savedForum).usingRecursiveComparison().ignoringFields("id").isEqualTo(forum);
    }

    @Test
    @Sql("classpath:forum-test-data.sql")
    void getForumByName() {
        Optional<Forum> forumOptional = forumRepository.getForumByName("FORUM");

        assertThat(forumOptional).isNotEmpty();
        assertThat(forumOptional.get()).isInstanceOf(Forum.class);
        assertThat(forumOptional.get().getName()).isEqualTo("FORUM");
        assertThat(forumOptional.get().getDescription()).isEqualTo("FORUM DESC");
        assertThat(forumOptional.get().getForumGameType()).isEqualTo(ForumGameType.FPS);
    }

    @Test
    @Sql("classpath:forum-test-data.sql")
    void findForumsByUser_Username() {
        User user = new User(1L, "test", "test@gmail.com", "test", true, null, null);
        List<Forum> forumsByUserUsername = forumRepository.findForumsByUser_Username(user.getUsername());

        assertThat(forumsByUserUsername.get(0).getName()).isEqualTo("FORUM");
        assertThat(forumsByUserUsername.get(0).getDescription()).isEqualTo("FORUM DESC");
        assertThat(forumsByUserUsername.get(0).getForumGameType()).isEqualTo(ForumGameType.FPS);
        assertThat(forumsByUserUsername.get(0).getUser()).isEqualTo(user);
    }

    @Test
    @Sql("classpath:forum-test-data.sql")
    void getAllByOrderByPostsAsc() {
        List<Forum> forumsByUserUsername = forumRepository.getAllByOrderByPostsAsc();

        assertThat(forumsByUserUsername.get(0).getName()).isEqualTo("FORUM");
        assertThat(forumsByUserUsername.get(0).getDescription()).isEqualTo("FORUM DESC");
        assertThat(forumsByUserUsername.get(0).getForumGameType()).isEqualTo(ForumGameType.FPS);
    }

    @Test
    @Sql("classpath:forum-test-data.sql")
    void getAllByNameContainingIgnoreCase() {
        List<Forum> forumsByUserUsername = forumRepository.getAllByNameContainingIgnoreCase("rum");

        assertThat(forumsByUserUsername.get(0).getName()).isEqualTo("FORUM");
        assertThat(forumsByUserUsername.get(0).getDescription()).isEqualTo("FORUM DESC");
        assertThat(forumsByUserUsername.get(0).getForumGameType()).isEqualTo(ForumGameType.FPS);
    }

    @Test
    @Sql("classpath:forum-test-data.sql")
    void deleteByName() {
        forumRepository.deleteByName("FORUM");
        List<Forum> allForums = forumRepository.findAll();

        assertThat(allForums.size()).isEqualTo(0);
    }

    @Test
    @Sql("classpath:forum-test-data.sql")
    void countAllByUserUsername() {
        Long forumCount = forumRepository.countAllByUserUsername("test");

        assertThat(forumCount).isNotNull();
        assertThat(forumCount).isEqualTo(1);
    }
}