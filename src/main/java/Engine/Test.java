package Engine;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;

public class Test {
    static double[] image = new double[2560 * 1440 * 3];
    public static void main(String args[] ) throws IOException{
        JFrame frame = new JFrame();
        frame.setSize(2560, 1440);
        frame.setContentPane(jf);
        frame.setVisible(true);
        image = render();
        jf.repaint();
        /*
        BufferedImage bf = new BufferedImage(2560, 1440, ColorSpace.TYPE_RGB);
        Graphics gg =  bf.getGraphics();
        for(int i = 0; i < image.length; i+=3){
            double r = Math.max(0, Math.min(1, image[i]));
            double g = Math.max(0, Math.min(1, image[i+1]));
            double b = Math.max(0, Math.min(1, image[i+2]));
            int x = i/3 % 2560;
            int y = -1*((x/3 /2560)-1440);
            gg.setColor(new Color((float)r , (float)g, (float)b));
            gg.fillRect(x, y, 2560, 1440);
        }
        File file = new File("fractal.jpg");
        ImageIO.write(bf, "jpg", file);
        */
    }
    public static double[] render(){
        double zoom = 1.25;
        double[] buffer = new double[2560 * 1440 * 3];
        for(int bb = 0; bb<buffer.length; bb+=3){
            double px = bb % 2560;
            double py = bb / 2560;
            double cx = zoom*(((-2560)+(px*2))/1440);
            double cy = zoom*(((-1440)+(py*2))/1440);
            double zr = 0.000000000000001;
            double zi = 0;
            double thresh = 6;
            double maxIterations = 100;
            int i;
            double power = 2;
            for(i = 0; i < maxIterations; i++){
                double arg = Math.atan(zi/zr);
                double mod = Math.sqrt(Math.pow(zr, 2)+Math.pow(zi, 2));
                zr = (Math.pow(mod, power)*Math.cos(arg*power))+cx;
                zi = (Math.pow(mod, power)*Math.sin(arg*power))+cy;
                if(((zr * zr)+(zi * zi)) >= thresh){
                    break;
                }
            }
            double r = 0;
            double g = 0;
            double b = 0;
            if(i < maxIterations){
                r = 0.5 + 0.5 * Math.cos(3 + i * 0.075 * power + 0.0) * 0.5;
                g = 0.5 + 0.5 * Math.cos(3 + i * 0.075 * power + 0.6) * 0.5;
                b = 0.5 + 0.5 * Math.cos(3 + i * 0.075 * power + 1) * 0.5;
            }
            buffer[bb] = r;
            buffer[bb+1] = g;
            buffer[bb+2] = b;
        }
        return buffer;
    }
    static JPanel jf = new JPanel() {
        RenderingHints QUALITY_RENDER = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        RenderingHints ANTIALIASING = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        @Override
        protected void paintComponent( Graphics g) {
            Graphics2D gx = (Graphics2D)g;
            gx.addRenderingHints(this.ANTIALIASING);
            gx.addRenderingHints(this.QUALITY_RENDER);
            for(int x = 0; x < 3*(2560*1440); x += 3) {
                float colr = (float)Math.max(0, Math.min(1, image[x]));
                float colg = (float)Math.max(0, Math.min(1, image[x+1]));
                float colb = (float)Math.max(0, Math.min(1, image[x+2]));
                int cx = x/3 % 2560;
                int cy = -1 * ((x/3 / 2560) - 1440);
                gx.setColor(new Color(colr, colg, colb));

                gx.fillRect(cx, cy, 1, 1);

            }
        }
    };

}

