package kr.ac.cnu.cse.termproject;

/**
 * Created by Administrator on 2017-12-09.
 */

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class UserInfoData implements Serializable{
    String name;
    String birth;
    String university;
    String major;
    String phone;
    String address_email;
    String address_blog;
    String uri;

    public UserInfoData(String name, String birth, String university, String major, String phone, String address_email, String address_blog, String uri) {
        this.name = name;
        this.birth = birth;
        this.university = university;
        this.major = major;
        this.phone = phone;
        this.address_email = address_email;
        this.address_blog = address_blog;
        this.uri = uri;
    }

    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
    public String getBirth() { return birth; }
    public void setBirth(String newBirth) { birth = newBirth; }
    public String getUniversity() { return university; }
    public void setUniversity(String newUniversity) { university = newUniversity; }
    public String getMajor() { return major; }
    public void setMajor(String newMajor) { major = newMajor; }
    public String getPhone() { return phone; }
    public void setPhone(String newPhone) { phone = newPhone; }
    public String getAddress_email() { return address_email; }
    public void setAddress_email(String newAddress_email) { address_email =  newAddress_email;}
    public String getAddress_blog() { return address_blog; }
    public void setAddress_blog(String newAddress_blog) { address_blog = newAddress_blog; }
    public String getUri() { return uri; }
    public void setUri(String newUri) { uri = newUri; }










}