#include "Keyboard.h"
#include "Mouse.h"

void setup() { // initialize the buttons' inputs:

  Serial.begin(9600);
  // start serial port at 9600 bps and wait for port to open:
  Serial1.begin(9600);
  while (!Serial || !Serial1) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  // initialize mouse control:
  Mouse.begin();
  Keyboard.begin();
  Serial.println("Let's get rolling in 5sec...");
  delay(5000);
  Serial.println("... and ... ACTION!");
}

void loop() {
  if (Serial1.available() > 0) {
    char inChar = Serial1.read();
    Serial.write(inChar);
  }
}
