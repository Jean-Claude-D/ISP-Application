CREATE OR REPLACE PACKAGE CUSTOMER_PCKG IS
	/* ========================================= */
	/* ========================================= */
	/* ========== GENERIC ARRAY TYPES ========== */
	/* ========================================= */
	/* ========================================= */
	TYPE NUM_ARRAY IS VARRAY(1000000) OF NUMBER;
	TYPE STR_ARRAY IS VARRAY(1000000) OF VARCHAR2(250);
	TYPE DATE_ARRAY IS VARRAY(1000000) OF DATE;
	
	/* ======================================== */
	/* ======================================== */
	/* ============== EXCEPTIONS ============== */
	/* ======================================== */
	/* ======================================== */
	/* IF CUSTOMER TRIES TO CHANGE PACKAGE TO A CHEAPER ONE */
	DOWNGRADE_NOT_ALLOWED EXCEPTION;
	/* GENERIC EXCEPTION IF ANY KIND OF LOGIN FAILS */
	INVALID_LOGIN EXCEPTION;
	/* IF CUSTOMER TRIES TO PAY AN ALREADY OVERPAID BALANCE */
	ALREADY_OVERPAID EXCEPTION;
	
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
	PROCEDURE CHANGE_PASSWORD (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PASSWORD IN RAW,
		IN_NEW_SALT IN CHAR,
		IN_OLD_PASSWORD IN RAW /* CONFIRMATION */
	);
	
	/* ======================================== */
	/* ======================================== */
	/* ========== PACKAGE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	/* SHOW WHAT A CUSTOMER CAN UPGRADE TO (ONLY UPGRADE) */
	FUNCTION GET_AVAILABLE_PACKAGES (
		IN_CUSTOMER IN VARCHAR2,
		OUT_DESCRIPTION OUT STR_ARRAY,
		OUT_SPEED_UP OUT NUM_ARRAY,
		OUT_SPEED_DOWN OUT NUM_ARRAY,
		OUT_BANDWIDTH OUT NUM_ARRAY,
		OUT_PRICE OUT NUM_ARRAY,
		OUT_OVERAGE OUT NUM_ARRAY,
		OUT_FEAT OUT STR_ARRAY,
		OUT_FEAT_DESCR OUT STR_ARRAY,
		OUT_FEAT_PRICE OUT NUM_ARRAY,
		OUT_FEAT_PERGB OUT STR_ARRAY
	) RETURN STR_ARRAY;
	
	/* UPGRADES CUSTOMER'S PACKAGE TO A MORE EXPENSIVE ONE */
	PROCEDURE UPGRADE_PACKAGE (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PACKAGE IN VARCHAR2
	);
	
	/* ======================================== */
	/* ======================================== */
	/* ========== INVOICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	/* CHRONOGICALLY LISTS DAILY USAGES OF A CUSTOMER */
	FUNCTION GET_USAGES (
		IN_CUSTOMER IN VARCHAR2,
		OUT_DOWNLOAD OUT NUM_ARRAY,
		OUT_UPLOAD OUT NUM_ARRAY
	) RETURN DATE_ARRAY;
		
	/* CHRONOGICALLY LISTS INVOICES OF A CUSTOMER */
	FUNCTION GET_INVOICES (
		IN_CUSTOMER IN VARCHAR2,
		OUT_PACK OUT NUM_ARRAY,
		OUT_DUE OUT DATE_ARRAY,
		OUT_CREATED OUT DATE_ARRAY,
		OUT_BALANCE OUT NUM_ARRAY
	) RETURN NUM_ARRAY;
	
	/* GETS BALANCE INFORMATION OF A SPECIFIC BALANCE */
	FUNCTION GET_BALANCE (
		IN_ID IN NUMBER,
		OUT_SUBTOTAL OUT NUMBER,
		OUT_FEES_NAME OUT STR_ARRAY,
		OUT_FEES_DESCR OUT STR_ARRAY,
		OUT_FEES OUT NUM_ARRAY
	) RETURN NUMBER;
	
	/* GET PAYMENTS A CUSTOMER HAS MADE FOR AN INVOICE */
	FUNCTION GET_PAYMENTS (
		IN_ID IN NUMBER,
		OUT_PAID OUT DATE_ARRAY
	) RETURN NUM_ARRAY;
	
	/* CUSTOMER PAYS AN AMOUNT FOR A SPECIFIC INVOICE */
	PROCEDURE PAY (
		IN_ID IN NUMBER,
		IN_AMOUNT IN NUMBER
	);
	
	/* GET REMINDERS ADDRESSED TO A CUSTOMER BY A REPRESENTATIVE */
	FUNCTION GET_REMINDERS (
		IN_CUSTOMER IN NUMBER,
		OUT_DATES OUT DATE_ARRAY,
		OUT_SEENS OUT STR_ARRAY
	) RETURN STR_ARRAY;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== SERVICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
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
		OUT_DPMT OUT STR_ARRAY,
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
		V_SALT VARCHAR2(30);
	BEGIN
		SELECT SALT INTO V_SALT
		FROM CUSTOMER
		WHERE IN_CUSTOMER = USERNAME;
		
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
		IN_PASSWORD IN VARCHAR2,
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
	
	PROCEDURE CHANGE_PASSWORD (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PASSWORD IN CHAR,
		IN_NEW_SALT IN CHAR,
		IN_OLD_PASSWORD IN CHAR
	) AS
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
		ELSE
			RAISE INVALID_LOGIN;
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
		IN_CUSTOMER IN VARCHAR2,
		OUT_DESCRIPTION OUT STR_ARRAY,
		OUT_SPEED_UP OUT NUM_ARRAY,
		OUT_SPEED_DOWN OUT NUM_ARRAY,
		OUT_BANDWIDTH OUT NUM_ARRAY,
		OUT_PRICE OUT NUM_ARRAY,
		OUT_OVERAGE OUT NUM_ARRAY,
		OUT_FEAT OUT STR_ARRAY,
		OUT_FEAT_DESCR OUT STR_ARRAY,
		OUT_FEAT_PRICE OUT NUM_ARRAY,
		OUT_FEAT_PERGB OUT STR_ARRAY
	) RETURN STR_ARRAY AS
		V_PACK_NAME STR_ARRAY;
	BEGIN
		SELECT
		NAME, DESCRIPTION, SPEED_UPLOAD_MBPS,
		SPEED_DOWNLOAD_MBPS, BANDWIDTH_GB,
		MONTHLY_PRICE, OVERAGE_COST_PGB
		BULK COLLECT INTO
		V_PACK_NAME, OUT_DESCRIPTION, OUT_SPEED_UP,
		OUT_SPEED_DOWN, OUT_BANDWIDTH,
		OUT_PRICE, OUT_OVERAGE
		FROM INTERNET_PACKAGE
		/* ONLY LIST CHEAPER PACKAGES */
		WHERE MONTHLY_PRICE > (
			SELECT IP.MONTHLY_PRICE
			FROM INTERNET_PACKAGE IP JOIN CUSTOMER C
			ON IP.NAME = C.INTERNET_PACKAGE
			WHERE C.USERNAME = IN_CUSTOMER
		);
		
		SELECT
		FEAT.NAME, FEAT.DESCRIPTION,
		FEAT.MONTHLY_PRICE, FEAT.PER_GB
		BULK COLLECT INTO
		OUT_FEAT, OUT_FEAT_DESCR,
		OUT_FEAT_PRICE, OUT_FEAT_PERGB
		FROM EXTRA_FEATURE FEAT JOIN PACK_FEAT PF
		ON FEAT.NAME = PF.EXTRA_FEATURE
		WHERE PF.INTERNET_PACKAGE = V_PACK_NAME;
		
		RETURN V_PACK_NAME;
	END;
	
	PROCEDURE UPGRADE_PACKAGE (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PACKAGE IN VARCHAR2
	) AS
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
		ELSE
			RAISE DOWNGRADE_NOT_ALLOWED;
		END IF;
	END;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== INVOICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
	FUNCTION GET_USAGES (
		IN_CUSTOMER IN VARCHAR2,
		OUT_DOWNLOAD OUT NUM_ARRAY,
		OUT_UPLOAD OUT NUM_ARRAY
	) RETURN DATE_ARRAY AS
		V_DATES DATE_ARRAY;
	BEGIN
		SELECT
		UPLOAD_GB, DOWNLOAD_GB, DAY_USAGE
		BULK COLLECT INTO
		OUT_DOWNLOAD, OUT_UPLOAD, V_DATES
		FROM DAILY_USAGE
		WHERE IN_CUSTOMER = CUSTOMER
		ORDER BY DAY_USAGE;
		
		RETURN V_DATES;
	END;
	
	FUNCTION GET_INVOICES (
		IN_CUSTOMER IN VARCHAR2,
		OUT_PACK OUT NUM_ARRAY,
		OUT_DUE OUT DATE_ARRAY,
		OUT_CREATED OUT DATE_ARRAY,
		OUT_BALANCE OUT NUM_ARRAY
	) RETURN NUM_ARRAY AS
		V_IDS NUM_ARRAY;
	BEGIN
		SELECT
		ID, INTERNET_PACKAGE, DUE_DATE,
		CREATED_DATE, BALANCE
		BULK COLLECT INTO
		V_IDS, OUT_PACK, OUT_DUE,
		OUT_CREATED, OUT_BALANCE
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
		
		RETURN V_IDS;
	END;
	
	FUNCTION GET_BALANCE (
		IN_ID IN NUMBER,
		OUT_SUBTOTAL OUT NUMBER,
		OUT_FEES_NAME OUT STR_ARRAY,
		OUT_FEES_DESCR OUT STR_ARRAY,
		OUT_FEES OUT NUM_ARRAY
	) RETURN NUMBER AS
		V_TOTAL NUMBER(6,2);
	BEGIN
		SELECT
		TOTAL, SUBTOTAL
		INTO
		V_TOTAL, OUT_SUBTOTAL
		FROM BALANCE
		WHERE ID = IN_ID;
		
		SELECT
		EX.NAME, EX.DESCRIPTION, EX.COST
		BULK COLLECT INTO
		OUT_FEES_NAME, OUT_FEES_DESCR, OUT_FEES
		FROM EXTRA_FEE EX JOIN BALANCE_EXTRA_FEE BEX
		ON BEX.EXTRA_FEE = EX.NAME
		WHERE BEX.BALANCE = IN_ID;
		
		RETURN V_TOTAL;
	END;
	
	FUNCTION GET_PAYMENTS (
		IN_ID IN NUMBER,
		OUT_PAID OUT DATE_ARRAY
	) RETURN NUM_ARRAY AS
		V_AMOUNTS NUM_ARRAY;
	BEGIN
		SELECT
		AMOUNT_PAID, DATE_PAID
		BULK COLLECT INTO
		V_AMOUNTS, OUT_PAID
		FROM PAYMENT
		WHERE INVOICE = IN_ID;
		
		RETURN V_AMOUNTS;
	END;
	
	PROCEDURE PAY (
		IN_ID IN NUMBER,
		IN_AMOUNT IN NUMBER
	) AS
		V_DUMMY_DARR DATE_ARRAY;
		V_DUMMY_NUM NUMBER(6,2);
		V_DUMMY_STARR STR_ARRAY;
		V_DUMMY_NARR NUM_ARRAY;
		V_PAYS NUM_ARRAY;
		
		V_I NUMBER(3,0);
		V_SUM NUMBER(6,2) := 0;
		V_BALANCE NUMBER(6,2);
	BEGIN
		/* GETTING ALL PREVIOUS PAYMENTS */
		V_PAYS := GET_PAYMENTS(
			IN_ID,
			V_DUMMY_DARR
		);
		
		/* SUMMING ALL PREVIOUS PAYMENTS */
		FOR V_I IN 1 .. V_PAYS.COUNT LOOP
			V_SUM := V_SUM + V_PAYS(V_I);
		END LOOP;
		
		/* GETTING THE CURRENT BALANCE */
		V_BALANCE := GET_BALANCE(
			IN_ID,
			V_DUMMY_NUM,
			V_DUMMY_STARR,
			V_DUMMY_STARR,
			V_DUMMY_NARR
		);
		
		/* INSERT ONLY IF THE BALANCE IS NOT ALREADY OVERPAID */
		IF V_SUM > V_BALANCE THEN
			INSERT INTO PAYMENT
			(INVOICE, DATE_PAID, AMOUNT_PAID)
			VALUES
			(IN_ID, SYSDATE(), IN_AMOUNT);
		ELSE
			RAISE ALREADY_OVERPAID;
		END IF;
	END;
	
	FUNCTION GET_REMINDERS (
		IN_CUSTOMER IN NUMBER,
		OUT_DATES OUT DATE_ARRAY,
		OUT_SEENS OUT STR_ARRAY
	) RETURN STR_ARRAY AS
		V_DETAILS STR_ARRAY;
	BEGIN
		SELECT
		DETAILS, EMISSION_DATE, SEEN
		BULK COLLECT INTO
		V_DETAILS, OUT_DATES, OUT_SEENS
		FROM REMINDER
		WHERE CUSTOMER = IN_CUSTOMER;
		
		RETURN V_DETAILS;
	END;
	
	/* ======================================== */
	/* ======================================== */
	/* ========== SERVICE MANAGEMENT ========== */
	/* ======================================== */
	/* ======================================== */
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
		OUT_DPMT OUT STR_ARRAY,
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
		SCHEDULED, DEPARTMENT, DETAILS
		BULK COLLECT INTO
		V_SCHEDULE_DATES, OUT_DPMT, OUT_DETAILS
		FROM REQUEST
		WHERE (CUSTOMER = IN_CUSTOMER) AND
		((APPOINTMENT IS NULL AND V_ACCEPTED = '0')
		OR (APPOINTMENT IS NOT NULL AND V_ACCEPTED = '1'));
		
		RETURN V_SCHEDULE_DATES;
	END;
	
END CUSTOMER_PCKG;
/
/* CREATE USER DEMO_CUSTOMER IDENTIFIED BY UZHaGAzgEd3Kp7LjpXi;
GRANT CREATE SESSION TO DEMO_CUSTOMER;
GRANT EXECUTE ON CUSTOMER_PCKG TO DEMO_CUSTOMER; */