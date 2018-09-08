package com.app.tuskan.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Yakup on 27.7.2018.
 */

public class RecipeContract {

    public static final String AUTHORITY = "com.app.tuskan.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";

    public static final long INVALID_RECIPE_ID = -1;

    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INGREDIENTS = "ingredients";
    }

}
