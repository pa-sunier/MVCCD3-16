package window.editor.diagrammer.elements.shapes.relations;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import m.interfaces.IMRelation;
import preferences.Preferences;
import window.editor.diagrammer.elements.interfaces.IShape;
import window.editor.diagrammer.elements.shapes.classes.ClassShape;
import window.editor.diagrammer.elements.shapes.classes.MCDEntityShape;
import window.editor.diagrammer.listeners.RelationFocusListener;
import window.editor.diagrammer.services.DiagrammerService;
import window.editor.diagrammer.utils.GeometryUtils;

public abstract class RelationShape extends JComponent implements IShape {

  protected LinkedList<RelationPointAncrageShape> pointsAncrage = new LinkedList<>();
  protected boolean isSelected = false;
  protected MCDEntityShape source;
  protected MCDEntityShape destination;
  protected IMRelation relation;
  protected LabelShape sourceRole;
  protected LabelShape destinationRole;
  protected LabelShape sourceCardinalite;
  protected LabelShape destinationCardinalite;
  protected LabelShape associationName;
  protected boolean isReflexive = false;

  public RelationShape(MCDEntityShape source, MCDEntityShape destination) {
    this.source = source;
    this.destination = destination;

    this.createPointsAncrage(isReflexive);
    this.createLabels();

    this.setFocusable(true);
    this.addListeners();

  }

  public RelationShape(MCDEntityShape source, MCDEntityShape destination, boolean isReflexive) {
    this.source = source;
    this.destination = destination;
    this.isReflexive = isReflexive;

    this.createPointsAncrage(isReflexive);
    this.createLabels();

    this.setFocusable(true);
    this.addListeners();

  }

  private void addListeners() {
    RelationFocusListener focusListener = new RelationFocusListener();
    this.addFocusListener(focusListener);
  }

  public void drawPointsAncrage(Graphics2D graphics2D) {
    for (RelationPointAncrageShape pointAncrage : this.pointsAncrage) {
      graphics2D.fillOval(pointAncrage.x - pointAncrage.DIAMETER / 2, pointAncrage.y - pointAncrage.DIAMETER / 2, pointAncrage.DIAMETER, pointAncrage.DIAMETER);
    }
  }

  public void addPointAncrage(RelationPointAncrageShape pointAncrage, int index) {
    this.pointsAncrage.add(index, pointAncrage);
  }

  public void deletePointAncrage(RelationPointAncrageShape pointAncrage) {
    this.pointsAncrage.remove(pointAncrage);
    this.reindexAllPointsAncrage();
  }

  @Override
  public void setLocationDifference(int differenceX, int differenceY) {
    for (RelationPointAncrageShape pointAncrage : this.pointsAncrage) {
      pointAncrage.setLocationDifference(differenceX, differenceY);
    }
  }

  @Override
  public void zoom(int fromFactor, int toFactor) {
    for (RelationPointAncrageShape pointAncrageShape : this.pointsAncrage) {
      pointAncrageShape.zoom(fromFactor, toFactor);
    }
  }

  @Override
  public void drag(int differenceX, int differenceY) {
  }

  @Override
  public boolean isSelected() {
    return isSelected;
  }

  @Override
  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  @Override
  public void setSize(int width, int height) {
    IShape.super.setSize(width, height);
  }

  @Override
  public void setSize(Dimension dimension) {
  }

  @Override
  public Rectangle getBounds() {
    return this.getPointsAncrageMinRectangle();
  }

  public LinkedList<RelationPointAncrageShape> getPointsAncrage() {
    return pointsAncrage;
  }

  public void drawSegments(Graphics2D graphics2D) {
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // Pour chaque point d'ancrage
    for (int i = 0; i < this.pointsAncrage.size(); i++) {
      if (i != this.pointsAncrage.size() - 1) {
        graphics2D.drawLine((int) pointsAncrage.get(i).getX(), (int) pointsAncrage.get(i).getY(), (int) pointsAncrage.get(i + 1).getX(), (int) pointsAncrage.get(i + 1).getY());
      }
    }
  }

  public int getPointAncrageIndex(RelationPointAncrageShape pointAncrage) {
    int index = Integer.MAX_VALUE;
    for (int i = 0; i < this.pointsAncrage.size(); i++) {
      if (pointAncrage.equals(this.pointsAncrage.get(i))) {
        index = this.pointsAncrage.get(i).getIndex();
      }
    }
    return index;
  }

  public RelationPointAncrageShape convertPoint2DToPointAncrage(Point2D point2D) {
    return new RelationPointAncrageShape((int) point2D.getX(), (int) point2D.getY());
  }

