CREATE TABLE IF NOT EXISTS subscriptions (
    id SERIAL PRIMARY KEY,
    follower_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    followed_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_sub UNIQUE (follower_id, followed_id),
    CONSTRAINT no_self_follow CHECK (follower_id <> followed_id)
);