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

INSERT INTO CUSTOMER VALUES ('Mika',
                    'A946BD445AADC8F248081E78CFE3C6E9AB7258F732728FDEDC2AF3859DC6B511'
                    ,'sisnfaak43cloie614d8bh9btsm4'
                    ,'Mika','Simona'
                    ,'450-000-0000'
                    ,'s@gmail.com'
                    ,'7845-Niagara-Street'
                    ,'HYBRID 15'
                    ,'10-MAY-18','1');
INSERT INTO CUSTOMER VALUES ('sammyCh'
                            ,'D7ECF8293FCDDEF363E7FEB41F948794248F22226FF69D4BB774ACD37DC71399'
                            ,'d0j9mqebgsf0lc7rkn67br9dpqol'
                            ,'xd'
                            ,'xd'
                            ,'450-445-0090'
                            ,'s@gmail.com'
                            ,'7845-Niagar-Street'
                            ,'HYBRID 15','09-MAY-18','1');
                             
INSERT INTO CUSTOMER VALUES ('french'
                            ,'AD9C61BFEB52BAC8BD39D25A175BEC361F6EE9A01F8271DFDF83309B77CE54B9'
                            ,'6dv9tghgualgbr8b5ovt7bod1401'
                            ,'adffsgfgfd'
                            ,'dfassdadfa',
                            '450-000-0000',	
                            's@gmail.com',
                            '7845-niagara-Street'
                            ,'HYBRID 15'
                            ,'10-MAY-18'
                            ,'1');
                             
INSERT INTO CUSTOMER VALUES ('hi',	
                            'F0BDBDEAC46384F6B2B1EBC544B5031187E763A707D384753965A895DB165D80',	
                            '887l37re27sm7vqa4u0db0h1u6ab'
                            ,'adffsgfgfd'
                            ,'dfassdadfa',
                            '450-000-0000',	
                            's@gmail.com',
                            '7845-niagara-Street'
                            ,'HYBRID 15'
                            ,'10-MAY-18'
                            ,'1');
                             
/******************************************************************************/

INSERT INTO PAYING_INFO VALUES('hi', 'CREDIT CARD', '1230987654');
INSERT INTO PAYING_INFO VALUES('french', 'CREDIT CARD', '1234567098');
INSERT INTO PAYING_INFO VALUES('sammyCh', 'PAYPAL', '1232234568');
INSERT INTO PAYING_INFO VALUES('Mika', 'CRYPTOCURRENCY', '1230123432');

/************************************************************************/

INSERT INTO DAILY_USAGE VALUES('hi', 6, 25, '9-FEB-18');
INSERT INTO DAILY_USAGE VALUES('french', 10, 12, '30-JAN-18');
INSERT INTO DAILY_USAGE VALUES('sammyCh', 0.5, 4, '18-MAR-18');
INSERT INTO DAILY_USAGE VALUES('Mika', 14, 18, '2-APR-18');

/*****************************************************************************************/
INSERT INTO BALANCE VALUES(1001,103.65, 119.20);
INSERT INTO BALANCE VALUES(1002,53.65, 61.7);
INSERT INTO BALANCE VALUES(1003,34.65, 39.85);
INSERT INTO BALANCE VALUES(1004,58.65, 67.45);

/**********************************************************************/
INSERT INTO EXTRA_FEE VALUES('INSTALATION', 'INSTALATION FEE IS ADDED FOR NEW CUSTOMERS ON THEIR FIRST INVOCE', 50);
INSERT INTO EXTRA_FEE VALUES('REPAIR', 'FIXING A PROPLEM AT CUSTOMER PLACE', 60);

/***********************************************/
INSERT INTO INVOICE VALUES(1001, null, 'hi','HYBRID 15', '24-MAY-18','3-MAY-18',1002);
INSERT INTO INVOICE VALUES(1002, null, 'french','UNLIMITED', '5-MAR-18','12-FEB-18', 1001);
INSERT INTO INVOICE VALUES(1003, null, 'sammyCh','HYBRID 15', '10-APR-18','20-MAR-18', 1003);
INSERT INTO INVOICE VALUES(1004, null, 'Mika','HYBRID 15', '25-APR-18', '4-APR-18', 1004);

/***********************************************************************/
INSERT INTO PAYMENT VALUES(1001, '15-FEB-18', 50);
INSERT INTO PAYMENT VALUES(1002, '23-FEB-18', 68);
INSERT INTO PAYMENT VALUES(1003, '19-MAR-18', 44);
INSERT INTO PAYMENT VALUES(1004, '6-APR-18', 63);
/***************************************************************/