  public void updateFirstAndLastPointsAncrage(ClassShape shape, boolean isResize) {
    RelationPointAncrageShape point;
    if (shape == this.source) {
      point = this.pointsAncrage.getFirst();
    } else {
      point = this.pointsAncrage.getLast();
    }
    if (isResize) {
      int cursor = shape.getCursor().getType();
      if (cursor == Cursor.E_RESIZE_CURSOR) {
        if (point.y != shape.getBounds().getMaxY() && point.y != shape.getBounds().getMinY() && point.x != shape.getBounds().getMinX()) {
          point.drag((int) shape.getBounds().getMaxX(), point.y);
        } else {
          if (!GeometryUtils.pointIsAroundShape(point, shape)) {
            point.drag((int) shape.getBounds().getMaxX(), point.y);
          }
        }
      } else if (cursor == Cursor.W_RESIZE_CURSOR) {
        if (point.y != shape.getBounds().getMaxY() && point.y != shape.getBounds().getMinY() && point.x != shape.getBounds().getMaxX()) {
          point.drag((int) shape.getBounds().getMinX(), point.y);
        } else {
          if (!GeometryUtils.pointIsAroundShape(point, shape)) {
            point.drag((int) shape.getBounds().getMinX(), point.y);
          }
        }
      } else if (cursor == Cursor.S_RESIZE_CURSOR) {
        if (point.x != shape.getBounds().getMaxX() && point.x != shape.getBounds().getMinX() && point.y != shape.getBounds().getMinY()) {
          point.drag(point.x, (int) shape.getBounds().getMaxY());
        } else {
          if (!GeometryUtils.pointIsAroundShape(point, shape)) {
            point.drag(point.x, (int) shape.getBounds().getMaxY());
          }
        }
      } else if (cursor == Cursor.N_RESIZE_CURSOR) {
        if (point.x != shape.getBounds().getMaxX() && point.x != shape.getBounds().getMinX() && point.y != shape.getBounds().getMaxY()) {
          point.drag(point.x, (int) shape.getBounds().getMinY());
        } else {
          if (!GeometryUtils.pointIsAroundShape(point, shape)) {
            point.drag(point.x, (int) shape.getBounds().getMinY());
          }
        }
      }
    }
    DiagrammerService.drawPanel.repaint();
  }

  private Rectangle getPointsAncrageMinRectangle() {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = 0;
    int maxY = 0;
    for (RelationPointAncrageShape pointAncrage : this.getPointsAncrage()) {
      minX = Math.min(minX, pointAncrage.getBounds().x);
      minY = Math.min(minY, pointAncrage.getBounds().y);
      maxX = Math.max(maxX, pointAncrage.getBounds().x + pointAncrage.getBounds().width);
      maxY = Math.max(maxY, pointAncrage.getBounds().y + pointAncrage.getBounds().height);
    }
    return new Rectangle(minX, minY, maxX - minX, maxY - minY);
  }

  public MCDEntityShape getSource() {
    return source;
  }

  public MCDEntityShape getDestination() {
    return destination;
  }

  public void reindexAllPointsAncrage() {
    for (int i = 0; i < this.pointsAncrage.size(); i++) {
      this.pointsAncrage.get(i).setIndex(i);
    }

  }

  public ClassShape getNearestClassShape(Point pointAncrageIndex) {
    if (GeometryUtils.pointIsAroundShape(pointAncrageIndex, this.source)) {
      return this.source;
    } else {
      return this.destination;
    }
  }

  public Point getCenter() {
    RelationPointAncrageShape left = GeometryUtils.getLeftPoint(this.pointsAncrage.getFirst(), this.pointsAncrage.getLast());
    RelationPointAncrageShape right = GeometryUtils.getRightPoint(this.pointsAncrage.getFirst(), this.pointsAncrage.getLast());
    if (pointsAncrage.size() == 2) {
      int x = (right.x - left.x) / 2;
      int y = (right.y - left.y) / 2;
      return new Point(left.x + x, left.y + y);
    } else if (pointsAncrage.size() % 2 != 0) {
      int indexCenter = Math.round(pointsAncrage.size() / 2);
      return new Point(pointsAncrage.get(indexCenter).x, pointsAncrage.get(indexCenter).y);
    } else {
      int indexCenter = pointsAncrage.size() / 2 - 1;
      RelationPointAncrageShape centerPoint = pointsAncrage.get(indexCenter);
      RelationPointAncrageShape pointNextToCenter = pointsAncrage.get(indexCenter + 1);
      return new Point((centerPoint.x + pointNextToCenter.x) / 2, (centerPoint.y + pointNextToCenter.y) / 2);
    }

  }

