package sk.kosickaakademia.company;

import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.enumerator.Gender;

import java.util.List;

/**
 * Company project
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
        Database database = new Database();
        //database.getConnection();
        //System.out.println(database.insertNewUser(new User("Jan", "Jurkovsky", 18, 0)));

        List<User> list = database.getMale();
        for(User user : list){
            System.out.println(user.getFname() + " " + user.getLname());
        }
    }
}
