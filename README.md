# Part 1 - Create MySQL Database
1. Start MySQL from console
2. Create a new Database named 'orm_lab'
3. Inside that database create a new table name 'account'
4. the schema should be as folllow


		*	id INT NOT NULL AUTO_INCREMENT
		* 	name VARCHAR(20)
		*  password VARCHAR(20)
		*  PRIMARY KEY = id

# PART 2 - Create Java App

## Adding dependencies
1 Create a new Java Maven project

2 Update the pom.xml file by adding the following dependencies. These are the dependencies for the ORM library we will be using.

```
        <!-- https://mvnrepository.com/artifact/com.j256.ormlite/ormlite-core -->
        <dependency>
            <groupId>com.j256.ormlite</groupId>
            <artifactId>ormlite-core</artifactId>
            <version>4.48</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.j256.ormlite/ormlite-jdbc -->
        <dependency>
            <groupId>com.j256.ormlite</groupId>
            <artifactId>ormlite-jdbc</artifactId>
            <version>4.48</version>
        </dependency>
```


3 Then also add the following. This is a dependency that allows us to connect our Java code to MySql

```
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.6</version>
        </dependency>

```
## Create Application Model
Create a new Java class name 'Account'

```
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "account")
public class Account {

    public static final String NAME_FIELD_NAME = "name";
    public static final String PASSWORD_FIELD_NAME = "password";

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "password")
    private String password;

    Account() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public Account(String name) {
        this.name = name;
    }

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return name.equals(((Account) other).name);
    }
}
```
The things to notice here are the annotations. These annotations tell OrmLite how to relate our Java Objects to the records in MySQL

## Implement the ORM

Create a Java class name 'AccountApp'

```
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class AccountApp {

    // we are using a MySQl database
    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/orm_lab?useUnicode=true";

    private Dao<Account, Integer> accountDao;

    public static void main(String[] args) throws Exception {
        // turn our static method into an instance of Main
        new AccountApp().doMain(args);
    }

    private void doMain(String[] args) throws Exception {
        ConnectionSource connectionSource = null;
        try {
            // create our data-source for the database
            connectionSource = new JdbcConnectionSource(DATABASE_URL, YOURUSERNAME, YOURPASSWORD);
            // setup our  DAOs
            setupDao(connectionSource);
            // read, write and delete some data
            processData();

            System.out.println("\n\nIt seems to have worked\n\n");
        } finally {
            // destroy the data source which should close underlying connections
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    /**
     * Read and write some example data.
     */
    private void processData() throws Exception {
        // create an instance of Account
        String name = "Jim Coakley";
        Account account = new Account(name);

        // persist the account object to the database
        accountDao.create(account);
        int id = account.getId();
        System.out.println(id);
        // assign a password
         account.setPassword("_secret");
        // update the database after changing the object
        accountDao.update(account);
        // delete the account
        //accountDao.deleteById(id);
    }

    /**
     * Setup our  DAOs
     */
    private void setupDao(ConnectionSource connectionSource) throws Exception {

        accountDao = DaoManager.createDao(connectionSource, Account.class);

    }
}
```
Some of the things in this class to take notice are the objects that the imported libraries offer us:

* JdbcConnectionSource from the JDBC library
* Dao and DaoManager from the OrmLite

! Links to the above APIs are listed below

Now if we run this we should see some info in the console of IntelliJ telling us that the program ran ok. And if we look over to MySQL we can query the table and see that our code has create and updated a record in out database

! Remember MySQL needs to be up and running

# Part 3

If you have made it this far and you're able to run the program properly, Congratulations!

The program this far only runs through a few lines of code before it exits. Upgrade the program so that it enters a control loop and allows the user to interact with your database until they quit the program. You will need to impletment CRUD functionality in the form of an account service. Dont forget to visit the ORMLite api as a reference for what you can do with the library

### References
* [OrmLite core API](http://ormlite.com/javadoc/ormlite-core/)
* [OrmLite JDBC](http://ormlite.com/javadoc/ormlite-jdbc/)
* [JdbcConnectionSource](http://ormlite.com/javadoc/ormlite-jdbc/com/j256/ormlite/jdbc/JdbcConnectionSource.html)
* [What is CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete)