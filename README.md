# java-filmorate
Template repository for Filmorate project.


Поиск любимых фильмов пользователя с определенным логином
```postgresql
SELECT f.name
FROM films AS f
INNER RJOIN likes AS l ON f.film_id = l.film_id
INNER JOIN user AS u ON l.film_id = u.film_id
WHERE u.login = 'Artem33'
```

Общее кол-во пользователей 
```postgresql
SELECT COUNT(user_id)
FROM user
```
