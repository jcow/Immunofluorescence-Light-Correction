
package ImmunofluorescenceLightCorrection;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageAdjuster {
	
	private final int binCount = 101;
	private String error = "";
	private final double binPercentage = 0.5;
	private final double contributionThresholdPercentage = 0.001;
	 
	public boolean adjustImage(String filepath, String writeTo){
		
		String[] fileChar = FileSystemHelper.getFileCharacteristics(filepath);
		String path = fileChar[0];
		String filename = fileChar[1];
		String fileExtension = fileChar[2];
		
		int[] bins = new int[binCount];
		
		BufferedImage currentImage = null;
		try {
			currentImage = ImageIO.read(new File(filepath));
			for (int y = 0; y < currentImage.getHeight(); y++) {
				for (int x = 0; x < currentImage.getWidth(); x++) {
					  int[] rgb = getRGB(currentImage, x, y);
					  int red = rgb[0];
					  int green = rgb[1];
					  int blue = rgb[2];
					  
					  float[] hsb = Color.RGBtoHSB(red, green, blue, null);
					  int bin = (int)(100*hsb[2]);
					  bins[bin]++;
				}
			}
			
		} 
		catch (IOException e) {
			error = e.getMessage();
			return false;
		}
		
		
		BufferedImage newImage = new BufferedImage(currentImage.getWidth(), currentImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		double contributionBins = getTotalContributingPercentage(bins, currentImage.getWidth(), currentImage.getHeight());
		
		// small amount of bins, do the secondary parsing
		if(contributionBins < binPercentage){
			
			int lowestFilledIndex = getLowestContributingIndex(bins);
			float brightnessIterateBy = 0.01f * ((float)(bins.length-lowestFilledIndex))/bins.length;

			for (int y = 0; y < newImage.getHeight(); y++) {
					for (int x = 0; x < newImage.getWidth(); x++) {
						int[] rgb = getRGB(currentImage, x, y);
						int red = rgb[0];
						int green = rgb[1];
						int blue = rgb[2];

						float[] hsb = Color.RGBtoHSB(red, green, blue, null);

						int bin = (int)(100*hsb[2]);
						float new_val = 0;
						if(bin > lowestFilledIndex){
							int multiplier = bin-lowestFilledIndex;
							new_val = (multiplier * brightnessIterateBy);
						}

						hsb[2] = new_val;

						int clr = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
						newImage.setRGB(x, y, clr);
					}
			}
		}
		// normal spread
		else{
			int maxIndex = getMaxIndex(bins);
			for (int y = 0; y < newImage.getHeight(); y++) {
					for (int x = 0; x < newImage.getWidth(); x++) {
						int[] rgb = getRGB(currentImage, x, y);
						int red = rgb[0];
						int green = rgb[1];
						int blue = rgb[2];
						
						float[] hsb = Color.RGBtoHSB(red, green, blue, null);
						float new_val = (float)(hsb[2] - maxIndex*0.01);
						if(new_val < 0){
							hsb[2] = 0f;
						}
						else{
							hsb[2] = new_val;
						}
						
						int clr = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
						newImage.setRGB(x, y, clr);
					}
			}
		}
		
		try {
			File outputfile = new File(writeTo);
			ImageIO.write(newImage, fileExtension, outputfile);
		} 
		catch (IOException e) {
			error = e.getMessage();
			return false;
		}
		
		return true;
	}
	
	public String getError(){
		return error;
	}
	
	private int[] getRGB(BufferedImage image, int x, int y){
		int[] colors = new int[3];
		int clr   = image.getRGB(x, y); 
		colors[0]   = (clr & 0x00ff0000) >> 16;
		colors[1] = (clr & 0x0000ff00) >> 8;
		colors[2]  =  clr & 0x000000ff;
		
		return colors;
	}
	
	private int getMaxIndex(int[] bins){
		int max = 0;
		int binIndex = 0;
		for(int i = 0; i < bins.length; i++){
			if(max <= bins[i]){
				max = bins[i];
				binIndex = i;
			}
		}
		return binIndex;
	}
	
	
	private double getTotalContributingPercentage(int[] bins, int imgWidth, int imgHeight){
		double contributionThreshold = getContributionThreshold(imgWidth, imgHeight);
		double count = 0;
		for(int i = 0; i < bins.length; i++){
			if(bins[i] > contributionThreshold){
				count += 1;
			}
		}
		
		return (count/bins.length);
	}
	
	private int getLowestContributingIndex(int[] bins){
		int lowestIndex = 0;
		for(int i = 0; i < bins.length; i++){
			if(bins[i] > 0){
				lowestIndex = i;
				break;
			}
		}
		return lowestIndex;
	}
	
	private double getContributionThreshold(int imgWidth, int imgHeight){
		return imgWidth * imgHeight * contributionThresholdPercentage;
	}
}
