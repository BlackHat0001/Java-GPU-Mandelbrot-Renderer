package Engine;

import com.aparapi.Kernel;
import com.aparapi.device.Device;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.kernel.KernelPreferences;
import com.aparapi.internal.opencl.OpenCLPlatform;

import javax.swing.*;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Main {

    static JFrame jfx = new JFrame();

    static float[] rgb = new float[0];

    static BufferedImage renderImage;

    static JPanel jf = new JPanel() {
        RenderingHints QUALITY_RENDER = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        RenderingHints ANTIALIASING = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



        @Override
        protected void paintComponent( Graphics g) {
            BufferedImage image = new BufferedImage(1921, 1081, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gx = (Graphics2D)g;
            gx.addRenderingHints(this.ANTIALIASING);
            gx.addRenderingHints(this.QUALITY_RENDER);
            for(int x = 0; x < 3*(1920*1080); x += 3) {
                float colr = clamp(rgb[x], 0, 1);
                float colg = clamp(rgb[x+1], 0, 1);
                float colb = clamp(rgb[x+2], 0, 1);
                int cx = x/3 % 1920;
                int cy = -1 * ((x/3 / 1920) - 1080);

                image.setRGB(cx, cy, new Color(colr, colg, colb).getRGB());

            }
            gx.drawImage(renderImage, 0, 0, 1920, 1080, this);
            System.out.println("Repaint done");
        }
    };

    public static void main(String[] args) {
        Render render = new Render();
        render.NewRender();
        renderImage = render.render;

        rgb = render.buffer;
        jfx.setUndecorated(false);
        jfx.setSize(1920, 1080);
        jfx.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jfx.setContentPane(jf);
        jfx.setVisible(true);

        float targetZoom = 0f;
        float zoom = 1.25f;


        jf.repaint();

        while(true) {
            render.NewRender();
            rgb = render.buffer;
            renderImage = render.render;
            jf.repaint();

        }


    }


    static float clamp(final float val, final float min, final float max) {
        return Math.max(min, Math.min(max, val));
    }


}
