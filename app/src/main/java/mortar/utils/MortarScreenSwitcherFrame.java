package mortar.utils;

import android.content.Context;
import android.util.AttributeSet;

import flow.path.Path;
import flow.utils.FramePathContainerView;
import flow.utils.SimplePathContainer;
import hu.zhu.example.mortarflowsetup.R;

public class MortarScreenSwitcherFrame extends FramePathContainerView {
    public MortarScreenSwitcherFrame(Context context, AttributeSet attrs) {
        super(context, attrs, new SimplePathContainer(R.id.screen_switcher_traversal_state_holder_tagkey,
                Path.contextFactory(new MortarContextFactory())));
    }
}