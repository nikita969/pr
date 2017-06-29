/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr1;
import GUI.Request;
import twitter4j.Status;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static pr1.Age_Gender_Prediction.findAge;
import static pr1.Age_Gender_Prediction.findGender;
import static pr1.Pr1.gatherByUserId;
import static pr1.SentiStrenghtApp.calc;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
 /*
 * @author nikitaivancov
 */
public class MongoDB {
   
       public static MongoClient mongo =new MongoClient ("localhost",27017); 
       public static DB db = mongo.getDB("PROJECT");
       public static DBCollection tweetDB = db.getCollection("tweets");
       public static DBCollection userDB = db.getCollection("users");
       public static DBCollection topicsDB = db.getCollection("topics");
       public static int flag=0;
       public static void main(String[] args){
         
       
        //BasicDBObject document = new BasicDBObject();
      //	table1.drop();
       // Insert(null);
       // reguestByTopic("hello");
     /*  BasicDBObject query = new BasicDBObject("name", "niki");
        query.put("age", 10);
        DBCursor cursor = table.find(query);*/
	//  DBCursor cursor = table.find(searchQuery);
     //  DBCursor cursor = MongoDB.topicsDB.find();
       //  while(cursor.hasNext())  
         //    System.out.println( cursor.next().get("topicName"));
       
//	 int num=0;
  //       DBCollection table = db.getCollection("tweets2");
    //   table.drop();
        // List cl=table.distinct("User_Screen_name");
         
       //  for(int i=0;i<cl.size();i++)
         //{
          // if(cl.get(i).toString().equals("2634695718"))
        //   num=i;
    //   } */
        //  System.out.println(num);
       //  System.out.println(cl.size());
      //   System.out.println(cl.get(0));
         //  searchQuery.put("User_Id",183867946);
	/*  DBCursor cursor = tweetDB.find();
          while (cursor.hasNext()) {
                System.out.println(cursor.next());
            } */
         //  gatherByUserId(cl);
     //   if(cursor.count() == 0)
       //     System.out.println("0");
 
     // mongo.close();
       
       // DBCursor cursor = table.find();
        
         
      //  DBCursor cursor = table1.find();
        //cursor.sort(new BasicDBObject("grade",1 ));
      //   cursor.sort(new BasicDBObject("grade",-1 ));
       //  System.out.println(cursor.size());
         
  
         /*  while (cursor.hasNext()) {
                System.out.println(cursor.next());
           } 
                 System.out.println(cursor.count());
            cursor.close(); */
          //  new Request().setVisible(true);
	
         
     }
       
    
       
       
       
       
       
       public static void addTweets(List<Status> statuses) throws IOException
    {
        User user=statuses.get(0).getUser();
        for(int i=0; i< statuses.size();i++){
        Status  t = statuses.get(i);
        BasicDBObject query = new BasicDBObject("User_Id",user.getId());
        query.put("User_Screen_name",user.getScreenName());
        query.put("Tweet", t.getText());
        query.put("Date", t.getCreatedAt().toString());
        DBCursor cursor = tweetDB.find(query);
        if(cursor.count()==0){  
        BasicDBObject document = new BasicDBObject(); 
        document.put("Tweet_ID",tweetDB.count()+1);
        document.put("User_Id",user.getId());
        document.put("User_name", user.getName());
        document.put("User_Screen_name",user.getScreenName());
        document.put("Tweet", t.getText());
        document.put("Date", t.getCreatedAt().toString());
        int grade=calc(t.getText());
        document.put("grade", grade);
        tweetDB.insert(document);
        }
        }
       
    }   
       public static void addUser(List<Status> st) throws IOException
     {
          User user=st.get(0).getUser();
          //String s="https://twitter.com/"+user.getScreenName();
          //System.out.println(s);
        //  java.awt.Desktop.getDesktop().browse(java.net.URI.create(s));
          BasicDBObject query = new BasicDBObject();
          query.put("User_Screen_name",user.getScreenName());
          DBCursor cursor = userDB.find(query);
          if( cursor.count() == 0 ){    
              String str=null;
          for (int i = 0; i < st.size(); i++){
             Status t=st.get(i);
                if(str==null)
                    str=t.getText();
                else
                    str+=t.getText();
          }    
          String gender = findGender(str);       
          double age = findAge(str); 
          
          BasicDBObject document = new BasicDBObject();
          document.put("User_Id",user.getId());
          document.put("User_name", user.getName());
          document.put("User_Screen_name",user.getScreenName());
          document.put("User_age",age);
          document.put("User_gender", gender);
          userDB.insert(document);   
           } 
          cursor.close();
          flag=1;
          
     }          
       
       public static void addToDataBase(List<Status> st) throws IOException
     {   
         addUser(st);
         addTweets(st);
         
     } 
   
       public static void updateGrade( String tweet, long tweetID)
       {
           BasicDBObject newDocument = new BasicDBObject();
           int grade= calc(tweet);
           newDocument.append("$set", new BasicDBObject().append("grade",grade));
           BasicDBObject search = new BasicDBObject();
           search.append("Tweet_ID", tweetID);
           tweetDB.update(search, newDocument);
           System.out.println(tweetID);   
       }         
       
