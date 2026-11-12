-- Связь заведения с его учётной записью: по ней узнаём, какой пользователь — какой клиент
ALTER TABLE client
    ADD COLUMN user_id BIGINT, -- пусто у клиентов, которым пропуск ещё не выдан
    ADD CONSTRAINT fk_client_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT uq_client_user UNIQUE (user_id); -- один пользователь — один клиент
