package ru.anri.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrimeListFragment extends Fragment {
//    private static final String LOG_TAG = "MyLogs";
    private static final int REQUEST_CRIME = 1;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mPositionCrime;
    private boolean mSubtitleVisible;
    private int mSize;
    private TextView mEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSize = -1;
        if(savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        mEmpty = view.findViewById(R.id.textViewEmpty);
        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
//        String subtitle = getString(R.string.subtitle_format, crimeCount);
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);
        if(!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        int oldSize = mSize;
        int mSize = crimes.size();

        if (mSize <= 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            if(oldSize > mSize) {
//                mAdapter.notifyDataSetChanged();
                mAdapter.removeAt(mPositionCrime);
            } else {
                mAdapter.notifyItemChanged(mPositionCrime);
            }
        }
        updateSubtitle();
    }

    public boolean isEmptyView () {
        return mSize <= 0;
    }

    // ************************************************************************************************
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
//        private TextView mTimeTextView;
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
//            mTimeTextView = itemView.findViewById(R.id.crime_time);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
//            mDateTextView.setText(mCrime.getDate().toString());
//            mDateTextView.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(mCrime.getDate()));
            mDateTextView.setText(formatDate(mCrime.getDate()));
//            mTimeTextView.setText(formatTime(mCrime.getDate()));
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

        private String formatTime(Date time){
            java.lang.CharSequence stTime = android.text.format.DateFormat.format(getString(R.string.full_time_format), time);
            String st = stTime.toString();
            return st;
        }

        @Override
        public void onClick(View view) {
//                Toast.makeText(getActivity(), mCrime.getTitle() + " - " + getString(R.string.crime_clicked), Toast.LENGTH_SHORT).show();
//                mCrimeRecyclerView.getAdapter().notifyItemMoved(5, 1);
//            Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
            mPositionCrime = getAdapterPosition();
//            Log.d(LOG_TAG, "mPositionCrime=" + mPositionCrime);
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
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

        public void removeAt(int position) {
//            mCrimes.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mCrimes.size());
        }

    }
}
