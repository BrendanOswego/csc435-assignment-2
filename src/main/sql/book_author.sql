CREATE TABLE book_author (
  book_id VARCHAR(36),
  author_id VARCHAR(36),
  FOREIGN KEY (book_id) REFERENCES book(book_id) ON UPDATE CASCADE,
  FOREIGN KEY (author_id) REFERENCES author(author_id) ON UPDATE CASCADE
);