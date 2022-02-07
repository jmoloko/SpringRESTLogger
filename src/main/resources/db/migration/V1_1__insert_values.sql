
INSERT INTO users
VALUES
       (DEFAULT, 'johndoe@yahoo.com', 'John Doe', '$2a$12$2b75/kZRtObHp4M88ZFEQe.FMM8P9r.aK6yl/OSI0AzA9xo.ucBca', 'ADMIN', 'ACTIVE'),
       (DEFAULT, 'mike@mail.ru', 'Mike Snow', '$2a$12$2b75/kZRtObHp4M88ZFEQe.FMM8P9r.aK6yl/OSI0AzA9xo.ucBca', 'MODERATOR', 'ACTIVE'),
       (DEFAULT, 'user@gmail.com', 'Test User', '$2a$12$qmyqcf.oco6EFN/C74Dxne.Xo8fcKPEKyT0WUV3sYXTO8aiYwSlyS', 'USER', 'ACTIVE');

INSERT INTO files
VALUES
       (DEFAULT, 'test.txt', '/home/milk/Code/JAVA/Suleymanov/Schooling/SpringRESTLogger/src/main/resources/uploads/JohnDoe'),
       (DEFAULT, 'some.txt', '/home/milk/Code/JAVA/Suleymanov/Schooling/SpringRESTLogger/src/main/resources/uploads/MikeSnow'),
       (DEFAULT, 'file.txt', '/home/milk/Code/JAVA/Suleymanov/Schooling/SpringRESTLogger/src/main/resources/uploads/TestUser');

INSERT INTO events
VALUES
       (DEFAULT, 1, 1, DEFAULT, 'UPLOAD'),
       (DEFAULT, 2, 2, DEFAULT, 'DOWNLOAD'),
       (DEFAULT, 3, 3, DEFAULT, 'RENAME');