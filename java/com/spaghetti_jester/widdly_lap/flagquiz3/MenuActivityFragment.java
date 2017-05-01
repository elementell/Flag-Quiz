package com.spaghetti_jester.widdly_lap.flagquiz3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuActivityFragment extends Fragment {

    public ArrayList<Integer> selectedReigons;
    public int selectedItem = 1;
    public MenuActivityFragment() {
    }
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu, container, false);
    }
    @Override
    public void onStart() {
        initListView();
        super.onStart();
    }

    private void initListView() {

        listView = (ListView) getActivity().findViewById(R.id.settings_list);

        Setting[] mySettings = new Setting[] {
                new Setting(getText(R.string.number_of_choices).toString(),getText(R.string.number_of_choices_description).toString()),
                new Setting(getText(R.string.world_regions).toString(),getText(R.string.world_regions_description).toString())
        };


        SettingAdapter adapter = new SettingAdapter(getActivity(), R.layout.listview_layout,mySettings);

        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                Setting itemValue = (Setting) listView.getItemAtPosition(position);

                // Show Alert
                switch (itemPosition) {
                    case 0:
                        generateAnswerDialog().show();
                        break;
                    case 1:
                        generateReigonDialog().show();
                        break;
                }
                Toast.makeText(getContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue.myTitle, Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    public Dialog generateReigonDialog() {
        final ArrayList<Integer> prevSelectedReigons = selectedReigons;
        selectedReigons = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.world_regions)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.regions_list_for_settings, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    selectedReigons.add(which);
                                } else if (selectedReigons.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selectedReigons.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MenuActivity)getActivity()).setSelectedReigons(selectedReigons);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedReigons = prevSelectedReigons;
                        ((MenuActivity)getActivity()).setSelectedReigons(selectedReigons);
                    }
                });

        return builder.create();
    }

    public Dialog generateAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        final int prevSelectedItem = selectedItem;
        builder.setTitle(R.string.number_of_choices)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.guesses_list, -1,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItem = which;
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MenuActivity)getActivity()).setNumOfAnswers((selectedItem+1)*2);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selectedItem = prevSelectedItem;
                        ((MenuActivity)getActivity()).setNumOfAnswers((selectedItem+1)*2);
                    }
                });

        return builder.create();
    }
}
