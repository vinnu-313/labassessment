/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package las.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import las.view.ClientView;

/**
 *
 * @author megha
 */
public class ViewQuestionViewListener extends WindowAdapter{

    @Override
    public void windowClosing(WindowEvent e) {
        ClientView.getViewQuestionView().dispose();
        ClientView.setViewQuestionView(null);
    }
    
}
