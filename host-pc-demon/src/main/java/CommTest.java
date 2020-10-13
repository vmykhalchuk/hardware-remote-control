import jtermios.Termios;
import purejavacomm.CommPortIdentifier;
import purejavacomm.PureJavaSerialPort;
import purejavacomm.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import static jtermios.JTermios.*;

public class CommTest {
  public void sample() {
    SerialPort port = null;

    int FD = ((PureJavaSerialPort) port).getNativeFileDescriptor();

    int messageLength = 25; // bytes
    int timeout = 200; // msec
    byte[] readBuffer = new byte[messageLength];

    Termios termios = new Termios();

    if (0 != tcgetattr(FD, termios))
      errorHandling();

    termios.c_cc[VTIME] = (byte) (timeout / 100); // 200 msec timeout
    termios.c_cc[VMIN] = (byte) messageLength; // minimum 10 characters

    if (0 != tcsetattr(FD, TCSANOW, termios))
      errorHandling();

    int n = read(FD, readBuffer, messageLength);
    if (n < 0)
      errorHandling();

  }

  public void errorHandling() {

  }

  static public void main(String[] args) {
    System.out.println("List of COMM ports:");
    Enumeration<CommPortIdentifier> portIdsEnum = CommPortIdentifier.getPortIdentifiers();
    int no = 0;
    while (portIdsEnum.hasMoreElements()) {
      CommPortIdentifier portId = portIdsEnum.nextElement();
      System.out.printf("   %s:\ttype: '%s',\tname: '%s'\n", no, portId.getPortType(), portId.getName());
      System.out.printf("   %s:\tcurr owner: '%s'\n", no, portId.getCurrentOwner());
    }
    System.out.println("---------------------");


    try {
      // Finding the port
      String portName = "COM3";//"tty.usbserial-FTOXM3NX";
      CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

      // Opening the port
      SerialPort port = (SerialPort) portId.open("Example1", 1000);
      port.setSerialPortParams(9600, 8, 1, 1);
      OutputStream outStream = port.getOutputStream();
      InputStream inStream = port.getInputStream();

      // Sending data
      byte[] dataToSend = {0x11, 0x22, 0x33, 0x44, 0x55};
      outStream.write(dataToSend, 0, dataToSend.length);

      // Receiving data
      int messageLength = 5;
      byte[] dataReceived = new byte[messageLength];
      int received = 0;
      while (received < messageLength)
        received += inStream.read(dataReceived, received, messageLength - received);

      // Checking the message
      for (int i = 0; i < received; i++) {
        if (dataReceived[i] != dataToSend[i]) {
          System.err.println("error at " + i + "th byte, sent " + dataToSend[i] + " received " + dataReceived);
        }
      }
      System.out.println("Done");
      port.close();
    } catch (Throwable thwble) {
      thwble.printStackTrace();
    }
  }
}
