package asdf.corporitagoals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.goalsButt:
                Intent i = new Intent(this, GoalsPage.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_to_mid,R.anim.mid_to_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void setupEvenlyDistributedToolbar() {
        // Use Display metrics to get Screen Dimensions
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // Inflate your menu
        mToolbar.inflateMenu(R.menu.menu_main);

        // Add 10 spacing on either side of the toolbar
        mToolbar.setContentInsetsAbsolute(10, 10);

        // Get the ChildCount of your Toolbar, this should only be 1
        int childCount = mToolbar.getChildCount();
        // Get the Screen Width in pixels
        int screenWidth = metrics.widthPixels;

        // Create the Toolbar Params based on the screenWidth
        Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(screenWidth, ActionMenuView.LayoutParams.WRAP_CONTENT);

        // Loop through the child Items
        for (int i = 0; i < childCount; i++) {
            // Get the item at the current index
            View childView = mToolbar.getChildAt(i);
            // If its a ViewGroup
            if (childView instanceof ViewGroup) {
                // Set its layout params
                childView.setLayoutParams(toolbarParams);
                // Get the child count of this view group, and compute the item widths based on this count & screen size
                int innerChildCount = ((ViewGroup) childView).getChildCount();
                int itemWidth = (screenWidth / innerChildCount);
                // Create layout params for the ActionMenuView
                ActionMenuView.LayoutParams params = new ActionMenuView.LayoutParams(itemWidth, ActionMenuView.LayoutParams.WRAP_CONTENT);
                // Loop through the children
                for (int j = 0; j < innerChildCount; j++) {
                    View grandChild = ((ViewGroup) childView).getChildAt(j);
                    if (grandChild instanceof ActionMenuItemView) {
                        // set the layout parameters on each View
                        grandChild.setLayoutParams(params);
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupEvenlyDistributedToolbar();
    }
}
