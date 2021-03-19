package sk.kosickaakademia.company.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kosickaakademia.company.log.Log;
import sk.kosickaakademia.company.util.Util;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SecretController {

    private final String PASSWORDIK = "Brano";
    Map<String,String> map = new HashMap<>();

    //secret
    @GetMapping("/secret")
    public String secret(@RequestHeader("token") String tokenik){
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
                   return "TOKEN IS OK";
                }
            }
        }
        //System.out.println(tokenik);
        return "BAD TOKEN";
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
}
