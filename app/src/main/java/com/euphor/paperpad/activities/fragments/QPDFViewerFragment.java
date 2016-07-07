package com.euphor.paperpad.activities.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*import com.qoppa.android.pdf.PDFException;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.viewer.QPDFViewerView;*/

/**
 * Created by euphordev02 on 18/09/2014.
 */
public class QPDFViewerFragment extends Fragment {

    public static QPDFViewerFragment create(String pdfURL){
        QPDFViewerFragment fragment = new QPDFViewerFragment();
        Bundle extra = new Bundle();
        extra.putString("pdfURL", pdfURL);
        fragment.setArguments(extra);
        return  fragment;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*        QPDFViewerView viewerView = new QPDFViewerView(inflater.getContext());
        PDFDocument pdfDocument = null;
        try {
             pdfDocument = new PDFDocument(getArguments().getString("pdfURL", "http://backoffice.paperpad.fr/pdf/52/carte2014_1609.pdf"), null);
        } catch (PDFException e) {
            e.printStackTrace();
        }
        //viewerView.loadDocument(getArguments().getString("pdfURL", "http://backoffice.paperpad.fr/pdf/52/carte2014_1609.pdf"));
        viewerView.setDocument(pdfDocument);*/
        View viewerView = super.onCreateView(inflater, container, savedInstanceState);
        return viewerView;
    }
}
