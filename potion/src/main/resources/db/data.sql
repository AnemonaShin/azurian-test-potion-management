INSERT INTO
    public.roles (
        active,
        created_at,
        updated_at,
        role_icon,
        role_name
    )
VALUES (
        true,
        '2026-06-10 12:59:38.999608',
        '2026-06-10 12:59:38.999608',
        'A',
        'ADMIN'
    ),
    (
        true,
        '2026-06-10 12:59:47.577853',
        '2026-06-10 12:59:47.577853',
        'B',
        'CRAFTER'
    ),
    (
        true,
        '2026-06-10 12:59:55.790559',
        '2026-06-10 12:59:55.790559',
        'C',
        'GARDENER'
    );

INSERT INTO
    public.users (
        active,
        created_at,
        role_id,
        updated_at,
        email,
        "password",
        username
    )
VALUES (
        true,
        '2026-06-10 13:00:21.942308',
        1,
        '2026-06-10 13:00:21.942308',
        'admin@root.com',
        '$2a$10$yqiJ8UzdOHMHYVrOAKIBXutDDkKcrHpZ9y4vEvKl8lQMVAoFtDp4q',
        'admin'
    );