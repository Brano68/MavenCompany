package sk.kosickaakademia.company.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.user.Login;
import sk.kosickaakademia.company.util.Util;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SecretController {

    private final String PASSWORDIK = "Brano";
    Map<String,String> map = new HashMap<>();

    //secret
    @GetMapping("/secret")
    public ResponseEntity<String> secret(@RequestHeader("token") String tokenik){
        System.out.println(tokenik);
        if(tokenik.startsWith("Bearer")){
            System.out.println("Zacina s B");
            String substringToken = tokenik.substring(7);
            System.out.println(substringToken);

            for(Map.Entry<String, String> entry : map.entrySet()){
                System.out.println("------------");
                //System.out.println(entry.getValue());
               // System.out.println(tokenik);

                if(entry.getValue().equals(substringToken)){
                    return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("TOKEN OK");
                }
            }
        }
        //System.out.println(tokenik);
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("BAD OK");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String auth){

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(auth);
            String login = (String)jsonObject.get("login");
            String password = (String)jsonObject.get("password");
            System.out.println(login + " " + password);
            if(login == null || password == null || login.equals("null") || password.equals("null")
            || login.isEmpty()){
                new Log().error("Something wrong!!!");
                return ResponseEntity.status(400).body("");
            }
            //testovanie pokusov
            Login login1 = new Login();
            String result = login1.loginUser(login,password);
            if(result.equals("User is blocked")){
                return ResponseEntity.status(401).body("User is blocked");
            }else if(result.equals("Wrong password")){
                return ResponseEntity.status(401).body("Wrong password");
            }
            //
            if(password.equals(PASSWORDIK)){
                String token = new Util().getToken();
                map.put(login,token);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("login", login);
                jsonObject1.put("token", "Bearer: "+token);

                new Log().print("Password is correct!!!");
                return ResponseEntity.status(200).body(jsonObject1.toJSONString());
            }else{
                new Log().error("Wrong password!!!");
                return ResponseEntity.status(401).body("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(400).body("Something wrong");
    }

    //logout
    @PostMapping ("/logout")
    public ResponseEntity<String> logout(@RequestHeader("token") String tok){
            System.out.println("---" + tok + "---");
            String login = "";
            for(Map.Entry<String, String> entry : map.entrySet()) {
                if(entry.getValue().equals(tok)){
                    login = entry.getKey();
                    map.remove(login);
                    return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("User has been logged out");
                }
            }
            if(login.equals("")){
                return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("User was not found");
            }
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body("Something wrong");
    }

    //aj pre prihlasenych aj pre neprihlasenych
    //student
    @PostMapping("/student")
    public ResponseEntity<String> student(@RequestHeader("token") String tok){
        //over token
        String login = "";
        for(Map.Entry<String, String> entry : map.entrySet()) {
            if(entry.getValue().equals(tok)){
                login = entry.getKey();
                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("Student of Kosicka Academy, name: " + login);
            }
        }
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("You are only a host user which is not a student");
    }
}
