/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import twitter4j.Status;

/**
 *
 * @author nikitaivancov
 */
public class Age_Gender_Prediction {
    static String str1="c:/gender.csv";
    static String str2="c:/14age.csv";
    BufferedReader br = null;
    final static Map<String, Object> arrGender = new HashMap<String, Object>();
    final static Map<String, Object> arrAge = new HashMap<String, Object>();
    String cvsSplitBy = ",";
    static int arr[];
    final static double ageIntercept=23.2188;
    
   public static void loadGender() throws FileNotFoundException, IOException{
     Scanner scanner = new Scanner(new File(str1));
       scanner.useDelimiter(",");
        scanner.nextLine();
        while(scanner.hasNextLine()){
           String line=scanner.nextLine();
           String[]LineArray=line.split(",");
           String s=LineArray[0];
           String str=LineArray[1];
           str=str.substring(1, str.length()-1);
           double num= Double.valueOf(str);
           s=s.substring(1, s.length()-1);
           arrGender.put(s, num);
        }  
          scanner.close();     
    }
    
    
    public static void loadAge() throws FileNotFoundException, IOException{
     Scanner scanner = new Scanner(new File(str1));
       scanner.useDelimiter(",");
        scanner.nextLine();
        while(scanner.hasNextLine()){
           String line=scanner.nextLine();
           String[]LineArray=line.split(",");
           String s=LineArray[0];
           String str=LineArray[1];
           str=str.substring(1, str.length()-1);
           double num= Double.valueOf(str);
           s=s.substring(1, s.length()-1);
           arrAge.put(s, num);
        }  
          scanner.close();     
    }
    
    
    
    public static String  findGender(String str) throws IOException{
        loadGender();
        String[] words=str.split(" ");
        Map<String, Object> wordsCount = new HashMap<String, Object>();
        int total_count =0;
        for(int i=0; i<words.length;i++){
            if(wordsCount.containsKey(words[i])){
              double value = (double) wordsCount.get(words[i]);
              wordsCount.put(words[i], ++value);}
            else
               wordsCount.put(words[i],(double)1);
               total_count+=(double) wordsCount.get(words[i]);
        }
        double gender=0;
        double res=0;
        for(int i=0;i<words.length;i++){
            if(arrGender.containsKey(words[i]))
                res+=((double)arrGender.get(words[i])+(double)wordsCount.get(words[i])/total_count);
        }
        
         if(res>0)
             return "male";
         else
            return "female"; 
           
    } 
    
    public static double  findAge(String str) throws IOException{
        loadAge();
        String[] words=str.split(" ");
        Map<String, Object> wordsCount = new HashMap<String, Object>();
        int total_count =0;
        for(int i=0; i<words.length;i++){
            if(wordsCount.containsKey(words[i])){
              double value = (double) wordsCount.get(words[i]);
              wordsCount.put(words[i], ++value);}
            else
               wordsCount.put(words[i],(double)1);
               total_count+=(double) wordsCount.get(words[i]);
        }
        double words_count=0;
        double res=0;
        for(int i=0;i<words.length;i++){
            if(arrAge.containsKey(words[i])){
           words_count+=(double)wordsCount.get(words[i]);
           res=(res+(double)arrAge.get(words[i])*(double)wordsCount.get(words[i]))+ageIntercept;
          }
        }
        res=res/words_count+ageIntercept;
        return res;
        
    } 
    
}
