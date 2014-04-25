/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xz.spacial;

import java.util.HashSet;
import java.util.Objects;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class SpacialUI extends Application implements MapEdgeListener<Circle>, MapNodeListener<Circle>
{
   FlatMap<Circle> map = new FlatMap<>();
   Node<Circle> selectedNode = null;
   Node<Circle> closestNode = null;
   private Rectangle mapBackground;
   private Group mapGroup;
   
   HashSet<Line> edges = new HashSet<>();

   @Override
   public void start(Stage primaryStage)
   {
      map.setSelectionTolerance(5.0);
      map.addEdgeListener(this);
      map.addNodeListener(this);
      
      mapGroup = createMapCanvas();
      
      
//      StackPane root = new StackPane(new Group(new Rectangle(200, 200, Color.RED)));
      Scene scene = new Scene(mapGroup, 800, 800);

      primaryStage.setTitle("Flat Map");
      primaryStage.setScene(scene);
      primaryStage.show();
   }

   private Group createMapCanvas()
   {
      mapBackground = new Rectangle(800, 800, Color.WHITE);
      Group mapGroup = new Group(mapBackground);
      mapBackground.setOnMouseClicked(new EventHandler<MouseEvent>()
      {

         @Override
         public void handle(MouseEvent event)
         {
            double x = event.getX();
            double y = event.getY();
            if (event.getButton().equals(MouseButton.PRIMARY))
            {
               Node<Circle> pointed = map.selectNode(x, y);
               if (pointed != null)
               {
                  if (selectedNode != null)
                     selectedNode.getData().setFill(Color.BLACK);
                  selectedNode = pointed;
                  selectedNode.getData().setFill(Color.BLUE);
                  System.out.println("Selected: x=" + selectedNode.getX() + ", y=" + selectedNode.getY());
               }
               else
               {
                  if (selectedNode != null)
                     selectedNode.getData().setFill(Color.BLACK);
                  selectedNode = null;
                  map.addNode(x, y, new Circle(x, y, 3.0, Color.BLACK));
               }
            }
            else if (event.getButton().equals(MouseButton.SECONDARY))
            {
               Node<Circle> closest = map.findNearestNode(x, y, false);
               if (closest == null)
               {
                  System.out.println("Closest node: null");
                  return;
               }
               
               if (closestNode != closest)
               {
                  Node<Circle> node = new Node<Circle>(new Point(x, y), null);
                  Double oldDist = closestNode != null ? closestNode.getCoords().distance(node.getCoords()) : null;
                  if (closestNode != null)
                     closestNode.getData().setFill(Color.BLACK);
                  closestNode = closest;
                  double newDist = closest.getCoords().distance(node.getCoords());
                  closest.getData().setFill(Color.RED);
                  System.out.println("Closest node: x=" + closest.getX() + ", y=" + closest.getY() + ", oldDist = " + oldDist + ", newDist = " + newDist);
               }
            }
         }
      });
      return mapGroup;
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args)
   {
      launch(args);
   }

   @Override
   public void edgeAdded(MapEdgeEvent<Circle> e)
   {
      Node<Circle> newNode = e.getNode1();
      Node<Circle> edge = e.getNode2();
      Line line = new Line(newNode.getX(), newNode.getY(), edge.getX(), edge.getY());
      line.setMouseTransparent(true);
      mapGroup.getChildren().add(line);
      edges.add(line);
   }

   @Override
   public void edgeRemoved(MapEdgeEvent<Circle> e)
   {
      Node<Circle> node1 = e.getNode1();
      Node<Circle> node2 = e.getNode2();
      Line line = edges.stream().filter(l -> {return compare(l, node1, node2);}).findFirst().get();
      if (line != null)
         mapGroup.getChildren().remove(line);
   }
   
   boolean compare(Line l, Node n1, Node n2)
   {
      return (l.getStartX() == n1.getX()) && (l.getStartY() == n1.getY()) && (l.getEndX() == n2.getX()) && (l.getEndY() == n2.getY())
          || (l.getStartX() == n2.getX()) && (l.getStartY() == n2.getY()) && (l.getEndX() == n1.getX()) && (l.getEndY() == n1.getY());
   }

   @Override
   public void nodeAdded(MapNodeEvent<Circle> e)
   {
      Circle circle = e.getNode().getData();
      if (circle != null)
      {
         circle.setMouseTransparent(true);
         mapGroup.getChildren().add(circle);
      }
      System.out.println("New node: x=" + e.getNode().getX() + ", y=" + e.getNode().getY());
   }

   @Override
   public void nodeRemoved(MapNodeEvent<Circle> e)
   {
      Circle circle = e.getNode().getData();
      if (circle != null)
      {
         circle.setMouseTransparent(true);
         mapGroup.getChildren().remove(circle);
      }
   }

   @Override
   public void nodeUpdated(MapNodeEvent<Circle> e)
   {
      /* noop */
   }
   
   class Edge 
   {
      Node<Circle> from, to;

      public Edge(Node<Circle> from, Node<Circle> to)
      {
         this.from = from;
         this.to = to;
      }
      
      Edge opposite()
      {
         return new Edge(to, from);
      }

      @Override
      public int hashCode()
      {
         int hash = 7;
         int fromHash = Objects.hashCode(this.from);
         int toHash = Objects.hashCode(this.to);
         
         hash = 47 * hash + fromHash < toHash ? fromHash : toHash;
         hash = 47 * hash + fromHash < toHash ? toHash : fromHash;
         return hash;
      }

      @Override
      public boolean equals(Object obj)
      {
         if (obj == null)
         {
            return false;
         }
         if (getClass() != obj.getClass())
         {
            return false;
         }
         final Edge other = (Edge) obj;
         return this.from.equals(other.from) && this.to.equals(other.to) 
               || this.from.equals(other.to) && this.from.equals(other.to);
      }
      
      
   }

}
