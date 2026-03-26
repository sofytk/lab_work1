-- Создаем расширение dblink для связи с другими бд
CREATE EXTENSION IF NOT EXISTS dblink;

INSERT INTO person (full_name, age)
SELECT DISTINCT full_name, age
FROM (
         -- из projects_db
         SELECT INITCAP(LOWER(full_name)) AS full_name, age
         FROM dblink(
                      'dbname=projects_db user=postgres password=postgres host=localhost',
                      'SELECT full_name, age FROM person'
              ) AS p(full_name TEXT, age SMALLINT)

         UNION

         -- из resume_db
         SELECT INITCAP(LOWER(full_name)) AS full_name, age
         FROM dblink(
                      'dbname=resume_db user=postgres password=postgres host=localhost',
                      'SELECT full_name, age FROM person'
              ) AS p(full_name TEXT, age SMALLINT)) t;


INSERT INTO project (project_name, description, tech_stack)
SELECT DISTINCT project_name, description, tech_stack
FROM dblink(
             'dbname=projects_db user=postgres password=postgres host=localhost',
             'SELECT project_name, description, tech_stack FROM project'
     ) AS pr(project_name TEXT, description TEXT, tech_stack TEXT[]);


INSERT INTO project_member_information (person_id,
                                        project_id,
                                        role,
                                        salary,
                                        bonus,
                                        skills)
SELECT ph.id,
       pr.id,
       pm.role_name,
       s.salary,
       s.bonus,
       r.skills
FROM
    -- project_member из projects_db
    dblink(
            'dbname=projects_db user=postgres password=postgres host=localhost',
            'SELECT pm.person_id, pm.project_id, pm.role_name, p.full_name
            FROM project_member pm
                JOIN person p ON p.id = pm.person_id'
    ) AS pm(person_id INT, project_id INT, role_name TEXT, full_name TEXT)

-- сопоставляем с person в ХД
        JOIN person ph
             ON ph.full_name = INITCAP(LOWER(pm.full_name))

-- подтягиваем проекты
        JOIN project pr
             ON pr.project_name = (SELECT project_name
                                   FROM dblink(
                                                'dbname=projects_db user=postgres password=postgres host=localhost',
                                                'SELECT id, project_name FROM project'
                                        ) AS proj(id INT, project_name TEXT)
                                   WHERE id = pm.project_id)

-- подтягиваем данные из resume_db
        LEFT JOIN dblink(
            'dbname=resume_db user=postgres password=postgres host=localhost',
            'SELECT p.full_name, s.salary, s.bonus, r.skills
            FROM person p
                LEFT JOIN salary s ON s.person_id = p.id
                LEFT JOIN resume r ON r.person_id = p.id'
                  ) AS s(full_name TEXT, salary INT, bonus INT, skills TEXT[])
                  ON INITCAP(LOWER(s.full_name)) = ph.full_name

        LEFT JOIN dblink(
            'dbname=resume_db user=postgres password=postgres host=localhost',
            'SELECT p.full_name, r.skills
            FROM person p
                LEFT JOIN resume r ON r.person_id = p.id'
                  ) AS r(full_name TEXT, skills TEXT[])
                  ON INITCAP(LOWER(r.full_name)) = ph.full_name;
