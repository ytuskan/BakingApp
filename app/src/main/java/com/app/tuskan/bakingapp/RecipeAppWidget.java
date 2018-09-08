package com.app.tuskan.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import static com.app.tuskan.bakingapp.provider.RecipeContract.RecipeEntry.COLUMN_INGREDIENTS;
import static com.app.tuskan.bakingapp.provider.RecipeContract.RecipeEntry.COLUMN_NAME;
import static com.app.tuskan.bakingapp.provider.RecipeContract.RecipeEntry.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider{

    public static final String ACTION_CHANGE_RECIPE = "changeRecipe";

    static Cursor cursor;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        cursor = context.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String ingredients = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS));

        Intent changePositionIntent = new Intent(context, RecipeAppWidget.class);
        changePositionIntent.setAction(ACTION_CHANGE_RECIPE);
        PendingIntent cpPendingIntent = PendingIntent.getBroadcast(context, 0, changePositionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        views.setTextViewText(R.id.recipe_widget_name_textview, name);
        views.setTextViewText(R.id.recipe_widget_ingredients_textview, ingredients);
        views.setOnClickPendingIntent(R.id.recipe_widget_layout, cpPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), RecipeAppWidget.class.getName());
        if(ACTION_CHANGE_RECIPE.equals(intent.getAction())){
            if(cursor.isLast()){
                cursor.moveToFirst();
            }else{
                cursor.moveToNext();
            }
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String ingredients = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS));
            views.setTextViewText(R.id.recipe_widget_name_textview, name);
            views.setTextViewText(R.id.recipe_widget_ingredients_textview, ingredients);
            appWidgetManager.updateAppWidget(thisAppWidget, views);
        }
    }
}

