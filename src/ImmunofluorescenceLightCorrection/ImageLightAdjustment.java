
package ImmunofluorescenceLightCorrection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public class ImageLightAdjustment {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ProcessController app = new ProcessController();
                app.setVisible(true);
            }
        });
	}
}
