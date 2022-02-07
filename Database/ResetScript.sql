DROP TABLE IF EXISTS "Cart";
DROP TABLE IF EXISTS "ProductTransaction";
DROP TABLE IF EXISTS "Transaction";
DROP TABLE IF EXISTS "Product";
DROP TABLE IF EXISTS "Customers";


CREATE TABLE IF NOT EXISTS "Customers"(
	"Customer_Id" serial NOT NULL PRIMARY KEY,
	"Username" varchar(25) NOT NULL UNIQUE,
	"Password" char(64) NOT null
);

CREATE TABLE IF NOT EXISTS "Product"(
	"Product_Id" serial NOT NULL PRIMARY KEY,
	"Name" varchar(25) NOT NULL,
	"Price" NUMERIC(20, 2) NOT NULL,
	"Available" BOOL NOT NULL,
	"Description" varchar(255)
);

CREATE TABLE IF NOT EXISTS "Transaction"(
	"Transaction_Id" serial NOT NULL PRIMARY KEY,
	"Customer_Id" int4 REFERENCES "Customers"("Customer_Id") ON DELETE RESTRICT ON UPDATE CASCADE,
	"Date" int8 NOT NULL,
	"Subtotal" NUMERIC(20, 2) NOT NULL
);


CREATE TABLE IF NOT EXISTS "ProductTransaction"(
	"Transaction_Id" int4 REFERENCES "Transaction"("Transaction_Id")ON DELETE RESTRICT ON UPDATE CASCADE,
	"Product_Id" int4 REFERENCES "Product"("Product_Id") ON DELETE RESTRICT ON UPDATE CASCADE,
	"Amount" int4 NOT NULL,
	PRIMARY KEY ("Transaction_Id", "Product_Id")
);

CREATE TABLE IF NOT EXISTS "Cart" (
	"Product_Id" int4 REFERENCES "Product"("Product_Id") ON DELETE RESTRICT ON UPDATE CASCADE,
	"Customer_Id" int4 REFERENCES "Customers"("Customer_Id") ON DELETE RESTRICT ON UPDATE CASCADE,
	"Amount" int4 NOT NULL,
	PRIMARY KEY ("Product_Id", "Customer_Id")
);

INSERT INTO "Customers" values
(DEFAULT, 'admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');