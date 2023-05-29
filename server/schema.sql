drop database if exists tfip_day41_miniproject;

create database tfip_day41_miniproject;

use tfip_day41_miniproject;

create table toilet (
    id int not null auto_increment,
    name varchar(255) not null,
    address varchar(255) not null,
    latitude varchar(255) not null,
    longitude varchar(255) not null,
    constraint pkey_toilet primary key(id)
);

insert into toilet (name, address, latitude, longitude) values ('NUS Business School', '15 Kent Ridge Dr, Singapore 119245', '1.2926980827338126', '103.77418945501725');
insert into toilet (name, address, latitude, longitude) values ('NUS School of Computing', 'NUS School of Computing, COM1, 13, Computing Dr, Singapore 117417', '1.2949673442649996', '103.77370905005719');
insert into toilet (name, address, latitude, longitude) values ('NUS the Deck Canteen', 'Computing Dr, Located in: National University of Singapore', '1.2945971334185111', '103.77256055341402');
insert into toilet (name, address, latitude, longitude) values ('NUS Central Library', '12 Kent Ridge Cres, Singapore 119275', '1.2968361160098325', '103.77316429842188');

create table person (
    id int not null auto_increment,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(255) not null,
    constraint pkey_person primary key(id)
);