package com.workintech.twitterapi.repository;

import com.workintech.twitterapi.entity.Tweet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByUserIdOrderByCreatedAtDesc(Long userId);
}
