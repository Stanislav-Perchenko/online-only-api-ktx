package cam.alperez.samples.onlineonlyapiktx.ui.utils;

import android.graphics.Point;

public final class UiUtils {

    public static void calculateScaledImageSize(int originalImageWidth, int originalImageHeight,
                                                int targetWidth, int targetHeight,
                                                Point scaledImageSize)
    {
        if ((originalImageWidth < targetWidth) && (originalImageHeight < targetHeight)) {
            scaledImageSize.set(originalImageWidth, originalImageHeight);
            return;
        } else if (((float)targetWidth / targetHeight) > ((float) originalImageWidth / originalImageHeight)) {
            scaledImageSize.set(
                    targetWidth,
                    Math.round((float)targetWidth * originalImageHeight / originalImageWidth));
        } else {
            scaledImageSize.set(
                    Math.round((float)targetHeight * originalImageWidth / originalImageHeight),
                    targetHeight);
        }

    }

    private UiUtils(){}
}
