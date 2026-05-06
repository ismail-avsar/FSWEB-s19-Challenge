package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Retweet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    boolean existsByTweetIdAndUserId(Long tweetId, Long userId);

    Optional<Retweet> findByTweetIdAndUserId(Long tweetId, Long userId);

    long countByTweetId(Long tweetId);

    void deleteByTweetId(Long tweetId);
}
