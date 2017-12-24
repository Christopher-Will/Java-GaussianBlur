/*
Christopher Will
CS335 Exercise 4
11/3/2017
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GaussianBlur extends JFrame {
    private MyImage view; //this will be the actual image that you can blur
    private BufferedImage buffImage; //the image that we load the 1st time the program is run
    private JSlider kernelSlider; //slider which controls the size of the kernel
    private JButton applyKernelButton; //button to apply the kernel and do the convolution
    private JLabel kernelLabel;
    private JPanel displayPanel; //panel to hold all the sliders and buttons
    private JMenuBar menuBar; //menu to open a image from a file or exit the program
    private JButton resetButton; //resets the image
    private JButton quitButton; //quits the program

    //build the display for the sliders and buttons
    private void buildDisplay(){
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        //kernel value is twice the slider value + 1. So 1 -> 3, 2 -> 5, etc...
        kernelSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 5, 1);
        kernelSlider.setMajorTickSpacing(1);
        kernelSlider.setPaintTicks(true);
        kernelLabel = new JLabel("Kernel Size: 3x3");
        displayPanel.add(kernelLabel);
        displayPanel.add(kernelSlider);
        applyKernelButton = new JButton("Apply Kernel");
        displayPanel.add(Box.createRigidArea(new Dimension(10, 10))); //add white space between the buttons
        displayPanel.add(applyKernelButton);
        resetButton = new JButton("Reset");
        displayPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        displayPanel.add(resetButton);
        displayPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        quitButton = new JButton("Quit");
        displayPanel.add(quitButton);
    }

    //builds the file IO menu to load in a new image to apply the blur to
    private void buildMenu(){
        JFileChooser fileChooser = new JFileChooser("."); //starts at the current directory
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem exitProgram = new JMenuItem("Exit");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(GaussianBlur.this);
                if(returnValue == JFileChooser.APPROVE_OPTION){ //user selected a file so try and load it in
                    File file = fileChooser.getSelectedFile(); //the file the user selected
                    try{
                        buffImage = ImageIO.read(file);
                    }catch(IOException e1){
                        //e1.printStackTrace(); can uncomment for debugging
                    }
                    //set the views image as the image selected
                    view.setImage(buffImage);
                    view.showImage();
                }
            }
        });
        //exit the program upon clicking this
        exitProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(openFile);
        fileMenu.add(exitProgram);
        menuBar.add(fileMenu);
    }


    private GaussianBlur(){
        JFrame frame = new JFrame("Gaussian Blur");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildDisplay();
        buildMenu();
        frame.setJMenuBar(menuBar);
        //update the label for the size of the kernel when this is moved
        kernelSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                final int newKernelSize = (kernelSlider.getValue() * 2) + 1;
                kernelLabel.setText("Kernel Size: " + newKernelSize + "x" + newKernelSize);
            }
        });
        //load in the default image when the program 1st starts
        view = new MyImage(readImage("src/ROME.jpg"));
        //apply the kernel and convolution upon clicking this, using the current value of the kernelSlider for the size
        applyKernelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.applyKernel((kernelSlider.getValue() * 2) + 1);
            }
        });
        //reset the image to its initial non-blurred value
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showImage();
            }
        });
        //exit the program upon clicking this button
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(displayPanel, BorderLayout.EAST);
        frame.getContentPane().add(view, BorderLayout.WEST);
        frame.setSize(900, 600);
        frame.setVisible(true);
    }

    //takes a string for the path to the image and sets the view as that image
    private BufferedImage readImage(String imageFile){
        Image image = Toolkit.getDefaultToolkit().getImage(imageFile);
        MediaTracker tracker = new MediaTracker (new Component () {});
        tracker.addImage(image, 0);
        try { tracker.waitForID (0);
        }catch(InterruptedException e){
            //e.printStackTrace();
        }
        //create buffered image of the width and height of the provided file
        BufferedImage bim = new BufferedImage(image.getWidth(this), image.getHeight(this),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bim.createGraphics();
        //draw the image and return it
        g2d.drawImage(image, 0, 0, this);
        return bim;
    }

    public static void main(String[] args) {
        new GaussianBlur();
    }
}
