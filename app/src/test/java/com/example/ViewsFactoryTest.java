package com.example;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.ViewsFactory;
import com.example.myapplication.model.Friend;

public class ViewsFactoryTest {

    private ViewsFactory viewsFactory;
    private Context context;
    private ImageView newImage;
    private TextView newText;

    @Before
    public void create(){
        this.context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        viewsFactory = new ViewsFactory(context);

        ConstraintLayout.LayoutParams newLayoutParams1 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams1.circleRadius = 100;
        newLayoutParams1.circleAngle = 0;
        newText = new TextView(context);
        newText.setLayoutParams(newLayoutParams1);

        ConstraintLayout.LayoutParams newLayoutParams2 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        newLayoutParams2.circleRadius = 100;
        newLayoutParams2.circleAngle = 0;
        newImage = new ImageView(context);
        newImage.setLayoutParams(newLayoutParams2);
    }

    @Test
    public void testBuildTextView() {
        Friend friend = new Friend("111", "friend1", 0, 0, 1);
        TextView textView = viewsFactory.buildTextView(friend);

        assertEquals(textView.getText(), friend.getLabel());

    }

    @Test
    public void testChangeAngle() {

        viewsFactory.changeAngle(newText, 50);
        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) newText.getLayoutParams();
        assertEquals(layout.circleAngle, 50, 0);

    }

    @Test
    public void testGetAngle() {

        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) newText.getLayoutParams();
        assertEquals(layout.circleAngle, viewsFactory.getAngle(newText), 0);

    }

    @Test
    public void testChangeDistance() {

        viewsFactory.changeDistance(newText, 30);
        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) newText.getLayoutParams();
        assertEquals(layout.circleRadius, 30, 0);

    }

    @Test
    public void testGetDistance() {

        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) newText.getLayoutParams();
        assertEquals(layout.circleRadius, viewsFactory.getDistance(newText), 0);
    }

}
