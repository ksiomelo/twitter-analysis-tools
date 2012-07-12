package vis;

import java.util.List;

import org.jfree.chart.labels.StandardXYLabelGenerator;
import org.jfree.data.xy.XYDataset;

public class ItemLabel extends StandardXYLabelGenerator {
	
	private List<String> objects;
	
	public ItemLabel(List<String> objs) {
		//this.
		objects = objs;
	}
	
	
	@Override
	public String generateLabel(XYDataset dataset, int series, int item) {
		// TODO Auto-generated method stub
		return objects.get(item);
	}
	
}
