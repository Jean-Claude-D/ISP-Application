/* == README ==

CONSTRAINT NAME GENERALLY FOLLOW THIS RULE : 
[TABLE NAME]_[FIELD NAME]_[CONSTRAINT TYPE]

FOR FOREIGN KEY CONSTRAINTS, THE 'ON DELETE' CLAUSE WAS
'SET NULL' BY DEFAULT TO ENSURE WE KEEP AS MUCH DATA AS POSSIBLE
AND I JUSTIFIED EVERY 'CASCADE' */

/* THIS RECORDS EVERY CUSTOMER THAT EVER HAS BEEN WITH OUR ISP */
CREATE TABLE CUSTOMER (
	/* USED FOR LOGIN */
	USERNAME VARCHAR2(20),
	
	/* USED FOR LOGIN, STORED AS HASH */
	PASSWORD CHAR(30) NOT NULL,
	SALT CHAR(30) NOT NULL,
	
    FIRSTNAME VARCHAR2(40) NOT NULL,
    LASTNAME VARCHAR2(40) NOT NULL,
	PHONE CHAR(12) NOT NULL,
	/* IF WE CUT THE CUSTOMER'S INTERNET,
	   CANNOT JOIN HIM BY EMAIL. THEREFORE
	   SHOULD NOT RELY ON THAT */
	EMAIL VARCHAR2(50),
	ADDRESS VARCHAR2(50) NOT NULL,
	
	/* THE PACKAGE CURRENTLY TAKING */
	INTERNET_PACKAGE VARCHAR2(40),
	
	/* THE DATE CUSTOMER FIRST SIGNED WITH ISP */
	CREATED_DATE DATE NOT NULL,
	/* IF THE CUSTOMER IS STILL WITH ISP */
	ACTIVE CHAR(1) NOT NULL,
	
	CONSTRAINT CUSTOMER_USERNAME_PK
	PRIMARY KEY(USERNAME),
	
	CONSTRAINT CUSTOMER_INTERNET_PACKAGE_FK
	FOREIGN KEY (INTERNET_PACKAGE)
	REFERENCES INTERNET_PACKAGE(NAME)
	ON DELETE SET NULL,
    
  	CONSTRAINT CUSTOMER_ACTIVE_BOOLEAN_CHECK
  	CHECK (ACTIVE IN ('1', '0'))
);
/* SAVES PAYING PREFERENCES FOR CUSTOMERS */
CREATE TABLE PAYING_INFO (
	CUSTOMER VARCHAR2(20) NOT NULL,
	
	PAY_TYPE VARCHAR2(20) NOT NULL,
	
	/* STORED AS HASH */
	PAY_ID VARCHAR2(30),
	SALT CHAR(30),
	
	/* IF THE CUSTOMER GETS DELETED,
	   WE DO NOT WANT TO KEEP HIS CREDIT CARD NUMBER */
	CONSTRAINT PAYING_INFO_CUSTOMER_FK
	FOREIGN KEY (CUSTOMER)
	REFERENCES CUSTOMER(USERNAME)
	ON DELETE CASCADE,
	
	/* WE MIGHT WANT TO ADD MORE */
	CONSTRAINT PAY_TYPE_VALID_OPTION_CHECK
	CHECK (PAY_TYPE IN ('CREDIT CARD', 'PAYPAL', 'CRYPTOCURRENCY'))
);
/* CUSTOMER'S DAILY UPLOAD AND DOWNLOAD USAGE */
CREATE TABLE DAILY_USAGE (
	CUSTOMER VARCHAR2(20),
	
	UPLOAD_GB NUMBER(6,3) NOT NULL,
	DOWNLOAD_GB NUMBER(6,3) NOT NULL,
	
	DAY_USAGE DATE,
	
	/* CANNOT HAVE MULTIPLE DAILY USAGES PER DAY */
	CONSTRAINT DAILY_USAGE_CUSTOMER_DAY_PK
	PRIMARY KEY (CUSTOMER, DAY_USAGE),
	
	/* CANNOT HAVE 'SET NULL' SINCE
	   CUSTOMER ALSO IS A PRIMARY KEY */
	CONSTRAINT DAILY_USAGE_CUSTOMER_FK
	FOREIGN KEY (CUSTOMER)
	REFERENCES CUSTOMER(USERNAME)
	ON DELETE CASCADE
);
/* THIS RECORDS ALL CURRENT POSSIBLE PACKAGES */
CREATE TABLE INTERNET_PACKAGE (
	/* UNIQUELY IDENTIFY EACH PACKAGE */
	NAME VARCHAR2(40),
	DESCRIPTION VARCHAR2(250),
	
	SPEED_UPLOAD_MBPS NUMBER(6,3),
	SPEED_DOWNLOAD_MBPS NUMBER(6,3),
	/* LIMIT FOR BOTH UPLOAD AND DOWNLOAD */
	BANDWIDTH_GB NUMBER(5,3),
	
    /* HOW MUCH IT COSTS FOR ISP TO PROVIDE PACKAGE */
    RUNNING_COST NUMBER(7,2),
	MONTHLY_PRICE NUMBER(5,2),
	/* PGB = PER GB */
	OVERAGE_COST_PGB NUMBER(4,2),
	
	CONSTRAINT INTERNET_PACKAGE_NAME_PK
	PRIMARY KEY (NAME)
);
/* THIS RECORDS ALL POSSIBLE ADDITIONAL FEATURE ON INTERNET PACKAGES */
CREATE TABLE EXTRA_FEATURE (
	/* UNIQUELY IDENTIFY EACH FEATURE */
	NAME VARCHAR2(40),
	DESCRIPTION VARCHAR2(250),
	
    /* HOW MUCH IT COSTS FOR ISP TO PROVIDE FEATURE */
    EXPENSE NUMBER(7,2),
	/* THE ADDED COST OF THIS FEATURE PER MONTH */
	MONTHLY_PRICE NUMBER(5,2) NOT NULL,
	
	/* IF THE MONTHLY_PRICE IS CHARGED PER GB
	   OR AS A MONTHLY FIXED PRICE */
	PER_GB CHAR(1) NOT NULL,
	
	CONSTRAINT EXTRA_FEATURE_NAME_PK
	PRIMARY KEY (NAME),
    
  	CONSTRAINT EXTRA_FEATURE_PER_GB_BOOLEAN_CHECK
  	CHECK (PER_GB IN ('1', '0'))
);
/* BRIDGING TABLE BETWEEN INTERNET_PACKAGE AND EXTRA_FEATURE */
CREATE TABLE PACK_FEAT (
	INTERNET_PACKAGE VARCHAR2(40) NOT NULL,
	EXTRA_FEATURE VARCHAR2(40) NOT NULL,
	
	/* A PACK_FEAT RECORD DOES NOT MAKE SENSE
	WITHOUT BOTH INTERNET_PACKAGE AND EXTRA_FEATURE */
	CONSTRAINT PACK_FEAT_INTERNET_PACKAGE_FK
	FOREIGN KEY (INTERNET_PACKAGE)
	REFERENCES INTERNET_PACKAGE(NAME)
	ON DELETE CASCADE,
	
	CONSTRAINT PACK_FEAT_EXTRA_FEATURE_FK
	FOREIGN KEY (EXTRA_FEATURE)
	REFERENCES EXTRA_FEATURE(NAME)
	ON DELETE CASCADE
);
/* THE BILL EACH ACTIVE CUSTOMER RECEIVES EACH MONTH */
CREATE TABLE INVOICE (
	ID NUMBER(9,0),
	/* THE INVOICE PRECEEDING THIS ONE */
	PREVIOUS NUMBER(9,0),
	
	CUSTOMER VARCHAR2(20),
	
	/* PACKAGE USED TO COMPUTE THE BALANCE */
	INTERNET_PACKAGE VARCHAR2(40),
	
	/* WHEN THE INVOICE SHOULD BE PAID */
	DUE_DATE DATE NOT NULL,
	CREATED_DATE DATE NOT NULL,
	
	/* PRICE INFO ABOUT THIS INVOICE */
	BALANCE NUMBER(9,0),
	
	CONSTRAINT INVOICE_ID_PK
	PRIMARY KEY (ID),
	
	CONSTRAINT INVOICE_CUSTOMER_FK
	FOREIGN KEY (CUSTOMER)
	REFERENCES CUSTOMER(USERNAME)
	ON DELETE SET NULL,
	
	CONSTRAINT INVOICE_INTERNET_PACKAGE_FK
	FOREIGN KEY (INTERNET_PACKAGE)
	REFERENCES INTERNET_PACKAGE(NAME)
	ON DELETE SET NULL,
	
	CONSTRAINT INVOICE_PREVIOUS_FK
	FOREIGN KEY (PREVIOUS)
	REFERENCES INVOICE(ID)
	ON DELETE SET NULL,
	
	CONSTRAINT INVOICE_BALANCE_FK
	FOREIGN KEY (BALANCE)
	REFERENCES BALANCE(ID)
	ON DELETE SET NULL
);
/* EXTRA FEE THAT COULD APPLY TO AN INVOICE E.G.: INSTALLATION */
CREATE TABLE EXTRA_FEE (
	/* UNIQUELY IDENTIFY EACH EXTRA FEE */
	NAME VARCHAR2(20),
	DESCRIPTION VARCHAR2(100),
	
	COST NUMBER(5,2) NOT NULL,
	
	CONSTRAINT EXTRA_FEE_NAME_PK
	PRIMARY KEY (NAME)
);
/* INFO ABOUT THE INVOICE'S AMOUNT TO BE PAID */
CREATE TABLE BALANCE (
	ID NUMBER(9,0),
	
	/* INTERNET PACKAGE PRICE +
	   OVERAGE PRICE +
	   EXTRA FEATURE(S) PRICE +
	   EXTRA FEE(S) PRICE =
	   SUBTOTAL
	   
	   REQUIRES TRIGGERS */
	SUBTOTAL NUMBER(6,2) NOT NULL,
	/* SUBTOTAL +
	   TAXES =
	   TOTAL
	   
	   ALSO REQUIRES TRIGGERS */
	TOTAL NUMBER(6,2) NOT NULL,
	
	CONSTRAINT BALANCE_ID_PK
	PRIMARY KEY (ID)
);
/* EACH PAYMENT OF ANY PORTION OF AN INVOICE */
CREATE TABLE PAYMENT (
	INVOICE NUMBER(9,0) NOT NULL,
	
	DATE_PAID DATE,
	
	AMOUNT_PAID NUMBER(5,2) NOT NULL,
	
	/* IF THE INVOICE GETS DELETED,
	   WE ONLY KNOW AN AMOUNT WAS PAID ON A CERTAIN DATE,
	   WHICH IS MEANINGLESS */
	CONSTRAINT PAYMENT_INVOICE_FK
	FOREIGN KEY (INVOICE)
	REFERENCES INVOICE(ID)
	ON DELETE CASCADE
);
/* BRIDGING TABLE BETWEEN BALANCE AND EXTRA_FEE */
CREATE TABLE BALANCE_EXTRA_FEE (
	BALANCE NUMBER(9,0) NOT NULL,
	EXTRA_FEE VARCHAR2(20) NOT NULL,
	
	/* A BALANCE_EXTRA_FEE RECORD DOES NOT MAKE SENSE
	WITHOUT BOTH BALANCE AND EXTRA_FEE */
	CONSTRAINT BALANCE_EXTRA_FEE_BALANCE_FK
	FOREIGN KEY (BALANCE)
	REFERENCES BALANCE(ID)
	ON DELETE CASCADE,
	
	CONSTRAINT BALANCE_EXTRA_FEE_EXTRA_FEE_FK
	FOREIGN KEY (EXTRA_FEE)
	REFERENCES EXTRA_FEE(NAME)
	ON DELETE CASCADE
);
/* RECORDS ALL EMPLOYEES OF ISP THAT WORK WITH CUSTOMERS */
CREATE TABLE REPRESENTATIVE (
	/* USED FOR LOGIN */
	USERNAME VARCHAR2(20),
	
	/* USED FOR LOGIN, STORED AS HASH */
	PASSWORD CHAR(30) NOT NULL,
	SALT CHAR(30) NOT NULL,
	
	PHONE CHAR(12) NOT NULL,
	EMAIL VARCHAR2(50) NOT NULL,
	ADDRESS VARCHAR2(50) NOT NULL,
	
	DEPARTMENT VARCHAR2(20),
	
	CONSTRAINT REPRESENTATIVE_USERNAME_PK
	PRIMARY KEY(USERNAME),
	
	/* ALL VALID DEPARTMENT */
	CONSTRAINT REPRESENTATIVE_DEPARTMENT_VALID_CHECK
	CHECK (DEPARTMENT IN ('TECHNICAL', 'SALE', 'BILLING', 'TECH SUPPORT'))
);
/* ANY CALL MADE BY A CUSTOMER IS LOGGED,
   ADDITIONAL INFORMATION IN FOLLOWING TABLES */
