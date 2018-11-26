import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class AccountApp {

    // we are using a MySQl database
    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/orm_lab?useUnicode=true";
    private final static String YOURUSERNAME = "root";
    private final static String YOURPASSWORD = "";

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