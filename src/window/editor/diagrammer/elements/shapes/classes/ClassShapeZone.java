package window.editor.diagrammer.elements.shapes.classes;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ClassShapeZone extends Rectangle {

  private ArrayList<String> elements;

  public ClassShapeZone() {
    this.elements = new ArrayList<>();
  }

  protected void addElement(String element){
    this.elements.add(element);
  }

  public ArrayList<String> getElements() {
    return elements;
  }

  public void setElements(ArrayList<String> elements) {
    this.elements = elements;
  }
}