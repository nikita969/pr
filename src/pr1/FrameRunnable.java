/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr1;

import GUI.NewJFrame;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author nikitaivancov
 */
public class FrameRunnable extends Thread {
   private Date date;
   private int [] pos;
   private int[] neg;
   private String topic;
   private NewJFrame frame;
    
    public FrameRunnable(int [] pos,int[] neg, String topic,Date date,NewJFrame frame){
        super();
        this.date=date;
        this.topic=topic;
        this.pos=pos;
        this.neg=neg;
        this.frame=frame;
        
    }
    @Override
    public void run() {
      new NewJFrame(pos,neg,topic,date).setVisible(true); 
    
    }
    
}