INSERT INTO BALANCE_EXTRA_FEE VALUES(1001,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(1002,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(1003,'INSTALATION');
INSERT INTO BALANCE_EXTRA_FEE VALUES(1004,'INSTALATION');

/*********************************************************************/

INSERT INTO REPRESENTATIVE VALUES('yanik', '8671D02CBEE3EF55780B3491C324AB5917B4D3F1DA13658705862EFA078A8E3A','9neg0q4bdj0hda25ji5p6t0t92gp', '450-445-9898', 'yanik@sql.com', '1191 Street HelloWorld', 'TECHNICAL');
INSERT INTO REPRESENTATIVE VALUES('mohammed', '818FB6DD20F68F506285669211C688D6CD47AE576625FFC29C74D9D2B19962CC','ujacbci0ld0vsc8qaj4ktd7tde7e', '450-445-9898', 'mohammed@sql.com', '1191 Street HelloWorld', 'SALE');
INSERT INTO REPRESENTATIVE VALUES('alpha', '9A90EC4411BA94B76A41AC5323336185F7F696120D497FA0502C6E89A9165C3F','99apaff4blatsbjmpm4ui99ncj23', '450-445-9898', 'alpha@sql.com', '1191 Street HelloWorld', 'BILLING');
INSERT INTO REPRESENTATIVE VALUES('hello', 'A23EB8942D59D1BF8F45FE875A978AF92FBCD2D142C6C8F284805248F7EB7EAF','tka50ocv2e9mrdrntkj1l06bpott', '450-445-9898', 'hello@sql.com', '1191 Street HelloWorld', 'TECH SUPPORT');


/*****************************************************************************/

INSERT INTO CUSTOMER_CALL VALUES(2001, 'yanik', 'sammyCh', '23-APR-18');
INSERT INTO CUSTOMER_CALL VALUES(2002, 'mohammed', 'Mika', '23-MAR-18');
INSERT INTO CUSTOMER_CALL VALUES(2003, 'alpha', 'french', '2-APR-18');
INSERT INTO CUSTOMER_CALL VALUES(2004, 'hello', 'hi', '3-JAN-18');

/***************************************************************************/
INSERT INTO SALES_CALL VALUES(2001, 0);
INSERT INTO SALES_CALL VALUES(2004, 1);

/***********************************************************************/
INSERT INTO TECH_CALL VALUES(2001, 'WEAK SIGNAL', 'CHANGE THE ROUTER');

/********************************************************************/
INSERT INTO BILLING_CALL VALUES (2003, 'QUESTION ABOUT EXTRAA CHRGES IN THE LAST INVOICE' );
INSERT  INTO BILLING_CALL VALUES(2004, 'EXTRA CHARGES');
/***********************************************************************/

INSERT INTO APPOINTMENT VALUES (101, 'yanik', 'WEAK SIGNAL, CHECK SIGNAL STRENGTH FROM THE CLIENT SIDE');
INSERT INTO APPOINTMENT VALUES (102, 'alpha', 'EXPLAINING THE REASON OF OVERCHARGE');
INSERT INTO APPOINTMENT VALUES (103, 'mohammed', 'ASK ABOUT PROMOTIN FOR UPDATING PLAN');
INSERT INTO APPOINTMENT VALUES (104, 'hello', 'INSTALL AN EXTRA ROUTER');

/**************************************************************************/

INSERT INTO REQUEST VALUES (101, 'french', '12-MAR-18', 'TECHNICAL', NULL );
INSERT INTO REQUEST VALUES (102, 'Mika', '17-APR-18', 'BILING', NULL );
INSERT INTO REQUEST VALUES (103, 'sammyCh', '8-MAR-18', 'SALE', NULL );
INSERT INTO REQUEST VALUES (104, 'hi', '5-APR-18', 'TECH SUPPORT', NULL );

/**************************************************************************/

INSERT INTO REFUND VALUES ('mohammed', 1001, 10, 'DOUBLE CHARGING FOR OVERAGE');
INSERT INTO REFUND VALUES ('yanik', 1002, 5, 'INCORRECT FIX MOUNTHLY CHARGE');
INSERT INTO REFUND VALUES ('alpha', 1003, 20, 'DOUBLE CHARGING FIX MONTHLY CHARGES');

/*********************************************************************************/

INSERT INTO INVOICE_MODIFICATION VALUES(1001, 'mohammed', '25-MAR-18','20-MAR-18');
INSERT INTO INVOICE_MODIFICATION VALUES(1002, 'yanik', '7-APR-18','4-APR-18');
INSERT INTO INVOICE_MODIFICATION VALUES(1003, 'alpha', '16-APR-18','15-APR-18');

/**************************************************************************/
INSERT INTO REMINDER VALUES ('Mika', 'mohammed', '12-FEB-18', 'YOUR NEW INVOICE IS READY', '0');
INSERT INTO REMINDER VALUES ('hi', 'hello', '17-APR-18', 'YOUR INVOICE DUE DATE IS IN ONE WEEK ON 25-APR-2018', '1');
INSERT INTO REMINDER VALUES ('sammyCh', 'alpha', '15-APR-18', 'YOUR NEW INVOICE IS READY','1');
INSERT INTO REMINDER VALUES ('french', 'yanik', '3-APR-18', 'YOUR INVOICE DUE DATE IS IN ONE WEEK ON 10-APR-2018', '0');