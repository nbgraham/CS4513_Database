--1) Enter a new employee (2/month).
CREATE OR REPLACE PROCEDURE NEWTECHNICALSTAFF(
    name_ in varchar,
    address_ in varchar,
    technical_position_ in varchar
)
AS
BEGIN
  --Insert new technical staff
  insert into technical_staff (name, address, technical_position)
    values (name_, address_, technical_position_);
END NEWTECHNICALSTAFF;
/

CREATE OR REPLACE PROCEDURE NEWQUALITYCONTROLLER(
    name_ in varchar,
    address_ in varchar,
    product_type_ in int
)
AS
BEGIN
  --Insert new quality controller
  insert into quality_controller (name, address, product_type)
    values (name_, address_, product_type_);
END NEWQUALITYCONTROLLER;
/

CREATE OR REPLACE PROCEDURE NEWWORKER(
    name_ in varchar,
    address_ in varchar,
    max_products_daily_ in int
)
AS
BEGIN
  --Insert new worker
  insert into worker (name, address, max_products_daily)
    values (name_, address_, max_products_daily_);
END NEWWORKER;
/




--2) Enter a new product associated with the person who made the product, repaired the product if it is repaired, or checked the product (400/day).
CREATE OR REPLACE PROCEDURE NEWPRODUCT(
  productID_ in int,
  date_produced_ in date,
  minutes_to_make_ in int,
  height_ in int,
  length_ in int, 
  width_ in int,
  product_num_ in int,
  tester_name_ in varchar,
  producer_name_ in varchar,
  weight_ in float, -- optional, depending on product type
  color_ in varchar, -- optional, depending on product type
  software_ in varchar -- optional, depending on product type
)
AS
BEGIN
  --Insert new product
  insert into product (productID, date_produced, minutes_to_make, height, length, width, product_num, tester_name, producer_name, weight, color, software)
    values (productID_, date_produced_, minutes_to_make_, height_, length_, width_, product_num_, tester_name_, producer_name_, weight_, color_, software_);
END NEWPRODUCT;
/

CREATE OR REPLACE PROCEDURE NEWREPAIR(
  tsName in varchar,
  productID_ in int,
  dateRepaired in date
)
AS
BEGIN
  --Insert new product
  insert into repair (staff_name, productid, date_of)
    values (tsName, productID_, dateRepaired);
END NEWREPAIR;
/




--3) Enter a customer associated with some products (50/day).
CREATE OR REPLACE PROCEDURE NEWCUSTOMER(
  name_ in varchar,
  address_ in varchar
)
AS
BEGIN
  --Insert new customer
  insert into customer (name, address)
    values (name_, address_);
END NEWCUSTOMER;
/

CREATE OR REPLACE PROCEDURE NEWPURCHASE(
  customer_name_ in varchar,
  productID_ in int
)
AS
BEGIN
  --Insert new purchase
  insert into purchase (customer_name, productID)
    values (customer_name_, productID_);
END NEWPURCHASE;
/




-- 4) Create a new account associated with a product (40/day).
CREATE OR REPLACE PROCEDURE NEWACCOUNT(
  num_ in int,
  date_established_ in date,
  product_cost_ in float,
  product_id_ in int
)
AS
BEGIN
  --Insert new customer
  insert into account (num, date_established, product_cost, product_id)
    values (num_, date_established_, product_cost_, product_id_);
END NEWACCOUNT;
/

CREATE OR REPLACE FUNCTION GET_PURCHASE_IDS
RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  --Get products made by the specified worker
  OPEN my_cursor FOR
    select productID from purchase;
  RETURN my_cursor;
END GET_PURCHASE_IDS;
/

CREATE OR REPLACE FUNCTION GET_PRODUCT_IDS(
  product_type_ in int
)
RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  --Get products made by the specified worker
  OPEN my_cursor FOR
    'select productID from product where product.product_num = :type' using product_type_;
  RETURN my_cursor;
END GET_PRODUCT_IDS;
/




--5) Enter a complaint associated with a customer and product (30/day)
CREATE OR REPLACE PROCEDURE NEWCOMPLAINT(
  complaintID_ in int,
  date_of_ in date,
  description_ in varchar,
  expected_treatment_ in varchar,
  purchaseID_ int
)
AS
BEGIN
  --Insert new purchase
  insert into complaint (complaintID, date_of, description, expected_treatment, purchaseID)
    values (complaintID_, date_of_, description_, expected_treatment_, purchaseID_);
END NEWCOMPLAINT;
/




--6) Enter an accident associated with appropriate employee and product (1/week).
CREATE OR REPLACE PROCEDURE NEWACCIDENT(
  num_ in int,
  date_of_ in date,
  days_lost_ in int,
  related_repair_ in int,
  related_production_ in int
)
AS
BEGIN
  --Insert new purchase
  insert into accident (num, date_of, days_lost, related_repair, related_production)
    values (num_, date_of_, days_lost_, related_repair_, related_production_);
END NEWACCIDENT;
/




--7) Retrieve the date produced and time spent to produce a particular product (100/day).
CREATE OR REPLACE PROCEDURE GET_DATE_TIME_PRODUCT(
  productID_ in int,
  date_produced_ out date,
  minutes_to_make_ out int
)
AS
BEGIN
  --Get date produced and time to produce specific product
  select date_produced, minutes_to_make 
    into date_produced_, minutes_to_make_
    from product
    where productID = productID_;
