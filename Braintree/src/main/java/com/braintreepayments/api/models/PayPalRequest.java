package com.braintreepayments.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import com.braintreepayments.api.BraintreeFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents the parameters that are needed to start a Checkout with PayPal
 *
 * In the checkout flow, the user is presented with details about the order and only agrees to a
 * single payment. The result is not eligible for being saved in the Vault; however, you will receive
 * shipping information and the user will not be able to revoke the consent.
 *
 * @see <a href="https://developer.paypal.com/docs/api/#inputfields-object">PayPal REST API Reference</a>
 */
public class PayPalRequest implements Parcelable {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({PayPalRequest.INTENT_SALE, PayPalRequest.INTENT_AUTHORIZE})
    @interface PayPalPaymentIntent {}
    public static final String INTENT_SALE = "sale";
    public static final String INTENT_AUTHORIZE = "authorize";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({PayPalRequest.USER_ACTION_DEFAULT, PayPalRequest.USER_ACTION_COMMIT})
    @interface PayPalPaymentUserAction {}
    public static final String USER_ACTION_DEFAULT = "";
    public static final String USER_ACTION_COMMIT = "commit";

    private String mAmount;
    private String mCurrencyCode;
    private String mLocaleCode;
    private String mBillingAgreementDescription;
    private boolean mShippingAddressRequired;
    private PostalAddress mShippingAddressOverride;
    private String mIntent = INTENT_AUTHORIZE;
    private String mUserAction = USER_ACTION_DEFAULT;

    /**
     * Constructs a description of a PayPal checkout for Single Payment and Billing Agreements.
     *
     * @note This amount may differ slight from the transaction amount. The exact decline rules
     *        for mismatches between this client-side amount and the final amount in the Transaction
     *        are determined by the gateway.
     *
     * @param amount The transaction amount in currency units (as
     * determined by setCurrencyCode). For example, "1.20" corresponds to one dollar and twenty cents.
     * Amount must be a non-negative number, may optionally contain exactly 2 decimal places separated
     * by '.', optional thousands separator ',', limited to 7 digits before the decimal point.
     */
    public PayPalRequest(String amount) {
        mAmount = amount;
        mShippingAddressRequired = false;
    }

    /**
     * Constructs a {@link PayPalRequest} with a null amount.
     */
    public PayPalRequest() {
        mAmount = null;
        mShippingAddressRequired = false;
    }

    /**
     * Optional: A valid ISO currency code to use for the transaction. Defaults to merchant currency
     * code if not set.
     *
     * If unspecified, the currency code will be chosen based on the active merchant account in the
     * client token.
     *
     * @param currencyCode A currency code, such as "USD"
     */
    public PayPalRequest currencyCode(String currencyCode) {
        mCurrencyCode = currencyCode;
        return this;
    }

    /**
     * Defaults to false. When set to true, the shipping address selector will be displayed.
     *
     * @param shippingAddressRequired Whether to hide the shipping address in the flow.
     */
    public PayPalRequest shippingAddressRequired(boolean shippingAddressRequired) {
        mShippingAddressRequired = shippingAddressRequired;
        return this;
    }

    /**
     * Whether to use a custom locale code.
     *
     * @param localeCode Whether to use a custom locale code.
     */
    public PayPalRequest localeCode(String localeCode) {
        mLocaleCode = localeCode;
        return this;
    }

    /**
     * Display a custom description to the user for a billing agreement.
     *
     * @param description The description to display.
     */
    public PayPalRequest billingAgreementDescription(String description) {
        mBillingAgreementDescription = description;
        return this;
    }

    /**
     * A custom shipping address to be used for the checkout flow.
     *
     * @param shippingAddressOverride a custom {@link PostalAddress}
     */
    public PayPalRequest shippingAddressOverride(PostalAddress shippingAddressOverride) {
        mShippingAddressOverride = shippingAddressOverride;
        return this;
    }

    /**
     * Payment intent. Only applies when calling
     * {@link com.braintreepayments.api.PayPal#requestOneTimePayment(BraintreeFragment, PayPalRequest)}.
     *
     * @param intent Can be either {@link PayPalRequest#INTENT_AUTHORIZE} or {@link PayPalRequest#INTENT_SALE}.
     */
    public PayPalRequest intent(@PayPalPaymentIntent String intent) {
        mIntent = intent;
        return this;
    }

    /**
     * The call-to-action in the PayPal one-time payment checkout flow.
     * By default the final button will show the localized phrase for "Continue",
     * implying that the final amount billed is not yet known.
     *
     * Setting the BTPayPalRequest's userAction to {@link PayPalRequest#USER_ACTION_COMMIT} changes the button text to "Pay Now",
     * conveying to the user that billing will take place immediately.
     *
     * @param userAction Can be either {@link PayPalRequest#USER_ACTION_COMMIT} or {@link PayPalRequest#USER_ACTION_DEFAULT}.
     */
    public PayPalRequest userAction(@PayPalPaymentUserAction String userAction) {
        mUserAction = userAction;
        return this;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    public String getLocaleCode() {
        return mLocaleCode;
    }

    public String getBillingAgreementDescription() {
        return mBillingAgreementDescription;
    }

    public boolean isShippingAddressRequired() {
        return mShippingAddressRequired;
    }

    public PostalAddress getShippingAddressOverride() {
        return mShippingAddressOverride;
    }

    @PayPalPaymentIntent
    public String getIntent() {
        return mIntent;
    }

    @PayPalPaymentUserAction
    public String getUserAction() {
        return mUserAction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAmount);
        parcel.writeString(mCurrencyCode);
        parcel.writeString(mLocaleCode);
        parcel.writeString(mBillingAgreementDescription);
        parcel.writeByte(mShippingAddressRequired ? (byte) 1:0);
        parcel.writeParcelable(mShippingAddressOverride, i);
        parcel.writeString(mIntent);
        parcel.writeString(mUserAction);
    }

    public PayPalRequest(Parcel in) {
        mAmount = in.readString();
        mCurrencyCode = in.readString();
        mLocaleCode = in.readString();
        mBillingAgreementDescription = in.readString();
        mShippingAddressRequired = in.readByte() > 0;
        mShippingAddressOverride = in.readParcelable(PostalAddress.class.getClassLoader());
        mIntent = in.readString();
        mUserAction = in.readString();
    }

    public static final Creator<PayPalRequest> CREATOR = new Creator<PayPalRequest>() {
        @Override
        public PayPalRequest createFromParcel(Parcel in) {
            return new PayPalRequest(in);
        }

        @Override
        public PayPalRequest[] newArray(int size) {
            return new PayPalRequest[size];
        }
    };
}
