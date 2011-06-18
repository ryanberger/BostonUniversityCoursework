
package cs665hw4;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComboBox;

/**
 *
 * @author Ryan
 */
public class Display implements Observer {

    Observable observable;
    JFrame frame;
    String data;

    public Display(Observable observable)
    {
        this.observable = observable;
    }

    public void update(Observable obs, Object arg)
    {
        if (obs instanceof Firm)
        {
            Firm firm = (Firm)obs;
            firm.initialize();
            firm.setFirm((String) arg);
            this.data = firm.getReport();
            frame.setTitle(data);
        }
    }

    public static void main(String[] args)
    {
        Firm firm = new Firm();
        Display display = new Display(firm);
        
        display.go();
    }

    public void go()
    {
        frame = new JFrame();
        JButton button = new JButton("Show Data");
        final JComboBox comboBox = new JComboBox();
        comboBox.addItem("2008");
        comboBox.addItem("2009");
        frame.getContentPane().add(BorderLayout.BEFORE_FIRST_LINE, comboBox);
        frame.getContentPane().add(BorderLayout.LINE_START, button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);
        frame.show();

        button.addActionListener(new ButtonListener(this, 
            observable, comboBox));
    }

}
