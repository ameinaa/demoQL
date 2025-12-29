-- ============================================
-- CATEGORIES (20 catégories)
-- ============================================

INSERT INTO category (category_name, parent_category_id)
SELECT
  CONCAT('Category ', X),
  NULL
FROM SYSTEM_RANGE(1, 20);

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
