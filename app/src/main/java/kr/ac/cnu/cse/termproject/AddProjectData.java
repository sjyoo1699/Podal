package kr.ac.cnu.cse.termproject;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Usung on 2017-12-08.
 */

public class AddProjectData implements Serializable{
    String projectName,host;
    String startDate, finishDate;
    String poster;

    public AddProjectData(String ProjectName, String host, String startDate, String finishDate, String poster){
        this.projectName = ProjectName;
        this.host = host;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.poster = poster;
    }

/*

    @SuppressWarnings("unchecked")
    public static final Creator CREATOR = new Creator() {
        @Override
        public AddProjectData createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public AddProjectData[] newArray(int i) {
            return new AddProjectData[i];
        }
    };

    public int describeContents(){return 0;}

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(projectName);
        dest.writeString(host);
        dest.writeString(startDate);
        dest.writeString(finishDate);
        dest.writeValue(poster);
    }
*/

    public String getProjectName(){return projectName;}
    public void setProjectName(String projectName){this.projectName = projectName;}
    public String getHost(){return host;}
    public void setHost(String host){this.host = host;}
    public String getStartDate(){return startDate;}
    public void setStartDate(String startDate){this.startDate = startDate;}
    public String getFinishDate(){return finishDate;}
    public void setFinishDate(String finishDate){this.finishDate = finishDate;}
    public String getPoster(){return poster;}
    public void setPoster(String poster){this.poster = poster;}
}
