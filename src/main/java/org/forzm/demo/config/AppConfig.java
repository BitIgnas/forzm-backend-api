package org.forzm.demo.config;

import lombok.AllArgsConstructor;
import org.forzm.demo.model.*;
import org.forzm.demo.repository.CommentRepository;
import org.forzm.demo.repository.ForumRepository;
import org.forzm.demo.repository.PostRepository;
import org.forzm.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.transaction.Transactional;
import java.time.Instant;

@Configuration
@AllArgsConstructor
@EnableScheduling
@Transactional
public class AppConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("ignas@gmg.com");
        user.setProfileImageUrl("user-stock.png");
        user.setEnabled(true);
        user.setDateCreated(Instant.now());

        User user2 = new User();
        user2.setUsername("ignas");
        user2.setPassword(passwordEncoder.encode("ignas"));
        user2.setEmail("ignas@gmg.com");
        user2.setProfileImageUrl("user-stock.png");
        user2.setEnabled(true);
        user2.setDateCreated(Instant.now());

        userRepository.save(user);
        userRepository.save(user2);

        Forum forum1 = new Forum();
        forum1.setUser(user2);
        forum1.setName("Batman arkham knight");
        forum1.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum1.setCreated(Instant.now().plusMillis(20000000000L));
        forum1.setImageUrl("https://cdn.wallpapersafari.com/39/15/wJ9rpS.jpg");

        Forum forum2 = new Forum();
        forum2.setUser(user2);
        forum2.setName("Assassin's creed 4: Black flag");
        forum2.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum2.setCreated(Instant.now());
        forum2.setImageUrl("https://www.wallpaperbetter.com/wallpaper/772/174/462/assassins-creed-4-black-flag-game-1080P-wallpaper.jpg");

        Forum forum3 = new Forum();
        forum3.setUser(user2);
        forum3.setName("Battlefield 1");
        forum3.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum3.setCreated(Instant.now().plusSeconds(200000000));
        forum3.setImageUrl("https://images2.alphacoders.com/706/thumb-1920-706162.jpg");

        Forum forum4 = new Forum();
        forum4.setUser(user2);
        forum4.setName("cyberpunk 2077");
        forum4.setDescription("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8HyXzHB_2T1iFR46HNEQO5WVHeibWA5fKgA&usqp=CAU");
        forum4.setCreated(Instant.now().plusMillis(200000000L));
        forum4.setImageUrl("https://www.notebookcheck.net/fileadmin/Notebooks/News/_nc3/cyberpunk_2077_rtx_3070.jpg");

        Forum forum5 = new Forum();
        forum5.setUser(user2);
        forum5.setName("War thunder");
        forum5.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum5.setCreated(Instant.now());
        forum5.setImageUrl("https://hdwallpaperim.com/wp-content/uploads/2017/08/24/107039-War_Thunder-airplane-Gaijin_Entertainment-tank-Tiger_I-Focke-Wulf_Fw_190.jpg");

        Forum forum6 = new Forum();
        forum6.setUser(user2);
        forum6.setName("Counter strike : Global offensive");
        forum6.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum6.setCreated(Instant.now().plusSeconds(200000000));
        forum6.setImageUrl("https://static-01.shop.com.mm/p/786b1154e512f63e2ad96cd78f470f1e.jpg");

        Forum forum7 = new Forum();
        forum7.setUser(user2);
        forum7.setName("METAL GEAR SOLID V");
        forum7.setDescription("This is forum for batman arkham games, all users all welcome!");
        forum7.setCreated(Instant.now().plusSeconds(2000000));
        forum7.setImageUrl("https://2game.com/wp/wp-content/uploads/2019/12/Metal-Gear-Solid-V-Phantom-Pain.jpg");


        Post post1 = new Post();
        post1.setForum(forum1);
        post1.setUser(user2);
        post1.setPostType(PostType.DISCUSSION);
        post1.setTitle("Need to rank to global");
        post1.setContent("How do i rank to global");
        post1.setCreated(Instant.now());

        Post post2 = new Post();
        post2.setForum(forum1);
        post2.setUser(user2);
        post2.setPostType(PostType.DISCUSSION);
        post2.setTitle("Need to find batman");
        post2.setContent("How do i find batman in world");
        post2.setCreated(Instant.now());

        Post post3 = new Post();
        post3.setForum(forum5);
        post3.setUser(user2);
        post3.setPostType(PostType.DISCUSSION);
        post3.setTitle("T34 better than tiger 1");
        post3.setContent("HOW DO I GET TIGER 1 tank?????");
        post3.setCreated(Instant.now());

        Post post4 = new Post();
        post4.setForum(forum5);
        post4.setUser(user2);
        post4.setPostType(PostType.DISCUSSION);
        post4.setTitle("How to get tiger 5 german tank");
        post4.setContent("HOW DO I GET panther 1 tank?????");
        post4.setCreated(Instant.now());

        Post post5 = new Post();
        post5.setForum(forum5);
        post5.setUser(user2);
        post5.setPostType(PostType.DISCUSSION);
        post5.setTitle("How to get sherman  german tank");
        post5.setContent("HOW DO I GET sherman tank?????");
        post5.setCreated(Instant.now());

        Comment comment1 = new Comment();
        comment1.setUser(user2);
        comment1.setPost(post3);
        comment1.setContent("Lol no");
        comment1.setDateReplied(Instant.now().plusSeconds(200000));

        forumRepository.save(forum1);
        forumRepository.save(forum2);
        forumRepository.save(forum3);
        forumRepository.save(forum4);
        forumRepository.save(forum5);
        forumRepository.save(forum6);
        forumRepository.save(forum7);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        commentRepository.save(comment1);
        userRepository.save(user2);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
