package vis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Scatterplot extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private double [] xdata1;
	private double [] ydata1;
	private double [] xdata2;
	private double [] ydata2;
	private double [] xdata3;
	private double [] ydata3;
	
	private List<List<String>> objectNames;
//	private List<String> objects2;
//	private List<String> objects3;
	
//	public static void main(String[] args) {
//		double xdata1[] = { 0.05, 0.20, 0.34, 0.45, 0.5, 0.7, 0.9, 1.0 };
//		double ydata1[] = { 0.94, 0.80, 0.69, 0.44, 0.31, 0.25, 0.01, 0.0 };
//		
//		Scaterplot frame = new Scaterplot(xdata1,ydata1, null);
//
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setBounds(10, 10, 500, 500);
//		frame.setTitle("Sample graph");
//		frame.setVisible(true);
//	}

	/*
	 * Constructor
	 */
	public Scatterplot(double [] xdata1, double [] ydata1, 
			double [] xdata2, double [] ydata2, 
			double [] xdata3, double [] ydata3, List<String> objs1, List<String> objs2, List<String> objs3) {
		
		this.objectNames = new ArrayList<List<String>>();
		
		this.xdata1 = xdata1;
		this.ydata1 = ydata1;
		this.objectNames.add(objs1);
		
		this.xdata2 = xdata2;
		this.ydata2 = ydata2;
		this.objectNames.add(objs2);
		
		this.xdata3 = xdata3;
		this.ydata3 = ydata3;
		this.objectNames.add(objs3);
		
		//JFreeChart chart = createChart();

		try {
			// ChartUtilities.saveChartAsPNG(new File("test.png"), chart, 300,
			// 300);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//ChartPanel cpanel = new ChartPanel(chart);
		getContentPane().add(createChart(), BorderLayout.CENTER);
	}

	
	private List<Dot> dotify(double [] xdata, double [] ydata){
		List<Dot> dots = new ArrayList<Dot>();
		
		for (int i = 0; i < xdata.length; i++) {
			Dot d = new Dot();
			d.x = xdata[i];
			d.y = ydata[i];
			dots.add(d);
		}
		return dots;
	}
	
	ChartPanel  chart;
	XYSeriesCollection data;
	private ChartPanel createChart() {

		
		data = new XYSeriesCollection();

		List<Dot> dots1 = dotify(xdata1, ydata1);
		List<Dot> dots2 = dotify(xdata2, ydata2);
		List<Dot> dots3 = dotify(xdata3, ydata3);
		
		addSeries("cluster 1", dots1);
		addSeries("cluster 2", dots2);
		addSeries("cluster 3", dots3);
		
		
		
		chart = new ChartPanel(ChartFactory.createScatterPlot("Distance",
				"X", "Y", data, PlotOrientation.VERTICAL, true,
				true, false));
		
		
		chart.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent e) {
                XYItemEntity xyitem=(XYItemEntity) e.getEntity(); // get clicked entity
                System.out.println(xyitem.getToolTipText());
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent arg0) {
            }
        });

		
		//XYPlot plot = chart.getXYPlot();
		// StandardXYItemRenderer renderer = (StandardXYItemRenderer)
		// plot.getRenderer();
		Font font = new Font("Meiryo", Font.PLAIN, 12);
		Font font2 = new Font("Meiryo", Font.PLAIN, 8);
		//chart.getLegend().setItemFont(font);
		chart.getChart().getTitle().setFont(font);
		XYPlot xyp = chart.getChart().getXYPlot();
		

		// fill and outline
		StandardXYItemRenderer r = (StandardXYItemRenderer) xyp.getRenderer(); 
		//XYLabelGenerator
		NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        
//		XYItemLabelGenerator generator =
//            new StandardXYItemLabelGenerator(
//                StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
//                format, format);
		
		
		//ItemLabel gen = new ItemLabel(this.objects);
		
		r.setToolTipGenerator(new XYToolTipGenerator() {
			
			@Override
			public String generateToolTip(XYDataset dataset, int series, int item) {
				return objectNames.get(series).get(item);
			}
		});
		
		r.setLabelGenerator(new XYLabelGenerator() {
			
			@Override
			public String generateLabel(XYDataset dataset, int series, int item) {
				return objectNames.get(series).get(item);
			}
		});
		
		
		r.setSeriesPaint(0, new Color(34,139,34)); // green
		r.setSeriesPaint(1, Color.MAGENTA);
		r.setSeriesPaint(2, Color.red);
		
		//r.setBaseLabelGenerator(gen);
		//r.setBaseItemLabelGenerator(generator);
        r.setBaseItemLabelsVisible(false);
		r.setItemLabelsVisible(false);
		//r.setItemLabelFont(new Font("SansSerif", Font.PLAIN, 10));
		
		r.setShapesFilled(true); 
    
		r.setSeriesOutlinePaint(0, Color.RED);
		r.setSeriesOutlinePaint(1, Color.BLUE);
		r.setSeriesShapesFilled(0, false);
		r.setSeriesShapesFilled(1, false);

		return chart;
	}

	private void addSeries(String componentId, List<Dot> dots) {
		
		XYSeries series1 = new XYSeries(componentId,false,true);

		for (int i = 0; i < dots.size(); i++) {
			series1.add(dots.get(i).y, dots.get(i).x);
		}

		data.addSeries(series1);

		//return data;
	}

	static class Dot {
		double x;
		double y;
	}
}
