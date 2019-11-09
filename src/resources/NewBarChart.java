/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;



/**
 *
 * @author Kyle
 */
public class NewBarChart {
    
    private BarChart<String, Number> newChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    
    public NewBarChart(){
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("File");
        yAxis.setLabel("Size");
        newChart = new BarChart(xAxis, yAxis);
        
        
    }
    
}
