# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table properties (
  id                            bigint auto_increment not null,
  name_of_building              varchar(255),
  street_name                   varchar(255),
  street_number                 varchar(255),
  postal_code                   varchar(255),
  city                          varchar(255),
  country                       varchar(255),
  description                   varchar(255),
  coordinates                   varchar(255),
  constraint pk_properties primary key (id)
);


# --- !Downs

drop table if exists properties;

