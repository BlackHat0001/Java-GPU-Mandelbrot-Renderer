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

import java.util.Arrays;
import java.util.List;

public class Main {

    static JFrame jfx = new JFrame();

    static float[] rgb = new float[0];

    static JPanel jf = new JPanel() {
        RenderingHints QUALITY_RENDER = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        RenderingHints ANTIALIASING = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        @Override
        protected void paintComponent( Graphics g) {
            Graphics2D gx = (Graphics2D)g;
            gx.addRenderingHints(this.ANTIALIASING);
            gx.addRenderingHints(this.QUALITY_RENDER);
            for(int x = 0; x < 3*(1920*1080); x += 3) {
                float colr = clamp(rgb[x], 0, 1);
                float colg = clamp(rgb[x+1], 0, 1);
                float colb = clamp(rgb[x+2], 0, 1);
                int cx = x/3 % 1920;
                int cy = -1 * ((x/3 / 1920) - 1080);
                gx.setColor(new Color(colr, colg, colb));

                gx.fillRect(cx, cy, 1, 1);

            }
        }
    };

    public static void main(String[] args) {
        Render render = new Render();
        render.Render();
        rgb = render.buffer;

        jfx.setSize(1920, 1080);
        jfx.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jfx.setContentPane(jf);
        jfx.setVisible(true);

        float targetZoom = 0f;
        float zoom = 1.25f;


        jf.repaint();



    }


    static float clamp(final float val, final float min, final float max) {
        return Math.max(min, Math.min(max, val));
    }


}
