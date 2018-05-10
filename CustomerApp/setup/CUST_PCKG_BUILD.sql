CREATE OR REPLACE PACKAGE CUSTOMER_PCKG IS
	/* ========================================= */
	/* ========================================= */
	/* ========== GENERIC ARRAY TYPES ========== */
	/* ========================================= */
	/* ========================================= */
	TYPE STR_ARRAY IS VARRAY(1000000) OF VARCHAR2(250);
	TYPE DATE_ARRAY IS VARRAY(1000000) OF DATE;
	
	/* ======================================== */
	/* ======================================== */
	/* ============== EXCEPTIONS ============== */
	/* ======================================== */
	/* ======================================== */
	/* GENERIC EXCEPTION IF ANY KIND OF LOGIN FAILS */
	INVALID_LOGIN EXCEPTION;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== ACCOUNT MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	/* USED FOR THE APPLICATION TO HASH CUSTOMER'S PASSWORD */
	FUNCTION GET_SALT (
		IN_CUSTOMER IN VARCHAR2
	) RETURN VARCHAR2;
	
	/* CONFIRM CUSTOMER'S CREDENTIALS, THEN RETURN CUSTOMER'S PERSONAL INFORMATION */
	FUNCTION LOGIN (
		IN_CUSTOMER IN VARCHAR2,
		IN_PASSWORD IN RAW,
		OUT_FNAME OUT VARCHAR2,
		OUT_LNAME OUT VARCHAR2,
		OUT_PHONE OUT VARCHAR2,
		OUT_EMAIL OUT VARCHAR2,
		OUT_ADDRESS OUT VARCHAR2,
		OUT_CREATED_DATE OUT DATE
	) RETURN VARCHAR2;
	
	/* CONFIRM CUSTOMER'S PASSWORD, THEN CHANGE IT TO A NEW ONE */
	FUNCTION CHANGE_PASSWORD (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PASSWORD IN RAW,
		IN_NEW_SALT IN CHAR,
		IN_OLD_PASSWORD IN RAW /* CONFIRMATION */
	) RETURN NUMBER;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== PACKAGE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	TYPE INTERNET_PACK_CURS IS REF CURSOR RETURN INTERNET_PACKAGE%ROWTYPE;
	/* SHOW WHAT A CUSTOMER CAN UPGRADE TO (ONLY UPGRADE) */
	FUNCTION GET_AVAILABLE_PACKAGES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN INTERNET_PACK_CURS;
	
	TYPE EXTRA_FEAT_CURS IS REF CURSOR RETURN EXTRA_FEATURE%ROWTYPE;
	FUNCTION GET_EXTRA_FEATS (
		IN_PACK IN VARCHAR2
	) RETURN EXTRA_FEAT_CURS;
	
	/* UPGRADES CUSTOMER'S PACKAGE TO A MORE EXPENSIVE ONE */
	FUNCTION UPGRADE_PACKAGE (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PACKAGE IN VARCHAR2
	) RETURN NUMBER;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== INVOICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	TYPE USAGE_CURS IS REF CURSOR RETURN DAILY_USAGE%ROWTYPE;
	/* CHRONOGICALLY LISTS DAILY USAGES OF A CUSTOMER */
	FUNCTION GET_USAGES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN USAGE_CURS;
	
	TYPE INVOICE_CURS IS REF CURSOR RETURN INVOICE%ROWTYPE;
	/* CHRONOGICALLY LISTS INVOICES OF A CUSTOMER */
	FUNCTION GET_INVOICES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN INVOICE_CURS;
	
	TYPE FEES_CURS IS REF CURSOR RETURN EXTRA_FEE%ROWTYPE;
	/* GETS BALANCE INFORMATION OF A SPECIFIC BALANCE */
	FUNCTION GET_BALANCE (
		IN_ID IN NUMBER,
		OUT_SUBTOTAL OUT NUMBER,
		OUT_FEES OUT FEES_CURS
	) RETURN NUMBER;
	
	TYPE PAYMENT_CURS IS REF CURSOR RETURN PAYMENT%ROWTYPE;
	/* GET PAYMENTS A CUSTOMER HAS MADE FOR AN INVOICE */
	FUNCTION GET_PAYMENTS (
		IN_ID IN NUMBER
	) RETURN PAYMENT_CURS;
	
	/* CUSTOMER PAYS AN AMOUNT FOR A SPECIFIC INVOICE */
	FUNCTION PAY (
		IN_ID IN NUMBER,
		IN_AMOUNT IN NUMBER
	) RETURN NUMBER;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== SERVICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	TYPE REMIND_CURS IS REF CURSOR RETURN REMINDER%ROWTYPE;
	/* GET REMINDERS ADDRESSED TO A CUSTOMER BY A REPRESENTATIVE */
	FUNCTION GET_REMINDERS (
		IN_CUSTOMER IN NUMBER
	) RETURN REMIND_CURS;
	
	/* CHECKS ON THE SEEN FIELD OF THE REMINDER */
	PROCEDURE SEE_REMINDER (
		IN_REMINDER IN NUMBER
	);
	
	/* CREATES A NEW SERVICE REQUEST FOR A CUSTOMER */
	PROCEDURE REQUEST_SERVICE (
		IN_CUSTOMER IN VARCHAR2,
		IN_DATE IN DATE,
		IN_DEPARTMENT IN VARCHAR2,
		IN_DETAILS IN VARCHAR2
	);
	
	/* LISTS ALL APPOINTMENTS THE CUSTOMER MADE WHICH WERE (OR NOT) ACCEPTED */
	FUNCTION CHECK_APPOINTMENTS (
		IN_CUSTOMER IN VARCHAR2,
		IN_ACCEPTED IN BOOLEAN,
		OUT_DETAILS OUT STR_ARRAY
	) RETURN DATE_ARRAY;
	
