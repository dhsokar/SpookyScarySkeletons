import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
//VCSTEST.


public class SpookyScarySkeletons extends JPanel {
    public static void main(String[] args){
        JFrame window = new JFrame("Spooky Scary Skeletons");
        SpookyScarySkeletons content = new SpookyScarySkeletons();
        window.setContentPane(content);
        window.setSize(800,600);
        window.setLocation(50,15);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);

    }

    private Timer timer;
    private int width, height;
    private Skeleton skeleton;
    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage img3;
    private Trumpet trumpet;
    private Owl owl;
    private int speed = 30;


    public SpookyScarySkeletons() {
        setBackground(new Color(56, 132, 34));

        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skeleton.edgeDetect();
                trumpet.updateForNewFrame();
                owl.updateForNewFrame();
                repaint();
            }
        };

        timer = new Timer(30, action);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocus();
            }
        } );

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                timer.start();
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                timer.stop();
                repaint();
            }
        } );

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_LEFT){
                    skeleton.centerX -= speed;
                }
                else if (code == KeyEvent.VK_RIGHT){
                    skeleton.centerX += speed;
                }
                else if(code == KeyEvent.VK_UP){
                    skeleton.centerY -= speed;
                }
                else if(code == KeyEvent.VK_DOWN){
                    skeleton.centerY += speed;
                }

            }
        });

        try{
            img1 = ImageIO.read(getClass().getResource("SkeletonBlack36x70.png"));
        }catch (IOException ex){
            ex.printStackTrace();
        }

        try{
            img2 = ImageIO.read(getClass().getResource("Trumpet60x30.png"));
        }catch (IOException ex){
            ex.printStackTrace();
        }

        try{
            img3 = ImageIO.read(getClass().getResource("Owl32x28.png"));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Font font = new Font("Serif", Font.BOLD ,25);
        Graphics2D g2 = (Graphics2D) g.create() ;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if ( skeleton == null) {
            width = getWidth();
            height = getHeight();
            skeleton = new Skeleton();
            trumpet = new Trumpet();
            owl = new Owl();
        }

        g.setFont(font);
        g.setColor(new Color(255, 157, 0));

        if(hasFocus()) {
            String score = String.valueOf(skeleton.score);
            g.drawString("Score: " + score, 10, 20);
            skeleton.draw(g);
            trumpet.draw(g);
            owl.draw(g);
        }else{
            g.drawString("The Spooky Scary Skeleton has lost his trumpet!", width /2 -250, height /2 - 50);
            g.drawString("Help him catch it by using the arrow keys.", width /2 -250, height /2 );
            g.drawString("Beware the OWL for he does not like trumpets.", width /2 -250, height /2 + 50);
            g.drawString("Click to play!", width /2 -250, height /2 + 100);
        }

    }

    private class Skeleton  {
        int centerX, centerY;
        int score = 0;
        Skeleton() {
            centerX = width/2;
            centerY = height/2;
        }
        void edgeDetect() {
            if (Math.abs(centerX - trumpet.centerX) <= 50 && Math.abs(centerY - trumpet.centerY) < 50) {
                trumpet.isCaught = true;
                trumpet.frameNumber = 1;
                score++;
                centerX = width/2;
                centerY = height/2;
                owl.reset();
            } else {
                if (centerX < 0)
                    centerX = 0;
                else if (centerX > width)
                    centerX = width;
                else if (centerY < 0)
                    centerY = 0;
                else if (centerY > height)
                    centerY = height;
            }
        }
        void draw(Graphics g2){
            g2.drawImage(img1,centerX - 18,centerY -35, SpookyScarySkeletons.this);
        }
    }

    private class Trumpet{
        int centerX, centerY;
        boolean isClockwise;
        boolean isCaught;
        int speed = 30;
        int top = 50;
        int bottom = height - 50;
        int left = 50;
        int right = width -50;
        int frameNumber = 0;
        Font font = new Font("Serif", Font.BOLD ,50);






        Trumpet(){
            centerX = width/(int) ((Math.random()*5 +1) );
            if((int) (Math.random()*2 +1) > 1){
                centerY = bottom;
            }else{
                centerY = top;
            }
            isClockwise = true;
        }

        void updateForNewFrame(){


            if(isCaught){
                frameNumber++;
                if(frameNumber == 30){
                    centerX = width/(int) ((Math.random()*5 +1) );
                    if((int) (Math.random()*2 +1) > 1){
                        centerY = bottom;
                    }else{
                        centerY = top;
                    }
                    isCaught = false;
                }
            }else {
                if(Math.random() < 0.04)
                    isClockwise = ! isClockwise;

                if (isClockwise) {
                    if (centerX >= right && centerY <= bottom) {
                        centerY += speed;
                    } else if (centerY >= bottom && centerX >= left) {
                        centerX -= speed;
                    } else if (centerX <= left && centerY >= top) {
                        centerY -= speed;
                    } else if (centerY <= top && centerX <= right) {
                        centerX += speed;
                    }
                }

                if (!isClockwise) {
                    if (centerX >= right && centerY >= top) {
                        centerY -= speed;
                    } else if (centerX >= left && centerY <= top) {
                        centerX -= speed;
                    } else if (centerX <= left && centerY <= bottom) {
                        centerY += speed;
                    } else if (centerX <= right && centerY >= bottom) {
                        centerX += speed;
                    }
                }
            }
        }

        void draw(Graphics g2) {
            if (isCaught) {
                g2.setColor(new Color(255, 148, 28));
                g2.setFont(font);
                g2.drawString("DOOT DOOT!", width / 2 - 150 , height / 2 + 100 );
            } else {
                g2.drawImage(img2,centerX - 30, centerY - 17, SpookyScarySkeletons.this);
                //g2.setColor(Color.ORANGE);
                //g2.fillRoundRect(centerX - 30, centerY - 15, 60, 30, 5, 5);
            }
        }
    }

    private class Owl {

        int centerX, centerY;
        int targetX, targetY;
        int speed = 1;
        boolean caught;
        int frameNumber = 0;
        Font font = new Font("Serif", Font.BOLD ,50);

        Owl(){

            if(Math.random() > 0.5){
                centerX = 150;
            }else {
                centerX = width - 150;
            }
            centerY = height / (int)(Math.random()*4 + 1);
        }

        void reset(){
            if (Math.random() > 0.5) {
                centerX = 150;
            } else {
                centerX = width - 150;
            }
            centerY = height / (int) (Math.random() * 4 + 1);
        }

        void updateForNewFrame() {
            targetX = skeleton.centerX;
            targetY = skeleton.centerY;
            if(Math.abs(centerX - skeleton.centerX) < 30 && Math.abs(centerY - skeleton.centerY) < 50){
                caught = true;
                frameNumber = 0;
                skeleton.centerY = height / 2;
                skeleton.centerX = width / 2;
                this.reset();
            }

            if (caught) {
                frameNumber++;
                if(frameNumber == 30) {
                    skeleton.score = 0;
                    caught = false;
                }
            } else {
                if (centerX < targetX) {
                    centerX += speed;
                }
                if (centerX > targetX) {
                    centerX -= speed;
                }
                if (centerY < targetY) {
                    centerY += speed;
                }
                if (centerY > targetY) {
                    centerY -= speed;
                }
            }
        }

        void draw(Graphics g2) {
            if (caught) {
                g2.setFont(font);
                g2.drawString("GAME OVER!", width / 2 - 150, height / 2 + 100);
            } else {
                //g2.setColor(new Color(110, 55, 20));
                //g2.fillRoundRect(centerX- 15, centerY - 15,30,30,20,20);
                g2.drawImage(img3,centerX-16,centerY-14, SpookyScarySkeletons.this);
            }
        }
    }
}

