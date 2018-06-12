# Part 1 - Create MySQL Database
1. Start MySQL from console
2. Create a new Database named 'orm-lab'
3. Inside that database create a new table name 'account'
4. the schema should be as folllow


		*	id INT NOT NULL AUTO_INCREMENT
		* 	name VARCHAR(20)
		*  password VARCHAR(20)
		*  PRIMARY KEY = id

# PART 2 - CREATE JAVA APP

## Adding dependencies
1 Create a new Java Maven project
2 Update the pom.xml file by adding the following dependencies
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
These are the dependencies for the ORM library we will be using

3 Then also add the following

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

    // for QueryBuilder to be able to find the fields
    public static final String NAME_FIELD_NAME = "name";
    public static final String PASSWORD_FIELD_NAME = "password";

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = PASSWORD_FIELD_NAME)
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
## Implement the ORM

Create a Java class name 'AccountApp'

```
public class AccountApp {

    // we are using a MySQl database
    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/orm-lab?useUnicode=true";

    private Dao<Account, Integer> accountDao;

    public static void main(String[] args) throws Exception {
        // turn our static method into an instance of Main
        new AccountApp().doMain(args);
    }

    private void doMain(String[] args) throws Exception {
        ConnectionSource connectionSource = null;
        try {
            // create our data-source for the database
            connectionSource = new JdbcConnectionSource(DATABASE_URL, "root", "");
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

Now if we run this we should see some info in the console of IntelliJ telling us that the program ran ok. And if we look over to MySQL we can query the table and see that our code has create and updated a record in out database

! Remember MySQL needs to be up and running

# Part 3

If you have made it this far and you're able to run the program properly, Congratulations!

The program this far only runs through a few lines of code before it exits. Upgrade the program so that it enters a control loop and allows the user to interact with your database until they quit the program. You will need to impletment CRUD functionality in the form of an account service. Dont forget to visit the ORMLite api as a reference for what you can do with the library

### References
* [OrmLite core API](http://ormlite.com/javadoc/ormlite-core/)
* [OrmLite JDBC](http://ormlite.com/javadoc/ormlite-jdbc/)
* [What is CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete)