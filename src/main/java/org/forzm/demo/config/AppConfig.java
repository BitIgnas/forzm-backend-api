package org.forzm.demo.config;

import lombok.AllArgsConstructor;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.Post;
import org.forzm.demo.model.PostType;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.PostRepository;
import org.forzm.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.transaction.Transactional;
import java.time.Instant;

@Configuration
@AllArgsConstructor
public class AppConfig implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("igna@gmg.com");
        user.setProfileImageUrl("user-stock.png");
        user.setEnabled(true);
        user.setDateCreated(Instant.now());

        userRepository.save(user);

        Forum forum1 = new Forum();
        forum1.setUser(user);
        forum1.setName("Batman arkham knight");
        forum1.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum1.setCreated(Instant.now().plusMillis(20000000000L));
        forum1.setImageUrl("https://cdn.wallpapersafari.com/39/15/wJ9rpS.jpg");

        Forum forum2 = new Forum();
        forum2.setUser(user);
        forum2.setName("Assassin's creed 4: Black flag");
        forum2.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum2.setCreated(Instant.now());
        forum2.setImageUrl("https://www.wallpaperbetter.com/wallpaper/772/174/462/assassins-creed-4-black-flag-game-1080P-wallpaper.jpg");

        Forum forum3 = new Forum();
        forum3.setUser(user);
        forum3.setName("Battlefield 1");
        forum3.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum3.setCreated(Instant.now().plusSeconds(200000000));
        forum3.setImageUrl("https://images2.alphacoders.com/706/thumb-1920-706162.jpg");

        Forum forum4 = new Forum();
        forum4.setUser(user);
        forum4.setName("cyberpunk 2077");
        forum4.setDescription("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8HyXzHB_2T1iFR46HNEQO5WVHeibWA5fKgA&usqp=CAU");
        forum4.setCreated(Instant.now().plusMillis(200000000L));
        forum4.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8HyXzHB_2T1iFR46HNEQO5WVHeibWA5fKgA&usqp=CAU");

        Forum forum5 = new Forum();
        forum5.setUser(user);
        forum5.setName("War thunder");
        forum5.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum5.setCreated(Instant.now());
        forum5.setImageUrl("https://static.warthunder.com/upload/image/wallpapers/1920x1080_logo_red_skies_cn_spaag_com_22ffd923591ff6c44b838185c64e127c.jpg");

        Forum forum6 = new Forum();
        forum6.setUser(user);
        forum6.setName("Counter strike : Global offensive");
        forum6.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum6.setCreated(Instant.now().plusSeconds(200000000));
        forum6.setImageUrl("https://static-01.shop.com.mm/p/786b1154e512f63e2ad96cd78f470f1e.jpg");

        Post post1 = new Post();
        post1.setForum(forum1);
        post1.setUser(user);
        post1.setPostType(PostType.DISCUSSION);
        post1.setTitle("Need to rank to global");
        post1.setContent("How do i rank to global");
        post1.setCreated(Instant.now());

        Post post2 = new Post();
        post2.setForum(forum1);
        post2.setUser(user);
        post2.setPostType(PostType.DISCUSSION);
        post2.setTitle("Need to find batman");
        post2.setContent("How do i find batman in world");
        post2.setCreated(Instant.now());

        forumRepository.save(forum1);
        forumRepository.save(forum2);
        forumRepository.save(forum3);
        forumRepository.save(forum4);
        forumRepository.save(forum5);
        forumRepository.save(forum6);
        postRepository.save(post1);
        postRepository.save(post2);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
