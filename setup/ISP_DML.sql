INSERT INTO INTERNET_PACKAGE VALUES('HIGH SPEED', 'High Speed DSL 10 250 GB',
                                    10, 30, 250, 17, 50.65, 2);
INSERT INTO INTERNET_PACKAGE VALUES('HYBRID GIGA', 'Hybrid Fiber Giga Internet',
                                    10, 60, 250, 22, 62.65, 2);
INSERT INTO INTERNET_PACKAGE VALUES('HYBRID 15', 'Hybrid Fiber 15 Internet',
                                    10, 15, 50, 13, 34.65, 2);
INSERT INTO INTERNET_PACKAGE VALUES('UNLIMITED', 'High Speed DSL 25 Unlimited',
                                    10, 45, 300, 20, 58.65, 2);
                                    
/******************************************/

INSERT INTO EXTRA_FEATURE VALUES('FREE_MODEM', 'FREE MODEM ON PURCHASING THE HYBRID GIGA PLAN',
                                20, 0, 0);
INSERT INTO EXTRA_FEATURE VALUES('RENTAL_ROUTER', 'RENTING A MODEM AT $2/MOUNTH ON PURCHASING THE HYGH SPEED PLAN',
                                20, 3, 0);
INSERT INTO EXTRA_FEATURE VALUES('50%MODEM', '50% OFF ON MODEM ON PURCHASING THE UNLIMITED PLAN',
                                20, 0, 0);
                                
/******************************************/

INSERT INTO PACK_FEAT VALUES ('HYBRID GIGA', 'FREE_MODEM');
INSERT INTO PACK_FEAT VALUES('HIGH SPEED', 'RENTAL_ROUTER');
INSERT INTO PACK_FEAT VALUES('UNLIMITED', '50%MODEM');

/****************************************/

INSERT INTO CUSTOMER VALUES ('LUISWAGNER',
                             'Lui$123',
                             '123',
                             'LUIS',
                             'WAGNER',
                             '5141234567',
                             'lwagner@hotmail.com',
                             '2420 HARVARD St.',
                             'HIGH SPEED',
                             '3-JAN-18',
                             '1');
                             

INSERT INTO CUSTOMER VALUES ('ELLEN',
                             'ellen@123',
                             '123',
                             'ELLEN',
                             'YOUNG',
                             '5141238967',
                             'ellenyoung@hotmail.com',
                             'App# 203, 1100 ROSEMONT',
                             'HYBRID GIGA',
                             '12-JAN-18',
                             '1');
                             
INSERT INTO CUSTOMER VALUES ('S.ELLIS',
                             'silVelli$',
                             '456',
                             'SILVIA',
                             'ELLIS',
                             '4381238967',
                             'silviellis@yahoo.com',
                             '3422 DENIS BOLV.',
                             'HYBRID 15',
                             '20-FEB-18',
                             '1');
                             
INSERT INTO CUSTOMER VALUES ('CONRAD',
                             'gray$%con',
                             '567',
                             'CONRAD',
                             'GRAY',
                             '4381211967',
                             'c.grayg@gmail.com',
                             '903 PEEL St.',
                             'UNLIMITED',
                             '4-MAR-18',
                             '1');
                             
INSERT INTO CUSTOMER VALUES ('TBROWN',
                             'tomas123',
                             '567',
                             'TOMAS',
                             'BROWN',
                             '4381781967',
                             'tbrown@gmail.com',
                             '657 ALFRED St',
                             'HYBRID GIGA',
                             '15-MAR-18',
                             '1');
                             
/******************************************************************************/

INSERT INTO PAYING_INFO VALUES('LUISWAGNER', 'CREDIT CARD', '1230987654', '123');
INSERT INTO PAYING_INFO VALUES('ELLEN', 'CREDIT CARD', '1234567098', '123');
INSERT INTO PAYING_INFO VALUES('S.ELLIS', 'PAYPAL', '1232234568', '123');
INSERT INTO PAYING_INFO VALUES('CONRAD', 'CRYPTOCURRENCY', '1230123432', '123');
INSERT INTO PAYING_INFO VALUES('TBROWN', 'PAYPAL', '1237650094', '123');

/************************************************************************/

INSERT INTO DAILY_USAGE VALUES('LUISWAGNER', 6, 25, '9-FEB-18');
INSERT INTO DAILY_USAGE VALUES('ELLEN', 10, 12, '30-JAN-18');
INSERT INTO DAILY_USAGE VALUES('S.ELLIS', 0.5, 4, '18-MAR-18');
INSERT INTO DAILY_USAGE VALUES('CONRAD', 14, 18, '2-APR-18');
INSERT INTO DAILY_USAGE VALUES('TBROWN', 2, 16, '29-MAR-18');

