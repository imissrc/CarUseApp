package com.caruseapp.entityes;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Event {
    //        {"createAt":"2020=10=02 11:19:56.0","prisonerName":"周雷","exceptionType":"发生行为异常",
//        "exceptionLevel":"1","dealState":true,"misdeclaration":false,"comment":"检查犯人并进行行为教育"}]
    String createAt;
    String prisonerName;
    String exceptionType;
    String exceptionLevel;
    Boolean dealState;
    Boolean misdeclaration;
    String comment;
    int id;
    String state;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getExceptionLevel() {
        return exceptionLevel;
    }

    public void setExceptionLevel(String exceptionLevel) {
        this.exceptionLevel = exceptionLevel;
    }

    public Boolean getDealState() {
        return dealState;
    }

    public void setDealState(Boolean dealState) {
        this.dealState = dealState;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPrisonerName() {
        return prisonerName;
    }

    public void setPrisonerName(String prisonerName) {
        this.prisonerName = prisonerName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Boolean getMisdeclaration() {
        return misdeclaration;
    }

    public void setMisdeclaration(Boolean misdeclaration) {
        this.misdeclaration = misdeclaration;
    }


    public String toString(){
        return "getCreateAt=" +getCreateAt()+"="+
                "name=" +getPrisonerName()+"="+
                "state=" +getState()+"="+
                "exceptionType="+getExceptionType() +"="+
                "detail="+getComment() +"="+
                "id="+getId()+"="+
                "misdeclaration="+getMisdeclaration() +"="+
                "exceptionLevel="+getExceptionLevel() +"=";
    }
    public ArrayList<Event> String2Events(String string){
        ArrayList<Event> eventList = new ArrayList<>();

        if (string ==null){

        }
        else {
            String[] arrayString = string.split("%");
            ArrayList<String>events = new ArrayList<>();
            for (int i=0 ;i<arrayString.length; i++) {
                if(arrayString[i]!=null && arrayString[i].length()!=0){ //过滤掉数组arrayString里面的空字符串
                    events.add(arrayString[i]);
                }
            }
            System.out.println(events);
            for(int i = 0;i<events.size();i++){
                String str = events.get(i);
                Event event_ = new Event();
                String pattern = "=.*=";
                Pattern p1 = Pattern.compile("getCreateAt"+pattern);
                Pattern p2 = Pattern.compile("name"+pattern);
                Pattern p3 = Pattern.compile("state"+pattern);
                Pattern p4 = Pattern.compile("exceptionLevel"+pattern);
                Pattern p7 = Pattern.compile("detail"+pattern);
                Pattern p8 = Pattern.compile("id"+pattern);
                Pattern p9 = Pattern.compile("misdeclaration"+pattern);
                Pattern p10 = Pattern.compile("exceptionType"+pattern);

                Matcher m10 = p10.matcher(str);
                Matcher m9 = p9.matcher(str);
                Matcher m7 = p7.matcher(str);
                Matcher m1 = p1.matcher(str);
                Matcher m2 = p2.matcher(str);
                Matcher m3 = p3.matcher(str);
                Matcher m4 = p4.matcher(str);
                Matcher m8 = p8.matcher(str);



                if (m1.find()){
                    String[] groups = m1.group().split("=");
                    event_.setCreateAt(groups[1]);
                }
                if (m2.find()) {
                    String[] groups = m2.group().split("=");
                    event_.setPrisonerName(groups[1]);
                }
                if (m3.find()) {
                    String[] groups = m3.group().split("=");
                    event_.setState(groups[1]);
                }
                if (m4.find()){
                    String[] groups = m4.group().split("=");
                    event_.setExceptionLevel(groups[1]);
                }
                if (m7.find()){
                    String[] groups = m7.group().split("=");
                    event_.setComment(groups[1]);
                } if (m8.find()){
                    String[] groups = m8.group().split("=");
                    event_.setId(Integer.valueOf(groups[1]));
                }

                if (m9.find()){
                    String[] groups = m9.group().split("=");
                    event_.setMisdeclaration(Boolean.getBoolean(groups[1]));
                }
                if (m10.find()){
                    String[] groups = m10.group().split("=");
                    event_.setExceptionType(groups[1]);
                }
                eventList.add(event_);
            }
        }
        return eventList;
    }
}
