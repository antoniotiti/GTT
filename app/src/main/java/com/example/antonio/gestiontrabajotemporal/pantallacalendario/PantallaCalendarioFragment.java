package com.example.antonio.gestiontrabajotemporal.pantallacalendario;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class PantallaCalendarioFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new PantallaCalendarioAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
	}

}
