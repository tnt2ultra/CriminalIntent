package ru.anri.android.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class BigImageFragment extends DialogFragment implements View.OnClickListener {
    private static final String ARG_FILEPATH = "filepath";

    private ImageView mBigImage;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String filePath = getArguments().getString(ARG_FILEPATH);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_big_image, null);

        mBigImage = v.findViewById(R.id.bigImage);
        mBigImage.setOnClickListener(this);
        Bitmap bitmap = PictureUtils.getScaledBitmapFull(filePath, getActivity());
        mBigImage.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public static BigImageFragment newInstance(String filePath) {
        Bundle args = new Bundle();
        args.putString(ARG_FILEPATH, filePath);
        BigImageFragment fragment = new BigImageFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
