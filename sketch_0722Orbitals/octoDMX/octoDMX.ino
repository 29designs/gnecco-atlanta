#include <OctoWS2811.h>

const int numLEDS = 1325;
const int ledsPerStrip = 212;

const int config = WS2811_GRB | WS2811_800kHz;
DMAMEM int displayMemory[ledsPerStrip * 6 + 53];
int drawingMemory[ledsPerStrip * 6 + 53];

int currIndex;
int numRecd;
bool isGettingData;


OctoWS2811 leds(ledsPerStrip, displayMemory, drawingMemory, config);

int led = 13;
void setup() {
  pinMode(led, OUTPUT);
  digitalWrite(led, LOW);
  leds.begin();
  leds.show();

  Serial.begin(115200);
  Serial.println("Ready");
  Serial.print(leds.numPixels());
  Serial.println("Pixels");
  isGettingData = false;
}
#define GREEN  0x00FF00
#define BLUE   0x0000FF
#define YELLOW 0xFFFF00
#define PINK   0xFF1088
#define ORANGE 0xE05800
#define WHITE  0xFFFFFF

void loop() {

//leds.begin();

//for(int i = 6*212; i < 6*212+53; i++)
 // leds.setPixel(i, BLUE);

  digitalWrite(led, HIGH);
//  int startChar = Serial.read();
  if ( Serial.read() == 254)
  {
    isGettingData = true;
    
//    Serial.println(Serial.available());
    int i = 0;
    while (Serial.available()) {
     
        char red = Serial.read();
        //char green = Serial.read();
        //char blue = Serial.read();
        leds.setPixel(i, red, red, red);
        numRecd++;
        i++;

        if(i == numLEDS)
          break;
    }
    //Serial.println(i);

  }
   //leds.show();
//if(numRecd == numLEDS){
//for(int i = 0; i < 1325; i++)
  //leds.setPixel(i, 127,127,127);
  leds.show();
  //numRecd = 0;
  isGettingData = false;
//}
  digitalWrite(led, LOW);

}
