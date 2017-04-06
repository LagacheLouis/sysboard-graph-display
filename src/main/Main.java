package main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;

import cern.mpe.systems.core.domain.relation.SystemRelation;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import json.JSONExporter;


public class Main extends Application {
	public static Scene scene;
    public static String path;
	@Override
	public void start(Stage primaryStage) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
			StackPane page = (StackPane) FXMLLoader.load(Main.class.getResource("graphdisplay.fxml"));
			scene = new Scene(page);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMaximized(true);
  
	}
	
	 public static void pathBuilder(){
         path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
         path = path.substring(0, path.lastIndexOf("/"));
         try {
             path = URLDecoder.decode(path, "UTF-8") +"/javascript/";
         } catch (UnsupportedEncodingException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         
     }
     
     public static String getGraphPath(String name){
         return "file://"+path+"/"+name+"/index.html";
     }

	
	public static void main(String[] args) {
		launch(args);
		
	}
}