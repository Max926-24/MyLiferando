-- Связь курьера с его учётной записью: по ней узнаём, какой пользователь — какой курьер
ALTER TABLE courier
    ADD COLUMN user_id BIGINT, -- пусто у курьеров, которым пропуск ещё не выдан
    ADD CONSTRAINT fk_courier_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT uq_courier_user UNIQUE (user_id); -- один пользователь — один курьер
