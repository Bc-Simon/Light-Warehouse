# Light Warehouse

The simple Warehouse Inventory System using Spring-boot framework for back-end and AngularJs for the front-end

## Features

* Able to store product data via csv file consumption. 
* Able to store quantities of such products in different locations via csv file consumption. 
* Able to transfer inventory from one location to another given amount of quantity and product code via UI

## Setup
This Project use MySQL server as the database, please follow the instruction below first:

 - Open and login the mysql in your cmd, and the default port number of the server is 3306.
 - Run the following commands at the mysql prompt:

```sh
mysql> create database db;
mysql> create database db_test;
mysql> create user 'test_user'@'%' identified by 'test123'; 
mysql> grant all on db.* to 'test_user'@'%';
mysql> grant all on db_test.* to 'test_user'@'%'; 
```
Or use your own database and user. And chanage the setting in "src\main\resources\application.properties" and "src\test\resources\application.properties"

## Test
Run the following commands to test:
```sh
mvn test
```

## Complie & Run
Run the following commands to compile the web application.

```sh
mvn spring-boot:run
```

## System Design and Requirement Analysis

[link to Analysis](https://docs.google.com/document/d/1OjGQ-cSAmY4GcJQYGsFCcUdXkuw6Ae0MDnQ10HItohM/edit?usp=sharing)

## UI Design
Simply click the tabs and switch to product/ stock panel.

## Daily Notes

[link to Daily Note](https://docs.google.com/document/d/1zSvQhru3gsgZ6Eu3doy-KdvCRkPKnuTo5pb4KNcdS_8/edit?usp=sharing)