       public static void requestByTopic (String str, Date date)
       {
           DBCollection table = db.getCollection(str + "_" +date);
           BasicDBObject topicDoc = new BasicDBObject();
           topicDoc.put("topicName",str);
           topicDoc.put("Date",date);
           topicsDB.insert(topicDoc);
           BasicDBObject q = new BasicDBObject(); 
           q.put("Tweet",  java.util.regex.Pattern.compile(str));
           DBCursor cursor = tweetDB.find(q);
           cursor.sort(new BasicDBObject ("User_Id",1));
           if(cursor.size()==0)
               return ;
          
           DBObject t=null;
           int sum=0;
           long userID=0;
           if(cursor.hasNext())
           {
               t = cursor.next();  
               userID = (long) t.get("User_Id");
               
           }   
               while(cursor.hasNext()){
                long ID = (long)t.get("User_Id");
                   if(userID == ID){
                       if(t.get("grade")!=null)
                     sum += (int) t.get("grade");
                     t=cursor.next();
                     
                   }
                   else{     
                    if(sum!=0){
                     BasicDBObject query = new BasicDBObject();    
                    query.put("User_Id",userID);
                    DBCursor uscursor = userDB.find(query);
                    DBObject t1 = uscursor.next();
                    String gender = (String)t1.get("User_gender");
                    double age = (double) t1.get("User_age");
                    String sign=null;
                    BasicDBObject document = new BasicDBObject(); 
                    document.put("User_Id",t1.get("User_Id"));
                    document.put("gender",gender);
                    document.put("User_age",age);
                    document.put("User_Screen_name",t1.get("User_Screen_name"));
                
                    if(sum>0)
                        sign="pos";  
                        
                    if(sum<0)
                        sign="neg";
                    
                    document.put("sign",sign);
                    table.insert(document);
                    document.clear();
                    uscursor.close(); 
                    query.clear();                  
                      }
                      sum=0;
                      userID=ID;
                   }     
               }
                   cursor.close();        
           }
              
      
          public static int[] specRequest(String topic,String subtopic,String group,Date date,String gender,Date from,Date to){
              
               DBCollection table = db.getCollection(topic + "_" +date);
               BasicDBObject query = new BasicDBObject();
               int [] specR=new int[4];
               if(group.equals("Negative"))
                   query.put("sign", "neg");
               else if(group.equals("Positive"))
                   query.put("sign","pos"); 
               if(gender.equals("Male"))
                 query.put("gender","male");
               else if(gender.equals("Female"))
                   query.put("gender",new BasicDBObject("$ne","male"));
                     
               DBCursor cursor = table.find(query);
               
               DBObject t;
               BasicDBObject q = new BasicDBObject();
               
               
               
               while(cursor.hasNext()){
                t=cursor.next();
                long ID = (long) t.get("User_Id");
                q.put("User_Id", ID);
                q.put("Tweet",java.util.regex.Pattern.compile(subtopic));
                //System.out.println(from);
               /* if(from!=null && to!=null){
                    q.put("Date",BasicDBObjectBuilder.start("$gte",from).add("$lte",to).get());
                    System.out.println("dkbnejkfnvwfjkvnfgerb");}
                else if (from!=null)
                    q.put("Date",BasicDBObjectBuilder.start("$gte",from).get());
                else if (to!=null)
                    q.put("Date",new BasicDBObject("$lte",to)); */
                
                DBCursor cursor1 = tweetDB.find(q);
                int sum=0;
                
                 if(cursor1.hasNext()){
                  
                 while(cursor1.hasNext()){
                  DBObject n;
                  n=cursor1.next();
                  if(n.get("grade")!=null)
                  sum += (int) n.get("grade");
                  
               }  
                 if(sum==0)
                     specR[2]++;
                 
                 else if(sum<0)
                     specR[1]++;
                 
                 else if(sum>0)
                     specR[0]++; 
                 }
                 else specR[3]++;
                q.clear();
                cursor1.close();
          }
              
           return specR;
          }
          public static int[]  generateGraphs(String str,Date date,String topic){   
          int[] arr = new int[7];
          
          DBCollection table1 = db.getCollection(topic + "_" + date);
        
          BasicDBObject query = new BasicDBObject("sign",str);
          
          DBCursor cursor = table1.find(query);
          arr[0]=cursor.count();
          
          query.put("User_age", new BasicDBObject("$gt", 0.0).append("$lt", 20.0));
          cursor = table1.find(query);
          arr[3]=(cursor.count());
           
          query.put("User_age", new BasicDBObject("$gt", 20.0).append("$lt", 30.0));
          cursor = table1.find(query);
          arr[4]=(cursor.count());
            
          query.put("User_age", new BasicDBObject("$gt", 30.0).append("$lt", 40.0));
          cursor = table1.find(query);
          arr[5]=(cursor.count());
            
          query.put("User_age", new BasicDBObject("$gt", 40.0).append("$lt", 120));
          cursor = table1.find(query);
          arr[6]=(cursor.count());
            
          cursor.close();
          query = new BasicDBObject("sign",str);
          query.put("gender", "male");
           
          cursor = table1.find(query);
          arr[1]=(cursor.count());
            
          arr[1]= cursor.count();
          arr[2]=arr[0]-arr[1]; 
          return arr;
          }    

         public static void createUsersLists(String topic,Date date,List<String> posUsers,List<String> negUsers)
         {
             DBCollection table = db.getCollection(topic + "_" +date);
             BasicDBObject query = new BasicDBObject();
             query.put("sign","pos");
             DBCursor cursor = table.find(query);
             for(int i=0;i<cursor.count();i++){
                 DBObject t=cursor.next();
                 posUsers.add((String) t.get("User_Screen_name"));
             }
             cursor.close();
             query.clear();
             query.put("sign", "neg");
             cursor = table.find(query);
             for(int i=0;i<cursor.count();i++){
                 DBObject t=cursor.next();
                 negUsers.add((String) t.get("User_Screen_name"));
             }
         }
     }          
       
