 package design_patterns.bridge_pattern;
 interface DatabaseDriver {
     void connect();
     String execute(String query);
     void close();
 }

 class PostgresDriver implements DatabaseDriver {
     @Override
     public void connect() {
         System.out.println("PostgreSQL: Connected");
     }

     @Override
     public String execute(String query) {
         System.out.println("PostgreSQL: Executing: " + query);
         return "pg_result";
     }

     @Override
     public void close() {
         System.out.println("PostgreSQL: Connection closed");
     }
 }

 class MongoDriver implements DatabaseDriver {
     @Override
     public void connect() {
         System.out.println("MongoDB: Connected");
     }

     @Override
     public String execute(String query) {
         System.out.println("MongoDB: Executing: " + query);
         return "mongo_result";
     }

     @Override
     public void close() {
         System.out.println("MongoDB: Connection closed");
     }
 }

 abstract class Repository {
     protected final DatabaseDriver databaseDriver;

     public Repository(DatabaseDriver driver) {
         this.databaseDriver = driver;
     }
 }

 class UserRepository extends Repository {
     public UserRepository(DatabaseDriver driver) {
         super(driver);
     }

     public void findById(int id) {
         databaseDriver.connect();
         databaseDriver.execute("SELECT * FROM users WHERE id = " + id);
         databaseDriver.close();
     }

     public void save(String name, String email) {
         databaseDriver.connect();
         databaseDriver.execute("INSERT INTO users (name, email) VALUES ('" + name + "', '" + email + "')");
         databaseDriver.close();
     }
 }

 class OrderRepository extends Repository {
     public OrderRepository(DatabaseDriver driver) {
         super(driver);
     }

     public void findByUserId(int userId) {
         databaseDriver.connect();
         databaseDriver.execute("SELECT * FROM orders WHERE user_id = " + userId);
         databaseDriver.close();
     }

     public void createOrder(int userId, String product, double amount) {
         databaseDriver.connect();
         databaseDriver.execute("INSERT INTO orders (user_id, product, amount) VALUES (" + userId + ", '" + product + "', " + amount + ")");
         databaseDriver.close();
     }
 }

 public class DatabaseAbsDemo {
     public static void main(String[] args) {
         DatabaseDriver pg = new PostgresDriver();
         UserRepository userRepo = new UserRepository(pg);
         userRepo.findById(42);
         userRepo.save("Alice", "alice@example.com");
     }
 }
