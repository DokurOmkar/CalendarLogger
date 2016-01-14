    package com.omkardokur.calendarlogger;


    import android.content.ComponentName;
    import android.content.Intent;
    import android.content.IntentFilter;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.database.Cursor;
    import android.os.Bundle;
    import android.provider.CallLog;
    import android.support.v4.app.Fragment;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.CompoundButton;
    import android.widget.EditText;
    import android.widget.Toast;


    /**
     * A simple {@link Fragment} subclass.
     */
    public class CallsFragment extends Fragment {
        EditText numberTextCalls;
        Button minusBtn, plusBtn, saveCallsBtn;
        CheckBox incoming, outgoing, missed, enableCbCalls;
        boolean in = true;
        boolean out = true;
        boolean miss = true;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        public CallsFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_calls, container, false);
            numberTextCalls = (EditText)rootView.findViewById(R.id.numberEtCalls);
            numberTextCalls.setGravity(Gravity.CENTER);
            numberTextCalls.setText("15");
            minusBtn = (Button) rootView.findViewById(R.id.decreaseBtnCalls);
            plusBtn = (Button) rootView.findViewById(R.id.increaseBtnCalls);
            incoming = (CheckBox) rootView.findViewById(R.id.incomingCallsCb);
            outgoing = (CheckBox) rootView.findViewById(R.id.outgoingCallsCb);
            missed = (CheckBox) rootView.findViewById(R.id.missedCallsCb);
            saveCallsBtn = (Button) rootView.findViewById(R.id.btnSaveCalls);
            enableCbCalls = (CheckBox) rootView.findViewById(R.id.enableCbCalls);
            sharedPreferences = this.getActivity().getPreferences(this.getActivity().MODE_PRIVATE);
            boolean autoCheck = sharedPreferences.getBoolean("calls",false);
            enableCbCalls.setChecked(autoCheck);
            autoSaveCalls();
            checkDecrease();
            checkIncrease();
            incomingCheckBox();
            outgoingCheckBox();
            missedCheckBox();
            saveCalls();

         return rootView;
        }

        private void autoSaveCalls() {
            enableCbCalls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    if (enableCbCalls.isChecked()) {
                        editor.putBoolean("calls", true);
                        // Switch On Broadcast Receiver
                        PackageManager pm = getActivity().getPackageManager();
                        ComponentName receiver = new ComponentName(getActivity(), CallsReceiver.class);
                        pm.setComponentEnabledSetting(receiver,
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);
                        Toast.makeText(getActivity(),
                                "Calls Broadcast Receiver Started", Toast.LENGTH_LONG)
                                .show();

                    } else {
                        editor.putBoolean("calls", false);
                        // Switch Off Broadcast Receiver
                        PackageManager pm  = getActivity().getPackageManager();
                        ComponentName receiver = new ComponentName(getActivity(), CallsReceiver.class);
                        pm.setComponentEnabledSetting(receiver,
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                        Toast.makeText(getActivity(),
                                "Calls Broadcast Receiver Stopped", Toast.LENGTH_LONG)
                                .show();
                    }
                    editor.commit();
                }
            });
        }

        private void missedCheckBox() {
            missed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    miss = isChecked;
                }
            });
        }

        private void outgoingCheckBox() {
            outgoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    out = isChecked;
                }
            });
        }

        private void incomingCheckBox() {
            incoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    in = isChecked;

                }
            });

        }

        private void saveCalls() {
            saveCallsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(numberTextCalls.getText().toString());
                    String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
                     /* Query the CallLog Content Provider */
                    Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null,
                            null, null, strOrder);
                    CallsAsyncTask saveCallsAsync = new CallsAsyncTask(getActivity(), in,out, miss, managedCursor);
                    saveCallsAsync.execute(num);
                }
            });
        }

        private void checkIncrease() {
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(numberTextCalls.getText().toString());
                    int number = num + 1;
                    String eNum = String.valueOf(number);
                    numberTextCalls.setText(eNum);
                }
            });
        }

        private void checkDecrease() {
            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(numberTextCalls.getText().toString());
                    int number = num -1;
                    String eNum = String.valueOf(number);
                    numberTextCalls.setText(eNum);
                }
            });
        }

    }
