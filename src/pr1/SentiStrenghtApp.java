/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr1;
import uk.ac.wlv.sentistrength.*;
/**
 *
 * @author nikitaivancov
 */
public class SentiStrenghtApp {
   
    public static int calc(String msg) {
    SentiStrength sentiStrength = new SentiStrength();
    String ssthInitialisation[] = {"sentidata", "c:/SentStrength_Data/"};
    sentiStrength.initialise(ssthInitialisation); //Initialise
    String str = sentiStrength.computeSentimentScores(msg); 
    int pos = Character.getNumericValue(str.charAt(0));
    int neg = Character.getNumericValue(str.charAt(3));
    int overall = pos - neg;
    return overall;
    }
}
