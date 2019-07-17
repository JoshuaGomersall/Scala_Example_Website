# --- Sample dataset

# --- !Ups

insert into company (id,name) values (  1,'Valenciargo');
insert into company (id,name) values (  2,'M4g1yK Tr1cKz');
insert into company (id,name) values (  3,' ThisGuyOnceSaid');

insert into computer (id,name,introduced,discontinued,company_id) values (1,'Gateway LT3103U','2008-01-01',null,null);
insert into computer (id,name,introduced,discontinued,company_id) values (2,'iPhone 4S','2011-10-14',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values (3,'iPh9one 4S','2011-10-14',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values (4,'iPho4ne 4S','2011-10-14',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values (5,'iPhonDWAe 4S','2011-10-14',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values (6,'iPhonAWDWAeD 4S','2011-10-14',null,1);
insert into computer (id,name,introduced,discontinued,company_id) values (7,'iPhoDAWne 4S','2011-10-14',null,1);


# --- !Downs

delete from computer;
delete from company;
