-- подключаем dblink
CREATE
    EXTENSION IF NOT EXISTS dblink;

-- Берём пользователей из БД projects_db и resume_db и объединяем их в одну таблицу person

INSERT INTO person (external_id, full_name, age)
SELECT DISTINCT external_id,
                INITCAP(LOWER(full_name)),
                age
FROM (

         -- из projects_db
         SELECT external_id, full_name, age
         FROM dblink(
                      'dbname=projects_db user=postgres password=postgres host=localhost',
                      'SELECT external_id, full_name, age FROM person'
              ) AS t(external_id UUID, full_name TEXT, age SMALLINT)

         UNION

         -- из resume_db
         SELECT external_id, full_name, age
         FROM dblink(
                      'dbname=resume_db user=postgres password=postgres host=localhost',
                      'SELECT external_id, full_name, age FROM person'
              ) AS t(external_id UUID, full_name TEXT, age SMALLINT)) s;


-- Берём проекты из projects_db
INSERT INTO project (project_name, description, tech_stack)
SELECT DISTINCT project_name,
                description,
                tech_stack
FROM dblink(
             'dbname=projects_db user=postgres password=postgres host=localhost',
             'SELECT project_name, description, tech_stack FROM project'
     ) AS t(project_name TEXT, description TEXT, tech_stack TEXT[]);


-- Объединяем person, project, project_member, salary, resume

INSERT INTO project_member_information (person_id,
                                        project_id,
                                        role,
                                        salary,
                                        bonus,
                                        skills)

SELECT p_dw.id,      -- id человека в текущей БД
       pr_dw.id,     -- id проекта в текущей БД
       pm.role_name, -- роль человека в проекте
       s.salary,     -- зарплата из другой БД
       s.bonus,      -- бонус из другой БД
       r.skills      -- навыки из другой БД


-- Получаем project_member из projects_db
FROM dblink(
             'dbname=projects_db user=postgres password=postgres host=localhost',
             '
             SELECT pm.project_id,
                 pm.role_name,
                 p.external_id
             FROM project_member pm
                 JOIN person p ON p.id = pm.person_id
             '
     ) AS pm(
             project_id INT,
             role_name TEXT,
             external_id UUID
    )

         -- Ищем человека в текущей БД по external_id
         JOIN person p_dw
              ON p_dw.external_id = pm.external_id

    -- Получем project из dblink, переводим project_id из внешней БД в project_name, затем сопоставляем с локальной таблицей

         JOIN project pr_dw
              ON pr_dw.project_name = (SELECT project_name
                                       FROM dblink(
                                                    'dbname=projects_db user=postgres password=postgres host=localhost',
                                                    'SELECT id, project_name FROM project'
                                            ) AS t(id INT, project_name TEXT)
                                       WHERE id = pm.project_id)

    -- Подтягиванием salary и bonus из resume_db
         LEFT JOIN dblink(
        'dbname=resume_db user=postgres password=postgres host=localhost',
        '
        SELECT p.external_id,
            s.salary,
            s.bonus
        FROM person p
            LEFT JOIN salary s ON s.person_id = p.id
        '
                   ) AS s(
                          external_id UUID,
                          salary INT,
                          bonus INT
    )
                   ON s.external_id = pm.external_id


    -- Подтягиванием skills из resume_db
         LEFT JOIN dblink(
        'dbname=resume_db user=postgres password=postgres host=localhost',
        '
        SELECT p.external_id,
            r.skills
        FROM person p
            LEFT JOIN resume r ON r.person_id = p.id
        '
                   ) AS r(
                          external_id UUID,
                          skills TEXT[]
    )
                   ON r.external_id = pm.external_id;