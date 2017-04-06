/**
 * Copyright (c) 2017 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.ResourceBundle;

import cern.mpe.systems.core.domain.relation.SystemRelation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import json.JSONExporter;

public class GraphController implements Initializable {
        
        @FXML
        private WebView webView;
        @FXML
        private Button button;
        @FXML
        private ComboBox<String> dropDownList;
        

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            dropDownList.getItems().addAll("Display1","Display2");
            dropDownList.getSelectionModel().selectFirst();
            
            try {
                Main.pathBuilder();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JSONExporter.generateJson((Collection<SystemRelation>) DataLayerImpl.getInstance().getData());
            
            webView.getEngine().load(Main.getGraphPath(dropDownList.getValue()));
            
            button.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        JSONExporter.generateJson((Collection<SystemRelation>) DataLayerImpl.getInstance().getData());
                        webView.getEngine().load("about:blank");
                        webView.getEngine().load(Main.getGraphPath(dropDownList.getValue()));
                    }
                
            });
        }

       
}