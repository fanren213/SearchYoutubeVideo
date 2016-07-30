/*
 * The model of YoutubeVideo. It stores the information of a Youtube video.
 * Author     : Bonan Cao
 */
package bonanc.cmu.edu.searchyoutubevideo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class YoutubeVideo {
    private String videoTitle; // the title of video
    private String videoDesc; // the description of video
    private Bitmap thumbnail; // the thumbnail bitmap of video
    private String publishTime; // the publish time of video
    private String videoUrl; // the url of video
    // the constructor of YoutubeVideo
    public YoutubeVideo() {
        this.videoTitle = null;
        this.videoDesc = null;
        this.thumbnail = null;
        this.publishTime = null;
        this.videoUrl = null;
    }
    // the constructor of YoutubeVideo with values of parameters
    public YoutubeVideo(String videoTitle, String videoDesc, Bitmap thumbnail, String publishTime, String videoUrl) {
        this.videoTitle = videoTitle;
        this.videoDesc = videoDesc;
        this.thumbnail = thumbnail;
        this.publishTime = publishTime;
        this.videoUrl = videoUrl;
    }
    // the constructor of YoutubeVideo with a jsonObject
    public YoutubeVideo(JSONObject jsonObject) {
        try {
            this.videoTitle = jsonObject.getString("videoTitle");
            this.videoDesc = jsonObject.getString("videoDesc");

            // reads the bitmap from the given url of thumbnail
            String thumbnailUrl = jsonObject.getString("thumbnailUrl");
            this.thumbnail = getRemoteImage(new URL(thumbnailUrl));
            this.publishTime = jsonObject.getString("publishTime");
            this.videoUrl = jsonObject.getString("videoUrl");
        } catch (JSONException | IOException e) {
            this.videoTitle = null;
            this.videoDesc = null;
            this.thumbnail = null;
            this.publishTime = null;
            this.videoUrl = null;
            e.printStackTrace();
        }
    }
    // the getters and setters of parameters
    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /*
     * Given a URL referring to an image, return a bitmap of that image
     */
    private Bitmap getRemoteImage(final URL url) {
        try {
            final URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            return bm;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
