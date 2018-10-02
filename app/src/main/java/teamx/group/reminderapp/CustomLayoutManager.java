package teamx.group.reminderapp;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

public class CustomLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 50f;
    private Context mContext;
    private volatile int monitor=0;



    public CustomLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(mContext) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }

                    //This controls the direction in which smoothScroll looks
                    //for your view
                    @Override
                    public PointF computeScrollVectorForPosition
                    (int targetPosition) {
                        return CustomLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
                    }
                };

        //while(true){
            //if(this.getMonitor()==1) {
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
                /*this.setMonitor(0);
            } else if(this.getMonitor()==0){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }

    @Override
    public synchronized void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }
}
