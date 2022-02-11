# 2201 - Java React Enterprise - WVU

# Set-up
In order to use the WebApp, you need to have a Postgres Database with which to
connect to, and the tables outlined in the SQL file in the Database Directory.

You also need to use mvn install on the ORM Project to install the package onto 
your machine.

Afterwards, you can build the webapp as a .war file and use it in a tomcat 10
server as a webapp. You then can take advantage of the functionality it gives
you by using GET POST PUT and DELETE HTTP Requests to the webapp.

# Configure
To configure the ORM you need at least two files a file named "SalAnb.cfg.xml"
A sample of the file looks like this:

```xml
	<?xml version = "1.0" encoding = "utf-8"?>
	<!DOCTYPE salanb-configuration SYSTEM
			"http://johnsalguero.com/dtd/salanb-configuration-0.1.dtd">
	<salanb-configuration>
		<session-factory>
	
			<property name = "salanb.connection.driver_class">
				org.postgresql.Driver
			</property>
	
			<property name = "salanb.connection.url">
				jdbc:postgresql://john2201javareact.c52ukl0dxifi.us-east-1.rds.amazonaws.com:5432/postgres
			</property>
	
			<property name = "salanb.connection.username">
				postgres
			</property>
	
			<property name = "salanb.connection.password">
				changeThePassword
			</property>
	
			<!-- List of XML mapping files -->
			<mapping resource = "Movie.salanb.test.xml"/>
		</session-factory>
	</salanb-configuration>
.```

If you are familiar with the way Hibernate uses XML files, you will find 
many simularities. You can actually list many session factory objects in this
file, and it will work with multiple databases. This helped with testing
different schemas without haveing to start the app over again.

The important part of this config file is the list of mapping resources, this
lists out xml files the session factory uses to map out the models it expects.
You can list out as many or as few as you need. you can opt to map all your 
models in 1 file, or use 1 file per model, or any combination of models and 
mapping files as long as there is no double mapping or inconsistencies.
A mapping configuration looks like this.

```xml
	<?xml version = "1.0" encoding = "utf-8"?>
	<!DOCTYPE salanb-mapping SYSTEM
			"http://johnsalguero.com/dtd/salanb-mapping-0.1.dtd">
	<salanb-mapping>
		<class name = "testmodels.Movie" table = "movies">
	
			<meta attribute = "class-description">
				This class contains the movie detail.
			</meta>
	
			<id name = "id" type = "integer" column = "m_id">
				<generator class="NATURAL"/>
			</id>
	
			<property name = "title" column = "title" type = "string"/>
			<property name = "price" column = "price" type = "big_decimal"/>
			<property name = "available" column = "available" type = "boolean"/>
			<property name = "returnDate" column = "return_date" type = "long"/>
			<property name = "directorId" column = "director_id" type = "integer"/>
			<property name = "genre_id" column = "genre_id" type = "integer"/>
	
		</class>
	</salanb-mapping>
.```