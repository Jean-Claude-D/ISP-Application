CREATE OR REPLACE PACKAGE CUSTOMER_PCKG IS
	/* MAIN INFORMATION ABOUT AN INVOICE */
	TYPE INVOICE_RECORD IS RECORD (
		INTERNET_PACKAGE VARCHAR2(40),
		DUE_DATE DATE,
		CREATED_DATE DATE,
		BALANCE NUMBER(9,0)
	);
	
	/* MAIN INFORMATION ABOUT DAILY USAGE FOR A CUSTOMER */
	TYPE DAILY_USAGE_RECORD IS RECORD (
		UPLOAD_GB NUMBER(6,3),
		DOWNLOAD_GB NUMBER(6,3),
		DAY_USAGE DATE
	);
	
	/* MAIN INFORMATION ABOUT A CUSTOMER'S SERVICE REQUEST */
	TYPE REQUEST_RECORD IS RECORD (
		SCHEDULED DATE,
		DEPARTMENT VARCHAR2(20),
		DETAILS VARCHAR2(200)
	);
	
	/* ALLOWS FOR USER AUTHENTICATION */
	FUNCTION LOGIN (
		IN_CUSTOMER IN VARCHAR2,
		IN_PASSWORD IN CHAR
	) RETURN CUSTOMER.USERNAME%TYPE;
		
	/* SHOW A USER PACKAGE HE/SHE MAY UPGRADE TO */
	FUNCTION AVAILABLE_PACKAGES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN INTERNET_PACKAGE%ROWTYPE;
		
	DOWNGRADE_NOT_ALLOWED EXCEPTION;
	
	/* DOES THE UPGRADE OF PACKAGES */
	PROCEDURE UPGRADE_PACKAGE (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PACKAGE IN VARCHAR2
	);
	
	/* PROVIDES THE ABILITY TO SEARCH THROUGH A CUSTOMER'S DAILY USAGE STARTING FROM MOST RECENT ONES */
	CURSOR RECENT_DAILY_USAGE (
		IN_CUSTOMER IN VARCHAR2
	) RETURN DAILY_USAGE_RECORD IS
		SELECT UPLOAD_GB, DOWNLOAD_GB, DAY_USAGE
		FROM DAILY_USAGE
		WHERE IN_CUSTOMER = CUSTOMER
		ORDER BY DAY_USAGE;
		
	/* PROVIDES THE ABILITY TO SEARCH THROUGH A CUSTOMER'S INVOICE STARTING FROM MOST RECENT ONES */
	CURSOR RECENT_INVOICE_FOR_CUSTOMER (
		IN_CUSTOMER IN VARCHAR2
	) RETURN INVOICE_RECORD IS
		SELECT INTERNET_PACKAGE, DUE_DATE,
		CREATED_DATE, BALANCE
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
		CONNECT BY PRIOR PREVIOUS = ID
		ORDER SIBLINGS BY CREATED_DATE;
		
	/* ALLOWS FOR CUSTOMER TO MAKE A REQUEST OF SERVICE */
	PROCEDURE REQUEST_SERVICE (
		IN_CUSTOMER IN VARCHAR2,
		IN_DATE IN DATE,
		IN_DEPARTMENT IN VARCHAR2,
		IN_DETAILS IN VARCHAR2
	);
	
	/* ALLOWS FOR CUSTOMER TO SEE WHICH OF THEIR REQUESTED APPOINTMENT WAS CONFIRMED OR NOT */
	CURSOR PENDING_REQUEST (
		IN_CUSTOMER IN VARCHAR2
	) RETURN REQUEST_RECORD IS
		SELECT SCHEDULED, DEPARTMENT, DETAILS
		FROM REQUEST
		WHERE (CUSTOMER = IN_CUSTOMER) AND
		(APPOINTMENT IS NULL);
		
	CURSOR ACCEPTED_APPOINTMENT (
		IN_CUSTOMER IN VARCHAR2
	) RETURN REQUEST_RECORD IS
		SELECT SCHEDULED, DEPARTMENT, DETAILS
		FROM REQUEST
		WHERE (CUSTOMER = IN_CUSTOMER) AND
		(APPOINTMENT IS NOT NULL);
	INVALID_LOGIN EXCEPTION;
	
	PROCEDURE CHANGE_PASSWORD (
		IN_CUSTOMER IN VARCHAR2,
		IN_NEW_PASSWORD IN CHAR,
		IN_NEW_SALT IN CHAR,
		IN_OLD_PASSWORD IN CHAR /* CONFIRMATION */
	);
END CUSTOMER_PCKG;
/
CREATE OR REPLACE PACKAGE BODY CUSTOMER_PCKG IS
	FUNCTION LOGIN (
		IN_CUSTOMER IN VARCHAR2,
		IN_PASSWORD IN CHAR
	) RETURN CUSTOMER.USERNAME%TYPE IS
		V_USERNAME CUSTOMER.USERNAME%TYPE;
	BEGIN
		SELECT C.USERNAME INTO V_USERNAME
		FROM CUSTOMER C
		WHERE (C.ACTIVE = '1') AND (C.USERNAME = IN_CUSTOMER) AND (IN_PASSWORD = C.PASSWORD);
		
		RETURN V_USERNAME;
	END;
	
	FUNCTION AVAILABLE_PACKAGES (
		IN_CUSTOMER IN VARCHAR2
	) RETURN INTERNET_PACKAGE%ROWTYPE;
	SELECT *
		FROM INTERNET_PACKAGE
		WHERE MONTHLY_PRICE > (
			SELECT IP.MONTHLY_PRICE
			FROM INTERNET_PACKAGE IP JOIN CUSTOMER C
			ON IP.NAME = C.INTERNET_PACKAGE
			WHERE C.USERNAME = IN_CUSTOMER
		);
	
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
		
		IF V_OLD_PASSWORD = IN_OLD_PASSWORD THEN
			UPDATE CUSTOMER
			SET PASSWORD = IN_NEW_PASSWORD
			WHERE USERNAME = IN_CUSTOMER;
		ELSE
			RAISE INVALID_LOGIN;
		END IF;
	END;
	
END CUSTOMER_PCKG;
/