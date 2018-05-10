create or replace package salesAppPack as 
   procedure registerNewCustomerPlusSetUp(userNa varchar2,pass raw, saltPass varchar2,fName varchar2,lName varchar2,phoneNum varchar2,email varchar2,address varchar2,internetPackage varchar2);
   procedure assignAppointment(repName varchar2, customerName varchar2);
   procedure modifyUserPackage(userId varchar2, desiredPackage varchar2);
    procedure insertRecruitingCall(repId varchar2,startTime date, newCustomer char); 
    procedure updatePasswordRep(repId varchar2,hashRep raw,salRep varchar2);
end salesAppPack;
/
create or replace package body salesAppPack as 
    procedure registerNewCustomerPlusSetUp(userNa varchar2,pass raw, saltPass varchar2 ,fName varchar2, lName varchar2,phoneNum varchar2,email varchar2,address varchar2,internetPackage varchar2) is
    creationDate date := sysdate;
    maxDate date;
    begin 
  
     insert into customer values(userNa,pass,saltPass,fName,lName,phoneNum,email,address,internetPackage, creationDate, '1');
    end;

    procedure assignAppointment(repName varchar2, customerName varchar2)
    is
     currentDate date := sysdate;
     maxDate date;
     appointmentId number := customerRequest.NEXTVAL;
     depart varchar2(30) := 'TECH SUPPORT';
     reportString varchar2(200) := 'Installation of the Internet Service';
     
    begin 
        
        Select max(SCHEDULED) into maxDate from REQUEST;
      if(maxDate is null) then
         maxDate := currentDate;
      else
       maxDate:= maxDate + 1;
      end if;
        insert into appointment values(appointmentId, repName,reportString);
        insert into  request values (appointmentId, customerName,maxDate,depart, reportString);
    end;
    
    procedure modifyUserPackage(userId varchar2, desiredPackage varchar2)
    is
    begin
        update customer set INTERNET_PACKAGE = desiredPackage where username = userId;

    end;
     procedure insertRecruitingCall(repId varchar2,startTime date, newCustomer char)
    is 
    currentCall number := customerCallId.NEXTVAL;
    begin 
        insert into customer_call values (currentCall, repId, NULL ,startTime);
        insert into sales_call values(currentCall, newCustomer);

    end; 
    
    procedure updatePasswordRep(repId varchar2,hashRep raw,salRep varchar2)
    is
    begin
      update representative set password = hashRep , salt = salRep where username = repId;
    end;
    
   
end salesAppPack;
/

CREATE OR REPLACE TRIGGER updateUserInvoice
  BEFORE UPDATE OF internet_package ON customer
  FOR EACH ROW
DECLARE

 currentDate date := sysdate; 
 DueDate date;  
 cash number(7,2);
 pastCash number(7,2);
 totalDue number(7,2);
 totalBalance number(7,2);
 subBalance number(7,2);
 balanceId number(9,0);
BEGIN
      
      select MIN(due_date) into DueDate from invoice where customer = :OLD.username;
      DBMS_OUTPUT.PUT_LINE(DueDate);
      Select MONTHLY_PRICE into cash from internet_package where name = :NEW.INTERNET_PACKAGE;
      DBMS_OUTPUT.PUT_LINE(cash);
      Select MONTHLY_PRICE into pastCash from internet_package where name = :OLD.INTERNET_PACKAGE;
      DBMS_OUTPUT.PUT_LINE(pastCash);
      select b.subtotal,b.total,b.id into subBalance ,totalBalance  , balanceId from invoice i
      join balance b on b.id = i.balance 
      where customer = :NEW.username;
      DBMS_OUTPUT.PUT_LINE(totalBalance);
      subBalance := subBalance - pastCash;
      subBalance := subBalance + cash;
      totalBalance := totalBalance - pastCash;
      totalBalance := totalBalance + cash;
      if(DueDate > currentDate)then
         DBMS_OUTPUT.PUT_LINE('hELLO WORLD');
        update balance set subtotal =  subBalance , total = totalBalance where id = balanceId;
     end if;

  
  
  
END;

CREATE SEQUENCE customerCallId
MINVALUE 0001
MAXVALUE 9999
START WITH 0001
INCREMENT BY 1
cache 10;

CREATE SEQUENCE customerRequest
MINVALUE 0001
MAXVALUE 9999
START WITH 0001
INCREMENT BY 1
cache 10;