END GET_DATE_TIME_PRODUCT;
/




--8) Retrieve all products made by a particular worker (2000/day).
CREATE OR REPLACE FUNCTION GET_PRODUCT_BY(
  worker_name_ in varchar
)
RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  --Get products made by the specified worker
  OPEN my_cursor FOR
    'select *
      from product
      where producer_name = :worker_name_' using worker_name_;
  RETURN my_cursor;
END GET_PRODUCT_BY;
/




--9) Retrieve the total number of errors a particular quality controller made. This is the total number of products certified by this controller and got some complaints (400/day).
-- param testerName varchar(50)
CREATE OR REPLACE PROCEDURE GET_ERRORS_FROM (
    qc_name_ in varchar,
    amount out int
)
AS
BEGIN
  select count (*) 
  into amount
  from complaint
    where (select tester_name from product
            where product.productID = complaint.purchaseID) = qc_name_;
END GET_ERRORS_FROM;
/          
    
    
          
          
--10) Retrieve the total costs of the products in the product3 category which were repaired at the request of a particular quality controller (40/day).
-- param testerName varchar(50) 
CREATE OR REPLACE PROCEDURE GET_PRODUCT3_COST (
    qc_name_ in varchar,
    cost out float
)
AS
BEGIN
  select sum(product_cost)
    into cost
    from account
    where product_id in (select productid from repair)
      and (select tester_name from product
            where product.productID = account.product_ID
                and product.product_num = 3) = qc_name_;
END GET_PRODUCT3_COST;
/



              
--11) Retrieve all customers who purchased all products of a particular color (5/month).
-- param color varchar(20) 
CREATE OR REPLACE FUNCTION GET_CUSTOMERS_COLOR(
  color_ in varchar
)
RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  OPEN my_cursor FOR
    'select * from customer
        where not exists ((select productID from product where product.color = :color_) minus 
            (select productID from purchase where purchase.customer_name = customer.name))' using color_;
  RETURN my_cursor;
END GET_CUSTOMERS_COLOR;
/                
                


                
--12) Retrieve the total number of work days lost due to accidents in repairing the products which got complaints (1/month).
CREATE OR REPLACE PROCEDURE GET_DAYS_LOST_REPAIRS (
    daysLost out int
)
AS
BEGIN
  select sum(days_lost)
    into daysLost
    from accident
    where related_repair is not null;
END GET_DAYS_LOST_REPAIRS;
/




--13) Retrieve all customers who are also workers (10/month).
CREATE OR REPLACE FUNCTION GET_CUSTOMER_WORKERS
  RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  OPEN my_cursor FOR
      select * from customer where
        exists (select * from technical_staff where customer.name = technical_staff.name) or
        exists (select * from worker where customer.name = worker.name) or
        exists (select * from quality_controller where customer.name = quality_controller.name);
  RETURN my_cursor;
END get_customer_workers;
/



  
--14) Retrieve all the customers who have purchased the products made or certified or repaired by themselves (5/day)
CREATE OR REPLACE FUNCTION GET_PURCHASED_OWN
RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  --Get products made by the specified worker
  OPEN my_cursor FOR
    select * from customer 
      where exists
        (select * from purchase 
          where purchase.customer_name = customer.name 
            and purchase.productID in (
              (select productID from product where producer_name = customer.name or tester_name = customer.name) union
              (select productID from repair where repair.staff_name = customer.name)
            )
        );
  RETURN my_cursor;
END GET_PURCHASED_OWN;
/





--15) Retrieve the average cost of all products made in a particular year (5/day).
CREATE OR REPLACE PROCEDURE AVG_COST (
    startDate in date,
    endDate in date,
    cost out float
)
AS
BEGIN
select avg(product_cost) 
  into cost
  from account 
  where (select date_produced from product where account.product_id = product.productid)
    between startDate and endDate;
END AVG_COST;
/
  
  
  
  
--16) Switch the position between a technical staff and a quality controller (1/ 3 months).
--param tName varchar(50)
--param qName varchar(50)
CREATE OR REPLACE PROCEDURE SWITCH_POSITIONS (
    tName in varchar,
    qName in varchar
)
AS
BEGIN
  insert into technical_staff (name, address)
    select name, address from quality_controller
      where name = qName;
  insert into quality_controller (name, address)
    select name, address from technical_staff
      where name = tName;
END SWITCH_POSITIONS;
/




--17) Delete all accidents whose dates are in some range (1/day).
--param start date
--param end date
CREATE OR REPLACE PROCEDURE DELETE_ACCIDENTS (
    startDate in date,
    endDate in date
)
AS
BEGIN
  delete from accident
    where date_of <= endDate and
    date_of >= startDate;
END DELETE_ACCIDENTS;
/


-- 19) Export: Retrieve all customers (in name order) and output them to a data file instead of screen (the user must be asked to enter the output file name);
CREATE OR REPLACE FUNCTION GET_CUSTOMERS
  RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  OPEN my_cursor FOR
    select * from customer
      order by name;
  RETURN my_cursor;
END GET_CUSTOMERS;
/




-- Testing
CREATE OR REPLACE PROCEDURE CLEAR_DATA
AS
BEGIN
    delete from lead_to;
    delete from degree;
    delete from accident;
    delete from repair;
    delete from complaint;
    delete from purchase;
    delete from account;
    delete from product;
    delete from customer;
    delete from worker;
    delete from quality_controller;
    delete from technical_staff;
END CLEAR_DATA;
/