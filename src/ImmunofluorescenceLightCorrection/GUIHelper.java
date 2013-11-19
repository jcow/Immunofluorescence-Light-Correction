package ImmunofluorescenceLightCorrection;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class GUIHelper {
	
	public GUIHelper(){}
	
	/**
	 * Sets the border for a JComponent item
	 * @param component
	 * @param title 
	 */
	public static void setBorder(JComponent component, String title){
		component.setBorder(BorderFactory.createCompoundBorder(new TitledBorder(title), new EmptyBorder(10, 10, 10, 10)));
	}
}
