package com.theironyard.charlotte;

import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:./posProject");
    }

    public static void createTables() throws SQLException {
        Statement s = getConnection().createStatement();
        s.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, userName VARCHAR, password VARCHAR, address VARCHAR, state VARCHAR,zipCode INT, emailAddress VARCHAR )");
        s.execute("CREATE TABLE IF NOT EXISTS products (id IDENTITY, productName VARCHAR, shortDescription VARCHAR, price DOUBLE, quantityAvailable INT, pictureURL VARCHAR,)");
        s.execute("CREATE TABLE IF NOT EXISTS carts (id IDENTITY, userId INT, itemId INT, itemQuantity INT, totalPrice DOUBLE, plusTax DOUBLE)");
        s.execute("CREATE TABLE IF NOT EXISTS states(id IDENTITY, stateName VARCHAR, taxRate DOUBLE)");
    }

    public static void createNewUser(User user) throws SQLException {
        PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?, ?, ?, ?)");
        stmt.setString(1, user.getUserName());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getAddress());
        stmt.setString(4, user.getState());
        stmt.setInt(5, user.getZipCode());
        stmt.setString(6, user.getEmailAddress());
        stmt.execute();
    }

    //Use this in order to see if the name is in use
    public static boolean userNameInUse(String name) throws SQLException {
        boolean thereIsAlready = false;
        PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM users WHERE userName = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String userName = results.getString("emailAddress");
            if (userName.equalsIgnoreCase(name)) {
                thereIsAlready = true;
            }
        }
        return thereIsAlready;
    }

    //This is simply get User object with user name
    public static User getUser(String currentUserInput) throws SQLException {
        PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM users WHERE userName = ?");
        stmt.setString(1, currentUserInput);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            // Step through each result
            int id = results.getInt("id");
            String userName = results.getString("userName");
            String password = results.getString("password");
            String address = results.getString("address");
            String state = results.getString("state");
            int zipCode = results.getInt("zipCode");
            String emailAddress = results.getString("emailAddress");

            return new User(id, userName, password, address, state, zipCode, emailAddress);
        }
        return null;
    }

    //If I need to know the length of the table
    public static int lengthOfTable(String currentUser) throws SQLException {
        int id = 1;
        PreparedStatement stmt = getConnection().prepareStatement("SELECT COUNT(*) FROM messages INNER JOIN users ON messages.user_name = users.user_name WHERE users.user_name = ?");
        stmt.setString(1, currentUser);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            id = results.getInt("COUNT(*)") + 1;
        }
        return id;
    }

    public static ArrayList<Products> showAllProducts() throws SQLException{
        ArrayList<Products> entireList = new ArrayList<>();
        PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM products");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = Integer.valueOf(results.getString("id"));
            String productName = results.getString("productName");
            String shortDescription = results.getString("shortDescription");
            double price = Double.valueOf(results.getString("price"));
            int quantityAvailable = Integer.valueOf(results.getString("quantityAvailable"));
            String pictureURL = results.getString("pictureURL");
            Products products = new Products(id, productName, shortDescription,price,quantityAvailable,pictureURL,false);
            entireList.add(products);
        }

        return entireList;
    }

    public static Products oneProductObject(int id) throws SQLException{
        PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM products WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            // Step through each result
            // ONLY if there is an an answer to the query will the product array get made otherwise it will stay empty
            String productName = results.getString("productName");
            String shortDescription = results.getString("shortDescription");
            double price = results.getDouble("price");
            int quantityAvailable = results.getInt("quantityAvailable");
            String pictureURL = results.getString("pictureURL");

            return new Products(id, productName, shortDescription, price, quantityAvailable, pictureURL,false);
        }
        return null;
    }

//    public static ArrayList<Products> showUserAllProducts(String userName) throws SQLException {
//
//    }



    //Ill use this to get the specific array lists I need
//    public static ArrayList<Messages> selectAllMessages(Connection conn, String currentUser) throws SQLException {
//        ArrayList<Messages> messages = new ArrayList<>();
//        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM messages INNER JOIN users ON messages.user_name = users.user_name WHERE users.user_name = ?");
//        stmt.setString(1, currentUser);
//        ResultSet results = stmt.executeQuery();
//        while (results.next()) {
//            int id = Integer.valueOf(results.getString("messages.postId"));
//            String name = results.getString("users.user_name");
//            String text = results.getString("messages.text");
//            Messages message = new Messages(id, name, text);
//            messages.add(message);
//        }
//        return messages;
//    }



