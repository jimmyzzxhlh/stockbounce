package paint.lowerindicator;

import java.awt.geom.Line2D;


public class VolumeLowerIndicator extends LowerIndicatorAbstract {
	
	private double volumeTotalWidth;
	private double volumeBodyWidth;
	private double volumeDistanceWidth;
	
	private long maxVolume;
	private long minVolume;
	private long volumeUnit;
	private double maxVolumeOnGrid;
	private double minVolumeOnGrid;
	private int yLineNumber;
	private double yUnit;
	
	@Override
	protected void paintBackgroundLines() {
		
	}

	@Override
	protected void paintIndicators() {
		
	}

	@Override
	protected void paintScaleLabel() {
		
	}

	@Override
	protected void initializeParameters() {
//		maxVolume = stockCandleList.getMaxVolume(startDateIndex, endDateIndex);
//		minVolume = stockCandleList.getMinVolume(startDateIndex, endDateIndex);
	}
	
	private void setMaxMinVolume() {
		
	}
}
