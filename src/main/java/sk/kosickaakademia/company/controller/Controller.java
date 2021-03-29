package sk.kosickaakademia.company.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.database.DatabaseMongo;
import sk.kosickaakademia.company.database.Statistic;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.enumerator.Gender;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Util;

import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@RestController
public class Controller {
    Log log = new Log();

    @PostMapping("/user/new")
    public ResponseEntity<String> insertNewUser(@RequestBody String data){
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(data);
            String fname = (String)jsonObject.get("fname");
            String lname = (String)jsonObject.get("lname");
            String gender = (String)jsonObject.get("gender");
            //int age = Integer.parseInt((String) jsonObject.get("age"));
            int age = Integer.parseInt(String.valueOf(jsonObject.get("age")));
            //long agee = (Long)jsonObject.get("age");
            System.out.println(fname + lname + age);
            if(fname == null || lname == null || fname.trim().equals("") || lname.trim().equals("") || age < 1){
                log.error("Missing name or surname!!!");
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
            }

            Gender g;
            if(gender==null){
                g = Gender.OTHER;
            }else if(gender.equalsIgnoreCase("male")){
                g = Gender.MALE;
            }else if(gender.equalsIgnoreCase("female")){
                g = Gender.FEMALE;
            }else{
                g = Gender.OTHER;
            }

            User user = new User(fname,lname,age,g.getValue());

            new Database().insertNewUser(user);
            //mongo
            new DatabaseMongo().insertUserIntoMongo(user);
            return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body("User has been added");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers(){
        List<User> list = new Database().getAllUser();
        String json = new Util().getJson(list);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }



    //user/{gender}
    @GetMapping("/user/{gender}")
    public ResponseEntity<String> getUserAccordingToTheGender(@PathVariable String gender){
        if(gender == null){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        }else if(gender.equalsIgnoreCase("male")){
            String json = new Util().getJson(new Database().getMale());
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
        }else if(gender.equalsIgnoreCase("female")){
            String json = new Util().getJson(new Database().getFemale());
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
        }else{
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        }
    }


    //user/age?from=15&to=24
    @GetMapping("/user/age")
    public ResponseEntity<String> getUserAccordingToTheAge(@RequestParam(value="from") Integer from, @RequestParam(value="to") Integer to){
        //System.out.println(from + " " + to);
        if(from == null || to == null || from < 1 || to < 1 || from > to){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        }
        String json = new Util().getJson(new Database().getUsersByAge(from,to));
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }


    //put - UPDATE
    //user/4
    @PutMapping("/user/{id}")
    public ResponseEntity<String> changeAge(@PathVariable Integer id, @RequestBody String body){
        Integer newAge = 0;
        try {
            JSONObject jsonObject = (JSONObject) (new JSONParser().parse(body));
            newAge = Integer.parseInt(String.valueOf(jsonObject.get("newAge")));
            //pozor ak nahodou by body obsahoval namiesto newAge by obsahoval napr newage alebo newAges
            //tak metoda vracia nie objekt null ale string "null"
            if(newAge == null){
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
            }
            boolean result = new Database().changeAge(id, newAge);
            if(result){
                return ResponseEntity.status(204).contentType(MediaType.APPLICATION_JSON).body("Update was successfully");
            }else{
                return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    //get
    //user?search=substring
    @GetMapping("/user")
    public ResponseEntity<String> getSubstring(@RequestParam(value="search") String substring){
        if(substring == null){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        }
        List<User> list = new Database().getUser(substring);
        if(list == null || list.isEmpty()){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("List empty");
        }else{
            String json = new Util().getJson(list);
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
        }
    }

    //get /
    @GetMapping("/")
    public ResponseEntity<String> getStatistic(){
        String json = new Statistic().makeStatistic();
        if(json == null){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        }
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }

    @GetMapping("/test")
    public ResponseEntity<String> getTest(){
        String json = "{\"test\":\"testik\"}";
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }

    //xml
    //users?type=xml

    @GetMapping("/usersss")
    public ResponseEntity<String>  getAllUsersXML(@RequestParam("type") String xml){
        if(xml == null || (!xml.equals("xml"))){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_XML).body("Something wrong");
        }

        List<User> list = new Database().getAllUser();
////
        String xmlFilePath = "resource/xmlfile.xml";
        try {

            //zdroje
            //https://examples.javacodegeeks.com/core-java/xml/parsers/documentbuilderfactory/create-xml-file-in-java-using-dom-parser-example/
            //https://www.journaldev.com/709/java-read-file-line-by-line
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("users");
            document.appendChild(root);

            // person element
            for(int i = 0; i < list.size(); i++){
                Element person = document.createElement("user");

                root.appendChild(person);

                // set an attribute to staff element
                Attr attr = document.createAttribute("id");
                attr.setValue(String.valueOf(list.get(i).getId()));
                person.setAttributeNode(attr);

                // firstname element
                Element firstName = document.createElement("firstname");
                firstName.appendChild(document.createTextNode(list.get(i).getFname()));
                person.appendChild(firstName);

                // lastname element
                Element lastname = document.createElement("lastname");
                lastname.appendChild(document.createTextNode(list.get(i).getLname()));
                person.appendChild(lastname);

            }
            
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
/////
        //teraz otvorit file xmlfile.xml
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader("resource/xmlfile.xml"));
            line = reader.readLine();
            System.out.println(line);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("+++++");
        System.out.println(line);
        //a poslanie riadku
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_XML).body(line);

    }

    @GetMapping("/backend")
    public ResponseEntity<String> getAllUsersFromMongo(){
        List<User> list = new DatabaseMongo().getAllUsersFromMongo();
        String json = new Util().getJson(list);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }


}
