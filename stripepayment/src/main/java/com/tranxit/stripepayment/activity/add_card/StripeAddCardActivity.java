package com.tranxit.stripepayment.activity.add_card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.tranxit.stripepayment.R;

public class StripeAddCardActivity extends AppCompatActivity {

    private static final String TAG = "StripePaymentActivity";
    CardForm cardForm;
    Button submit;

    private String stripe_token;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Stripe Payment");
        setContentView(R.layout.activity_add_card);
        cardForm = (CardForm) findViewById(R.id.card_form);
        submit = (Button) findViewById(R.id.submit);
        stripe_token = getIntent().getStringExtra("stripe_token");

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel("Add card Details")
                .setup(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

    }


    public void onSubmit() {

        if (cardForm.getCardNumber().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (cardForm.getExpirationMonth().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_expiration_details), Toast.LENGTH_SHORT).show();
            return;
        }
        if (cardForm.getCvv().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_cvv), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(cardForm.getExpirationMonth()) || !TextUtils.isDigitsOnly(cardForm.getExpirationYear())) {
            Toast.makeText(this, getString(R.string.please_enter_card_expiration_details), Toast.LENGTH_SHORT).show();
            return;
        }

        String cardNumber = cardForm.getCardNumber();
        Integer cardMonth = Integer.parseInt(cardForm.getExpirationMonth());
        Integer cardYear = Integer.parseInt(cardForm.getExpirationYear());
        String cardCvv = cardForm.getCvv();
        Log.d("CARD", "CardDetails Number: " + cardNumber + "Month: " + cardMonth + " Year: " + cardYear + " Cvv " + cardCvv);
        Card card = new Card(cardNumber, cardMonth, cardYear, cardCvv);
        addCard(card);

    }

    private void addCard(Card card) {
        Stripe stripe = new Stripe(this, stripe_token);
        stripe.createToken(card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        Log.d("CARD:", " " + token.getId());
                        Log.d("CARD:", " " + token.getCard().getLast4());
                        String stripeToken = token.getId();

                        Intent intent = new Intent();
                        intent.putExtra("stripetoken", stripeToken);
                        setResult(100, intent);
                        finish();
                    }

                    public void onError(Exception error) {
                        try {
                            Log.d(TAG, "onError: ");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


}
