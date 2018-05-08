create or replace procedure registerNewCustomerPlusSetUp(userNa varchar2,pass varchar2,fName varchar2, lName varchar2,phoneNum varchar2,email varchar2,address varchar2, saltPass varchar2,internetPackage varchar2, ActiveUser char) is
  creationDate date := sysdate;
begin 
  
  insert into customer values(userNa,pass,fName,lName,phoneNum,email,address, creationDate,saltPass,internetPackage, ActiveUser);
   
end;

create or replace procedure assignAppointment(appointmentId number,repName varchar2, reportString varchar2)
as
begin 
  insert into appointment values(appointmentId, repName,reportString);
end;

create or replace procedure modifyUserPackage(userId varchar2, desiredPackage varchar2)
as 
begin
    update customer set INTERNET_PACKAGE = desiredPackage where username = userId;

end;


create or replace procedure insertRecruitingCall