package window.editor.diagrammer.elements.shapes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import window.editor.diagrammer.elements.shapes.relations.RelationShape;
import window.editor.diagrammer.listeners.LabelShapeListener;
import window.editor.diagrammer.utils.GeometryUtils;

public class LabelShape extends JLabel {

  private int distanceInXFromPointAncrage = 0;
  private int distanceInYFromPointAncrage = 0;

  private Point pointAncrage;
  private RelationShape relationShape;
  private boolean isRole;
  private boolean firstDisplay;

  public LabelShape() {
    this.initUI();
    this.addListeners();
  }
  public LabelShape(Point pointAncrage, RelationShape relationShape, boolean isRole) {
    this();
    this.pointAncrage = pointAncrage;
    this.relationShape = relationShape;
    this.isRole = isRole;
    this.firstDisplay = true;

    this.setSize(new Dimension(100, 30));
    Point location = this.calculateLocation(this.firstDisplay);

    this.setLocation(location);
    this.repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphics2D = (Graphics2D) g;
    Point location = this.calculateLocation(this.firstDisplay);
    Dimension size = this.calculateSize(graphics2D);
    this.setBounds(location.x, location.y, size.width, size.height);
  }

  private void addListeners(){
    LabelShapeListener listener = new LabelShapeListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
  }

  private Point calculateDestionationInformationsFirstDisplay(){
    int x;
    int y;
    if (GeometryUtils.pointIsOnLeftSideOfBounds(this.pointAncrage, this.relationShape.getDestination().getBounds())){
      // Le point d'ancrage est situé sur le côté gauche de la ClassShape
      if(isRole){
        x = pointAncrage.x - this.getWidth() - 10;
        y = pointAncrage.y - this.getHeight() - 10;
      } else{
        x = this.pointAncrage.x - this.getWidth() - 10;
        y = this.pointAncrage.y + 10;
      }
    } else if (GeometryUtils.pointIsOnRightSideOfBounds(this.pointAncrage, this.relationShape.getDestination().getBounds())){
      // Le point d'ancrage est situé sur le côté droit de la ClassShape
      if (isRole){
        x = pointAncrage.x + 10;
        y = pointAncrage.y + 10;
      } else{
        x = pointAncrage.x + 10;
        y = pointAncrage.y - this.getHeight() - 10;
      }
    } else if (GeometryUtils.pointIsOnBottomSideOfBounds(this.pointAncrage, this.relationShape.getDestination().getBounds())){
      // Le point d'ancrage est situé sur le bas de la ClassShape
      if (isRole){
        x = pointAncrage.x + 10;
        y = pointAncrage.y + 10;
      } else {
        x = pointAncrage.x - this.getWidth() - 10;
        y = pointAncrage.y + 10;
      }
    } else {
      // Le point d'ancrage est situé sur le haut de la ClassShape
      if (isRole){
        x = pointAncrage.x + 10;
        y = pointAncrage.y - this.getHeight() - 10;
      } else{
        x = pointAncrage.x - this.getWidth() - 10;
        y = pointAncrage.y - this.getHeight() - 10;
      }
    }
    return new Point(x, y);
  }
  private Point calculateSourceInformationsFirstDisplay(){
    int x;
    int y;
    if (GeometryUtils.pointIsOnLeftSideOfBounds(this.pointAncrage, this.relationShape.getSource().getBounds())){
      // Le point d'ancrage est situé sur le côté gauche de la ClassShape
      if(isRole){
        x = pointAncrage.x - this.getWidth() - 10;
        y = pointAncrage.y + this.getHeight() + 10;
      } else{
        x = this.pointAncrage.x - this.getWidth() - 10;
        y = this.pointAncrage.y - this.getHeight() - 10;
      }
    } else if (GeometryUtils.pointIsOnRightSideOfBounds(this.pointAncrage, this.relationShape.getSource().getBounds())){
      // Le point d'ancrage est situé sur le côté droit de la ClassShape
      if (isRole){
        x = pointAncrage.x + 10;
        y = pointAncrage.y + 10;
      } else{
        x = pointAncrage.x + 10;
        y = pointAncrage.y - this.getHeight() - 10;
      }
    } else if (GeometryUtils.pointIsOnBottomSideOfBounds(this.pointAncrage, this.relationShape.getSource().getBounds())){
      // Le point d'ancrage est situé sur le bas de la ClassShape
      if (isRole){
        x = pointAncrage.x + 10;
        y = pointAncrage.y + 10;
      } else {
        x = pointAncrage.x - this.getWidth() - 10;
        y = pointAncrage.y + 10;
      }
    } else {
      // Le point d'ancrage est situé sur le haut de la ClassShape
      if (isRole){
        x = pointAncrage.x + 10;
        y = pointAncrage.y - this.getHeight() - 10;
      } else{
        x = pointAncrage.x - this.getWidth() - 10;
        y = pointAncrage.y - this.getHeight() - 10;
      }
    }
    return new Point(x, y);
  }

  private void initUI(){
    this.setOpaque(false); // Pour la transparence
    this.setHorizontalAlignment(SwingConstants.CENTER);
    this.setVerticalAlignment(SwingConstants.CENTER);
  }

  private Dimension calculateSize(Graphics2D graphics2D){
    int width = graphics2D.getFontMetrics().stringWidth(this.getText());
    int height = graphics2D.getFontMetrics().getHeight();
    return new Dimension(40, height); // TODO -> vérifier pourquoi le width retourne toujours 0
  }

  public void setDistanceInXFromPointAncrage(int distanceInXFromPointAncrage) {
    this.distanceInXFromPointAncrage = distanceInXFromPointAncrage;
  }

  public void setDistanceInYFromPointAncrage(int distanceInYFromPointAncrage) {
    this.distanceInYFromPointAncrage = distanceInYFromPointAncrage;
  }

  public Point calculateLocation(boolean firstDisplay) {

    if (!firstDisplay){
      // S'il s'agit du nom d'association
      if (!(this.pointAncrage == relationShape.getPointsAncrage().getFirst()) && !(this.pointAncrage == relationShape.getPointsAncrage().getLast()) ){
        return new Point(relationShape.getCenter().x + distanceInXFromPointAncrage, relationShape.getCenter().y + distanceInYFromPointAncrage);
      } else{
        Point pointIfNotFirstDisplay = new Point(this.pointAncrage.x + distanceInXFromPointAncrage,this.pointAncrage.y + distanceInYFromPointAncrage);
        return pointIfNotFirstDisplay;
      }
    } else{
        if (this.pointAncrage == this.relationShape.getPointsAncrage().getFirst()) {
          return this.calculateSourceInformationsFirstDisplay();
        } else if (this.pointAncrage == this.relationShape.getPointsAncrage().getLast()) {
          return this.calculateDestionationInformationsFirstDisplay();
        } else {
          return this.relationShape.getCenter();
        }
      }
  }

  public int getDistanceInXFromPointAncrage() {
    return distanceInXFromPointAncrage;
  }
  public int getDistanceInYFromPointAncrage() {
    return distanceInYFromPointAncrage;
  }

  public void setFirstDisplay(boolean firstDisplay) {
    this.firstDisplay = firstDisplay;
  }
}
