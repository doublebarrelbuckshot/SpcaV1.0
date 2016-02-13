package com.example.jedgar.spca;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.ShippingAddress;

import java.math.BigDecimal;



/**
 * Created by JEdgar on 4/19/2015.
 */
public class DonateFragment extends Fragment implements View.OnClickListener, GetActionBarTitle {


//    TextView tv_donation_description;
//    TextView tv_donation_fineprint;
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;//ENVIRONMENT_SANDBOX;// PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AYoNGOVMZGUeur10Oj_4S5eLHq8R24lqvdbicZj6B60lwQhvCZB5pUSeQFzIidXZLN_gUnbVIcQq_G_p";//"AbfSckYTLi5pS-KXLvRbejFbUHO0oL4EIZ4_wyF8gDwBmDw3wiMTdIoAi15rDPqzV-zvL_ojAMx8yCDs";//"AZwfgYU9KJ6fLvOX_5TvjSjHu068ZD3Zr_04z-eeanSZrsKVHe_u1JYDQlpgpYwKco5mhdBFmIBb5uG-";//"ATwtGvOKmfr3XHZWIn5QS7SqGxZGmQoGTIwpC2C8wzHlNXXoBr-EJ0nP217ChEf4sKHVgiYWspXvKQF5";//"AR0bLsI4ZS36IYi3TJtXdu8rEtMzJJvwJewPUx0HghR5Ixx_Sc7XRDIdVMm2N_0-mN9GOHLkkw6BzNKG";//"credential from developer.paypal.com";


    private static PayPalConfiguration config = new PayPalConfiguration()
            .acceptCreditCards(false)
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
                    // The following are only used in PayPalFuturePaymentActivity.
            /*.merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));*/
    Button btn_give;
    EditText et_donation_amount;

    public static DonateFragment newInstance(){
        DonateFragment fragment = new DonateFragment();
        return fragment;
    }

    public DonateFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_donate, container, false);
        btn_give = (Button) rootView.findViewById(R.id.btn_give);
        btn_give.setOnClickListener(this);
        et_donation_amount = (EditText) rootView.findViewById(R.id.donation_amount);
//        tv_donation_description = (TextView) rootView.findViewById(R.id.text_donation_description);
//        Spanned description = Html.fromHtml(getString(R.string.donation_description));
//        tv_donation_description.setText(description.toString().trim());

//        tv_donation_fineprint  = (TextView) rootView.findViewById(R.id.text_donation_fineprint);
//        tv_donation_fineprint.setText(Html.fromHtml(getString(R.string.donation_fineprint)));
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity)activity).onSectionAttached(9);
    }

    @Override
    public void onClick(View v) {
        String donation_amount = et_donation_amount.getText().toString();
        if(donation_amount.isEmpty() || donation_amount == null)
        {
            Toast.makeText(getActivity(),"You must enter a donation amount greater than $0", Toast.LENGTH_LONG).show();
        }
        else {
            PayPalPayment ppp = new PayPalPayment(new BigDecimal(donation_amount), "CAD", "SPCA Donation",
                    PayPalPayment.PAYMENT_INTENT_SALE);
            enableShippingAddressRetrieval(ppp, true);
            ((MainActivity) DonateFragment.this.getActivity()).doDonation(ppp);
        }
    }

    /*
     * Enable retrieval of shipping addresses from buyer's PayPal account
     */
    private void enableShippingAddressRetrieval(PayPalPayment paypalPayment, boolean enable) {
        paypalPayment.enablePayPalShippingAddressesRetrieval(enable);
    }

    @Override
    public int getActionBarTitleId() {
        return R.string.donateTitle;
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

}
