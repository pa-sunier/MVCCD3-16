package window.editor.diagrammer.listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import preferences.Preferences;
import window.editor.diagrammer.drawpanel.DrawPanel;
import window.editor.diagrammer.elements.shapes.classes.MCDEntityShape;
import window.editor.diagrammer.menus.EntityShapeMenu;

public class MCDEntityShapeListener extends MouseAdapter {

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    if (SwingUtilities.isRightMouseButton(e)) {
      this.showMenu(e);
    }
  }

  private void showMenu(MouseEvent event) {
    MCDEntityShape shape = (MCDEntityShape) event.getSource();
    DrawPanel drawPanel = (DrawPanel) SwingUtilities.getAncestorNamed(Preferences.DIAGRAMMER_DRAW_PANEL_NAME, shape);
    Point converted = SwingUtilities.convertPoint(shape, event.getPoint(), drawPanel);

    EntityShapeMenu menu = new EntityShapeMenu(shape, converted.x, converted.y);
    menu.show(drawPanel, converted.x, converted.y);
  }
}