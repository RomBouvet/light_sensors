import java.awt.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * The Class Graph.
 */
public class Graph extends JFrame {
	
	/**
	 * Instantiates a new graph.
	 *
	 * @param title the title
	 * @param data the data
	 */
	public Graph(final String title, Map<Integer, ArrayList<ArrayList<Integer>>> data) {
	    super(title);
	    
	    final XYDataset dataset = createDataset(data);
	    final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        setContentPane(chartPanel);
	}
	
	/**
	 * Creates the dataset.
	 *
	 * @param data the data
	 * @return the dataset
	 */
	public XYDataset createDataset(Map<Integer, ArrayList<ArrayList<Integer>>> data) {
		final XYSeriesCollection dataCollection = new XYSeriesCollection();
		Set keys = data.keySet();
		Iterator it = keys.iterator();
		
		while(it.hasNext()) {
			int key = (Integer)it.next();
			System.out.println("Clé = " + key);
			ArrayList<ArrayList<Integer>> dataset = (ArrayList<ArrayList<Integer>>)data.get(key);
			final XYSeries series = new XYSeries("Capteur" + key);
			
			for (ArrayList array : dataset) {
				ArrayList<Integer> inner = array;
				series.add(inner.get(0), inner.get(1));
			}
			dataCollection.addSeries(series);
		}
		
		return dataCollection;
	}
	
	/**
	 * Creates the chart.
	 *
	 * @param dataset the dataset
	 * @return the chart
	 */
	private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
        	"Mesure de la luminosite",
    		"Temps (s)",
    		"Lumen",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        chart.setBackgroundPaint(Color.white);
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                
        return chart;
	}
}
