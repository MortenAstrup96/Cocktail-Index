package com.example.application.cocktailindex.Fragments.DialogFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.cocktailindex.Activities.NewCocktailActivity;
import com.example.application.cocktailindex.Objects.Cocktail;
import com.example.application.cocktailindex.R;

/**
 * AssignmentEnterNew is used when the user wants to add an assignment to the Task. This is a
 * popup window with a textview and two buttons.
 *
 * @author Morten Astrup
 */
public class FinaliseCocktailDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Fragment thisFragment;


    private Button next;
    private Uri imagePath;
    private ImageView thumbnail;

    private ImageButton selectCamera;
    private ImageButton selectGallery;
    private Cocktail temporaryCocktail;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addcocktail_fragment_finalise, container, false);

        temporaryCocktail = ((NewCocktailActivity)getActivity()).getEditableCocktail();
        imagePath = ((NewCocktailActivity)getActivity()).getSelectedImage();
        temporaryCocktail.imagePath = imagePath.toString();

        thumbnail = view.findViewById(R.id.addcocktail_fragment_setimage_thumbnail2);
        selectCamera = view.findViewById(R.id.addcocktail_fragment_setimage_imagebutton_camera2);
        selectGallery = view.findViewById(R.id.addcocktail_fragment_setimage_imagebutton_gallery2);
        next = view.findViewById(R.id.addcocktail_fragment_setimage_imagebutton_finish);


        if(imagePath != null) {
            Glide.with(this)
                    .load(imagePath)
                    .override(400, 400)
                    .centerInside()
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_nopicture)
                    .override(400, 400)
                    .centerInside()
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail);
        }

        //next = view.findViewById(R.id.addcocktail_fragment_setimage_button_next);
        setupListeners();
        Uri imagePath = Uri.parse(temporaryCocktail.imagePath);
        setThumbnail(imagePath);


        return view;
    }

    public void setThumbnail(Uri image) {
        if(image != null) {
            Glide.with(this)
                    .load(image)
                    .override(400, 400)
                    .centerInside()
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_nopicture)
                    .override(400, 400)
                    .centerInside()
                    .apply(RequestOptions.circleCropTransform())
                    .into(thumbnail);
        }
    }

    private void setupListeners() {
        selectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingImageButton(2);
            }
        });

        selectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingImageButton(3);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingImageButton(1);
            }
        });
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPressingImageButton(int button);
    }
}
