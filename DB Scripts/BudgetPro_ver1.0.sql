/*
DROP TABLE BP_user CASCADE CONSTRAINTS;
DROP TABLE BP_expense CASCADE CONSTRAINTS;
DROP TABLE BP_account CASCADE CONSTRAINTS;
DROP TABLE BP_income CASCADE CONSTRAINTS;
DROP TABLE BP_organization CASCADE CONSTRAINTS;
DROP TABLE BP_category CASCADE CONSTRAINTS;
DROP SEQUENCE idEntry_seq;
DROP SEQUENCE idUser_seq;
*/
CREATE TABLE BP_user (
	idUser number(5) NOT NULL,
	FirstName varchar2(10),
	lastName varchar2(10),
	Email varchar2(25),
	CONSTRAINT user_id_pk PRIMARY KEY(idUser));
    
CREATE TABLE BP_account (
	idAccount number(2) NOT NULL,
	idUser number(5) NOT NULL,
    accountName varchar2(20),
    balance number(12,2),
    totalAsset number(12,2),
    totalLiabilities number(12,2),
    CONSTRAINT account_user_fk FOREIGN KEY (idUser)
                           REFERENCES BP_user (idUser),
	CONSTRAINT account_id_pk PRIMARY KEY(idAccount));

CREATE TABLE BP_organization (
	idOrg number(5) NOT NULL,
    orgName varchar2(20),
    contactPerson varchar2(20),
    Email varchar2(25),
    categoryType number(1),
	CONSTRAINT org_id_pk PRIMARY KEY(idOrg));

CREATE TABLE BP_expense (
	idEntry number(5) NOT NULL,
	idOrg number(5),
    idAccount number(2),
    price number(12,2),
    paymentDate DATE,
    categoryName varchar2(10),
    usernotes varchar2(20),
    CONSTRAINT expense_org_fk FOREIGN KEY (idOrg)
                           REFERENCES BP_organization (idOrg),
    CONSTRAINT expense_account_fk FOREIGN KEY (idAccount)
                           REFERENCES BP_account (idAccount),
	CONSTRAINT expense_id_pk PRIMARY KEY(idEntry));

CREATE TABLE BP_income (
	idEntry number(5) NOT NULL,
	idOrg number(5),
    idAccount number(2),
    amount number(12,2),
    paymentDate DATE,
    categoryName varchar2(10),
    usernotes varchar2(20),
    CONSTRAINT income_org_fk FOREIGN KEY (idOrg)
                           REFERENCES BP_organization (idOrg),
    CONSTRAINT income_account_fk FOREIGN KEY (idAccount)
                           REFERENCES BP_account (idAccount),
	CONSTRAINT income_id_pk PRIMARY KEY(idEntry));
    

CREATE TABLE BP_category (
	idCategory number(2) NOT NULL,
    categoryType number(1),
    categoryName varchar2(10),
	CONSTRAINT cat_id_pk PRIMARY KEY(idCategory));

CREATE SEQUENCE idEntry_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE idUser_seq START WITH 1 INCREMENT BY 1;

-- BP_user (idUser, FirstName, lastName, Email) 
INSERT INTO BP_user
    VALUES (1, 'John', 'Doe', 'john.doe@example.com');
    
-- BP_category (idCategory, categoryType, categoryName)
INSERT INTO BP_category
    VALUES (1, 2, 'SALARY');
INSERT INTO BP_category
    VALUES (2, 1, 'FOOD');
INSERT INTO BP_category
    VALUES (3, 1, 'DRINK');
    
-- BP_account (idAccount, idUser, accountName, balance, totalAsset, totalLiabilities) 
INSERT INTO BP_account (idAccount, idUser, accountName, balance, totalAsset, totalLiabilities) 
    VALUES (1, 1, 'Savings', 1500.00, 2500.00, 1000.00);
INSERT INTO BP_account (idAccount, idUser, accountName, balance, totalAsset, totalLiabilities) 
    VALUES (2, 1, 'Checking', 1000.00, 2000.00, 1000.00);
INSERT INTO BP_account (idAccount, idUser, accountName, balance, totalAsset, totalLiabilities) 
    VALUES (3, 1, 'CreditCard', -500.00, 0.00, 500.00);
    
-- BP_organization (idOrg, orgName, contactPerson, Email, categoryType)
INSERT INTO BP_organization
    VALUES (1, 'No Frills', NULL, NULL, 1);
INSERT INTO BP_organization
    VALUES (2, 'TheAppLab', 'Jerry Smith', 'info@theapplab.com', 2);

-- BP_expense (idEntry, idOrg, idAccount, price, paymentDate, categoryName, usernotes) 
INSERT INTO BP_expense 
    VALUES (idEntry_seq.nextval, 1, 1, 5000.00,'18-FEB-2024', 'FOOD', NULL);
INSERT INTO BP_expense 
    VALUES (idEntry_seq.nextval, 1, 1, 5000.00,'01-MAR-2024', 'DRINK', NULL);
-- BP_income (idEntry, idOrg, idAccount, amount, paymentDate, categoryName, usernotes) 
INSERT INTO BP_income 
    VALUES (idEntry_seq.nextval, 2, 1, 500.00, '01-MAR-2024', 'SALARY', 'Monthly paycheck');


ALTER TABLE BP_expense MODIFY idEntry DEFAULT idEntry_seq.nextval;
ALTER TABLE BP_income MODIFY idEntry DEFAULT idEntry_seq.nextval;
COMMIT;