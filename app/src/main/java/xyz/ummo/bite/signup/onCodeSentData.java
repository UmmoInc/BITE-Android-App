package xyz.ummo.bite.signup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.PhoneAuthProvider;

public class onCodeSentData implements Parcelable {

   private  String verificationId;
   private PhoneAuthProvider.ForceResendingToken token;

    protected onCodeSentData(Parcel in) {
        verificationId = in.readString();
        token = in.readParcelable(PhoneAuthProvider.ForceResendingToken.class.getClassLoader());
    }

    public static final Creator<onCodeSentData> CREATOR = new Creator<onCodeSentData>() {
        @Override
        public onCodeSentData createFromParcel(Parcel in) {
            return new onCodeSentData(in);
        }

        @Override
        public onCodeSentData[] newArray(int size) {
            return new onCodeSentData[size];
        }
    };

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public void setToken(PhoneAuthProvider.ForceResendingToken token) {
        this.token = token;
    }


    public PhoneAuthProvider.ForceResendingToken getToken() {
        return token;
    }

    public String getVerificationId() {
        return verificationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(verificationId);
        dest.writeParcelable(token, flags);
    }
}

