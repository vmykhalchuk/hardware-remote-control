public class HidEvent {

  private HidDeviceType deviceType;

  private String kbrdInputText;
  private KeyboardFunctionalKey[] keyboardFunctionalKeys;

  private int mouseRelMovePixels;
  private Direction mouseRelMoveDirection;

  public HidDeviceType getDeviceType() {
    return deviceType;
  }

  public HidEvent setDeviceType(HidDeviceType deviceType) {
    this.deviceType = deviceType;
    return this;
  }

  public String getKbrdInputText() {
    return kbrdInputText;
  }

  public HidEvent setKbrdInputText(String kbrdInputText) {
    this.kbrdInputText = kbrdInputText;
    return this;
  }

  public KeyboardFunctionalKey[] getKeyboardFunctionalKeys() {
    return keyboardFunctionalKeys;
  }

  public HidEvent setKeyboardFunctionalKeys(KeyboardFunctionalKey[] keyboardFunctionalKeys) {
    this.keyboardFunctionalKeys = keyboardFunctionalKeys;
    return this;
  }

  public int getMouseRelMovePixels() {
    return mouseRelMovePixels;
  }

  public HidEvent setMouseRelMovePixels(int mouseRelMovePixels) {
    this.mouseRelMovePixels = mouseRelMovePixels;
    return this;
  }

  public Direction getMouseRelMoveDirection() {
    return mouseRelMoveDirection;
  }

  public HidEvent setMouseRelMoveDirection(Direction mouseRelMoveDirection) {
    this.mouseRelMoveDirection = mouseRelMoveDirection;
    return this;
  }
}
