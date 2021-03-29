package sk.kosickaakademia.company.database;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.simple.JSONObject;
import sk.kosickaakademia.company.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatabaseMongo {

    public static void main(String[] args) {
        //User user = new User("Peter", "Novotny", 34, 0);
        //new DatabaseMongo().insertUserIntoMongo(user);
        new DatabaseMongo().getAllUsersFromMongo();
    }

    //
    public boolean insertIntoMongo(){
        //vytvorenie mongo klinta
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        //vytvorenie connection
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytvorenie kolekcie
        database.createCollection("users");
        //priprava dokumentu
        //
        List<User> list = new Database().getAllUser();
        int j = 0;
        for(int i = 0; i < list.size(); i++){
            j++;
            Document document = new Document();
            document.append("name", list.get(i).getFname());
            document.append("lname", list.get(i).getLname());
            document.append("age", list.get(i).getAge());
            //vkladanie dokumentu do kolekcie
            database.getCollection("users").insertOne(document);
        }
        if(j == list.size()){
            System.out.println("Document inserted successfully");
            return true;
        }
        return false;

    }

    //vlozenie do mongo databazy funkcia je volana v controller na inserte
    //localhost:8083/user/new
    public void insertUserIntoMongo(User user){
        //vytvorenie mongo klinta
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        //vytvorenie connection
        MongoDatabase database = mongo.getDatabase("allUsers");
        Document document = new Document();
        document.append("name", user.getFname());
        document.append("lname", user.getLname());
        document.append("age", user.getAge());
        document.append("gender", user.getGender().getValue());
        database.getCollection("users").insertOne(document);
        System.out.println("Document inserted successfully");

    }

    //vrati vsetkych z mongo databazy
    public List<User> getAllUsersFromMongo(){
        //vytvorenie mongo klinta
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        //vytvorenie connection
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("users")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);
        List<User> list = new ArrayList<>();
        for(Document document : collection.find()){
            User user = new User((String)document.get("name"), (String)document.get("lname"), (int)document.get("age"), (int)document.get("gender"));
            //System.out.println(document.get("name"));
            list.add(user);
        }
        return list;
    }



}
