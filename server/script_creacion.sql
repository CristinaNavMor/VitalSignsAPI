CREATE TABLE User ( 
	username VARCHAR(50), 
	email VARCHAR(100) NOT NULL UNIQUE, 
    name VARCHAR(50) NOT NULL,
    first_surname VARCHAR(50) NOT NULL,	
    second_surname VARCHAR(50) DEFAULT '',
	role VARCHAR(20) NOT NULL, 
    title VARCHAR(10) NOT NULL,
	password VARCHAR(255) NOT NULL, 
	password_changed BOOLEAN DEFAULT FALSE, 
    CONSTRAINT CHK_title CHECK (title IN ('Dr.', 'Dra.', 'Admin.', 'Enf.')),
	CONSTRAINT CHK_role CHECK (role IN ('ADMIN', 'NURSE', 'DOCTOR')), 
	CONSTRAINT PK_username PRIMARY KEY (username) 
); 

CREATE TABLE Patient (
    medical_record_number INT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    CONSTRAINT CHK_gender CHECK (gender IN ('MALE', 'FEMALE', 'NON_BINARY')),
    CONSTRAINT PK_Patient PRIMARY KEY (medical_record_number)
);

CREATE TABLE Vital_sign (
    name VARCHAR(20),
    min_value DECIMAL(5,2) NOT NULL,
    max_value DECIMAL(5,2) NOT NULL,
    CONSTRAINT PK_vitalSign PRIMARY KEY (name)
);

CREATE TABLE Record (
    patient_recordNumber INT,
    username VARCHAR(50),
    vitalSign VARCHAR(20),
    dateTime TIMESTAMP,
    value DECIMAL(6,2) NOT NULL,
    alert BOOLEAN DEFAULT FALSE,
    CONSTRAINT PK_Record PRIMARY KEY (patient_recordNumber, username, vitalSign, dateTime),
    CONSTRAINT FK_PatientRecordNumber FOREIGN KEY (patient_recordNumber) REFERENCES Patient(medical_record_number) ON DELETE CASCADE,
    CONSTRAINT FK_username FOREIGN KEY (username) REFERENCES User(username) ON DELETE CASCADE,
    CONSTRAINT FK_vitalSign FOREIGN KEY (vitalSign) REFERENCES Vital_sign(name) ON DELETE CASCADE
);
