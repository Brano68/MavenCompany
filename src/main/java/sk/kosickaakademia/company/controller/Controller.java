package sk.kosickaakademia.company.controller;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.company.database.Database;
import sk.kosickaakademia.company.entity.User;
import sk.kosickaakademia.company.enumerator.Gender;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Util;

import javax.xml.crypto.Data;
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
        if(from == null || to == null || from < 1 || to < 1){
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
        }
        String json = new Util().getJson(new Database().getUsersByAge(from,to));
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(json);
    }

}
