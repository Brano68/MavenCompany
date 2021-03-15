package sk.kosickaakademia.company.database;

import org.json.simple.JSONObject;
import sk.kosickaakademia.company.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Statistic {

    private int count = 0;
    private int male = 0;
    private int female = 0;
    private double age = 0;
    private int min = 0;
    private int max = 0;

    public static void main(String[] args) {
        System.out.println(new Statistic().makeStatistic());
    }

    public String makeStatistic(){
        List<User> list = new Database().getAllUser();
        if(list == null){
            return null;
        }
        count = list.size();
        male = (new Database().getMale()).size();
        female = (new Database().getFemale()).size();
        age = countAverage(list);
        min = findMin(list);
        max = findMax(list);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        jsonObject.put("male", male);
        jsonObject.put("female", female);
        jsonObject.put("age", age);
        jsonObject.put("min", min);
        jsonObject.put("max", max);
        return jsonObject.toJSONString();
    }

    private double countAverage(List<User> list){
        double ageAverage = 0;
        for(User user : list){
            ageAverage = ageAverage + user.getAge();
        }
        ageAverage = (ageAverage/count)*100;
        return Math.round(ageAverage)/100;
    }

    private int findMin(List<User> list){
        int ageFind = list.get(0).getAge();
        for(int i = 0; i < list.size(); i++){
            if(ageFind > list.get(i).getAge()){
                ageFind = list.get(i).getAge();
            }
        }
        return ageFind;
    }

    private int findMax(List<User> list){
        int ageFind = list.get(0).getAge();
        for(int i = 0; i < list.size(); i++){
            //System.out.println(list.get(i).getAge());
            if(ageFind < list.get(i).getAge()){
                ageFind = list.get(i).getAge();
            }
        }
        return ageFind;
    }



}
