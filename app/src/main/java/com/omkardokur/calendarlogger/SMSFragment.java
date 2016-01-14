package com.omkardokur.calendarlogger;


import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
public class SMSFragment extends Fragment {

    EditText numberText;
    Button minusBtn, plusBtn, saveBtn;
    boolean received = true;
    boolean sent = true;
    CheckBox receivedSms, sentSms, enableCb;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public SMSFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
        numberText = (EditText)rootView.findViewById(R.id.numberEt);
        numberText.setGravity(Gravity.CENTER);
        numberText.setText("15");
        minusBtn = (Button) rootView.findViewById(R.id.decreaseBtn);
        plusBtn = (Button) rootView.findViewById(R.id.increaseBtn);
        receivedSms = (CheckBox) rootView.findViewById(R.id.receivedSmsCb);
        sentSms = (CheckBox) rootView.findViewById(R.id.sentSmsCb);
        enableCb = (CheckBox) rootView.findViewById(R.id.enableCb);
        saveBtn = (Button) rootView.findViewById(R.id.btnSave);

        sharedpreferences = this.getActivity().getPreferences(this.getActivity().MODE_PRIVATE);
        boolean autoCheckBox =  sharedpreferences.getBoolean("value",false);
        enableCb.setChecked(autoCheckBox);

        autoSave();
        checkDecrease();
        checkIncrease();
        receivedCheckBox();
        sentCheckBox();
        saveSms();

        return rootView;
    }



    private void sentCheckBox() {
        sentSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sent = isChecked;
            }
        });
    }

    private void receivedCheckBox() {

        receivedSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                received = isChecked;
            }
        });
    }

    private void saveSms() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numberText.getText().toString());

                SmsAsyncTask saveSmsAsync = new SmsAsyncTask(getActivity(), received, sent);
                saveSmsAsync.execute(num);
            }
        });
    }

    private void checkIncrease() {
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numberText.getText().toString());
                int number = num + 1;
                String eNum = String.valueOf(number);
                numberText.setText(eNum);
            }
        });
    }

    private void checkDecrease() {
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numberText.getText().toString());
                int number = num -1;
                String eNum = String.valueOf(number);
                numberText.setText(eNum);
            }
        });
    }

    private void autoSave() {

        enableCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sharedpreferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
                editor = sharedpreferences.edit();
                if (enableCb.isChecked()) {
                    editor.putBoolean("value", true);
                    // Switch On Broadcast Receiver
                    PackageManager pm = getActivity().getPackageManager();
                    ComponentName receiver = new ComponentName(getActivity(), SmsReceiver.class);
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getActivity(),
                            "Sms Broadcast Receiver Started", Toast.LENGTH_LONG)
                            .show();
                } else {
                    editor.putBoolean("value", false);
                    // Switch Off Broadcast Receiver
                    PackageManager pm  = getActivity().getPackageManager();
                    ComponentName receiver = new ComponentName(getActivity(), SmsReceiver.class);
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getActivity(),
                            "Sms Broadcast Receiver Stopped", Toast.LENGTH_LONG)
                            .show();

                }
                editor.commit();
            }
        });

    }


}
