void setup()
{
  Serial.begin(9600);
  pinMode(2, INPUT);
  pinMode(4, INPUT);
  pinMode(8,OUTPUT);
  pinMode(7,INPUT);
  pinMode(13, OUTPUT);
  pinMode(9, OUTPUT);
}
 
const int LOCKED = 1;
const int WAITING = 2;
const int UNLOCKED = 3;

int state = LOCKED;
int buttonHandle = 0;
int pirHandle = 0;
int lastPress = 0;
int wrongPin = 0;

void loop()
{
  
  int btn1 = digitalRead(4);
  int btn2 = digitalRead(2);
  int pir = digitalRead(7);

  
  if(pir == HIGH && pirHandle == 0) {
    state = WAITING;
  	pirHandle == 1;
  }
  switch (state) {
    
    case LOCKED:
        digitalWrite(13, LOW);
        digitalWrite(9, LOW);
        digitalWrite(8, HIGH);
    	lastPress = 0;
    break;  
   
    case WAITING:
    	digitalWrite(13, LOW);
		digitalWrite(9,HIGH);
    	digitalWrite(8, LOW);
    if(btn1 == HIGH && lastPress == 0) {
      digitalWrite(9,LOW);
      delay(1000);
      digitalWrite(9,HIGH);
      lastPress = 1;
    } else if(btn1 == HIGH && lastPress == 1) {
      state = LOCKED;
    }
    if(btn2 == HIGH && lastPress == 1){
      digitalWrite(9,LOW);
      delay(1000);
      digitalWrite(9,HIGH);
      lastPress = 2;
      state = UNLOCKED;
    } else if(btn2 == HIGH && lastPress == 2 || lastPress == 0){
      state = LOCKED;
    }
    
    break;

    case UNLOCKED:
    	digitalWrite(13, HIGH);
    	digitalWrite(9,LOW);
    	digitalWrite(8, LOW);
    	delay(10000);
    	state = LOCKED;
    break;
  }


}