/*****************************************************************************************/
INSERT INTO BALANCE VALUES(1001,103.65, 119.20);
INSERT INTO BALANCE VALUES(1002,162.65, 187.05);
INSERT INTO BALANCE VALUES(1003,93.65, 107.70);
INSERT INTO BALANCE VALUES(1004,108.65, 124.95);
INSERT INTO BALANCE VALUES(1005,112.65, 129.55);
INSERT INTO BALANCE VALUES(2001,53.65, 61.7);
INSERT INTO BALANCE VALUES(2002,122.65, 141.05);
INSERT INTO BALANCE VALUES(2003,34.65, 39.85);
INSERT INTO BALANCE VALUES(2004,58.65, 67.45);

/**********************************************************************/

INSERT INTO INVOICE VALUES(1001, null, 'LUISWAGNER','HIGH SPEED', '24-FEB-18','3-FEB-18',1001);
INSERT INTO INVOICE VALUES(1002, null, 'ELLEN','HYBRID GIGA', '5-MAR-18','12-FEB-18', 1002);
INSERT INTO INVOICE VALUES(1003, null, 'S.ELLIS','HYBRID 15', '10-APR-18','20-MAR-18', 1003);
INSERT INTO INVOICE VALUES(1004, null, 'CONRAD','UNLIMITED', '25-APR-18', '4-APR-18', 1004);
INSERT INTO INVOICE VALUES(1005, null, 'TBROWN','HYBRID GIGA', '6-MAY-18', '15-APR-18', 1005);
INSERT INTO INVOICE VALUES(2001, 1001, 'LUISWAGNER','HIGH SPEED', '24-MAR-18','3-MAR-18', 2001);
INSERT INTO INVOICE VALUES(2002, 1002, 'ELLEN','HYBRID GIGA', '2-APR-18','12-MAR-18', 2002);
INSERT INTO INVOICE VALUES(2003, 1003, 'S.ELLIS','HYBRID 15', '10-MAY-18','20-APR-18', 2003);
INSERT INTO INVOICE VALUES(2004, 1004, 'CONRAD','UNLIMITED', '25-MAY-18', '4-MAY-18', 2004);

/***********************************************************************/

INSERT INTO EXTRA_FEE VALUES('INSTALATION', 'INSTALATION FEE IS ADDED FOR NEW CUSTOMERS ON THEIR FIRST INVOCE', 50);
INSERT INTO EXTRA_FEE VALUES('REPAIR', 'FIXING A PROPLEM AT CUSTOMER PLACE', 60);

/***********************************************/

INSERT INTO PAYMENT VALUES(1001, '15-FEB-18', 50);
INSERT INTO PAYMENT VALUES(1002, '23-FEB-18', 68);
INSERT INTO PAYMENT VALUES(1003, '19-MAR-18', 44);
INSERT INTO PAYMENT VALUES(1004, '6-APR-18', 63);
INSERT INTO PAYMENT VALUES(2001, '10-MAR-18', 70);

/***************************************************************/

