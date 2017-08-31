package ru.artempugachev.homeinformation.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import ru.artempugachev.homeinformation.R;

/**
 * Component for wind arrow
 */

public class WindArrow extends FrameLayout {
    private Context context;
    private AttributeSet attrs;

    public WindArrow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater  = LayoutInflater.from(context);
        inflater.inflate(R.layout.wind_arrow, this);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.WindArrow);
        int windDir = attrArray.getInt(R.styleable.WindArrow_direction, 0);
        attrArray.recycle();
        setDirection(windDir);
    }

    public void setDirection(int direction) {
        ImageView arrowImageView = (ImageView) findViewById(R.id.windArrowImageView);
        Wind wind = new Wind(0.0, direction);
        int arrowResource = wind.toWindArrowResource();
        arrowImageView.setImageResource(arrowResource);
    }

}