END CUSTOMER_PCKG;
/
CREATE OR REPLACE PACKAGE BODY CUSTOMER_PCKG IS
	/* ======================================== */
	/* ======================================== */
	/* ========== ACCOUNT MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	FUNCTION GET_SALT (
		IN_CUSTOMER IN VARCHAR2
	) RETURN VARCHAR2 AS
		V_SALT CHAR(30);
	BEGIN
		SELECT SALT INTO V_SALT
		FROM CUSTOMER
		WHERE USERNAME = IN_CUSTOMER;
		
		RETURN V_SALT;
	EXCEPTION
		/* GIVE MALICIOUS USER AS LESS INFORMATION AS POSSIBLE */
		WHEN NO_DATA_FOUND THEN
			RAISE INVALID_LOGIN;
		WHEN TOO_MANY_ROWS THEN
			RAISE INVALID_LOGIN;
	END;
	
	FUNCTION LOGIN (
		IN_CUSTOMER IN VARCHAR2,
		IN_PASSWORD IN RAW,
		OUT_FNAME OUT VARCHAR2,
		OUT_LNAME OUT VARCHAR2,
		OUT_PHONE OUT VARCHAR2,
		OUT_EMAIL OUT VARCHAR2,
		OUT_ADDRESS OUT VARCHAR2,
		OUT_CREATED_DATE OUT DATE
	) RETURN VARCHAR2 AS
		V_USERNAME VARCHAR2(20);
	BEGIN
		SELECT
		USERNAME, FIRSTNAME, LASTNAME, PHONE,
		EMAIL, ADDRESS, CREATED_DATE
		INTO
		V_USERNAME, OUT_FNAME, OUT_LNAME, OUT_PHONE,
		OUT_EMAIL, OUT_ADDRESS, OUT_CREATED_DATE
		FROM CUSTOMER
		/* ONLY ACTIVE CUSTOMER MAY LOGIN */
		WHERE (ACTIVE = '1') AND
		(USERNAME = IN_CUSTOMER) AND
		(IN_PASSWORD = PASSWORD);
		
		RETURN V_USERNAME;
	EXCEPTION
		/* GIVE MALICIOUS USER AS LESS INFORMATION AS POSSIBLE */
		WHEN NO_DATA_FOUND THEN
			RAISE INVALID_LOGIN;
		WHEN TOO_MANY_ROWS THEN
			RAISE INVALID_LOGIN;
	END;
	
	FUNCTION CHANGE_PASSWORD (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PASSWORD IN RAW,
		IN_NEW_SALT IN CHAR,
		IN_OLD_PASSWORD IN RAW /* CONFIRMATION */
	) RETURN NUMBER AS
		V_OLD_PASSWORD CUSTOMER.PASSWORD%TYPE;
	BEGIN
		SELECT PASSWORD INTO V_OLD_PASSWORD
		FROM CUSTOMER
		WHERE USERNAME = IN_CUSTOMER;
		
		/* VERIFY USER IS AUTHENTICATED */
		IF V_OLD_PASSWORD = IN_OLD_PASSWORD THEN
			UPDATE CUSTOMER
			SET PASSWORD = IN_NEW_PASSWORD,
			SALT = IN_NEW_SALT
			WHERE USERNAME = IN_CUSTOMER;
			
			RETURN 1;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		/* GIVE MALICIOUS USER AS LESS INFORMATION AS POSSIBLE */
		WHEN NO_DATA_FOUND THEN
			RAISE INVALID_LOGIN;
		WHEN TOO_MANY_ROWS THEN
			RAISE INVALID_LOGIN;
	END;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== PACKAGE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	FUNCTION GET_AVAILABLE_PACKAGES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN INTERNET_PACK_CURS AS
		V_PACK INTERNET_PACK_CURS;
	BEGIN
		OPEN V_PACK FOR
		SELECT *
		FROM INTERNET_PACKAGE
		/* ONLY LIST CHEAPER PACKAGES */
		WHERE MONTHLY_PRICE > (
			SELECT IP.MONTHLY_PRICE
			FROM INTERNET_PACKAGE IP JOIN CUSTOMER C
			ON IP.NAME = C.INTERNET_PACKAGE
			WHERE C.USERNAME = IN_CUSTOMER
		);
		
		RETURN V_PACK;
	END;
	
	FUNCTION GET_EXTRA_FEATS (
		IN_PACK IN VARCHAR2
	) RETURN EXTRA_FEAT_CURS AS
		V_FEAT EXTRA_FEAT_CURS;
	BEGIN
		OPEN V_FEAT FOR
		SELECT FEAT.NAME, FEAT.DESCRIPTION, FEAT.EXPENSE,
		FEAT.MONTHLY_PRICE, FEAT.PER_GB
		FROM EXTRA_FEATURE FEAT JOIN PACK_FEAT PF
		ON FEAT.NAME = PF.EXTRA_FEATURE
		WHERE PF.INTERNET_PACKAGE = IN_PACK;
		
		RETURN V_FEAT;
	END;
	
	FUNCTION UPGRADE_PACKAGE (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PACKAGE IN VARCHAR2
	) RETURN NUMBER AS
		V_CURRENT_PACKAGE_COST INTERNET_PACKAGE.MONTHLY_PRICE%TYPE;
		
		V_NEW_PACKAGE_COST INTERNET_PACKAGE.MONTHLY_PRICE%TYPE;
	BEGIN
		/* GETTING PRICES OF OLD AND NEW PACKAGE */
		SELECT MONTHLY_PRICE INTO V_CURRENT_PACKAGE_COST
		FROM INTERNET_PACKAGE IP JOIN CUSTOMER C
		ON IP.NAME = C.INTERNET_PACKAGE
		WHERE C.USERNAME = IN_CUSTOMER;
		
		SELECT MONTHLY_PRICE INTO V_NEW_PACKAGE_COST
		FROM INTERNET_PACKAGE IP JOIN CUSTOMER C
		ON IP.NAME = C.INTERNET_PACKAGE
		WHERE IP.NAME = IN_NEW_PACKAGE;
		
		/* CHECKING IF IT REALLY IS A DOWNGRADE */
		IF V_CURRENT_PACKAGE_COST < V_NEW_PACKAGE_COST THEN
			UPDATE CUSTOMER
			SET INTERNET_PACKAGE = IN_NEW_PACKAGE
			WHERE USERNAME = IN_CUSTOMER;
			
			RETURN 1;
		ELSE
			RETURN 0;
		END IF;
	EXCEPTION
		WHEN NO_DATA_FOUND THEN
			RETURN 0;
	END;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== INVOICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	FUNCTION GET_USAGES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN USAGE_CURS AS
		V_USAGES USAGE_CURS;
	BEGIN
		OPEN V_USAGES FOR
		SELECT *
		FROM DAILY_USAGE
		WHERE IN_CUSTOMER = CUSTOMER
		ORDER BY DAY_USAGE;
		
		RETURN V_USAGES;
	END;
	
	FUNCTION GET_INVOICES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN INVOICE_CURS AS
		V_INVOICES INVOICE_CURS;
	BEGIN
		OPEN V_INVOICES FOR
		SELECT *
		FROM INVOICE
		START WITH ID = (
			SELECT ID
			FROM INVOICE
			WHERE CUSTOMER = IN_CUSTOMER
			HAVING CREATED_DATE = (
				SELECT MAX(CREATED_DATE)
				FROM INVOICE
				WHERE CUSTOMER = IN_CUSTOMER
			)
		)
		/* RECURSIVE SELF-JOIN */
		CONNECT BY PRIOR PREVIOUS = ID
		ORDER SIBLINGS BY CREATED_DATE;
		
		RETURN V_INVOICES;
	END;
	
	FUNCTION GET_BALANCE (
		IN_ID IN NUMBER,
		OUT_SUBTOTAL OUT NUMBER,
		OUT_FEES OUT FEES_CURS
	) RETURN NUMBER AS
		V_TOTAL NUMBER(6,2);
	BEGIN
		SELECT
		TOTAL, SUBTOTAL
		INTO
		V_TOTAL, OUT_SUBTOTAL
		FROM BALANCE
		WHERE ID = IN_ID;
		
		OPEN OUT_FEES FOR
		SELECT EX.NAME, EX.DESCRIPTION, EX.COST
		FROM EXTRA_FEE EX
		JOIN BALANCE_EXTRA_FEE BEX
		ON BEX.EXTRA_FEE = EX.NAME
		WHERE BEX.BALANCE = IN_ID;
		
		RETURN V_TOTAL;
	END;
	
	FUNCTION GET_PAYMENTS (
		IN_ID IN NUMBER
	) RETURN PAYMENT_CURS AS
		V_PAYMENTS PAYMENT_CURS;
	BEGIN
		OPEN V_PAYMENTS FOR
		SELECT *
		FROM PAYMENT
		WHERE INVOICE = IN_ID;
		
		RETURN V_PAYMENTS;
	END;
	
	FUNCTION PAY (
		IN_ID IN NUMBER,
		IN_AMOUNT IN NUMBER
	) RETURN NUMBER AS
		V_DUMMY_NUM NUMBER(6,2);
		DUMMY_CURS FEES_CURS;
		
		V_PAYMENT PAYMENT%ROWTYPE;
		C_PAYMENT PAYMENT_CURS := GET_PAYMENTS(IN_ID);
		
		V_SUM NUMBER(6,2) := 0;
		V_BALANCE NUMBER(6,2);
	BEGIN
		/* GETTING ALL PREVIOUS PAYMENTS */
		LOOP
			FETCH C_PAYMENT INTO V_PAYMENT;
			EXIT WHEN C_PAYMENT%NOTFOUND;
			
			V_SUM := V_SUM + V_PAYMENT.AMOUNT_PAID;
		END LOOP;
		
		/* GETTING THE CURRENT BALANCE */
		V_BALANCE := GET_BALANCE(
			IN_ID,
			V_DUMMY_NUM,
			DUMMY_CURS
		);
		
		/* INSERT ONLY IF THE BALANCE IS NOT ALREADY OVERPAID */
		IF V_SUM > V_BALANCE THEN
			INSERT INTO PAYMENT
			(INVOICE, DATE_PAID, AMOUNT_PAID)
			VALUES
			(IN_ID, SYSDATE(), IN_AMOUNT);
			
			RETURN 1;
		ELSE
			RETURN 0;
		END IF;
	END;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== SERVICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	FUNCTION GET_REMINDERS (
		IN_CUSTOMER IN NUMBER
	) RETURN REMIND_CURS AS
		V_REMIND REMIND_CURS;
	BEGIN
		OPEN V_REMIND FOR
		SELECT *
		FROM REMINDER
		WHERE CUSTOMER = IN_CUSTOMER;
		
		UPDATE SET SEEN = '1'
		WHERE CUSTOMER = IN_CUSTOMER;
		
		RETURN V_REMIND;
	END;
	
	PROCEDURE SEE_REMINDER (
		IN_REMINDER IN NUMBER
	) AS
	BEGIN
		UPDATE SET SEEN = '1'
		WHERE ID = IN_REMINDER;
	END;
	
	PROCEDURE REQUEST_SERVICE (
		IN_CUSTOMER IN VARCHAR2,
		IN_DATE IN DATE,
		IN_DEPARTMENT IN VARCHAR2,
		IN_DETAILS IN VARCHAR2
	) AS
	BEGIN
		INSERT INTO REQUEST
		(APPOINTMENT, CUSTOMER,
		SCHEDULED, DEPARTMENT, DETAILS)
		VALUES
		(NULL, IN_CUSTOMER,
		IN_DATE, IN_DEPARTMENT, IN_DETAILS);
	END;
	
	FUNCTION CHECK_APPOINTMENTS (
		IN_CUSTOMER IN VARCHAR2,
		IN_ACCEPTED IN BOOLEAN,
		OUT_DETAILS OUT STR_ARRAY
	) RETURN DATE_ARRAY AS
		V_SCHEDULE_DATES DATE_ARRAY;
		V_ACCEPTED CHAR(1);
	BEGIN
		IF IN_ACCEPTED THEN
			V_ACCEPTED := '1';
		ELSE
			V_ACCEPTED := '0';
		END IF;
		
		SELECT
		SCHEDULED, DETAILS
		BULK COLLECT INTO
		V_SCHEDULE_DATES, OUT_DETAILS
		FROM REQUEST
		WHERE (CUSTOMER = IN_CUSTOMER) AND
		((APPOINTMENT IS NULL AND V_ACCEPTED = '0')
		OR (APPOINTMENT IS NOT NULL AND V_ACCEPTED = '1'));
		
		RETURN V_SCHEDULE_DATES;
	END;
	
END CUSTOMER_PCKG;
/