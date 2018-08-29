package main.embl.rieslab.mm.uidevint.ui.graph;

import java.awt.Color;
import java.awt.Dimension;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart {

	String name_, nameX_, nameY_;
	int width_, height_, maxN_;
	int maxrange_;
    private XYSeries series;
    ChartPanel cp;
	
	public Chart(String name, String nameX, String nameY, int maxN, int width, int height, int maxRange){
		name_ = name;
		nameX_ = nameX;
		nameY_ = nameY;
		width_ = width;
		height_ = height;
		maxN_ = maxN;
		maxrange_ = maxRange;
		
		initialize();
	}
	
	public void initialize(){		
	    series = new XYSeries(name_);
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createScatterPlot(null, null,
            null, dataset, PlotOrientation.VERTICAL, false, false, false);
        
        chart.setBackgroundPaint(new Color(240,240,240));

        
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setRange(0, maxrange_);
        ValueAxis xAxis = plot.getDomainAxis();
        xAxis.setRange(0, maxrange_);
        
      	plot.setBackgroundPaint(new Color(230,230,230));
    	plot.setDomainGridlinePaint(new Color(100,100,100));
    	plot.setRangeGridlinePaint(new Color(100,100,100));

    	XYItemRenderer renderer = plot.getRenderer();  
    	renderer.setSeriesPaint(0, new Color(255,91,91));    	    	
        
    	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    	rangeAxis.setTickUnit(new NumberTickUnit(200));
    	NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    	domainAxis.setTickUnit(new NumberTickUnit(200));
    	
    	
        cp = new ChartPanel(chart) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 2350250611998987051L;

			@Override
            public Dimension getPreferredSize() {
                return new Dimension(width_, height_);
            }
        };
	}
	
	public ChartPanel getChart(){
		return cp;
	}
	
	public void clearChart(){
		series.clear();
	}
	
	public void addPoint(double x, double y){
		int n = series.getItemCount();
		if(n>=maxN_){
			series.remove(0);
			series.add(x, y);
		} else {
			series.add(x, y);
		}
	}
	
}
