package dev.astrup.cocktailindex.Modules.Index;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import dev.astrup.cocktailindex.Database.AppDatabase;
import dev.astrup.cocktailindex.Modules.Details.IdeaActivity;
import dev.astrup.cocktailindex.Objects.Cocktail;
import dev.astrup.cocktailindex.OnItemClickListener;
import dev.astrup.cocktailindex.OnItemLongClickListener;
import dev.astrup.cocktailindex.R;
import dev.astrup.cocktailindex.Utility.CocktailController;

import java.util.ArrayList;
import java.util.List;


public class IdeaFragment extends Fragment {
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    private IdeaAdapter mAdapter;
    // Field variables for RecyclerView - The taskList will be shown in RecyclerView
    private CocktailController cocktailController = CocktailController.getInstance();
    private List<Cocktail> ideaList = new ArrayList<>();
    private AppDatabase db;
    private Cocktail tempDeletion = null;

    /**
     * Creates the Fragment and sets up listeners
     *
     * @param inflater              Inflator to inflate the layout
     * @param container             The container in which this view will be inflated
     * @param savedInstanceState    Previous saved instance
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_idea,container,false);
        db = AppDatabase.getDatabase(getContext());

        // Item click listener
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                java.util.Collections.sort(cocktailController.getIdeas());

                // Scales up the new activity from the cardview clicked
                Intent intent = new Intent(getActivity(), IdeaActivity.class);
                intent.putExtra("cocktail", cocktailController.getIdeas().get(position)); // TODO: Switch to using ID instead later
                startActivity(intent);
            }
        };


        RecyclerView recyclerView = view.findViewById(R.id.idea_fragment_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new IdeaAdapter(cocktailController.getIdeas(), listener, longClickListener, getContext());
        recyclerView.setAdapter(mAdapter);

        showIconIfEmpty(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void showIconIfEmpty(View view) {
        if(mAdapter.getItemCount() <= 0) {
            view.findViewById(R.id.idea_emptylist_text).setVisibility(View.VISIBLE);
            view.findViewById(R.id.idea_emptrylist_icon).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.idea_emptylist_text).setVisibility(View.GONE);
            view.findViewById(R.id.idea_emptrylist_icon).setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        showIconIfEmpty(getView());
        super.onResume();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String name = item.getTitle().toString();
        int itemPosition = mAdapter.getPosition();
        Cocktail cocktail = cocktailController.getIdeas().get(itemPosition);

        if(name.equals("Create Cocktail")) {
            ((MainActivity)getActivity()).updateSpecificCocktailForResult(cocktail);

        } else if(name.equals("Delete Idea")) {
            tempDeletion = cocktailController.getIdeas().get(itemPosition);
            cocktailController.removeCocktail(tempDeletion, db);
            mAdapter.notifyDataSetChanged();

            // Revert deletion
            Snackbar.make(getView(), "Idea Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cocktailController.addCocktail(tempDeletion, db);
                            java.util.Collections.sort(ideaList);
                            mAdapter.notifyDataSetChanged();

                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .show();

        }else if(name.equals("Share Recipe")) {
            ((MainActivity)getActivity()).shareRecipe(cocktail);
        }
        return super.onContextItemSelected(item);
    }

    public void updateList() {
        if(mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    /** ==== STANDARD FRAGMENT METHODS ==== */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
