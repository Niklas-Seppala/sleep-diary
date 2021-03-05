package com.example.sleepdiary.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.sleepdiary.R;
import com.example.sleepdiary.data.models.Rating;

public abstract class SleepRatingIconService {

    public static Drawable getIconFromRating(Context context, Rating userRating) {
        switch (userRating) {
            case VERY_BAD:
                return AppCompatResources.getDrawable(context,
                        R.drawable.ic_baseline_sentiment_very_dissatisfied_64);
            case BAD:
                return AppCompatResources.getDrawable(context,
                        R.drawable.ic_baseline_sentiment_dissatisfied_64);
            case OK:
                return AppCompatResources.getDrawable(context,
                        R.drawable.ic_baseline_sentiment_neutral_64);
            case GOOD:
                return AppCompatResources.getDrawable(context,
                        R.drawable.ic_baseline_sentiment_satisfied_64);
            case VERY_GOOD:
                return AppCompatResources.getDrawable(context,
                        R.drawable.ic_baseline_sentiment_very_satisfied_64);
            case UNDEFINED:
                throw new IllegalArgumentException("Rating is undefined!");
            default:
                throw new IllegalArgumentException("Something VERY bad");
        }
    }
}
