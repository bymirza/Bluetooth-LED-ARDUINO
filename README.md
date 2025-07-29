# Bluetooth LED ARDUINO



char data = 0; //Değişken

void setup() {
Serial.begin(9600); //Veri iletimi için veri hızını saniye başına bit olarak ayarlıyoruz
pinMode(13, OUTPUT); //Dijital pin 13'ü çıkış pini olarak ayarlıyoruz
}

void loop(){
if(Serial.available() > 0){ //Yalnızca veri alındığında işlem yapıyoruz

data = Serial.read(); //Gelen verileri okuyarak değişkende saklıyoruz
Serial.print(data); //Monitörde verilerin içindeki değeri Yazdırıyoruz
Serial.print("\n"); //Alt satıra geçiyoruz
if(data == '1')
digitalWrite(13, HIGH);
else if(data == '0')
digitalWrite(13, LOW);
}
}



Pin Bağlantıları aşağıdaki gibi olmalıdır.
Arduino Pins | Bluetooth Pins
RX (Pin 0) ——> TX
TX (Pin 1) ——> RX
5V —————> VCC
GND ————> GND


https://play.google.com/store/apps/details?id=com.bymirza.net.btled
