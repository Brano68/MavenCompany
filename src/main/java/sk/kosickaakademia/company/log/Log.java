package sk.kosickaakademia.company.log;

public class Log {
    public void error(String message){
        System.out.println("ERROR: " + message);
    }

    public void print(String message){
        System.out.println("OK: " + message);
    }
}
