import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import static java.lang.Math.abs;

public class AwtHelloComPort {

  public static class AwtHelloComPortDummy extends AwtHelloComPort {
    @Override
    public void initialize(String portName) {
    }

    @Override
    protected void writeData(byte[] dataToSend) {
      System.out.print("  COM_" + dataToSend.length + ":");
      for (int i = 0; i < dataToSend.length; i++) {
        System.out.format("%02X", dataToSend[i]);
      }
      System.out.println();
    }
  }

  OutputStream outStream;
  InputStream inStream;

  public void printList() {
    System.out.println("List of COMM ports:");
    Enumeration<CommPortIdentifier> portIdsEnum = CommPortIdentifier.getPortIdentifiers();
    int no = 0;
    while (portIdsEnum.hasMoreElements()) {
      CommPortIdentifier portId = portIdsEnum.nextElement();
      System.out.printf("   %s:\ttype: '%s',\tname: '%s'\n", no, portId.getPortType(), portId.getName());
      System.out.printf("   %s:\tcurr owner: '%s'\n", no, portId.getCurrentOwner());
    }
    System.out.println("---------------------");
  }

  public void initialize(String portName) {
    try {
      // Finding the port
      CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

      // Opening the port
      SerialPort port = (SerialPort) portId.open("Example1", 1000);
      port.setSerialPortParams(9600, 8, 1, 1);

      outStream = port.getOutputStream();
      inStream = port.getInputStream();

    } catch (Throwable thwble) {
      thwble.printStackTrace();
    }
  }

  protected void writeData(byte[] dataToSend) {
    try {
      outStream.write(dataToSend, 0, dataToSend.length);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // code
  // 0x1d - 1 means mouse move
  //      - 2 means mouse key pressed
  //      - 3 means mouse key released
  //      - 4 means mouse wheel rotated
  //      - 5 means keyboard key pressed
  //      - 6 means keyboard key released

  // delta not more then 120
  public void writeMouseMove(int relX, int relY) {
    if (relX < -120 || relX > 120) {
      throw new RuntimeException("relX is out of range: " + relX);
    }
    if (relY < -120 || relY > 120) {
      throw new RuntimeException("relY is out of range: " + relY);
    }

    // above d - is a direction, 0b0011 - y and x are positive
    //                           0b0010 - y is positive and x is negative
    //                           0b0001 - y is negative and x is positive
    //                           0b0000 - y and x are negative
    byte d = relX >=0 ? 0x00 : (byte)0x01;
    d += relY >= 0 ? 0x00 : (byte)0x02;
    byte code = (byte)(0x10 + d);
    byte[] dataToSend = {code, (byte) abs(relX), (byte) abs(relY)};
    writeData(dataToSend);
  }

  // 1 - left; 2 - middle; 3 - right
  public void writeMouseKeyPressed(byte key) {
    if (key < 1 || key > 3) {
      throw new RuntimeException("Wrong key: " + key);
    }
    byte code = (byte)(0x20 + key);
    byte[] dataToSend = {code};
    writeData(dataToSend);
  }

  // 1 - left; 2 - middle; 3 - right
  public void writeMouseKeyReleased(byte key) {
    if (key < 1 || key > 3) {
      throw new RuntimeException("Wrong key: " + key);
    }
    byte code = (byte)(0x30 + key);
    byte[] dataToSend = {code};
    writeData(dataToSend);
  }

  // true - up (+1)    => write 0x41
  // false - down (-1) => write 0x42
  public void writeMouseWheelRotated(boolean upNotDown) {
    byte code = (byte)(0x40 + (upNotDown ? 1 : 2));
    byte[] dataToSend = {code};
    writeData(dataToSend);
  }

  public void writeKeyboardKeyPressed(byte key) {
    byte[] dataToSend = {0x50, key};
    writeData(dataToSend);
  }

  public void writeKeyboardKeyReleased(byte key) {
    byte[] dataToSend = {0x60, key};
    writeData(dataToSend);
  }
}
