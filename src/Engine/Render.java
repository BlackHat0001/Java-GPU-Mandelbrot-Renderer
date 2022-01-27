package Engine;

import Render.Fractals.Mandelbrot;
import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.internal.kernel.KernelManager;

import java.util.Arrays;
import java.util.List;

public class Render {

    final Kernel kernel;


    public final float[] buffer;

    public Render() {
        this.buffer = new float[1920 * 1080 * 3];


        this.kernel = new Mandelbrot(1920, 1080, buffer);


    }

    public void Render() {


        //range = device.createRange2D(1920, 1080);


        List<OpenCLDevice> devices = OpenCLDevice.listDevices(null);
        for(OpenCLDevice device : devices) {
            System.out.println(device.getName());
        }
        Device device =  KernelManager.instance().bestDevice();
        Range range = device.createRange(1920 * 1080);

        System.out.println(devices.size());

        this.kernel.execute(range);

        this.kernel.get(this.buffer);

        this.kernel.getAccumulatedExecutionTime();

        System.out.println(this.kernel.getExecutionTime());

        this.kernel.dispose();

    }

}