//    public static ArrayList<Products> showUserAllProducts(String userName) throws SQLException {
//        PreparedStatement s = getConnection().prepareStatement(
//                "SELECT songs.id, songs.artist, songs.title, votes.rating " +
//                        "FROM songs LEFT OUTER JOIN votes " +
//                        "ON songs.id = votes.song_id " +
//                        "WHERE votes.user_id = ? OR votes.user_id IS NULL"
//        );
//        s.setInt(1, currentUser().getId());
//
//        ArrayList<Song> songs = new ArrayList<>();
//        ResultSet r = s.executeQuery();
//        while(r.next()) {
//            // Step through each result
//            int id = r.getInt("id");
//            String artist = r.getString("artist");
//            String title = r.getString("title");
//            int rating = r.getInt("rating");
//
//            songs.add(new Song(id, artist, title, rating));
//        }
//
//        return songs;
//    }

    static String currentUserSession;
    static int sessionCartCounter=1;
    static String currentUserCartSession;

    public static void main(String[] args) throws SQLException {

        Spark.staticFileLocation("/templates");
        Spark.init();
       // Server.createWebServer().start();
        createTables();

        Spark.get("/",
                ((request, response) -> {
                    HashMap model = new HashMap();

                    Session session = request.session();
                    String currentUser = session.attribute(currentUserSession);
                    //Makes it so you cant shop unless you login
                    if (currentUser == null) {
//                        ArrayList allItems=showAllProducts();
//                        model.put("products", allItems);
                        return new ModelAndView(model, "login.html");
                    }

                    User user = getUser(currentUser);
                    //This is a catch for the default user who can do anything BUT shop which
                    //will kick him to the login window ALSO populates entire list with no login.
//                    if(user.getUserName().equalsIgnoreCase("default")){
//
//                        ArrayList allItems=showAllProducts();
//                        model.put("products", allItems);
//                        return new ModelAndView(model, "home.html");
//
//                    }
                    //TODO THIS HAS TO POPULATE THE SHOPPING SCREEN

                    ArrayList allItems=showAllProducts();
                    model.put("products", allItems);

                    return new ModelAndView(model, "home.html");

                }),
                new MustacheTemplateEngine()
        );

        Spark.post("/login",
                ((request, response) -> {
                    String username = request.queryParams("emailAddress");
                    String password = request.queryParams("password");

                    if (username == null || password == null) {
                        //username or password invalid try again
                        //set the table settings to show the message
                        response.redirect("/");
                        return "";
                    }

                    if (!userNameInUse(username)) {
                        //his catches if the name is not in use and would return null I want it instead
                        //to say
                        //username or password invalid try again
                        //set the table settings to show the message
                        response.redirect("/");
                        return "";
                    }

                    User user = getUser(username);

                    if (user.getEmailAddress().equalsIgnoreCase(username) && !user.getPassword().equals(password)) {
                        //his catches if the name is not in use and would return null I want it instead
                        //to say
                        //username or password invalid try again
                        //set the table settings to show the message
                        response.redirect("/");
                        return "";
                    }

                    //This is great now I can have every different person with their own unique
                    // ID as their own session
                    currentUserSession=user.getEmailAddress();

                    Session session = request.session();
                    session.attribute(currentUserSession, username);

                    response.redirect("/");
                    return "";

                })
        );


        Spark.post("/create-user",
                ((request, response) -> {

                    //////////MAKE THIS A PAGE BOOLEAN FOR LOGIN WINDOW
                    String username = request.queryParams("createName");
                    String password = request.queryParams("password");
                    String address = request.queryParams("address");
                    String state = request.queryParams("state");
                    int zipCode = Integer.valueOf(request.queryParams("zipCode"));
                    String emailAddress = request.queryParams("emailAddress");

                    //GIVE A MESSAGE OF NULL / there wasn't correct information given

                    if (request.queryParams("createName") == null || request.queryParams("password") == null||request.queryParams("address")==null || request.queryParams("state")==null||request.queryParams("zipCode")==null||request.queryParams("emailAddress")==null) {

                        //username or password invalid try again
                        //set the table settings to show the message

                        response.redirect("/");
                        return "";
                    }

                    //MAKING THE EMAILADDRESS unique
                    if (userNameInUse(emailAddress)) {

                        //send boolean for already in use
                        //HTML TRY ANOTHER NAME'
                        response.redirect("/");
                        return "";
                    }

                    User createdUser = new User(username,password,address,state,zipCode,emailAddress);
                    createNewUser(createdUser);

                    response.redirect("/");
                    return "";
                })
        );

        //TODO SO I HAVE TO MAKE ONE BLANK ACCOUNT FOR PAGE ENTRY AND THEN I HAVE TO MAKE AN ADMIN ACCOUNT;
        //TODO WILL SAID SOMETHING ABOUT MAKING OTHER ADMIN ACCOUNTS BUT THAT SEEMS OVERKILL
        //TODO ANOTHER TODO WAS TO UNDERSTAND THAT THE 'CART' IS IN SESSIONS BUT THE CHECKOUT AT THE END
        //TODO SHOULD BE THE TABLE AT THE VERY END

        //TODO THIS IS THE PAGE FOR THE SPECIFIC ITEM THIS IS THE ONLY PAGE THAT ALLOWS FOR THE USER TO ADD TO CART
        //TODO THIS ALSO MUST HAVE A BUTTON FOR GOING BACK TO THE SHOP ALL MENU

        Spark.get("/productpage/:id",
                ((request, response) -> {
                    HashMap model = new HashMap();

                    //This populates the specific page for the one item
                    int specificItemId = Integer.valueOf(request.params("id"));
                    ArrayList productPage = new ArrayList();
                    productPage.add(oneProductObject(specificItemId));

                    model.put("products", productPage);

                    return new ModelAndView(model, "pageDescription.html");

                }),
                new MustacheTemplateEngine()

                );

        Spark.post("/addToCart",
                ((request, response) -> {

                    int productID = Integer.valueOf(request.queryParams("productCode"));
                    int quantity = Integer.valueOf(request.queryParams("quantity"));

                    Products selectedItem = oneProductObject(productID);
                    SessionsCart sessionsCart = new SessionsCart(sessionCartCounter, selectedItem.getId(),quantity,selectedItem.getPrice());

                    Session session = request.session();
                    session.attribute(Integer.toString(sessionCartCounter), sessionsCart);
                    sessionCartCounter++;

                    response.redirect("/");
                    return"";
                })
                );

        Spark.get("/seeCart",
                ((request, response) -> {
                    HashMap model = new HashMap();

                    Session session = request.session();
                    String currentUser = session.attribute(currentUserSession);

                    //Makes it so you cant shop unless you login
                    if (currentUser == null) {
                        return new ModelAndView(model, "login.html");
                    }

                    User user = getUser(currentUser);
                    ArrayList myCart = new ArrayList();
                    for(int i =1;i <= sessionCartCounter;i++){
                        SessionsCart sessionsCart = session.attribute(Integer.toString(i));
                        myCart.add(sessionsCart);
                    }
                    model.put("showCart",myCart);
                    return new ModelAndView(model, "seeCart.html");

                }),
                new MustacheTemplateEngine()
        );

        Spark.post("/checkout",
                ((request, response) -> {
                    HashMap model = new HashMap();

                    Session session = request.session();
                    String currentUser = session.attribute(currentUserSession);

                    if (currentUser == null) {
                        return new ModelAndView(model, "login.html");
                    }

                    User user = getUser(currentUser);


                    int myCartPrice = Integer.valueOf(request.queryParams("wholeCart"));
//                    for(int i =1;i <= sessionCartCounter;i++){
//                        SessionsCart sessionsCart = session.attribute(Integer.toString(i));
//                        myCart.add(sessionsCart);
//                    }

                    return "";
                })
        )

//          THIS IS WAY TO FANCY FOR NO REASON
//        Spark.get("/loginPage",
//                ((request, response) -> {
//
//                    User user = getUser("Default");
//                    //currentUserSession=user.getEmailAddress();
//
//                    Session session = request.session();
//                    session.attribute(currentUserSession, user.emailAddress);
//
//                    HashMap model = new HashMap();
//
//                    String currentUser = session.attribute(currentUserSession);
//
//                    if (currentUser == null) {
//                        throw new Exception("There is no user current user active impossible since I set " +
//                                "one up before you got to this stage ");
//                    }
//
//                    //TODO THIS HAS TO POPULATE THE SHOPPING SCREEN
//
//                    ArrayList<PageSettings> allOptions= new ArrayList<>();
//
//                    allOptions.add(user.settings);
//
//                    model.put("pageSettings", allOptions);
//
//                    return new ModelAndView(model, "login.html");
//
//                }),new MustacheTemplateEngine()
//                );
//              THIS WAS TRYING TO BE REALLY FANCY
//        Spark.post("/createBool",
//                ((request, response) -> {
//
//                    Session session = request.session();
//                    String currentUser = session.attribute(currentUserSession);
//
//                    User user = getUser(currentUser);
//                    user.settings.toggle();
//                    if (currentUser == null) {
//                        throw new Exception("This cannot be null unless you bypassed clicking the login button " +
//                                "to ge the login window");
//                    }
//
//                    User user = getUser(currentUser);
//
//                    if(user.settings.createUser){
//                        user.settings.createUser = false;
//                    }
//                    else {
//                        user.settings.createUser = true;
//                    }
//
//                    response.redirect("/loginPage");
//                    return"";
//                })
//                );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

                    //TODO DELETING ITEMS OF THEIR CARTS
//        Spark.post("/delete-entry",
//                (((request, response) -> {
//                    int someId = Integer.valueOf(request.queryParams("postId"));
//                    //deleteMessage(conn, Integer.valueOf(request.queryParams("postId")));
//                    response.redirect("/");
//                    return "";
//                }))
//        );


//        Spark.post("/create-message",
//                ((request, response) -> {
//                    Session session = request.session();
//                    String name = session.attribute("userName");
//                    User user = selectUser(conn, name);
//                    if (user == null) {
//                        throw new Exception("User is not logged in");
//                    }
//
//                    String post = request.queryParams("blogPost");
//                    insertMessage(conn, post, user.getUserName());
//
//                    response.redirect("/");
//                    return "";
//                })
//        );

    }
}
