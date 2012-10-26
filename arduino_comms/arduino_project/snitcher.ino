const byte BUILDING = 0;
const byte BUILD_INTERRUPTED = 1;
const byte BUILD_FAILED = 2;
const byte BUILD_SUCCEEDED = 3;

const int C1_G = 2;
const int C1_R = 3;

const int C2_G = 4;
const int C2_R = 5;

const int C3_G = 6;
const int C3_R = 7;

const int C4_G = 8;
const int C4_R = 9;

const int C5_G = 10;
const int C5_R = 11;

const int C6_G = 12;
const int C6_R = 13;




void setup(){
  Serial.begin(9600);
  
  pinMode(C1_G, OUTPUT);
  pinMode(C1_R, OUTPUT);
  
  pinMode(C2_G, OUTPUT);
  pinMode(C2_R, OUTPUT);
  
  pinMode(C3_G, OUTPUT);
  pinMode(C3_R, OUTPUT);
  
  pinMode(C4_G, OUTPUT);
  pinMode(C4_R, OUTPUT);
  
  pinMode(C5_G, OUTPUT);
  pinMode(C5_R, OUTPUT);
  
  pinMode(C6_G, OUTPUT);
  pinMode(C6_R, OUTPUT);
  
}

boolean read_frame()
{
  while (Serial.available() == 0);
  byte byte_read  = Serial.read();
  if (byte_read == 7)
  {
    while (Serial.available() == 0);
    byte_read  = Serial.read();  
    if (byte_read == (byte)0xE)
    {
      return true;
    }
  }
  return false;
}




void setBuilding(byte channel)
{
  
}

void setBuildInterrupted(byte channel)
{
}

void setBuildFailed(byte channel)
{
  Serial.print("Channel ");
  Serial.print(channel);
  Serial.println(" - BuildFailed ");
  
  byte green_pin = (channel * 2) + 2;
  byte red_pin = (channel * 2) + 3;
  
  
  
  digitalWrite(green_pin, LOW);
  digitalWrite(red_pin, HIGH);
}

void setBuildSucceeded(byte channel)
{ 
  Serial.print("Channel ");
  Serial.print(channel);
  Serial.println(" - BuildSucceeded ");
  
  
  byte green_pin = (channel * 2) + 2;
  byte red_pin = (channel * 2) + 3;
  
  digitalWrite(green_pin, HIGH);
  digitalWrite(red_pin, LOW);
  
}


void loop(){

  while (read_frame() != true);
 
  byte command =7;
  byte channel = 14;
 
  while ((command == 7) && (channel == 14))
  {
    while (Serial.available() == 0);
    command = Serial.read();
    while (Serial.available() == 0);
    channel = Serial.read();
  }
  
 
  if (read_frame())
  {
    switch (command)
    {
      case BUILDING:
      setBuilding(channel); 
      break;
      case BUILD_INTERRUPTED:
      setBuildInterrupted(channel);
      break;
      case BUILD_FAILED:
      setBuildFailed(channel);
      break;
      case BUILD_SUCCEEDED:
      setBuildSucceeded(channel);
      break;

    }
    
  }
  
}
