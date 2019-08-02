# --- Sample dataset

# --- !Ups

insert into gamestatus (id,name) values (  1,'Waiting');
insert into gamestatus (id,name) values (  2,'In Progress');
insert into gamestatus (id,name) values (  3,'Done');
insert into gamestatus (id,name) values (  4,'Tournament Starting Game');
insert into gamestatus (id,name) values (  5,'Tournament Game');
insert into gamestatus (id,name) values (  6,'Tournament Semi-Final Game');
insert into gamestatus (id,name) values (  7,'Tournament Finals Game');

insert into player (id,name,win,loss,gamestatus_id) values (1,'John',3,0,1);
insert into player (id,name,win,loss,gamestatus_id) values (2,'Bruno',33,5,3);
insert into player (id,name,win,loss,gamestatus_id) values (3,'Valentin',4,1,3);
insert into player (id,name,win,loss,gamestatus_id) values (109,'Luke',10,-1,3);
insert into player (id,name,win,loss,gamestatus_id) values (49,'Joe Simmons',8,21,2);
insert into player (id,name,win,loss,gamestatus_id) values (4,'New Guy',0,0,null);

# --- !Drops
delete from player;
delete from gamestatus;