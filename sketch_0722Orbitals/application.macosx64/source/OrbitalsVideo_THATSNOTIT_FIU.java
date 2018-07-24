import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 
import java.util.concurrent.*; 
import processing.video.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class OrbitalsVideo_THATSNOTIT_FIU extends PApplet {





final int MAX_BRIGHTNESS = 100;
final int previsSizeX = 400;
final int previsSizeY = 400;
final int LEDS_PER_NODE = 53;
final int NUM_NODES = 25;
final int PIXEL_SIZE_X = 3;
final int PIXEL_SIZE_Y = 5;

Movie myMovie;
PImage image;

Serial myPort;
int remap[] = 
  {
  23, 22, 21, 20, 24, 
  19, 15, 11, 7, 3, 
  18, 14, 10, 6, 2, 
  17, 13, 9, 5, 1, 
  16, 12, 8, 4, 0
};
int reverse[] = 
  {
  -1, 1, -1, 1, -1, 
  -1, -1, -1, 1, -1, 
  1, -1, -1, 1, 1, 
  -1, 1, -1, -1, 1,
  -1, -1, 1, 1, 1
};
byte buffer[] = new byte[1326];
int cBuffer[] = new int[1326];
BlockingQueue newImageQueue;



Boolean isNewFrame = false;
Boolean DEBUG = false;
public void setup()
{

  newImageQueue = new ArrayBlockingQueue(2);
  
  surface.setResizable(true);
  //surface.setSize(previsSizeX, previsSizeY);

  myMovie = new Movie(this, "tni.mov");
  myMovie.loop();

  image = loadImage("PixelMap.png");
  myPort = new Serial(this, "/dev/cu.usbmodem3129031", 115200);
  //myPort = new Serial(this, "COM3", 115200);
}

public void draw()
{
//delay(500);
  //image(image, 0, 0);

  image(myMovie.get(0, 0, 795, 25), 0, 0);

  buffer[0] = (byte)254;

  if (isNewFrame || DEBUG) {
    isNewFrame = false;
    for (int i = 0; i < NUM_NODES; i++)
    {
      //int nodeIdx = remap[0];
      int nodeIdx = i;
      for (int j = 0; j < LEDS_PER_NODE; j++)
      {  

        //int nodeIdx = i;
        int nodeColumn = i%5;
        int nodeRow = i/5;

        int ledIdx = remap[nodeIdx]*LEDS_PER_NODE+j+1;

        //int pixelIdx = LEDS_PER_NODE*nodeColumn*PIXEL_SIZE +  PIXEL_SIZE*myMovie.width*nodeRow + j*2;
        int pixelIdx;

//pixelIdx = nodeRow*myMovie.width*2 + myMovie.width*2 + nodeColumn*3 + 2*j;


int led = j;
        if (reverse[nodeIdx] > 0)
          led = LEDS_PER_NODE-j-1;
        
          pixelIdx = LEDS_PER_NODE*nodeColumn*PIXEL_SIZE_X +  myMovie.width*nodeRow*PIXEL_SIZE_Y + (led+2*led);
        //else
         // pixelIdx = LEDS_PER_NODE*nodeColumn*PIXEL_SIZE_X +  myMovie.width*nodeRow*PIXEL_SIZE_Y +(LEDS_PER_NODE-j+1)*3;


     //   buffer[ledIdx] = (byte)127;
         
         buffer[ledIdx] = (byte) (red(myMovie.pixels[pixelIdx]));
         //buffer[ledIdx] += (byte) myMovie.pixels[pixelIdx-1];
         //buffer[ledIdx] += (byte) myMovie.pixels[pixelIdx+1];
         
         //buffer[ledIdx] =  byte((float)buffer[ledIdx] / 3.0f);
         
       // buffer[ledIdx] = 0;
        //println(buffer[ledIdx]);
        //buffer[ledIdx] = (byte)image.pixels[pixelIdx];
        cBuffer[ledIdx] = image.pixels[pixelIdx];
        
        drawPrevis(i, j);
      }
    }
    //for(int i = 1; i < 1326; i++)
    // buffer[i] = 127;
    myPort.write(buffer);
  }

  //String inbuf = myPort.readString();
  //if (inbuf != null)
  // println(inbuf);
}


// Called every time a new frame is available to read
public void movieEvent(Movie m) {
  m.read();

  isNewFrame = true;
}
public void drawPrevis(int i, int j)
{
  //int ledIdx = i*LEDS_PER_NODE+j+1;
int ledIdx = remap[i]*LEDS_PER_NODE+j+1;

  pushMatrix();
  translate(150, 150);

  float angle = ((float)j)/((float)LEDS_PER_NODE);
  float size = min(width, height);

  float x = (size/5)*(i%5) + (size/15)*cos(TWO_PI*angle);
  float y = (size/5)*(i/5) + (size/15)*sin(TWO_PI*angle);
  noStroke();
  fill(buffer[ledIdx]);
  fill(cBuffer[ledIdx]);
  ellipse(x, y, size/150, size/150);
  popMatrix();
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "OrbitalsVideo_THATSNOTIT_FIU" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
