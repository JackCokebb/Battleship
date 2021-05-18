import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame implements ActionListener{
    JPanel panel = new JPanel();
    JButton pacman = new JButton();
    JButton battleship_easy = new JButton();
    JButton battleship_hard = new JButton();
    JButton exit = new JButton();

    MyFrame(){
        panel.setBackground(Color.BLACK);
        panel.setBounds(500,50,250,400);

        JLabel label_p = new JLabel();
        label_p.setText("Choose a game to start~");
        label_p.setForeground(new Color(0xFF00FF));
        label_p.setFont(new Font("MV Boli",Font.BOLD,20));

        panel.add(label_p);

        pacman.setPreferredSize(new Dimension(40,40));
        pacman.setText("Pacman!");
        battleship_easy.setText("Battleship! - Easy");
        battleship_easy.addActionListener(this);
        battleship_hard.setText("Battleship! - Hard");
        battleship_hard.addActionListener(this);
        exit.setText("EXIT!");
        exit.addActionListener(e -> this.dispose());

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(pacman);
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(battleship_easy);
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(battleship_hard);
        panel.add(Box.createRigidArea(new Dimension(0, 60)));
        panel.add(exit);
        this.add(panel);

        ImageIcon img = new ImageIcon("tlos.png");
        Border border = BorderFactory.createLineBorder(Color.magenta,2);

        JLabel label = new JLabel();
        label.setIcon(img);
        label.setForeground(new Color(0xFFFFFF));
        label.setBackground(Color.black);
        label.setOpaque(true);
        label.setBorder(border);
        label.setBounds(0,50,400,400);
        this.add(label);

        this.setTitle("JAVA TERM PROJECT");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800,600);
        this.setVisible(true);
        this.setLayout(null);

        ImageIcon image = new ImageIcon("emoticon.png");
        this.setIconImage(image.getImage());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==battleship_easy){
            this.dispose();
            battlefield battlefield1 = new battlefield();
            battlefield battlefield2 = new battlefield();
            
            field game_b=new field(battlefield1, battlefield2,"easy");
            game_b.connect();
           

        }
        else if (e.getSource()==battleship_hard){
            this.dispose();
            battlefield battlefield1 = new battlefield();
            battlefield battlefield2 = new battlefield();
            field game_b=new field(battlefield1, battlefield2,"hard");
            game_b.connect();

        }
    }
}
