CREATE DATABASE qlap;
USE qlap;

CREATE TABLE Household (
    HouseholdID INT PRIMARY KEY,
    OwnerName VARCHAR(100),
    Address VARCHAR(255),
    MembersCount INT
);

CREATE TABLE Fee (
    FeeID INT PRIMARY KEY,
    FeeName VARCHAR(100),
    Amount DECIMAL(10,2),
    IsMandatory BOOLEAN
);

CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY,
    HouseholdID INT,
    FeeID INT,
    AmountPaid DECIMAL(10,2),
    PaymentDate DATE,
    FOREIGN KEY (HouseholdID) REFERENCES Household(HouseholdID),
    FOREIGN KEY (FeeID) REFERENCES Fee(FeeID)
);

CREATE TABLE Report (
    ReportID INT PRIMARY KEY,
    TotalAmountCollected DECIMAL(10,2),
    TotalHouseholdsPaid INT,
    ReportDate DATE
);

INSERT INTO Household (HouseholdID, OwnerName, Address, MembersCount)
VALUES (1, 'Nguyen Van A', 'Floor 1, Apartment 101', 4),
       (2, 'Tran Thi B', 'Floor 2, Apartment 202', 3),
       (3, 'Le Van C', 'Floor 3, Apartment 303', 5);

INSERT INTO Fee (FeeID, FeeName, Amount, IsMandatory)
VALUES (1, 'Electricity Fee', 100000, TRUE),
       (2, 'Water Fee', 50000, TRUE),
       (3, 'Maintenance Fee', 20000, FALSE);

INSERT INTO Payment (PaymentID, HouseholdID, FeeID, AmountPaid, PaymentDate)
VALUES (1, 1, 1, 100000, '2024-09-01'),
       (2, 1, 2, 50000, '2024-09-02'),
       (3, 2, 1, 100000, '2024-09-03'),
       (4, 2, 3, 20000, '2024-09-04'),
       (5, 3, 2, 50000, '2024-09-05'),
       (6, 3, 3, 100000, '2024-09-06'),
       (7, 2, 2, 20000, '2024-09-07'),
       (8, 1, 3, 20000, '2024-09-08');

INSERT INTO Report (ReportID, TotalAmountCollected, TotalHouseholdsPaid, ReportDate)
VALUES (1, 250000, 2, '2024-09-01'),
       (2, 70000, 3, '2024-09-02');
