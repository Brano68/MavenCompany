package sk.kosickaakademia.company.database;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sk.kosickaakademia.company.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DatabaseMongo {

    public static void main(String[] args) {
        //User user = new User("Peter", "Novotny", 34, 0);
        //new DatabaseMongo().insertUserIntoMongo(user);
        //new DatabaseMongo().getAllUsersFromMongo();
        //new DatabaseMongo().deleteUser("6062e1036e20fe325c82524f");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0, "Lyzovanie");
        new DatabaseMongo().updateUser("Jan", jsonArray);
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

    //vlozenie do databazy
    public boolean insertOneUserWithHobby(String name, JSONArray hobby){
        //vytvorenie mongo klinta
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        //vytvorenie connection
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytvorenie kolekcie iba raz ked spustam prvykrat
        //database.createCollection("hobbies");
        if(name == null){
            return false;
        }
        if(hobby == null){
            Document document = new Document();
            document.append("name", name);
            document.append("hobby", new JSONArray());
            database.getCollection("hobbies").insertOne(document);
            System.out.println("Document inserted successfully");
            return true;
        }else{
            Document document = new Document();
            document.append("name", name);
            document.append("hobby", hobby);
            database.getCollection("hobbies").insertOne(document);
            System.out.println("Document inserted successfully");
        }
        return true;
    }

    //ziskanie vsetkych userov
    public JSONObject gainAllUsers(){
        JSONObject js = new JSONObject();
        //vytvorenie mongo klinta
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        //vytvorenie connection
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytiahnut kolekciu users z databazy mongo -> allUsers
        MongoCollection<Document> collection = database.getCollection("hobbies")
                .withReadPreference(ReadPreference.primary())
                .withReadConcern(ReadConcern.MAJORITY)
                .withWriteConcern(WriteConcern.MAJORITY);

        int i = 0;
        for(Document document : collection.find()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", document.get("name"));
            jsonObject.put("hobby", document.get("hobby"));
            //System.out.println(document.get("name"));
            //System.out.println(jsonObject);
            System.out.println(document.get("_id"));
            //js.put(String.valueOf(i),jsonObject);
            js.put(document.get("_id"),jsonObject);
            //System.out.println(js);
            //i++;
        }
        return js;
    }

    //delete usra
    public boolean deleteUser(String hexString){
        //vytvorenie mongo klinta
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        //vytvorenie connection
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytvorenie kolekcie iba raz ked spustam prvykrat
        DeleteResult dr = database.getCollection("hobbies").deleteOne(new Document("_id", new ObjectId(hexString)));
        System.out.println(dr);
        long i = dr.getDeletedCount();
        if(i == 0){
            return false;
        }
        return true;
    }

    //update usra
    public boolean updateUser(String name, JSONArray jsonArray){
        //vytvorenie mongo klinta
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        //vytvorenie connection
        MongoDatabase database = mongo.getDatabase("allUsers");
        //vytvorenie kolekcie iba raz ked spustam prvykrat
        MongoCollection<Document> collection = database.getCollection("hobbies");
        //Updating a document
        UpdateResult updateResult = collection.updateOne(Filters.eq("name", name), Updates.set("hobby", jsonArray));
        System.out.println("Document update successfully...");
        System.out.println(updateResult);
        long i = updateResult.getModifiedCount();
        if(i == 0){
            return false;
        }
        return true;
    }

}
