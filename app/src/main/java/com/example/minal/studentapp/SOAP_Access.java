package com.example.minal.studentapp;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SOAP_Access {

    private static String SOAP_ACTION = "http://tempuri.org/GetData";
    private static String METHOD_NAME = "GetData";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String URL = "http://chws.eng.cu.edu.eg/webservice1.asmx";

    String TAG = "Response";

    public static SoapObject Request=null;

    private SOAP_Access()
    {
    }

    static SOAP_Access _instance = null;

    static SOAP_Access _getInstance()
    {
        if( _instance == null)   _instance=new SOAP_Access();

        Request = new SoapObject(NAMESPACE, METHOD_NAME);
        return _instance;
    }


    public SoapPrimitive getResponse(String InvokeMessage) {


        try {

            this.Request.addProperty("Params_CommaSeparated", InvokeMessage);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            try {
                transport.call(SOAP_ACTION, soapEnvelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            SoapPrimitive resultString = null;
            try {
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                Log.i(TAG, "Grades: " + resultString);

            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();

            }


            return resultString;
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return null;
    }
}
