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
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMaximized(true);
			
			WebView browser = (WebView)scene.lookup("#webview");
			Button btn = (Button)scene.lookup("#btn");
			ComboBox<String> ddl = (ComboBox<String>)scene.lookup("#DropDownList");
			ddl.getItems().addAll("Display1","Display2");
			ddl.getSelectionModel().selectFirst();
			
			pathBuilder();			
			
			JSONExporter.generateJson((Collection<SystemRelation>) DataLayerImpl.getInstance().getData());
			browser.getEngine().load(getGraphPath(ddl.getValue()));
            btn.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent event) {
					try {
						JSONExporter.generateJson((Collection<SystemRelation>) DataLayerImpl.getInstance().getData());
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					browser.getEngine().load("about:blank");
					browser.getEngine().load(getGraphPath(ddl.getValue()));
				}
				
			});
  
	}
	
	private void pathBuilder() throws UnsupportedEncodingException{
		path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0, path.lastIndexOf("/"));
		path = URLDecoder.decode(path, "UTF-8") +"/javascript/";
		
	}
	
	private String getGraphPath(String name){
		return "file://"+path+"/"+name+"/index.html";
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}