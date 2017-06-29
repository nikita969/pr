/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr1;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import static pr1.MongoDB.addUser;
import twitter4j.GeoLocation;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import pr1.Age_Gender_Prediction;
import static pr1.Age_Gender_Prediction.findGender;
import static pr1.Age_Gender_Prediction.loadGender;
import static pr1.MongoDB.addToDataBase;
/**
 *
 * @author nikitaivancov
 */
public class Pr1 {
       public final ConfigurationBuilder configuration = new ConfigurationBuilder();
       
   
    public static void main(String[] args) throws TwitterException, IOException {
       ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
       configurationBuilder.setDebugEnabled(true)
      .setOAuthConsumerKey("EmRxcWqQmx6FKTJgMMGw8uBNj")
      .setOAuthConsumerSecret("Q1agKp2hVgNdl8TaSLTzP6lab4gjTSqayC0u7juwTpHM47NPT6")
      .setOAuthAccessToken("849206934599344128-5fbEH2JAepOQEEfzns5HeOBORG1fyXC")
      .setOAuthAccessTokenSecret("kIhdacJp61DJXcK6pq1AdeTod4HfrUNoCtfBRXBKFNXVh");
        
     
      Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
      Query query = new Query("Golovkin");
     
      int size=0;
      int numberOfTweets =99999;
      long lastID = Long.MAX_VALUE;
      ArrayList<Status> tweets = new ArrayList<Status>();
     
      while (size  < numberOfTweets ) {
        if (numberOfTweets - size > 100 )
          query.setCount(100);
      else 
        query.setCount(numberOfTweets - size);
      
   
      
     try {
        
        QueryResult result = twitter.search(query);
        tweets.addAll(result.getTweets());
        System.out.println("Gathered " + tweets.size() + " tweets"+"\n");
      }
     catch (TwitterException te) {
        System.out.println("Couldn't connect: " + te);
     }
     
       query.setMaxId(lastID-1);
       size+=tweets.size();
     
     for (int i = 0; i < tweets.size(); i++) {
      Status t = (Status) tweets.get(i);
      System.out.println(t.getText());
       gatherByUserId(t);
        }
        tweets.removeAll(tweets);
      } 
        query.setMaxId(lastID-1);
   
    }
    
    public static void gatherByUserId(Status st) throws TwitterException, IOException
    {
          ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
          configurationBuilder.setDebugEnabled(true)
         .setOAuthConsumerKey("EmRxcWqQmx6FKTJgMMGw8uBNj")
         .setOAuthConsumerSecret("Q1agKp2hVgNdl8TaSLTzP6lab4gjTSqayC0u7juwTpHM47NPT6")
         .setOAuthAccessToken("849206934599344128-5fbEH2JAepOQEEfzns5HeOBORG1fyXC")
         .setOAuthAccessTokenSecret("kIhdacJp61DJXcK6pq1AdeTod4HfrUNoCtfBRXBKFNXVh");
     
      Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
      
       Paging page = new Paging(1,200);
       List<Status> statuses = twitter.getUserTimeline(st.getUser().getScreenName(),page);
       
          if(statuses.size()>20)
             addToDataBase(statuses);
          
          statuses.removeAll(statuses);
    } 
    
    
    
}