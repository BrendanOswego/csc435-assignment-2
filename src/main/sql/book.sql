CREATE TABLE book (
  book_id VARCHAR(36),
  title VARCHAR(50) NOT NULL,
  genre VARCHAR(30),
  year_published INT,
  pages INT,
  PRIMARY KEY (book_id)
);