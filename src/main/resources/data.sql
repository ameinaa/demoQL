-- ============================================
-- CATEGORIES (20 catégories)
-- ============================================


-- ============================================
-- CATEGORIES (20 catégories hiérarchisées)
-- ============================================

INSERT INTO category (category_name, parent_category_id) VALUES
('Science', NULL),                 -- 1
('Computer Science', 1),           -- 2
('AI', 2),                         -- 3
('Math', 1),                        -- 4
('Literature', NULL),               -- 5
('Novels', 5),                      -- 6
('History', NULL),                  -- 7
('Physics', 1),                     -- 8
('Chemistry', 1),                   -- 9
('Biology', 1),                     -- 10
('Programming', 2),                 -- 11
('Databases', 2),                   -- 12
('Networks', 2),                    -- 13
('Poetry', 5),                      -- 14
('Fantasy', 6),                     -- 15
('Classics', 6),                    -- 16
('Machine Learning', 3),            -- 17
('Deep Learning', 3),               -- 18
('Algorithms', 2),                  -- 19
('Software Engineering', 2);        -- 20


-- ============================================
-- AUTEURS (70 auteurs)
-- ============================================

INSERT INTO author (name, age, nationality)
SELECT
  CONCAT('Author ', X),
  25 + MOD(X, 50),
  CASE 
    WHEN MOD(X, 3) = 0 THEN 'Algerian'
    WHEN MOD(X, 3) = 1 THEN 'French'
    ELSE 'Canadian'
  END
FROM SYSTEM_RANGE(1, 70);

-- ============================================
-- LIVRES (170 livres)
-- ============================================

INSERT INTO book (title, publication_year, language, nb_pages, category_id, author_id)
SELECT
  CONCAT('Book ', X),
  1990 + MOD(X, 35),
  CASE 
    WHEN MOD(X, 3) = 0 THEN 'English'
    WHEN MOD(X, 3) = 1 THEN 'French'
    ELSE 'Arabic'
  END,
  100 + MOD(X, 400),
  CAST(FLOOR(RANDOM() * 20 + 1) AS INT),  -- FK: category_id
  CAST(FLOOR(RANDOM() * 70 + 1) AS INT)   -- FK: author_id
FROM SYSTEM_RANGE(1, 170);
