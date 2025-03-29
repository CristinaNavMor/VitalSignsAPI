INSERT INTO User (username, email, name, first_surname, second_surname, role, title, password) VALUES
('admin01', 'admin1@gmail.com', 'Juan', 'Pérez', 'Gómez', 'ADMIN', 'Admin.', '$2a$10$27gcqKCJMUWSqzMoaRXiwO8e8tKBDEXeamBD2pIvoplC4e5BlBxr.'),
('doctor01', 'doctor1@example.com', 'Laura', 'Fernández', 'López', 'DOCTOR', 'Dra.', '$2a$10$7A5CEZAN0StNgh/bS7awZu.6eoahargyxtyAlwww9mGKcQ7e5hmjG'),
('nurse01', 'nurse1@example.com', 'Carlos', 'Ramírez', 'Díaz', 'NURSE', 'Enf.', '$2a$10$3KOdGpz8vyhLQSK2er2ZRed.LQvH5L8LcIZ7nhQzkGV0wjlkUfjUe'),
('doctor02', 'doctor2@example.com', 'Ana', 'García', 'Martínez', 'DOCTOR', 'Dra.', '$2a$10$aZpbzEruyZR7xZQYrpgJeeTPkgyCbdLBL9FY.rAKImM7tyb1NA1j.');

/*admin1@gmail.com password : "Admin123?" */
/*doctor1@example.com password : "Doctor123?" */
/*doctor2@example.com password : "Doctor456?" */
/*nurse1@gmail.com password : "Nurse123?" */

INSERT INTO Patient (name, surname, birthday, gender) VALUES
('Miguel', 'Torres', '1985-04-12', 'MALE'),
('Sofía', 'Hernández', '1992-07-08', 'FEMALE'),
('Alex', 'López', '2000-11-23', 'NON_BINARY');

INSERT INTO Vital_sign (name, min_value, max_value) VALUES
('HEART_RATE', 50.00, 100.00),  
('BLOOD_PRESSURE', 90.00, 140.00),  
('BODY_TEMPERATURE', 36.00, 38.00),  
('RESPIRATORY_RATE', 12.00, 20.00),  
('OXYGEN_SATURATION', 95.00, 100.00);

INSERT INTO Record (patient_recordNumber, username, vitalSign, dateTime, value, alert) VALUES
(1, 'doctor01', 'HEART_RATE', '2025-03-08 18:30:00', 88.00, FALSE),
(1, 'doctor01', 'HEART_RATE', '2025-03-08 08:45:00', 75.00, FALSE),
(1, 'nurse01', 'HEART_RATE', '2025-03-06 17:33:00', 88.00, FALSE),
(1, 'nurse01', 'HEART_RATE', '2025-03-06 09:45:00', 75.00, FALSE),
(1, 'doctor02', 'HEART_RATE', '2025-03-01 10:15:00', 70.00, FALSE),
(1, 'doctor02', 'HEART_RATE', '2025-02-28 10:00:00', 45.00, TRUE),
(1, 'nurse01', 'HEART_RATE', '2025-03-06 18:45:00', 88.00, FALSE),
(1, 'nurse01', 'HEART_RATE', '2025-03-06 08:15:00', 75.00, FALSE),
(1, 'doctor02', 'HEART_RATE', '2025-03-01 09:15:00', 70.00, FALSE),
(1, 'doctor01', 'BLOOD_PRESSURE', '2025-03-08 18:45:00', 135.00, FALSE),
(1, 'doctor01', 'BODY_TEMPERATURE', '2025-03-08 19:00:00', 37.80, TRUE),
(1, 'nurse01', 'RESPIRATORY_RATE', '2025-03-06 08:30:00', 15.00, FALSE),
(1, 'nurse01', 'OXYGEN_SATURATION', '2025-03-06 08:45:00', 94.00, TRUE),
(1, 'doctor02', 'BLOOD_PRESSURE', '2025-03-04 09:15:00', 142.00, TRUE),
(1, 'doctor02', 'HEART_RATE', '2025-03-01 19:15:00', 70.00, FALSE),
(1, 'doctor02', 'BODY_TEMPERATURE', '2025-02-28 10:00:00', 45.00, TRUE),
(1, 'nurse01', 'OXYGEN_SATURATION', '2025-02-26 14:00:00', 97.00, FALSE),
(2, 'doctor01', 'BLOOD_PRESSURE', '2025-03-08 18:30:00', 120.00, FALSE),
(2, 'doctor01', 'BODY_TEMPERATURE', '2025-03-08 18:45:00', 36.50, FALSE),
(2, 'nurse01', 'RESPIRATORY_RATE', '2025-03-08 19:00:00', 18.00, FALSE),
(2, 'doctor02', 'OXYGEN_SATURATION', '2025-03-06 08:30:00', 96.00, FALSE),
(2, 'doctor02', 'BODY_TEMPERATURE', '2025-03-05 09:15:00', 37.20, FALSE),
(2, 'nurse01', 'BLOOD_PRESSURE', '2025-03-04 10:00:00', 90.00, TRUE),
(2, 'doctor01', 'RESPIRATORY_RATE', '2025-02-28 11:00:00', 14.00, FALSE),
(2, 'nurse01', 'BLOOD_PRESSURE', '2025-02-25 12:00:00', 100.00, FALSE),
(2, 'doctor02', 'OXYGEN_SATURATION', '2025-02-20 14:00:00', 95.00, FALSE),
(3, 'nurse01', 'HEART_RATE', '2025-03-08 18:30:00', 65.00, FALSE),
(3, 'doctor01', 'HEART_RATE', '2025-03-08 18:45:00', 80.00, FALSE),
(3, 'doctor02', 'HEART_RATE', '2025-03-08 19:00:00', 49.00, TRUE),
(3, 'doctor01', 'HEART_RATE', '2025-03-06 08:30:00', 55.00, FALSE),
(3, 'doctor02', 'HEART_RATE', '2025-03-05 09:15:00', 102.00, TRUE),
(3, 'nurse01', 'HEART_RATE', '2025-03-04 10:00:00', 70.00, FALSE),
(3, 'doctor01', 'HEART_RATE', '2025-02-28 11:00:00', 90.00, FALSE),
(3, 'doctor02', 'HEART_RATE', '2025-02-25 12:00:00', 95.00, FALSE),
(3, 'nurse01', 'HEART_RATE', '2025-02-20 14:00:00', 45.00, TRUE);