INSERT INTO BALANCE_EXTRA_FEE VALUES(1001,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(1002,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(1003,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(1004,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(1005,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(2002,'REPAIR');

/*********************************************************************/

INSERT INTO REPRESENTATIVE VALUES('DANIEL', 'DAN123','123', 'DANIEL', 'GILL', '5141234567', 'dan1989@yahoo.com', '1023, GREEN St.','SALE');
INSERT INTO REPRESENTATIVE VALUES('MARTA', 'mart!23','123', 'MARTA', 'ANDERSON', '4381234567', 'marta.a@yahoo.com', '623, PARK St.','SALE');
INSERT INTO REPRESENTATIVE VALUES('ALEX', 'ALEX123', '123','ALEX', 'SMIT', '4381987567', 'a.smit@yahoo.com', '453, VIOLET St.','TECH SUPPORT');
INSERT INTO REPRESENTATIVE VALUES('SONYA', 'SONYA123', '123','SONYA', 'SANDERS', '5141987567','sandra123@yahoo.com', '123, ROGERS St.','TECHNICAL');
INSERT INTO REPRESENTATIVE VALUES('TARA', 'TARA123', '123', 'TARA', 'COLE', '5141234123', 'tara65@yahoo.com', '10234, SHERBROOK St.','BILLING');
INSERT INTO REPRESENTATIVE VALUES('JOE', 'JOE123', '123', 'JOE', 'MAIRE', '5140984123', 'jmaire@yahoo.com', '104, TULIP St.','BILLING');

/*****************************************************************************/

INSERT INTO CUSTOMER_CALL VALUES(2001, 'DANIEL', 'ELLEN', '23-APR-18');
INSERT INTO CUSTOMER_CALL VALUES(2002, 'SONYA', 'CONRAD', '23-MAR-18');
INSERT INTO CUSTOMER_CALL VALUES(2003, 'TARA', 'S.ELLIS', '2-APR-18');
INSERT INTO CUSTOMER_CALL VALUES(2004, 'DANIEL', 'LUISWAGNER', '3-JAN-18');
INSERT INTO CUSTOMER_CALL VALUES(2005, 'JOE', 'TBROWN', '15-MAR-18');

/***************************************************************************/
INSERT INTO SALES_CALL VALUES(2001, 0);
INSERT INTO SALES_CALL VALUES(2004, 1);

/***********************************************************************/
INSERT INTO TECH_CALL VALUES(2002, 'WEAK SIGNAL', 'CHANGE THE ROUTER');

/********************************************************************/
INSERT INTO BILLING_CALL VALUES (2003, 'QUESTION ABOUT EXTRAA CHRGES IN THE LAST INVOICE' );
INSERT  INTO BILLING_CALL VALUES(2005, 'EXTRA CHARGES');
/***********************************************************************/

INSERT INTO APPOINTMENT VALUES (101, 'SONYA', 'WEAK SIGNAL, CHECK SIGNAL STRENGTH FROM THE CLIENT SIDE');
INSERT INTO APPOINTMENT VALUES (102, 'TARA', 'EXPLAINING THE REASON OF OVERCHARGE');
INSERT INTO APPOINTMENT VALUES (103, 'DANIEL', 'ASK ABOUT PROMOTIN FOR UPDATING PLAN');
INSERT INTO APPOINTMENT VALUES (104, 'SONYA', 'INSTALL AN EXTRA ROUTER');

/**************************************************************************/

INSERT INTO REQUEST VALUES (101, 'S.ELLIS', '12-MAR-18', 'TECHNICAL', NULL );
INSERT INTO REQUEST VALUES (102, 'TBROWN', '17-APR-18', 'BILING', NULL );
INSERT INTO REQUEST VALUES (103, 'ELLEN', '8-MAR-18', 'SALE', NULL );
INSERT INTO REQUEST VALUES (104, 'CONRAD', '5-APR-18', 'TECH SUPPORT', NULL );

/**************************************************************************/

INSERT INTO REFUND VALUES ('TARA', 1003, 10, 'DOUBLE CHARGING FOR OVERAGE');
INSERT INTO REFUND VALUES ('TARA', 1004, 5, 'INCORRECT FIX MOUNTHLY CHARGE');
INSERT INTO REFUND VALUES ('JOE', 1005, 20, 'DOUBLE CHARGING FIX MONTHLY CHARGES');

/*********************************************************************************/

INSERT INTO INVOICE_MODIFICATION VALUES(1003, 'TARA', '25-MAR-18','20-MAR-18');
INSERT INTO INVOICE_MODIFICATION VALUES(1004, 'TARA', '7-APR-18','4-APR-18');
INSERT INTO INVOICE_MODIFICATION VALUES(1005, 'JOE', '16-APR-18','15-APR-18');

/**************************************************************************/
INSERT INTO REMINDER VALUES ('ELLEN', 'TARA', '12-FEB-18', 'YOUR NEW INVOICE IS READY', '0');
INSERT INTO REMINDER VALUES ('CONRAD', 'TARA', '17-APR-18', 'YOUR INVOICE DUE DATE IS IN ONE WEEK ON 25-APR-2018', '1');
INSERT INTO REMINDER VALUES ('TBROWN', 'JOE', '15-APR-18', 'YOUR NEW INVOICE IS READY','1');
INSERT INTO REMINDER VALUES ('S.ELLIS', 'JOE', '3-APR-18', 'YOUR INVOICE DUE DATE IS IN ONE WEEK ON 10-APR-2018', '0');
INSERT INTO REMINDER VALUES ('LUISWAGNER', 'JOE', '17-FEB-18','YOUR INVOCE DUE DATE IS IN ONE WEEK ON 24-FEB-18', '1');