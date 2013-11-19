
package ImmunofluorescenceLightCorrection;

public class Config {
	
	public static String[] processableImages = {"jpg"};
	
	public static boolean isProcessableImageFile(String extension){
		boolean in = false;
		
		for(int i = 0; i < Config.processableImages.length; i++){
			if(extension.compareTo(Config.processableImages[i]) == 0){
				in = true;
				break;
			}
		}
		
		return in;
	}
}
