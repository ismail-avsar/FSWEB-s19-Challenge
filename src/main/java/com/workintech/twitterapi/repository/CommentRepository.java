package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTweetIdOrderByCreatedAtAsc(Long tweetId);

    long countByTweetId(Long tweetId);

    void deleteByTweetId(Long tweetId);
}
