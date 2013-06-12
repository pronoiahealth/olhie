package com.pronoiahealth.olhie.client.widgets.progressbar;

public class CancelProgressBarTextFormatter extends ProgressBar.TextFormatter {  
    @Override  
    protected String getText(ProgressBar bar, double curProgress) {  
        if (curProgress < 0) {  
            return "Cancelled";  
        }  
        return ((int) (100 * bar.getPercent())) + "%";  
    }  
}  
