# --- !Ups
create table user (
  id                        bigint not null,
  name                      varchar(255),
  uuid                      varchar(255),
  constraint pk_user primary key (id))
;
create sequence user_seq;
INSERT INTO user(id, name, uuid) VALUES (user_seq.nextVal, 'Jeff','0001234');
INSERT INTO user(id, name, uuid) VALUES (user_seq.nextVal, 'Jack','0001235');
INSERT INTO user(id, name, uuid) VALUES (user_seq.nextVal, 'John','0001236');
# --- !Downs
SET REFERENTIAL_INTEGRITY FALSE;
drop table if exists user;
SET REFERENTIAL_INTEGRITY TRUE;
drop sequence if exists user_seq;

