package ru.anri.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID = "ru.anri.android.criminalintent.crime_id";
    private static final String EXTRA_CRIME_POSITION = "ru.anri.android.criminalintent.crime_position";

    @Override
    protected Fragment createFragment() {
//        return new CrimeFragment();
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        int position = getIntent().getIntExtra(EXTRA_CRIME_POSITION, 0);
        return CrimeFragment.newInstance(crimeId, position);
    }

//    public static Intent newIntent(Context packageContext, UUID crimeId) {
//        Intent intent = new Intent(packageContext, CrimeActivity.class);
//        intent.putExtra(EXTRA_CRIME_ID, crimeId);
//        return intent;
//    }

    public static Intent newIntent(Context packageContext, UUID crimeId, int position) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_POSITION, position);
        return intent;
    }
}
