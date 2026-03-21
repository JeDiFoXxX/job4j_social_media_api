package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import ru.job4j.model.Friendship;
import ru.job4j.model.Subscription;
import ru.job4j.model.User;
import ru.job4j.repository.FriendshipRepository;
import ru.job4j.repository.SubscriptionRepository;

@Service
@AllArgsConstructor
public class SubscribeService {
    private final SubscriptionRepository subscriptionRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional(rollbackFor = {Exception.class})
    public void subscribe(User follower, User followed) {
        subscriptionRepository.save(new Subscription(follower, followed));
        friendshipRepository.save(new Friendship(follower, followed));
        var friendship = getPair(follower, followed);
        if (friendship.size() == 2) {
            friendship.get(0).setAccepted(true);
            friendship.get(1).setAccepted(true);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public int unsubscribe(User follower, User followed) {
        var rsl = subscriptionRepository
                .deleteByFollowerIdAndFollowedId(follower.getId(), followed.getId());
        friendshipRepository
                .deleteByUserIdAndFriendId(follower.getId(), followed.getId());
        var friendship = getPair(follower, followed);
        if (friendship.size() == 1) {
            friendship.get(0).setAccepted(false);
        }
        return rsl;
    }

    private List<Friendship> getPair(User follower, User followed) {
        return friendshipRepository.findPair(follower.getId(), followed.getId());
    }
}
