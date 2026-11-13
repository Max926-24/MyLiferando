-- Первый диспетчер: без него на пустой базе некому заводить остальных сотрудников
-- (дверь POST /users открывает только диспетчер).
-- Учебные данные для локальной базы: логин admin, пароль admin12345678.
-- На настоящем сервере этот пароль нужно сменить сразу после первого входа.
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$iqDBYA0KsvYn0KYvknukA.XDKlZ7rzAUG73tmrYqxe21UA/ZicHfe', 'DISPATCHER')
ON CONFLICT (username) DO NOTHING; -- если такой логин уже есть, ничего не трогаем
