/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pr1;

import javax.swing.JProgressBar;
import static GUI.Analysis.flag;
import java.awt.Color;
import javax.swing.UIManager;
/**
 *
 * @author nikitaivancov
 */
public class Loadbar extends Thread {
     private JProgressBar progress;
     Thread t1;
    public Loadbar(JProgressBar progress){
        super();
        this.progress=progress;
    }
    
    @Override
    public void run() {
       
        while(!flag){
              progress.setOpaque(true);
              UIManager.put("JProgressBar.foreground",Color.GREEN);
              UIManager.put("progress.background",Color.GREEN);
              
             progress.setIndeterminate(true);
             progress.setForeground(Color.green);
             UIManager.put("progress.foreground",Color.GREEN);
         
            progress.setVisible(true);
            progress.setValue(50);
        }
        
        flag=false;
    }
    
    
    
    
    
}