CREATE TABLE CUSTOMER_CALL (
	ID NUMBER(8,0),
	
	REPRESENTATIVE VARCHAR2(20),
	CUSTOMER VARCHAR2(20),
	
	/* TIME AT WHICH THE CALL STARTED */
	START_TIME DATE,
	
	CONSTRAINT CUSTOMER_CALL_ID_PK
	PRIMARY KEY(ID),
	
	CONSTRAINT CUSTOMER_CALL_REPRESENTATIVE_FK
	FOREIGN KEY (REPRESENTATIVE)
	REFERENCES REPRESENTATIVE(USERNAME)
	ON DELETE SET NULL,
	
	CONSTRAINT CUSTOMER_CALL_CUSTOMER_FK
	FOREIGN KEY (CUSTOMER)
	REFERENCES CUSTOMER(USERNAME)
	ON DELETE SET NULL
);
/* CALLS MADE TO A SALES REPRESENTATIVE */
CREATE TABLE SALES_CALL (
	CUSTOMER_CALL NUMBER(8,0) NOT NULL,
	
	/* IF THE CALL RESULTED IN A NEW CUSTOMER */
	SUCCESS CHAR(1) NOT NULL,
	
	/* NO NEED TO KEEP A NULL ALONG WITH A SINGLE CHARACTER
	   IF THE ACTUAL CALL RECORD GETS DELETED */
	CONSTRAINT SALES_CALL_CUSTOMER_CALL_FK
	FOREIGN KEY (CUSTOMER_CALL)
	REFERENCES CUSTOMER_CALL(ID)
	ON DELETE CASCADE,
    
  	CONSTRAINT SALES_CALL_SUCCESS_BOOLEAN_CHECK
  	CHECK (SUCCESS IN ('1', '0'))
);
/* CALLS MADE TO A TECHNICAL REPRESENTATIVE */
CREATE TABLE TECH_CALL (
	CUSTOMER_CALL NUMBER(8,0) NOT NULL,
	
	/* THE ISSUE THE CUSTOMER CALLED FOR */
	ISSUE_TYPE VARCHAR2(40) NOT NULL,
	
	DETAILS VARCHAR2(100) NOT NULL,
	
	/* NO NEED TO KEEP A NULL ALONG WITH A SINGLE CHARACTER
	   IF THE ACTUAL CALL RECORD GETS DELETED */
	CONSTRAINT TECH_CALL_CUSTOMER_CALL_FK
	FOREIGN KEY (CUSTOMER_CALL)
	REFERENCES CUSTOMER_CALL(ID)
	ON DELETE CASCADE
);
/* CALLS MADE TO A BILLING REPRESENTATIVE */
CREATE TABLE BILLING_CALL (
	CUSTOMER_CALL NUMBER(8,0) NOT NULL,
	
	DETAILS VARCHAR2(100) NOT NULL,
	
	/* NO NEED TO KEEP A NULL ALONG WITH A SINGLE CHARACTER
	   IF THE ACTUAL CALL RECORD GETS DELETED */
	CONSTRAINT TECH_CALL_CUSTOMER_CALL_FK
	FOREIGN KEY (CUSTOMER_CALL)
	REFERENCES CUSTOMER_CALL(ID)
	ON DELETE CASCADE
);
/* RECORDS CUSTOMER'S REQUESTS FOR APPOINTMENTS */
CREATE TABLE REQUEST (
	/* MULTIPLE REQUESTS SHOULD NOT
	   RESULT IN A SINGLE APPOINTMENT */
	/* NULL WHEN NO APPOINTMENT HAS BEEN CONFIRMED */
	APPOINTMENT NUMBER(7,0) UNIQUE,
	
	CUSTOMER VARCHAR2(20) NOT NULL,
	
	/* DESIRED DATE AND TIME */
	SCHEDULED DATE NOT NULL,
	
	/* CONCERNED DEPARTMENT */
	DEPARTMENT VARCHAR2(20) NOT NULL,
	
	/* DETAILS THE CUSTOMER MAY WANT TO GIVE */
	DETAILS VARCHAR2(200),
	
	CONSTRAINT REQUEST_APPOINTMENT_FK
	FOREIGN KEY (APPOINTMENT)
	REFERENCES APPOINTMENT(ID)
	ON DELETE SET NULL,
	
	CONSTRAINT REQUEST_CUSTOMER_FK
	FOREIGN KEY (CUSTOMER)
	REFERENCES CUSTOMER(USERNAME)
	ON DELETE CASCADE
);
/* CONCRETE APPOINTMENT THAT A REPRESENTATIVE HAS CONFIRMED */
CREATE TABLE APPOINTMENT (
	ID NUMBER(7,0),
	
	REPRESENTATIVE VARCHAR2(20) NOT NULL,
	
	/* REPRESENTATIVE'S REPORT ON THE APPOINTMENT */
	REPORT VARCHAR2(150),
	
	CONSTANT APPOINTMENT_ID_PK
	PRIMARY KEY (ID)
);
/* REFUND A BILLING REPRESENTATIVE MAY DO FOR AN INVOICE */
CREATE TABLE REFUND (
	/* REPRESENTATIVE WHO DID THE REFUND */
	REPRESENTATIVE VARCHAR2(20),
	
	INVOICE NUMBER(9,0) NOT NULL,
	AMOUNT NUMBER(5,2) NOT NULL,
	
	/* EXPLAINS WHY THE REFUND WAS MADE */
	REASON VARCHAR2(200) NOT NULL,
	
	CONSTRAINT REFUND_REPRESENTATIVE_FK
	FOREIGN KEY (REPRESENTATIVE)
	REFERENCES REPRESENTATIVE(USERNAME)
	ON DELETE SET NULL,
	
	/* IF THE INVOICE DOES NOT EXIST ANYMORE,
	   REFUND MAKES NO SENSE ON ITS OWN */
	CONSTRAINT REFUND_INVOICE_FK
	FOREIGN KEY (INVOICE)
	REFERENCES INVOICE(ID)
	ON DELETE CASCADE
);
/* RECORD ANY MODIFICATION A BILLING REPRESENTATIVE MAKES */
CREATE TABLE MODIFICATION (
	INVOICE NUMBER(9,0) NOT NULL,
	
	REPRESENTATIVE VARCHAR2(20),
	
	/* DATE ON WHICH MODIFICATION WAS MADE */
	MODIF_DATE DATE NOT NULL,
	
	/* DATE BEFORE THE CHANGE */
	PREVIOUS_DATE DATE NOT NULL,
	
	/* IF THE INVOICE DOES NOT EXIST ANYMORE,
	   KEEPING ITS PREVIOUS DATE IS USELESS */
	CONSTRAINT MODIFICATION_INVOICE_FK
	FOREIGN KEY (INVOICE)
	REFERENCES INVOICE(ID)
	ON DELETE CASCADE,
	
	CONSTRAINT MODIFICATION_REPRESENTATIVE_FK
	FOREIGN KEY (REPRESENTATIVE)
	REFERENCES REPRESENTATIVE(USERNAME)
	ON DELETE SET NULL,
);
/* RECORD REMINDERS SENT TO CUSTOMERS ABOUT THEIR INVOICES */
CREATE TABLE REMINDER (
	CUSTOMER VARCHAR2(20) NOT NULL,
  	REPRESENTATIVE VARCHAR2(20),
  	
  	EMISSION_DATE DATE NOT NULL,
  	
 	DETAILS VARCHAR2(150),
  	
  	/* IF THE CUSTOMER HAS SEEN THE REMINDER */
  	SEEN CHAR(1),
  	
  	/* A REMINDER WITHOUT THE CUSTOMER
       TO WHICH IT IS ADDRESSED IS MEANINGLESS */
  	CONSTRAINT REMINDER_CUSTOMER_FK
  	FOREIGN KEY (CUSTOMER)
  	REFERENCES CUSTOMER(USERNAME)
  	ON DELETE CASCADE,
  	
  	CONSTRAINT REMINDER_REPRESENTATIVE_FK
  	FOREIGN KEY (REPRESENTATIVE)
  	REFERENCES REPRESENTATIVE(USERNAME)
  	ON DELETE SET NULL,
  
  	CONSTRAINT REMINDER_SEE_BOOLEAN_CHECK
  	CHECK (SEEN IN ('1', '0'))
);