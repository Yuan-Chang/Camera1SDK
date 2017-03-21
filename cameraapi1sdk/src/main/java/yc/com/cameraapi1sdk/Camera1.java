package yc.com.cameraapi1sdk;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by ychang3 on 2/26/17.
 */

public class Camera1 {

    final static int layoutID = 56755;

    static Camera1Fragment camera1Fragment;

    public static Camera1Fragment init(AppCompatActivity activity)
    {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        if (viewGroup == null)
            return null;

        RelativeLayout parentLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        parentLayout.setLayoutParams(params);
        parentLayout.setId(layoutID);
        //parentLayout.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_light));

        if (camera1Fragment == null)
        {
            camera1Fragment = new Camera1Fragment();
        }
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(layoutID, camera1Fragment);
        ft.commit();

        viewGroup.addView(parentLayout);

        return camera1Fragment;
    }

    public static Camera1Fragment getCamera1Fragment() {
        return camera1Fragment;
    }


}
