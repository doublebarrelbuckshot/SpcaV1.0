package com.walterrizzifoundation.grizzi.spca;


import android.support.v4.app.Fragment;
import android.view.Menu;

/**
 * Created by pascal on 20/04/15.
 */
public interface SystemStatus {
    void setSystemStatus(int statusCodeParam);
    void displaySystemStatus(Fragment fragment, Menu menu);
}
