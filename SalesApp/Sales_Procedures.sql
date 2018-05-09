create or replace package salesAppPack as 
   procedure registerNewCustomerPlusSetUp(userNa varchar2,pass raw, saltPass varchar2,fName varchar2,lName varchar2,phoneNum varchar2,email varchar2,address varchar2,internetPackage varchar2);
   procedure assignAppointment(appointmentId number,repName varchar2, reportString varchar2);
   procedure modifyUserPackage(userId varchar2, desiredPackage varchar2);
    procedure insertRecruitingCall(repId varchar2,startTime date, newCustomer char); 
    procedure updatePasswordRep(repId varchar2,hashRep raw,salRep varchar2);
end salesAppPack;
/
create or replace package body salesAppPack as 
    procedure registerNewCustomerPlusSetUp(userNa varchar2,pass raw, saltPass varchar2 ,fName varchar2, lName varchar2,phoneNum varchar2,email varchar2,address varchar2,internetPackage varchar2) is
    creationDate date := sysdate;
    begin 
  
     insert into customer values(userNa,pass,saltPass,fName,lName,phoneNum,email,address,internetPackage, creationDate, '1');
   
    end;

    procedure assignAppointment(appointmentId number,repName varchar2, reportString varchar2)
    is
    begin 
        insert into appointment values(appointmentId, repName,reportString);
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
CREATE SEQUENCE customerCallId
MINVALUE 0001
MAXVALUE 9999
START WITH 0001
INCREMENT BY 1
cache 10;






