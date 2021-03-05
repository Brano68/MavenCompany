package sk.kosickaakademia.company.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sk.kosickaakademia.company.entity.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;



public class Util {

    public String getJson(List<User> list){
        if(list == null){
            return "{}";
        }
        JSONObject object = new JSONObject();
        object.put("datetime", getCurrentDateTime());
        object.put("size", list.size());
        JSONArray jsonArray = new JSONArray();
        for(User user : list){
            JSONObject userJSON = new JSONObject();
            userJSON.put("id", user.getId());
            userJSON.put("fname", user.getFname());
            userJSON.put("lname", user.getLname());
            userJSON.put("age", user.getAge());
            userJSON.put("gender", user.getGender().toString());
            jsonArray.add(userJSON);
        }
        object.put("users", jsonArray);
        return object.toJSONString();
    }

    public String getJson(User user){
        if(user == null){
            return "{}";
        }
        JSONObject object = new JSONObject();
        object.put("datetime", getCurrentDateTime());
        object.put("size", 1);
        JSONArray jsonArray = new JSONArray();
        JSONObject userJSON = new JSONObject();
        userJSON.put("id", user.getId());
        userJSON.put("fname", user.getFname());
        userJSON.put("lname", user.getLname());
        userJSON.put("age", user.getAge());
        userJSON.put("gender", user.getGender().toString());
        jsonArray.add(userJSON);
        object.put("users", jsonArray);
        return object.toJSONString();
    }

    public String getCurrentDateTime(){
        //Calendar calendar = Calendar.getInstance();
        String result1 = ( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) ).format( Calendar.getInstance().getTime() );
        /*
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        String result = year + "-" +month+"-"+day+ " " + hour + ":" + minute + ":" + second;

         */
        return result1; //  "2021-03-03 15:07:23"
    }

    public String normalizeName(String name){
        // MILAN -> Milan   joZef -> Jozef
        if(name.isEmpty()){
            return null;
        }
        name = name.trim();
        String name1 = "";
        name1 = "" + name.charAt(0);
        name1 = name1.toUpperCase();
        String substring = name.substring(1,name.length()).toLowerCase();
        //System.out.println(substring);
        return name1+substring;
    }
}
