# --- Sample dataset

# --- !Ups

insert into company (id,name) values (  1,'Waiting');
insert into company (id,name) values (  2,'In Progress');
insert into company (id,name) values (  3,'Done');

insert into computer (id,name,win,loss,company_id) values (1,'John',3,0,1);
insert into computer (id,name,win,loss,company_id) values (2,'Bruno',33,5,3);
insert into computer (id,name,win,loss,company_id) values (3,'Valentin',4,1,3);
insert into computer (id,name,win,loss,company_id) values (109,'Luke',10,-1,3);
insert into computer (id,name,win,loss,company_id) values (49,'Joe Simmons',8,21,2);
insert into computer (id,name,win,loss,company_id) values (4,'New Guy',0,0,null);

# --- !Drops
delete from computer;
delete from company;