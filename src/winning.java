import javax.swing.*;
import java.awt.*;

public class winning extends JFrame{
    JButton exit = new JButton();
    JPanel panel  = new JPanel();
    JLabel label = new JLabel();
    winning(){
        ImageIcon image = new ImageIcon("bs.png");
        this.setIconImage(image.getImage());

        this.setTitle("BATTLESHIP");
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400,400);
        this.getContentPane().setBackground(new Color(0,0,0));

        label.setText("CONGRATS!");
        label.setForeground(new Color(0xFF01FF));
        label.setFont(new Font("MV Boli",Font.BOLD,40));

        panel.add(label);
        panel.setBackground(Color.BLACK);
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(exit);
        this.add(panel,BorderLayout.CENTER);

        panel.setBounds(500,50,250,400);
        exit.setText("EXIT!");
        exit.addActionListener(e -> this.dispose());

        this.add(panel);
        this.setVisible(true);
    }
}