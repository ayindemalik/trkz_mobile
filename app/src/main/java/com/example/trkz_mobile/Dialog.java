package com.example.trkz_mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Dialog {
    public LayoutInflater inflater;
    public View customDialogView;
    public TextView operationTitle, messageContent;
    public AlertDialog.Builder dialogBuilder;
    public AlertDialog dialog;

    public Button noButton, yesButton;

    public Dialog(LayoutInflater inflater, AlertDialog.Builder dialogBuilder) {
        this.inflater = inflater;
        this.customDialogView = this.inflater.inflate(R.layout.dialog_layout, null, false);;
        this.operationTitle = customDialogView.findViewById(R.id.operationTitle);
        this.messageContent = customDialogView.findViewById(R.id.dialogContent);
        this.dialogBuilder = dialogBuilder;
    }

    public void operationDialog(String opTitle, String message,
                                Boolean noBtn, String noBtnText,
                                Boolean yesBtn, String yesBtnText){
        this.dialogBuilder.setView(this.customDialogView);
        this.dialog = dialogBuilder.create();
        this.dialog.show();
    }
}
