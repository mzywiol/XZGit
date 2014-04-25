/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xz.spacial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
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


public class SpacialUI extends Application
{
   FlatMap<Circle> map = new FlatMap<>();
   Node<Circle> selectedNode = null;
   Node<Circle> closestNode = null;
   private Rectangle mapBackground;
   private Group mapGroup;
   
   HashMap<Edge, Line> edges = new HashMap<>();

   @Override
   public void start(Stage primaryStage)
   {
      map.setSelectionTolerance(5.0);
      
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
                  
                  Node<Circle> newNode = map.addNode(x, y, null);
                  if (newNode.getData() == null)
                  {
                     Circle circle = new Circle(x, y, 7.0, Color.BLACK);
                     circle.setMouseTransparent(true);
                     mapGroup.getChildren().add(circle);
                     newNode.setData(circle);
                  }
                  
                  ArrayList<Edge> toRemove = new ArrayList<>();
                  for (Node<Circle> edge : newNode.edges.values())
                  {
                     Line line = new Line(newNode.getX(), newNode.getY(), edge.getX(), edge.getY());
                     line.setMouseTransparent(true);
                     mapGroup.getChildren().add(line);
                     edges.put(new Edge(newNode, edge), line);
                     edges.put(new Edge(edge, newNode), line);
                     
                     Stream<Map.Entry<Edge, Line>> edgesOfEdge = edges.entrySet().stream().filter(e -> e.getKey().from == edge);
                     edgesOfEdge.forEach(entry -> {
                        if (!edge.edges.values().contains(entry.getKey().to)) 
                        {
                           mapGroup.getChildren().remove(entry.getValue());
                           toRemove.add(entry.getKey());
                           toRemove.add(entry.getKey().opposite());
                        }
                     }
                     );
                     for (Edge toRem : toRemove)
                     {
                        edges.remove(toRem);
                     }
                     toRemove.clear();
                  }
                  System.out.println("New node: x=" + x + ", y=" + y);
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
         hash = 47 * hash + Objects.hashCode(this.from);
         hash = 47 * hash + Objects.hashCode(this.to);
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
         if (!Objects.equals(this.from, other.from))
         {
            return false;
         }
         if (!Objects.equals(this.to, other.to))
         {
            return false;
         }
         return true;
      }
      
      
   }

}