  public abstract void setDestinationRole(String role);
  public abstract void setSourceRole(String role);
  public abstract void setRelationName(String name);
  public abstract void setSourceCardinalite(String cardinalite);
  public abstract void setDestinationCardinalite(String cardinalite);

  public ArrayList<Line2D> getSegments() {
    ArrayList segments = new ArrayList();
    for (int i = 0; i < this.pointsAncrage.size() - 1; i++) {
      Line2D segment = new Line2D.Double();
      segment.setLine(this.pointsAncrage.get(i).getX(), this.pointsAncrage.get(i).getY(), this.pointsAncrage.get(i + 1).getX(), this.pointsAncrage.get(i + 1).getY());
      segments.add(segment);
    }
    return segments;
  }

  public abstract void draw(Graphics2D graphics2D);

  public void addPointAncrage(Point point) {
    Line2D nearestSegment = this.getNearestSegment(point);
    if (nearestSegment != null) {
      int newIndex = this.getPointAncrageIndex(this.convertPoint2DToPointAncrage(nearestSegment.getP2()));
      Point nearestPointOnSegment = GeometryUtils.getNearestPointOnLine(nearestSegment.getX1(), nearestSegment.getY1(), nearestSegment.getX2(), nearestSegment.getY2(), point.x, point.y, true, null);
      RelationPointAncrageShape newPointAncrage = new RelationPointAncrageShape(nearestPointOnSegment, newIndex);
      this.addPointAncrage(newPointAncrage, newIndex);
      this.reindexAllPointsAncrage();
      DiagrammerService.drawPanel.repaint();
    }
  }

  private Line2D getNearestSegment(Point point) {
    for (Line2D segment : this.getSegments()) {
      if (GeometryUtils.getDistanceBetweenLineAndPoint(segment, point) <= Preferences.DIAGRAMMER_RELATION_CLICK_AREA) {
        return segment;
      }
    }
    return null;
  }

  public abstract void setInformations();

  public boolean isReflexive() {
    return isReflexive;
  }

  private void createPointsAncrage(boolean isReflexive) {
    if (isReflexive) {
      RelationPointAncrageShape p1 = new RelationPointAncrageShape((int) source.getBounds().getMaxX() - 50, (int) source.getBounds().getMinY());
      RelationPointAncrageShape p2 = new RelationPointAncrageShape((int) source.getBounds().getMaxX() - 50, (int) source.getBounds().getMinY() - 50);
      RelationPointAncrageShape p3 = new RelationPointAncrageShape((int) source.getBounds().getMaxX() + 50, (int) source.getBounds().getMinY() - 50);
      RelationPointAncrageShape p4 = new RelationPointAncrageShape((int) source.getBounds().getMaxX() + 50, (int) source.getBounds().getMinY() + 50);
      RelationPointAncrageShape p5 = new RelationPointAncrageShape((int) source.getBounds().getMaxX(), (int) source.getBounds().getMinY() + 50);
      this.addPointAncrage(p1, 0);
      this.addPointAncrage(p2, 1);
      this.addPointAncrage(p3, 2);
      this.addPointAncrage(p4, 3);
      this.addPointAncrage(p5, 4);
      this.isReflexive = true;
    } else {
      this.pointsAncrage.add(new RelationPointAncrageShape(source.getX() + source.getWidth(), source.getY() + source.getHeight() / 2, 0));
      this.pointsAncrage.add(new RelationPointAncrageShape(destination.getX(), destination.getY() + destination.getHeight() / 2, 1));
    }
  }

  private void createLabels() {
    sourceRole = new LabelShape(this.pointsAncrage.getFirst(), this, true);
    sourceCardinalite = new LabelShape(this.pointsAncrage.getLast(), this, false);
    destinationRole = new LabelShape(this.pointsAncrage.getLast(), this, true);
    destinationCardinalite = new LabelShape(this.pointsAncrage.getFirst(), this, false);
    associationName = new LabelShape(new RelationPointAncrageShape(this.getCenter().x, this.getCenter().y), this, false);

    DiagrammerService.drawPanel.add(sourceRole, JLayeredPane.DRAG_LAYER);
    DiagrammerService.drawPanel.add(sourceCardinalite, JLayeredPane.DRAG_LAYER);
    DiagrammerService.drawPanel.add(destinationRole, JLayeredPane.DRAG_LAYER);
    DiagrammerService.drawPanel.add(destinationCardinalite, JLayeredPane.DRAG_LAYER);
    DiagrammerService.drawPanel.add(associationName, JLayeredPane.DRAG_LAYER);
  }
}
