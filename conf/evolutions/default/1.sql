# --- First database schema

# --- !Ups

set ignorecase true;

create table gamestatus (
                         id                        bigint not null,
                         name                      varchar(80) not null,
                         constraint pk_gamestatus primary key (id))
;

create table player (
                          id                        bigint not null,
                          name                      varchar(25) not null,
                          win                       bigint,
                          loss                      bigint,
                          gamestatus_id                bigint,
                          constraint pk_player primary key (id))
;

create sequence gametatus_seq start with 1000;

create sequence player_seq start with 1000;

alter table player add constraint fk_player_gamestatus_1 foreign key (gamestatus_id) references gamestatus (id) on delete restrict on update restrict;
create index ix_player_gamestatus_1 on player (gamestatus_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists gamestatus;

drop table if exists player;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists gametatus_seq;

drop sequence if exists player_seq;