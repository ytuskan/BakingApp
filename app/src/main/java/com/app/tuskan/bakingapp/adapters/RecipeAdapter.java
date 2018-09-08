package com.app.tuskan.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tuskan.bakingapp.R;
import com.app.tuskan.bakingapp.activities.DescriptionActivity;
import com.app.tuskan.bakingapp.models.Recipe;

import java.util.List;

/**
 * Created by Yakup on 18.7.2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String INTENT_KEY = "key";


    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public static class recipeHolder extends RecyclerView.ViewHolder {

        TextView recipeItemNameTextView;

        public recipeHolder(View itemView) {
            super(itemView);
            recipeItemNameTextView = itemView.findViewById(R.id.recipe_item_name_textview);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return new recipeHolder(layoutInflater.inflate(R.layout.recipe_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Recipe recipe = recipes.get(position);
        final recipeHolder recipeHolder = (RecipeAdapter.recipeHolder) holder;

        recipeHolder.recipeItemNameTextView.setText(recipe.getName());

        recipeHolder.recipeItemNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra(INTENT_KEY, recipe);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
