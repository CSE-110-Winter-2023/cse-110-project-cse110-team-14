package com.example;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import static org.junit.Assert.assertEquals;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.FriendViewAdaptor;
import com.example.myapplication.ViewsFactory;
import com.example.myapplication.model.Friend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
@RunWith(AndroidJUnit4.class)
public class FriendViewAdaptorTest {

    private Context context;
    private ViewsFactory viewsFactory;
    private FriendViewAdaptor viewAdaptor;


    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        viewsFactory = new ViewsFactory(context);
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        viewAdaptor = new FriendViewAdaptor(context, constraintLayout);
    }

    @Test
    public void testAddNewView() {
        Friend newFriend = new Friend("22222", "friend2", 0, 0, 1);
        viewAdaptor.addNewView(newFriend);

        ArrayList<Friend> friends = viewAdaptor.getFriends();
        ArrayList<TextView> labelView = viewAdaptor.getLabelView();
        ArrayList<ImageView> iconView = viewAdaptor.getIconView();
        ArrayList<TextView> tempLabelView = viewAdaptor.getTempLabelView();
        ArrayList<Boolean> overlaps = viewAdaptor.getOverlaps();

        assertEquals(1, friends.size());
        assertEquals(newFriend, friends.get(0));

        assertEquals(1, labelView.size());
        assertEquals("friend2", labelView.get(0).getText());

        assertEquals(1, iconView.size());

        assertEquals(1, tempLabelView.size());
        assertEquals("friend2", tempLabelView.get(0).getText());

        assertEquals(1, overlaps.size());
        assertEquals(false, overlaps.get(0));

    }

    @Test
    public void testChangeAngle() {
        Friend friend = new Friend("33333", "friend2", 0, 0, 1);
        viewAdaptor.addNewView(friend);
        double angle = 45.0;

        viewAdaptor.changeAngle(0, angle);

        TextView labelView = viewAdaptor.getLabelView().get(0);
        ImageView iconView = viewAdaptor.getIconView().get(0);
        TextView tempLabelView = viewAdaptor.getTempLabelView().get(0);

        ConstraintLayout.LayoutParams labelParams = (ConstraintLayout.LayoutParams) labelView.getLayoutParams();
        ConstraintLayout.LayoutParams iconParams = (ConstraintLayout.LayoutParams) iconView.getLayoutParams();
        ConstraintLayout.LayoutParams tempParams = (ConstraintLayout.LayoutParams) tempLabelView.getLayoutParams();

        assertEquals(45.0, labelParams.circleAngle, 0);
        assertEquals(45.0, iconParams.circleAngle, 0);
        assertEquals(45.0, tempParams.circleAngle, 0);

    }

}
