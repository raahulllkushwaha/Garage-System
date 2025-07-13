create database garage;
use garage;

/* Customer table */
create table customer(
id int auto_increment primary key not null,
name varchar(50),
phone varchar(20)
);

// Vehicle table
create table vehicle(
id int auto_increment primary key not null,
customer_id int,
number_plate varchar(20),
model varchar(10),
foreign key (customer_id) references customer(id)
);

//Service table
create table services(
id int auto_increment primary key,
description varchar(50),
cost double
);

//Insert data in service table
insert into services(description, cost) values
('Oil Change', 1500),
('Engine Repair', 5000),
('Tyre Replacement', 2500),
('Washing', 500);

//Invoice table
create table invoices(
id int auto_increment primary key not null,
customer_id int,
vehicle_id int,
service_id int,
date timestamp default current_timestamp,
foreign key (customer_id) references customer(id),
foreign key (vehicle_id) references vehicle(id),
foreign key (service_id) references services(id)
);

//Insert data into vehicle table
insert into vehicle values(1, 1, "MP15UN9645", "2022");