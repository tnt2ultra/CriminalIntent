package ru.anri.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import android.text.format.DateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class CrimeListFragment extends Fragment {
    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mPositionCrime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
//            mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemChanged(mPositionCrime);
        }
    }
    // ************************************************************************************************
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            setCrimeHolderParams();
        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int typeCrime) {
            super(inflater.inflate(R.layout.list_item_crime_1, parent, false));
            setCrimeHolderParams();
        }

        private void setCrimeHolderParams() {
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
//            mDateTextView.setText(mCrime.getDate().toString());
//            mDateTextView.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(mCrime.getDate()));
            mDateTextView.setText(formatDate(mCrime.getDate()));
//            android.text.format.DateUtils.formatDateTime(getContext(), millis,
//                    android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY | android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_SHOW_YEAR);
            if (mSolvedImageView != null) {
                mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE: View.GONE);
            }
        }

        private String formatDate(Date date){
//            java.text.DateFormat df = android.text.format.DateFormat.getLongDateFormat(getActivity().getApplicationContext());
//            return android.text.format.DateFormat.format(getString(R.string.full_date_format), date) + ", " + df.format(date);
            java.lang.CharSequence stDate = android.text.format.DateFormat.format(getString(R.string.full_date_time_format), date);  //import android.text.format.DateFormat;
            String st = stDate.toString();
            return st;
        }

        @Override
        public void onClick(View view) {
//                Toast.makeText(getActivity(), mCrime.getTitle() + " - " + getString(R.string.crime_clicked), Toast.LENGTH_SHORT).show();
//                mCrimeRecyclerView.getAdapter().notifyItemMoved(5, 1);
//            Intent intent = new Intent(getActivity(), CrimeActivity.class);
            mPositionCrime = getAdapterPosition();
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId(), mPositionCrime);
            startActivity(intent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_CRIME) {
            // Обработка результата
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ************************************************************************************************
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            CrimeHolder crimeHolder;
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType == 1){
                crimeHolder = new CrimeHolder(layoutInflater, viewGroup, 1);
            } else {
                crimeHolder = new CrimeHolder(layoutInflater, viewGroup);
            }
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int position) {
            Crime crime = mCrimes.get(position);
            crimeHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if(crime.isRequiresPolice())
                return 1;
            else
                return super.getItemViewType(position);
        }
    }
}
