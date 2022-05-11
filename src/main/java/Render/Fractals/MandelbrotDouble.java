package Render.Fractals;

import com.aparapi.Kernel;

public class MandelbrotDouble extends Kernel {

    final int height;
    final int width;

    final float[] buffer;
    final float[] data;

    public MandelbrotDouble(final int width, final int height, final float[] buffer, final float[] data) {

        this.height = height;
        this.width = width;

        this.buffer = buffer;
        this.data = data;

        setExplicit(true);
        put(this.buffer);
        put(this.data);

    }

    @Override
    public void run() {
        final int index = this.getGlobalId();
        final int pickIndex = this.height / 2 * this.width + this.width / 2;

        final double currentx = index % this.width;//= - this.width * 0.5F + 0.5F;
        final double currenty = index / this.width;// - this.height * 0.5F + 0.5F;

        Render(currentx, currenty);

    }

    void Render(double pix, double piy) {
        final int id = this.getGlobalId();
        final double iTime = data[0]*0.005f;

        final int AA = 2;
        final double power = 2;
        final double zoom = 1.92E-2;//*(1-abs(sin(iTime)));//(1/abs(sin(iTime)));

        final double x = pow(2, 2);

        double colx = 0;
        double coly = 0;
        double colz = 0;
        for(int a=0; a<AA; a++) {
            for(int b=0; b<AA; b++) {

                final double aox = (double) a / (double) AA - 0.5f;
                final double aoy = (double) b / (double) AA - 0.5f;

                final double crt = zoom * (((this.width * -1.0f) + ((pix + aox) * 2.0f)) / this.height);
                final double cit = zoom * (((this.height * -1.0f) + ((piy + aoy) * 2.0f)) / this.height);

                final double cr = crt + 0.281717921930775;
                final double ci = cit + 0.5771052841488505;

                double r = 0.0000000000000000001f;
                double i = 0;
                int iter = 0;
                final double thresh = 16;
                final int maxIter = 2000;
                for (int k=0; k<maxIter; k++) {
                    final double argz = atan(i/r);
                    final double modz = len(r, i);
                    r = (pow(modz, power) * cos(argz * power)) + cr;
                    i = (pow(modz, power) * sin(argz * power)) + ci;

                    if(dot(r, i, r, i) > thresh) {
                        k = maxIter;
                    } else {
                        iter++;
                    }

                }
                if(iter < maxIter) {
                    //https://iquilezles.org/www/articles/mset_smooth/mset_smooth.htm
                    final double sn = iter - log2(log2(dot(r, i, r, i))/(log2(thresh)))/log2(power);

                    colx = colx + (0.5f + 0.5f*cos(3.0f + sn*0.075f*power + 0.0f))*0.2f;
                    coly = coly + (0.5f + 0.5f*cos(3.0f + sn*0.075f*power + 0.8f))*0.2f;
                    colz = colz + (0.5f + 0.5f*cos(3.0f + sn*0.075f*power + 5.8f))*0.2f;

                }


            }
        }

        colx = colx / AA*AA;
        coly = coly / AA*AA;
        colz = colz / AA*AA;

        buffer[id*3] = (float)colx;
        buffer[id*3+1] = (float)coly;
        buffer[id*3+2] = (float)colz;

    }

    double dot(final double ax, final double ay, final double bx, final double by) {
        return (ax * bx) + (ay * by);
    }

    double len(final double ax, final double ay) {
        return sqrt(pow(ax, 2f) + pow(ay, 2f));
    }

    double smoothstep(final double a, final double b, final double x) {
        final double t = clamp((x - a) / (b - a), 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }

    double clamp(final double val, final double min, final double max) {
        return Math.max(min, Math.min(max, val));
    }

}
