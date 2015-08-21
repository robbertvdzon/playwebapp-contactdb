# --- !Ups

create table contact (
  id                        bigint not null,
  uuid                      varchar(255),
  user_uuid                 varchar(255),
  name                      varchar(255),
  email                     varchar(255),
  constraint pk_contact primary key (id))
;

create sequence contact_seq;

INSERT INTO contact(id, uuid, user_uuid, name, email) VALUES (contact_seq.nextVal ,'0001','0001234', 'robberts-contact1','email1');
INSERT INTO contact(id, uuid, user_uuid, name, email) VALUES (contact_seq.nextVal ,'0002','0001234', 'robberts-contact2','email2');
INSERT INTO contact(id, uuid, user_uuid, name, email) VALUES (contact_seq.nextVal ,'0003','0001234', 'robberts-contact3','email3');
INSERT INTO contact(id, uuid, user_uuid, name, email) VALUES (contact_seq.nextVal ,'0004','0001235', 'jan-contact1','email4');
INSERT INTO contact(id, uuid, user_uuid, name, email) VALUES (contact_seq.nextVal ,'0005','0001236', 'jan-contact1','email5');
INSERT INTO contact(id, uuid, user_uuid, name, email) VALUES (contact_seq.nextVal ,'0006','0001237', 'kees-contact1','email6');

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists contact;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists contact_seq;

