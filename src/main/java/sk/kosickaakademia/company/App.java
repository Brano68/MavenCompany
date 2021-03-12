package sk.kosickaakademia.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.enumerator.Gender;
import sk.kosickaakademia.company.util.Util;

import java.util.Collections;
import java.util.List;

/**
 * Company project
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "sk.kosickaakademia.company.controller")
public class App 
{
    public static void main( String[] args )
    {
        /*
        //System.out.println( "Hello World!" );
        Database database = new Database();
        //database.getConnection();
        //System.out.println(database.insertNewUser(new User("Jan", "Jurkovsky", 18, 0)));

        List<User> list = database.getMale();

        printListOfUsers(list);

        System.out.println("*********");
        list = database.getUsersByAge(20,60);
        printListOfUsers(list);



        System.out.println("+++++");
        list = database.getAllUser();
        printListOfUsers(list);


        System.out.println("-----");
        User user = database.getUserAccordingToTheID(5);
        System.out.println(user.getFname() + " " + user.getLname());

        ///
        System.out.println("///////");
        database.changeAge(5,55);

        ////
        list = database.getUser("B");
        printListOfUsers(list);

         */
        //Util util = new Util();
        //System.out.println(util.getCurrentDateTime());

        //System.out.println(util.getJson(new User(5,"Brano", "Kovac", 18, 0)));
        //System.out.println(util.getJson(new Database().getAllUser()));
        //System.out.println(util.normalizeName("jAnIcKo"));

        //iny port 8083
        SpringApplication app = new SpringApplication(App.class);
        app.setDefaultProperties(Collections.<String, Object>singletonMap("server.port", "8083"));
        app.run(args);
    }






    public static void printListOfUsers(List<User> list){
        for(User user : list){
            //System.out.println(user.getFname() + " " + user.getLname());
            System.out.println(user.toString());
        }
    }
}
