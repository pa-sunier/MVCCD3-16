package diagram.mcd;

import diagram.mcd.testDrawPanel.*;
import main.MVCCDElement;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.util.ArrayList;

public class MCDZoneDessin {
    private JPanel panelZoneDessin;
    MVCCDElement parent;
    String mode ;
    MCDDiagram diagram;
    DiagramList diagramList;
    boolean created = false;

    public MCDZoneDessin(MVCCDElement parent, javax.swing.JPanel panelZoneDessin, String mode, MCDDiagram diagram) {
        this.panelZoneDessin = panelZoneDessin;
        this.parent = parent;
        this.mode = mode;
        this.diagram = diagram;
    }

    public void getContentNew() {
        panelZoneDessin.setBackground(Color.white);

        panelZoneDessin.removeAll();
        ArrayList<MCDEntityDraw> entites = new ArrayList<>();

        String nom= new String();


        DiagramList diagramList = new DiagramList(nom, entites);
        diagramList.setName("");
        DrawEntity draw = new DrawEntity(entites);
        diagram.setMCDEntityDraws(entites);
        draw.setPreferredSize(new Dimension(1400, 820));
        //panelZoneDessin.add(draw);


    }

    public void getContentUpdate(ArrayList<MCDEntityDraw> listeEntite){
        panelZoneDessin.setBackground(Color.white);

        panelZoneDessin.removeAll();
        DrawEntity draw = new DrawEntity(listeEntite);
        draw.setPreferredSize(new Dimension(1400, 820));
        panelZoneDessin.add(draw);


    }
}
