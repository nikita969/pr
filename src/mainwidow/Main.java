
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class Main {
  public static void main(String[] args) {
    final JProgressBar progressBar = new JProgressBar();
    progressBar.setOpaque(false);
    progressBar.setUI(new GradientPalletProgressBarUI());

    JPanel p = new JPanel();
    p.add(progressBar);
    p.add(new JButton(new AbstractAction("Start") {
      @Override public void actionPerformed(ActionEvent e) {
        SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>() {
          @Override public Void doInBackground() {
            int current = 0, lengthOfTask = 100;
            while(current<=lengthOfTask && !isCancelled()) {
              try {
                Thread.sleep(50);
              } catch(Exception ie) {
                return null;
              }
              setProgress(100 * current / lengthOfTask);
              current++;
            }
            return null;
          }
        };
        worker.addPropertyChangeListener(new ProgressListener(progressBar));
        worker.execute();
      }
    }));
    
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().add(p);
    frame.setSize(320, 240);
    frame.setVisible(true);
  }
}
class ProgressListener implements PropertyChangeListener {
  private final JProgressBar progressBar;
  ProgressListener(JProgressBar progressBar) {
    this.progressBar = progressBar;
    this.progressBar.setValue(0);
  }
  @Override public void propertyChange(PropertyChangeEvent evt) {
    String strPropertyName = evt.getPropertyName();
    if("progress".equals(strPropertyName)) {
      progressBar.setIndeterminate(false);
      int progress = (Integer)evt.getNewValue();
      progressBar.setValue(progress);
    }
  }
}
class GradientPalletProgressBarUI extends BasicProgressBarUI {

  public GradientPalletProgressBarUI() {
    super();
  }
  @Override 
  public void paintDeterminate(Graphics g, JComponent c) {
    if (!(g instanceof Graphics2D)) {
      return;
    }
    Insets b = progressBar.getInsets(); // area for border
    int barRectWidth  = progressBar.getWidth()  - (b.right + b.left);
    int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
    if (barRectWidth <= 0 || barRectHeight <= 0) {
      return;
    }
    int cellLength = getCellLength();
    int cellSpacing = getCellSpacing();

    int amountFull = getAmountFull(b, barRectWidth, barRectHeight);

    if(progressBar.getOrientation() == JProgressBar.HORIZONTAL) {

      float x = amountFull / (float)barRectWidth;
      g.setColor(Color.GREEN);
      g.fillRect(b.left, b.top, amountFull, barRectHeight);

    } else { // VERTICAL
    
    }
    if(progressBar.isStringPainted()) {
      paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
    }
  }
}