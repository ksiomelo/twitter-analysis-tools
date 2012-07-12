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
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartOne extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private double [] xdata;
	private double [] ydata;
	private List<String> objects;

	public static void main(String[] args) {
		double xdata1[] = { 0.05, 0.20, 0.34, 0.45, 0.5, 0.7, 0.9, 1.0 };
		double ydata1[] = { 0.94, 0.80, 0.69, 0.44, 0.31, 0.25, 0.01, 0.0 };
		
		ChartOne frame = new ChartOne(xdata1,ydata1, null);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 500, 500);
		frame.setTitle("Sample graph");
		frame.setVisible(true);
	}

	/*
	 * Constructor
	 */
	public ChartOne(double [] xdata, double [] ydata, List<String> objs) {
		this.xdata = xdata;
		this.ydata = ydata;
		this.objects = objs;
		
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

	ChartPanel  chart;
	private ChartPanel createChart() {

		List<Dot> dots = new ArrayList<Dot>();
		
		for (int i = 0; i < xdata.length; i++) {
			Dot d = new Dot();
			d.x = xdata[i];
			d.y = ydata[i];
			dots.add(d);
		}

		XYDataset data = (XYDataset) createData("component", dots);
		chart = new ChartPanel(ChartFactory.createScatterPlot("Distance",
				"X", "Y", data, PlotOrientation.VERTICAL, true,
				true, false));
		
		
		chart.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent e) {
               /* System.out.println("Click event!");
                XYPlot xyPlot2 = chart.getChart().getXYPlot();
                  // Problem: the coordinates displayed are the one of the previously selected point !
                System.out.println(xyPlot2.getDomainCrosshairValue() + " "
                        + xyPlot2.getRangeCrosshairValue());*/
                
                XYItemEntity xyitem=(XYItemEntity) e.getEntity(); // get clicked entity
                System.out.println(xyitem.getToolTipText());
//                XYDataset dataset = (XYDataset)xyitem.getDataset(); // get data set
//                System.out.println(xyitem.getItem()+" item of "+xyitem.getSeriesIndex()+"series");
//                System.out.println(dataset.getXValue(xyitem.getSeriesIndex(), xyitem.getItem()));
//                System.out.println(dataset.getYValue(xyitem.getSeriesIndex(), xyitem.getItem()));

                
                
                
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
		
		
		ItemLabel gen = new ItemLabel(this.objects);
		
		//r.setBaseItemLabelGenerator(generator);
		r.setToolTipGenerator(new XYToolTipGenerator() {
			
			@Override
			public String generateToolTip(XYDataset arg0, int arg1, int item) {
				return objects.get(item);
			}
		});
		
		r.setLabelGenerator(gen);
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

	private XYSeriesCollection createData(String componentId, List<Dot> dots) {
		XYSeriesCollection data = new XYSeriesCollection();

		XYSeries series1 = new XYSeries(componentId,false,true);

		for (int i = 0; i < dots.size(); i++) {
			series1.add(dots.get(i).y, dots.get(i).x);
		}

		data.addSeries(series1);

		return data;
	}

	static class Dot {
		double x;
		double y;
	}
}
