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
        'bi-shield-fill',
        'ADMIN'
    ),
    (
        true,
        '2026-06-10 12:59:47.577853',
        '2026-06-10 12:59:47.577853',
        'bi-hammer',
        'CRAFTER'
    ),
    (
        true,
        '2026-06-10 12:59:55.790559',
        '2026-06-10 12:59:55.790559',
        'bi-flower1',
        'GARDENER'
    )
ON CONFLICT (role_name) DO NOTHING;

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
    ),
    (
        true,
        '2026-06-10 15:28:33.411779',
        2,
        '2026-06-10 15:28:33.411779',
        'pivote1@pivote.com',
        '$2a$10$7/McaFr/NG7L6NrQ4Roxy.svuV1nrSQ9f90Bqov.uBclOd5wOht8S',
        'pivote1'
    )
ON CONFLICT (username) DO NOTHING;