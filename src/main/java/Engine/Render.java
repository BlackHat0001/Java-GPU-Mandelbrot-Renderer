package Engine;

import Render.Fractals.BurningShip;
import Render.Fractals.BurningShipDouble;
import Render.Fractals.Mandelbrot;
import Render.Fractals.MandelbrotDouble;
import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;
import com.aparapi.device.JavaDevice;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.exception.QueryFailedException;
import com.aparapi.internal.kernel.KernelManager;
import com.aparapi.internal.opencl.OpenCLPlatform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Render {

    final Kernel kernel;


    public final float[] buffer;
    final float[] data;

    float frame = 1;

    public Range range;

    public BufferedImage render;

    public Render() {
        this.buffer = new float[1920 * 1080 * 3];
        this.data = new float[3];

        this.kernel = new BurningShipDouble(1920, 1080, buffer, data);
        //range = device.createRange2D(1920, 1080);


        List<OpenCLPlatform> devices = OpenCLPlatform.getUncachedOpenCLPlatforms();
        System.out.println(kernel.getTargetDevice().toString());

        for(OpenCLPlatform device : devices) {
            System.out.println(device.getName());
        }

        Device device = JavaDevice.ALTERNATIVE_ALGORITHM;
        device.setMaxWorkGroupSize(256);
        range = device.createRange(1920 * 1080, 256);

        System.out.println(range.getDevice());

        try {
            System.out.println(kernel.getKernelMaxWorkGroupSize(device));
            System.out.println(Arrays.toString(kernel.getKernelCompileWorkGroupSize(device)));
        } catch (QueryFailedException e) {
            e.printStackTrace();
        }



    }

    public void NewRender() {


        data[0] = (float) Math.abs(Math.sin(frame)*8)+1;
        System.out.println("| " + data[0]);
        this.kernel.put(data);

        this.kernel.execute(range);

        this.kernel.get(this.buffer);

        this.kernel.getAccumulatedExecutionTime();

        System.out.println(this.kernel.getExecutionTime());

        BufferedImage image = new BufferedImage(1921, 1081, BufferedImage.TYPE_INT_ARGB);

        for(int x = 0; x < 3*(1920*1080); x += 3) {
            float colr = clamp(buffer[x], 0, 1);
            float colg = clamp(buffer[x+1], 0, 1);
            float colb = clamp(buffer[x+2], 0, 1);
            int cx = x/3 % 1920;
            //int cy = -1 * ((x/3 / 1920) - 1080);
            int cy = ((x/3 / 1920));

            image.setRGB(cx, cy, new Color(colr, colg, colb).getRGB());

        }


        render = image;

        frame+=0.01f;

    }

    float clamp(final float val, final float min, final float max) {
        return Math.max(min, Math.min(max, val));
    }
}
