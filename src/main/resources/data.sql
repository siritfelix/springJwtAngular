INSERT INTO
    users (first_name, last_name, email, password, role)
VALUES
    (
        'Felix',
        'Sirit',
        'siritfelix@gmail.com',
        '$2a$10$u8Mz3ZpaDjR4IrVzua6nbeZ0JXsP7z7Rj6XedE/mYSVcFQR8z8Cgu',
        'USER'
    ),
    (
        'Felix2',
        'Sirit2',
        'siritfelix2@gmail.com',
        '$2a$10$u8Mz3ZpaDjR4IrVzua6nbeZ0JXsP7z7Rj6XedE/mYSVcFQR8z8Cgu',
        'ADMIN'
    );

INSERT INTO
    tasks (
        titulo,
        completada,
        prioridad,
        descripcion,
        etiquetas,
        asignadoA,
        user_id
    )
VALUES
    (
        'Comprar alimentos',
        false,
        'baja',
        'Comprar frutas, verduras y otros alimentos esenciales.',
        'hogar,urgente',
        'Felix Sirit',
        1
    ),
    (
        'Comprar alimentos 2',
        true,
        'media',
        'Comprar frutas, verduras y otros alimentos esenciales.',
        'hogar,urgente',
        'Felix Sirit',
        1
    ),
    (
        'Comprar alimentos 3',
        false,
        'alta',
        'Comprar frutas, verduras y otros alimentos esenciales.',
        'hogar,urgente',
        'Felix Sirit',
        1
    ),
    (
        'Comprar alimentos 4',
        true,
        'baja',
        'Comprar frutas, verduras y otros alimentos esenciales.',
        'hogar,urgente',
        'Felix Sirit',
        1
    ),
    (
        'Clases de naracion 5',
        false,
        'baja',
        'Ir a clases de natacion',
        'hogar,urgente',
        'Felix Sirit',
        2
    ),
    (
        'Clases de naracion 6',
        true,
        'media',
        'CIr a clases de natacion',
        'hogar,urgente',
        'Felix Sirit',
        2
    ),
    (
        'Clases de naracion 7',
        false,
        'alta',
        'Ir a clases de natacion',
        'hogar,urgente',
        'Felix Sirit',
        2
    ),
    (
        'Clases de naracion 8',
        true,
        'baja',
        'Ir a clases de natacion',
        'hogar,urgente',
        'Felix Sirit',
        2
    )