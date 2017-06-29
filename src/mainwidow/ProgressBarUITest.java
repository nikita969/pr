
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

/** @see http://stackoverflow.com/questions/8884297 */
public class ProgressBarUITest extends JPanel {

    public ProgressBarUITest() {
        JProgressBar jpb = new JProgressBar();
        jpb.setUI(new MyProgressUI());
        jpb.setForeground(Color.BLUE);
        jpb.setIndeterminate(true);
        this.add(jpb);
    }

    public static class MyProgressUI extends BasicProgressBarUI {

        private Rectangle r = new Rectangle();

        @Override
        public void paintIndeterminate(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            r = getBox(r);
            g.setColor(progressBar.getForeground());
            g.fillOval(r.x, r.y, r.width, r.height);
        }
    }

  /*  public void display() {
        JFrame f = new JFrame("ProgressBarUITest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ProgressBarUITest().display();
            }
        });
    } */
} 