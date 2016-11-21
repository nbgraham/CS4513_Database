drop table lead_to;
drop table degree;
drop table accident;
drop table repair;
drop table complaint;
drop table purchase;
drop table account;
drop table product;
drop table customer;
drop table worker;
drop table quality_controller;
drop table technical_staff;

create table technical_staff (
  name varchar(50) primary key not null,
  address varchar(50),
  technical_position varchar(50) -- allows null to allow swap with quality controller
);

create table quality_controller (
  name varchar(50) primary key not null,
  address varchar(50),
  product_type int -- allows null to allow swap with technical staff
);

create table worker (
  name varchar(50) primary key not null,
  address varchar(50),
  max_products_daily int not null
);

create table customer (
  name varchar(50) primary key not null,
  address varchar(50)
);

create table product (
  productID int primary key not null,
  date_produced date not null,
  minutes_to_make int not null,
  height int not null,
  length int not null, 
  width int not null,
  product_num int not null,
  tester_name varchar(50) not null,
  producer_name varchar(50) not null,
  weight float, 
  color varchar(20),
  software varchar(50)
);

create table account (
  num int primary key not null,
  date_established date not null,
  product_cost float not null,
  product_id int not null,
  constraint fk_acct_product_id foreign key (product_id)
    references product(productID)
);

create table purchase (
  customer_name varchar(50) not null,
  productID int primary key not null,
  constraint fk_purchase_name foreign key (customer_name)
    references customer(name),
  constraint fk_purchase_productID foreign key (productID)
    references product(productID)
);

create table complaint (
  complaintID int primary key,
  date_of date,
  description varchar(200),
  expected_treatment varchar(200),
  purchaseID int,
  constraint fk_purchaseID foreign key (purchaseID)
    references purchase(productID)
);

create table repair (
  staff_name varchar(50) not null,
  productID int primary key not null,
  date_of date not null,
  constraint fk_repair_name foreign key (staff_name)
    references technical_staff(name),
  constraint fk_repair_productID foreign key (productID)
    references product(productID)
);

create table accident (
  num int primary key not null,
  date_of date not null,
  days_lost int not null,
  related_repair int,
  related_production int,
  constraint fk_related_repair foreign key (related_repair)
    references repair(productID),
  constraint fk_related_production foreign key (related_production)
    references product(productID),
  constraint related_event check (related_repair is not null or related_production is not null)
);
create index accident_index on accident(date_of);

create table degree (
  recipient_name varchar(50) not null,
  specialty varchar(50),
  isGraduate int not null,
  constraint fk_degree_name foreign key (recipient_name)
    references technical_staff(name),
  constraint pk_degreeID primary key (recipient_name, isGraduate)
);

create table lead_to (
  complaintID int primary key not null,
  repairID int not null,
  constraint fk_complaintID foreign key (complaintID)
    references complaint(complaintID),
  constraint fk_repairID foreign key (repairID)
    references repair(productID)
);
