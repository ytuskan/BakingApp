package com.app.tuskan.bakingapp.utilities;

import com.app.tuskan.bakingapp.models.Recipe;
import com.app.tuskan.bakingapp.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yakup on 18.7.2018.
 */

public class JsonUtils {

    private final static String NAME = "name";
    private final static String ID = "id";

    private final static String INGREDIENTS = "ingredients";
    private final static String QUANTITY = "quantity";
    private final static String MEASURE = "measure";
    private final static String INGREDIENT = "ingredient";

    private final static String STEPS = "steps";
    private final static String SHORT_DESCRIPTION = "shortDescription";
    private final static String DESCRIPTION = "description";
    private final static String VIDEO_URL = "videoURL";
    private final static String THUMBNAIL_URL = "thumbnailURL";

    public static ArrayList<Recipe> parseRecipeJson(String json) throws JSONException {

        ArrayList<Recipe> recipes = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonResultObject = jsonArray.getJSONObject(i);
            String name = jsonResultObject.optString(NAME);
            int id = jsonResultObject.optInt(ID);

            JSONArray jsonIngredientsArray = jsonResultObject.optJSONArray(INGREDIENTS);
            String ingredients = "";
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < jsonIngredientsArray.length(); j++) {
                JSONObject jsonIngredientObject = jsonIngredientsArray.getJSONObject(j);
                double quantity = jsonIngredientObject.optDouble(QUANTITY);
                String measure = jsonIngredientObject.optString(MEASURE);
                String ingredient = jsonIngredientObject.optString(INGREDIENT);
                sb.append(quantity + " - " + measure + " - " + ingredient + "\n");
            }
            ingredients = sb.toString();

            Recipe recipe = new Recipe(id, name, ingredients);
            recipes.add(recipe);
        }

        return recipes;
    }

    public static ArrayList<Video> parseVideoJson(String json, int id) throws JSONException {
        ArrayList<Video> videos = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(json);

        JSONObject jsonObject = jsonArray.getJSONObject(id - 1);
        JSONArray jsonStepsArray = jsonObject.getJSONArray(STEPS);
        for (int j = 0; j < jsonStepsArray.length(); j++) {
            JSONObject step = jsonStepsArray.getJSONObject(j);
            String shortDescription = step.optString(SHORT_DESCRIPTION);
            String description = step.optString(DESCRIPTION);
            String videoUrl = step.optString(VIDEO_URL);
            String thumbnailUrl = step.optString(THUMBNAIL_URL);
            String url = "";
            if (videoUrl != "") {
                url = videoUrl;
            } else if (thumbnailUrl != "") {
                url = thumbnailUrl;
            }
            Video video = new Video(shortDescription, description, url);
            videos.add(video);
        }
        return videos;
    }


}
