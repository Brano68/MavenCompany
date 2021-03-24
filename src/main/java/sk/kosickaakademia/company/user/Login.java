package sk.kosickaakademia.company.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Login {

    private static Map<String, Date> blocked = new HashMap<>();
    private static Map<String, Integer> attempt = new HashMap<>();
    private static String PASSWORDIK = "Brano";

    public Login() {
        //blocked = new HashMap<>();
        //attempt = new HashMap<>();
    }


    public String loginUser(String userName, String password){
        //je blokobavany??
        //vrat blokovanie
        if(isUserBlocked(userName)){
            return "User is blocked";
        }
        //nespravne heslo??
        //pripocitaj pokus
        if(!(password.equals(PASSWORDIK))){
            //pozor na null
            if(attempt.get(userName) == null){
               attempt.put(userName, 1);
            }else{
                attempt.put(userName, attempt.get(userName) + 1);
            }

            if(attempt.get(userName) == 3){
                //vymazanie pokusov
                attempt.put(userName, attempt.get(userName)-3);
                Date date = new Date();
                blocked.put(userName,date);
                System.out.println(date.getTime());
                return "User is blocked";
            }
            return "Wrong password";
        }
        //spravne heslo??
        //vymazanie pokusov
        deleteAttempt(userName);
        return "Congratulation password correct";

    }

    private boolean isUserBlocked(String userName){

        if(blocked.get(userName) == null){
            return false;
        }

        Long dateNow = new Date().getTime();
        //blokacia 10 sekund
        if(blocked.get(userName).getTime()+10000 > dateNow){
            return true;
        }else{
            blocked.remove(userName);
            return false;
        }

    }

    private void deleteAttempt(String userName){
        for(Map.Entry<String, Integer> entry : attempt.entrySet()) {
            if(entry.getValue().equals(userName)){
                attempt.remove(entry.getKey());
            }
        }
    }


}
