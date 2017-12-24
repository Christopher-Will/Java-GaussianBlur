import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class MyImage extends JPanel {
    private boolean showBlurred; //whether we should show the blurred image or not
    private BufferedImage bufferedImage = null; //the non-blurred image
    private BufferedImage blurredBufferedImage = null; //the blurred image

    //the following 5 arrays are the kernels for each size needed for thsi program. Their values came from the link
    //provided in the project spec.
    private final float[] kernel3x3 = {0.077847f, 0.123317f, 0.077847f, 0.123317f,
            0.195346f, 0.123317f, 0.077847f, 0.123317f, 0.077847f};

    private final float[] kernel5x5 = {0.003765f, 0.015019f, 0.023792f, 0.015019f, 0.003765f, 0.015019f, 0.059912f,
            0.094907f, 0.059912f, 0.015019f, 0.023792f, 0.094907f, 0.150342f, 0.094907f, 0.023792f, 0.015019f,
            0.059912f, 0.094907f, 0.059912f, 0.015019f, 0.003765f, 0.015019f, 0.023792f, 0.015019f, 0.003765f};

    private final float[] kernel7x7 = {0.000036f, 0.000363f, 0.001446f, 0.002291f, 0.001446f, 0.000363f, 0.000036f,
            0.000363f, 0.003676f, 0.014662f, 0.023226f, 0.014662f, 0.003676f, 0.000363f, 0.001446f, 0.014662f,
            0.058488f, 0.092651f, 0.058488f, 0.014662f, 0.001446f, 0.002291f, 0.023226f, 0.092651f, 0.146768f,
            0.092651f, 0.023226f, 0.002291f, 0.001446f, 0.014662f, 0.058488f, 0.092651f, 0.058488f, 0.014662f,
            0.001446f, 0.000363f, 0.003676f, 0.014662f, 0.023226f, 0.014662f, 0.003676f, 0.000363f, 0.000036f,
            0.000363f, 0.001446f, 0.002291f, 0.001446f, 0.000363f, 0.000036f};

    private final float[] kernel9x9 = {0f, 0.000001f, 0.000014f, 0.000055f, 0.000088f, 0.000055f, 0.000014f, 0.000001f,
            0f, 0.000001f, 0.000036f, 0.000362f, 0.001445f, 0.002289f, 0.001445f, 0.000362f, 0.000036f, 0.000001f,
            0.000014f, 0.000362f, 0.003672f, 0.014648f, 0.023205f, 0.014648f, 0.003672f, 0.000362f, 0.000014f,
            0.000055f, 0.001445f, 0.014648f, 0.058434f, 0.092566f, 0.058434f, 0.014648f, 0.001445f, 0.000055f,
            0.000088f, 0.002289f, 0.023205f, 0.092566f, 0.146634f, 0.092566f, 0.023205f, 0.002289f, 0.000088f,
            0.000055f, 0.001445f, 0.014648f, 0.058434f, 0.092566f, 0.058434f, 0.014648f, 0.001445f, 0.000055f,
            0.000014f, 0.000362f, 0.003672f, 0.014648f, 0.023205f, 0.014648f, 0.003672f, 0.000362f, 0.000014f,
            0.000001f, 0.000036f, 0.000362f, 0.001445f, 0.002289f, 0.001445f, 0.000362f, 0.000036f, 0.000001f, 0f,
            0.000001f, 0.000014f, 0.000055f, 0.000088f, 0.000055f, 0.000014f, 0.000001f, 0f};

    private final float[] kernel1x11 = {0f, 0f, 0f, 0f, 0.000001f, 0.000001f, 0.000001f, 0f, 0f, 0f, 0f, 0f, 0f,
            0.000001f, 0.000014f, 0.000055f, 0.000088f, 0.000055f, 0.000014f, 0.000001f, 0f, 0f, 0f, 0.000001f,
            0.000036f, 0.000362f, 0.001445f, 0.002289f, 0.001445f, 0.000362f, 0.000036f, 0.000001f, 0f, 0f, 0.000014f,
            0.000362f, 0.003672f, 0.014648f, 0.023204f, 0.014648f, 0.003672f, 0.000362f, 0.000014f, 0f, 0.000001f,
            0.000055f, 0.001445f, 0.014648f, 0.058433f, 0.092564f, 0.058433f, 0.014648f, 0.001445f, 0.000055f, 0.000001f,
            0.000001f, 0.000088f, 0.002289f, 0.023204f, 0.092564f, 0.146632f, 0.092564f, 0.023204f, 0.002289f, 0.000088f,
            0.000001f, 0.000001f, 0.000055f, 0.001445f, 0.014648f, 0.058433f, 0.092564f, 0.058433f, 0.014648f, 0.001445f,
            0.000055f, 0.000001f, 0f, 0.000014f, 0.000362f, 0.003672f, 0.014648f, 0.023204f, 0.014648f, 0.003672f,
            0.000362f, 0.000014f, 0f, 0f, 0.000001f, 0.000036f, 0.000362f, 0.001445f, 0.002289f, 0.001445f, 0.000362f,
            0.000036f, 0.000001f, 0f, 0f, 0f, 0.000001f, 0.000014f, 0.000055f, 0.000088f, 0.000055f, 0.000014f, 0.000001f,
            0f, 0f, 0f, 0f, 0f, 0f, 0.000001f, 0.000001f, 0.000001f, 0f, 0f, 0f, 0f};

    //apply the kernel and convolution the the image
    public void applyKernel(int kernelSize){
        if(bufferedImage == null) return;
        float[] kernelValues = getKernelValues(kernelSize); //get the right kernel depending on the size of the kernelSlider
        Kernel kernel = new Kernel(kernelSize, kernelSize, kernelValues);

        //create a ConvolveOp object for the convolution
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        //create a copy of the current bufferedImage
        BufferedImage bufferedImageCopy = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImageCopy.createGraphics();
        g2d.drawImage(bufferedImage, 0, 0, null);
        convolveOp.filter(bufferedImageCopy, blurredBufferedImage); //apply the filter to blur the image
        showBlurred = true; //call paintComponent and show the blurred image
        this.repaint();

    }

    //takes the size of the kernel from the kernelSlider and returns the corresponding array
    private float[] getKernelValues(int kernelSize){
        float[] retKernel; //the array to return
        if(kernelSize == 3){
            retKernel = kernel3x3;
        }else if(kernelSize == 5){
            retKernel = kernel5x5;
        }else if(kernelSize == 7){
            retKernel = kernel7x7;
        }else if(kernelSize == 9){
            retKernel = kernel9x9;
        }else{
            retKernel = kernel1x11; //only other option is to return the 11x11 kernel
        }
        return retKernel;
    }

    //constructor for this class takes a BufferedImage and assigns it to bufferedImage
    public MyImage(BufferedImage img){
        bufferedImage = img;
        blurredBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        setPreferredSize( new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
        showBlurred = false;
        this.repaint(); //draw the bufferedImage with no blurring
    }

    //draw the BufferedImage, using the boolean to know whether the blurred or non-blurred should be drawn
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bufferedImage, 0, 0, this);
        if(showBlurred){
            g2d.drawImage(blurredBufferedImage, 0, 0, this);
        }else{
            g2d.drawImage(bufferedImage, 0, 0, this);
        }

    }

    //update the bufferedImage to the given value and repaint it
    public void setImage(BufferedImage img){
        if(img == null) return;
        bufferedImage = img;
        blurredBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
        showBlurred = false;
        this.repaint();
    }

    //just call the repaint method to display the non-blurred image
    public void showImage(){
        if(bufferedImage == null) return;
        showBlurred = false;
        this.repaint();
    }
}
