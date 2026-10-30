DELETE FROM reservations;
DELETE FROM rooms;

INSERT INTO rooms (id, name, capacity) VALUES
                                           ('11111111-1111-1111-1111-111111111111', 'Salle Atlas', 8),
                                           ('22222222-2222-2222-2222-222222222222', 'Salle Yasmine', 4),
                                           ('33333333-3333-3333-3333-333333333333', 'Salle Cèdre', 12);