CREATE TABLE book_author (
  ba_id VARCHAR(36) NOT NULL,
  book_id VARCHAR(36),
  author_id VARCHAR(36),
  PRIMARY KEY (ba_id),
  FOREIGN KEY (book_id) REFERENCES book(book_id),
  FOREIGN KEY (author_id) REFERENCES author(author_id)
);