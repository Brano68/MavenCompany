package sk.kosickaakademia.company;

import sk.kosickaakademia.company.database.Database;

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
        database.getConnection();
    }
}
