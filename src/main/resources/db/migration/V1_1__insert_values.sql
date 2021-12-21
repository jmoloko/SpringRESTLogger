
INSERT INTO users
VALUES
       (DEFAULT, 'John Doe', 'ADMIN'),
       (DEFAULT, 'Mike Snow', 'MODERATOR'),
       (DEFAULT, 'Test User', 'USER');

INSERT INTO files
VALUES
       (DEFAULT, 'image.jpg', '/home/milk/Code/JAVA/Suleymanov/Schooling/SpringRESTLogger/src/main/resources/uploads'),
       (DEFAULT, 'photo.png', '/home/milk/Code/JAVA/Suleymanov/Schooling/SpringRESTLogger/src/main/resources/uploads'),
       (DEFAULT, 'textfile.txt', '/home/milk/Code/JAVA/Suleymanov/Schooling/SpringRESTLogger/src/main/resources/uploads');

INSERT INTO events
VALUES
       (DEFAULT, 1, 1, DEFAULT, 'UPLOAD'),
       (DEFAULT, 2, 2, DEFAULT, 'DOWNLOAD'),
       (DEFAULT, 3, 3, DEFAULT, 'RENAME');