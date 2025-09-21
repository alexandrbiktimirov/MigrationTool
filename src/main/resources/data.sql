INSERT INTO company (name, city, country) VALUES
                                              ('Acme Corp','Warsaw','PL'),
                                              ('Globex','Krakow','PL'),
                                              ('Initech','Gdansk','PL'),
                                              ('Umbrella','Wroclaw','PL'),
                                              ('Soylent','Poznan','PL');

INSERT INTO skill (name) VALUES
                             ('Java'), ('Python'), ('SQL'), ('Docker'), ('Kubernetes'),
                             ('HTML'), ('CSS'), ('JavaScript'), ('Spring'), ('React');

INSERT INTO person (name, city, country, company_id, nickname)
SELECT
    'Person ' || x,
    CASE MOD(x, 5)
        WHEN 0 THEN 'Warsaw'
        WHEN 1 THEN 'Krakow'
        WHEN 2 THEN 'Gdansk'
        WHEN 3 THEN 'Wroclaw'
        ELSE 'Poznan'
        END,
    'PL',
    MOD(x - 1, 5) + 1,
    'p' || x
FROM SYSTEM_RANGE(1, 50);

INSERT INTO passport (person_id, passport_no)
SELECT id, 'P' || RIGHT('000000' || CAST(id AS VARCHAR), 6)
FROM person;

INSERT INTO person_skill (person_id, skill_id)
SELECT p.id, ((p.id - 1) % 10) + 1
FROM person p;

INSERT INTO person_skill (person_id, skill_id)
SELECT p.id, ((p.id + 2) % 10) + 1
FROM person p;

INSERT INTO person (name, city, country, company_id, nickname)
SELECT
    'Contractor ' || x,
    'Remote',
    'PL',
    NULL,
    'c' || x
FROM SYSTEM_RANGE(51, 70);

INSERT INTO passport (person_id, passport_no)
SELECT id, 'P' || RIGHT('000000' || CAST(id AS VARCHAR), 6)
FROM person
WHERE name LIKE 'Contractor %';

INSERT INTO person_skill (person_id, skill_id)
SELECT p.id, ((p.id + 4) % 10) + 1
FROM person p
WHERE p.name LIKE 'Contractor %';



DROP TABLE IF EXISTS COMPANY;

DROP TABLE IF EXISTS MIGRATION_HISTORY;

DROP TABLE IF EXISTS PASSPORT;

DROP TABLE IF EXISTS PERSON;

DROP TABLE IF EXISTS PERSON_SKILL;

DROP TABLE IF EXISTS SKILL;

DROP TABLE IF EXISTS AUDIT_ENTRY;