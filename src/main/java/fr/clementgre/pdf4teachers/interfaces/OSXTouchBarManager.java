package fr.clementgre.pdf4teachers.interfaces;

import com.thizzer.jtouchbar.JTouchBar;
import com.thizzer.jtouchbar.common.Image;
import com.thizzer.jtouchbar.common.ImageAlignment;
import com.thizzer.jtouchbar.common.ImageName;
import com.thizzer.jtouchbar.item.TouchBarItem;
import com.thizzer.jtouchbar.item.view.TouchBarButton;
import com.thizzer.jtouchbar.item.view.TouchBarScrubber;
import com.thizzer.jtouchbar.item.view.TouchBarTextField;
import com.thizzer.jtouchbar.javafx.JTouchBarJavaFX;
import com.thizzer.jtouchbar.scrubber.ScrubberDataSource;
import com.thizzer.jtouchbar.scrubber.view.ScrubberImageItemView;
import com.thizzer.jtouchbar.scrubber.view.ScrubberTextItemView;
import com.thizzer.jtouchbar.scrubber.view.ScrubberView;
import fr.clementgre.pdf4teachers.Main;

public class OSXTouchBarManager {

    public OSXTouchBarManager(){

        if(Main.isOSX()) setup();

    }

    public void setup(){

        JTouchBar jTouchBar = new JTouchBar();
        jTouchBar.setCustomizationIdentifier("MyJavaFXJavaTouchBar");

// button
        TouchBarButton touchBarButtonImg = new TouchBarButton();
        touchBarButtonImg.setTitle("Button 1");
        touchBarButtonImg.setAction(view -> System.out.println("Clicked Button_1."));

        Image image = new Image(ImageName.NSImageNameTouchBarColorPickerFill, false);
        touchBarButtonImg.setImage(image);

        jTouchBar.addItem(new TouchBarItem("Button_1", touchBarButtonImg, true));

// fixed space
        jTouchBar.addItem(new TouchBarItem(TouchBarItem.NSTouchBarItemIdentifierFixedSpaceSmall));

// label
        TouchBarTextField touchBarTextField = new TouchBarTextField();
        touchBarTextField.setStringValue("TextField 1");

        jTouchBar.addItem(new TouchBarItem("TextField_1", touchBarTextField, true));

// flexible space
        jTouchBar.addItem(new TouchBarItem(TouchBarItem.NSTouchBarItemIdentifierFlexibleSpace));

// scrubber
        TouchBarScrubber touchBarScrubber = new TouchBarScrubber();
        touchBarScrubber.setActionListener((scrubber, index) -> System.out.println("Selected Scrubber Index: " + index));
        touchBarScrubber.setDataSource(new ScrubberDataSource() {
            @Override
            public ScrubberView getViewForIndex(TouchBarScrubber scrubber, long index) {
                if(index == 0) {
                    ScrubberTextItemView textItemView = new ScrubberTextItemView();
                    textItemView.setIdentifier("ScrubberItem_1");
                    textItemView.setStringValue("Scrubber TextItem");

                    return textItemView;
                }
                else {
                    ScrubberImageItemView imageItemView = new ScrubberImageItemView();
                    imageItemView.setIdentifier("ScrubberItem_2");
                    imageItemView.setImage(new Image(ImageName.NSImageNameTouchBarAlarmTemplate, false));
                    imageItemView.setAlignment(ImageAlignment.CENTER);

                    return imageItemView;
                }
            }

            @Override
            public int getNumberOfItems(TouchBarScrubber scrubber) {
                return 2;
            }
        });

        jTouchBar.addItem(new TouchBarItem("Scrubber_1", touchBarScrubber, true));

        JTouchBarJavaFX.show(jTouchBar, Main.window);

    }

}