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
import com.example.application.cocktailindex.R;

/**
 * AssignmentEnterNew is used when the user wants to add an assignment to the Task. This is a
 * popup window with a textview and two buttons.
 *
 * @author Morten Astrup
 */
public class SelectImageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Fragment thisFragment;
    private Uri selectedImage;
    private ImageView thumbnail;

    private ImageButton selectCamera;
    private ImageButton selectGallery;

    private Button skip;
    private Button next;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
        selectedImage = ((NewCocktailActivity)getActivity()).getSelectedImage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addcocktail_fragment_setimage, container, false);

        thumbnail = view.findViewById(R.id.addcocktail_fragment_setimage_thumbnail);
        selectCamera = view.findViewById(R.id.addcocktail_fragment_setimage_imagebutton_camera);
        selectGallery = view.findViewById(R.id.addcocktail_fragment_setimage_imagebutton_gallery);
        skip = view.findViewById(R.id.addcocktail_fragment_setimage_button_skip);
        next = view.findViewById(R.id.addcocktail_fragment_setimage_button_next);
        setupListeners();

        Glide.with(this)
                .load(R.drawable.ic_nopicture)
                .override(400, 400)
                .centerInside()
                .apply(RequestOptions.circleCropTransform())
                .into(thumbnail);


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
                ((NewCocktailActivity)getActivity()).onPressingImageButton(1);
            }
        });

        selectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingImageButton(2);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingImageButton(4);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewCocktailActivity)getActivity()).onPressingImageButton(3);
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
