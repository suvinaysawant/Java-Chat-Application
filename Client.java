import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {
    

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Component
    private JLabel heading = new JLabel("Client Base");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();


    private Font font= new Font("Roboto",Font.PLAIN,20);


    //Constructor
    public Client()
    {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection done...");


            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
          // e.printStackTrace();
        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
               if(e.getKeyCode()==10 ){
                //System.out.println("You have pressed Enter.");
                String contentToSend=messageInput.getText();
                messageArea.append("Me :"+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
               }
                
            }
        });
    }

    private void createGUI()
    {
        //Code GUI
        this.setTitle("Lab Messanger[END]");
        this.setSize(700,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        


        //Code for Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        //Name and Logo
        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        messageArea.setEditable(false);
        //Frame Layout
        this.setLayout(new BorderLayout());


        //Component To Frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane =new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);

    } 

    //Functons
       public void startReading()
        {
            //Thred is reading
            Runnable r1=()->{
                    System.out.println("Reading Started...");
                    try{
                    while(true)
                    {
                        
                        String msg=br.readLine();
                        if(msg.equals("exit"))
                        {
                           // System.out.println("Server terminated the chat.");
                            JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                            messageInput.setEnabled(false);
                            socket.close();
                            break;
                        }
    
                       // System.out.println("Server :"+msg);
                       messageArea.append("Server:"+msg+"\n");
                   
                    }
                }catch(Exception e){
                    //e.printStackTrace();
                    System.out.println("Connection Closed...");
                } 
            };
    
            new Thread(r1).start();
        }
    
        public void startWriting()
    {
        //thread write and send
        Runnable r2=()->{
            System.out.println("Writer Started...");
            try{
                while(true && !socket.isClosed())
                {
                   

                        BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                        String content = br1.readLine();
                        out.println(content);
                        out.flush();

                        if(content.equals("exit"))
                        {
                            socket.close();
                            break;
                        }
                       
                }
                System.out.println("Connection Closed...");
            }catch(Exception e){
              e.printStackTrace();
              
            } 
        };
        new Thread(r2).start();
    }


    public static void main(String[] args){
        System.out.println(" This is client...");
        new Client();
    }
}
