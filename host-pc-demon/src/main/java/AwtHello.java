import java.awt.*;
import java.awt.event.*;

public class AwtHello {

  public static final boolean ENABLE_COM_PORT = false;
  public static final String COM_PORT_NAME = "COM3";

  public static void main(String[] args) {
    new AwtHello().run();
  }

  boolean altInAction = false;
  boolean shiftInAction = false;
  boolean ctrlInAction = false;
  int moveX=-1,moveY;

  void reset() {
    altInAction = false;
    shiftInAction = false;
    ctrlInAction = false;
    moveX = -1;
  }

  AwtHelloComPort comPort = ENABLE_COM_PORT ? new AwtHelloComPort() : new AwtHelloComPort.AwtHelloComPortDummy();

  void run() {
    comPort.printList();
    comPort.initialize(COM_PORT_NAME);

    Frame f=new Frame("Hello World example of awt application");

    // make window transparent(but removes top controls)
    f.setUndecorated(true);
    f.setBackground(new Color(50, 50, 122, 25));

    Label label1=new Label("Hello World", Label.CENTER);
    f.add(label1);

    f.setSize(400,300);
    label1.addMouseListener(new AwtMouseListener());
    label1.addMouseMotionListener(new AwtMouseMotionListener());
    label1.addMouseWheelListener(new AwtMouseWheelListener());
    label1.addKeyListener(new AwtKeyListener());
    label1.setFocusable(true);
    f.setVisible(true);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        System.exit(0);
      }
    });
    System.out.println("Test");

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image image = toolkit.getImage("icons/handwriting.gif");
    Cursor c = toolkit.createCustomCursor(image, new Point(f.getX(), f.getY()), "img");
    c = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    f.setCursor(c);
  }

  private int limit(int z) {
    if (z > 20) {
      return 20;
    }
    if (z < -20) {
      return -20;
    }
    return z;
  }

  private void onMouseMove(int relMoveX, int relMoveY) {
    if (relMoveX == 0 && relMoveY == 0) {
      return;
    }
    relMoveX = limit(relMoveX);
    relMoveY = limit(relMoveY);
    if (shiftInAction) {
      relMoveX *= 3;
      relMoveY *= 3;
    }
    //System.out.println("Relative move: " + (byte)relMoveX + "\t" + (byte)relMoveY);
    comPort.writeMouseMove((byte)relMoveX, (byte)relMoveY);
  }

  public class AwtMouseMotionListener implements MouseMotionListener {


    @Override
    public void mouseDragged(MouseEvent e) {
      if (ctrlInAction) {
        if (moveX != -1) {
          onMouseMove(e.getX() - moveX, e.getY() - moveY);
        }
        moveX = e.getX();
        moveY = e.getY();
        //System.out.format("dragged: %s,\t %s\n", e.getX(), e.getY());
      }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      if (ctrlInAction) {
        if (moveX != -1) {
          onMouseMove(e.getX() - moveX, e.getY() - moveY);
        }
        moveX = e.getX();
        moveY = e.getY();
        //System.out.format("moved: %s,\t %s\n", e.getX(), e.getY());
      }
    }
  }

  public class AwtMouseWheelListener implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      if (e.getWheelRotation() == 0) {
        return;
      }
      System.out.println("wheel rotated: " + e.getWheelRotation() + "\t" + e.getPreciseWheelRotation());
      comPort.writeMouseWheelRotated(e.getWheelRotation() == 1);
    }
  }

  public class AwtMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
      comPort.writeMouseKeyPressed((byte) e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      comPort.writeMouseKeyReleased((byte) e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      reset();
    }

    @Override
    public void mouseExited(MouseEvent e) {
      reset();
    }
  }

  public class AwtKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      int extKeyCode = e.getExtendedKeyCode();
      if (extKeyCode == KeyEvent.VK_ALT) {
        altInAction = true;
      } else if (extKeyCode == KeyEvent.VK_SHIFT) {
        shiftInAction = true;
      } else if (extKeyCode == KeyEvent.VK_CONTROL) {
        ctrlInAction = true;
      } else {
        if (extKeyCode < 128) {
          comPort.writeKeyboardKeyPressed((byte) extKeyCode);
        } else {
          System.out.println("key pressed: " + e.getExtendedKeyCode());
        }
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      int extKeyCode = e.getExtendedKeyCode();
      if (extKeyCode == KeyEvent.VK_ALT) {
        altInAction = false;
      } else if (extKeyCode == KeyEvent.VK_SHIFT) {
        shiftInAction = false;
      } else if (extKeyCode == KeyEvent.VK_CONTROL) {
        ctrlInAction = false;
        moveX = -1;
      } else {
        if (extKeyCode < 128) {
          comPort.writeKeyboardKeyReleased((byte) extKeyCode);
        } else {
          System.out.println("key released: " + e.getExtendedKeyCode());
        }
      }
    }
  }

}