INSERT INTO "user" (user_id, username, password, role, register_at, status, email, first_name, last_name, date_of_birth, avatar_url, bio, gender, private_profile)
VALUES
    ('U1XXXXXXXXXXXXXXXXXXXXXXXX', 'admin', 'Admin123!@#', 'ADMIN', '2024-09-01 10:00:00+00', 'ACTIVE', 'john@example.com', 'John', 'Doe', '1990-05-15', 'https://example.com/avatar1.png', 'Music lover and software engineer', 'MALE', false),
    ('U2XXXXXXXXXXXXXXXXXXXXXXXX', 'jane_doe', 'password456', 'USER', '2024-09-02 11:00:00+00', 'ACTIVE', 'jane@example.com', 'Jane', 'Doe', '1995-08-25', 'https://example.com/avatar2.png', 'Artist and tech enthusiast', 'FEMALE', true),
    ('U3XXXXXXXXXXXXXXXXXXXXXXXX', 'alex_smith', 'password789', 'USER', '2024-09-03 12:00:00+00', 'ACTIVE', 'alex@example.com', 'Alex', 'Smith', '1988-12-10', 'https://example.com/avatar3.png', 'Loves traveling and technology', 'MALE', false);
INSERT INTO room (room_id, name, description, created_at)
VALUES
    ('R1XXXXXXXXXXXXXXXXXXXXXXXX', 'Music Room', 'A room for music enthusiasts to share and discuss', '2024-09-01 09:00:00+00'),
    ('R2XXXXXXXXXXXXXXXXXXXXXXXX', 'Art Room', 'A place for artists to share their work and collaborate', '2024-09-02 10:00:00+00'),
    ('R3XXXXXXXXXXXXXXXXXXXXXXXX', 'Tech Talk', 'A discussion room for tech-related topics', '2024-09-03 11:00:00+00'),
    ('R4XXXXXXXXXXXXXXXXXXXXXXXX', 'Travel Room', 'Share travel experiences and recommendations', '2024-09-04 12:00:00+00'),
    ('R5XXXXXXXXXXXXXXXXXXXXXXXX', 'Cooking Club', 'A room for cooking enthusiasts to share recipes', '2024-09-05 13:00:00+00');
INSERT INTO user_room (user_room_id, user_id, room_id, join_at)
VALUES
    ('UR1XXXXXXXXXXXXXXXXXXXXXXX', 'U1XXXXXXXXXXXXXXXXXXXXXXXX', 'R1XXXXXXXXXXXXXXXXXXXXXXXX', '2024-09-01 10:30:00+00'),
    ('UR2XXXXXXXXXXXXXXXXXXXXXXX', 'U1XXXXXXXXXXXXXXXXXXXXXXXX', 'R2XXXXXXXXXXXXXXXXXXXXXXXX', '2024-09-01 11:00:00+00'),
    ('UR3XXXXXXXXXXXXXXXXXXXXXXX', 'U1XXXXXXXXXXXXXXXXXXXXXXXX', 'R2XXXXXXXXXXXXXXXXXXXXXXXX', '2024-09-02 12:00:00+00'),
    ('UR4XXXXXXXXXXXXXXXXXXXXXXX', 'U1XXXXXXXXXXXXXXXXXXXXXXXX', 'R3XXXXXXXXXXXXXXXXXXXXXXXX', '2024-09-02 12:30:00+00'),
    ('UR5XXXXXXXXXXXXXXXXXXXXXXX', 'U1XXXXXXXXXXXXXXXXXXXXXXXX', 'R4XXXXXXXXXXXXXXXXXXXXXXXX', '2024-09-03 13:00:00+00'),
    ('UR6XXXXXXXXXXXXXXXXXXXXXXX', 'U1XXXXXXXXXXXXXXXXXXXXXXXX', 'R5XXXXXXXXXXXXXXXXXXXXXXXX', '2024-09-03 13:30:00+00');