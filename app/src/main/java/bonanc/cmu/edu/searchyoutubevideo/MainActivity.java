/*
 * The main activity of this app. This app will take an input word from user and give the first
 * video from youtube result. It gives the title, description, published time and thumbnail. The
 * title is a hyperlink that user can click the title to watch the video in browser.
 * Created on : Mar 30 2016
 * Author     : Bonan Cao
 */
package bonanc.cmu.edu.searchyoutubevideo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    private MainActivity ma = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity ma = this;
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                search(searchTerm, ma);
            }
        });
    }
    /*
     * calls the asynctask and get the result
     */
    private void search(String searchTerm, MainActivity ma) {
        this.ma = ma;
        new GetJsonFromService().execute(searchTerm);
    }

    private class GetJsonFromService extends AsyncTask<String, Void, YoutubeVideo> {
        @Override
        protected YoutubeVideo doInBackground(String... urls) {
            return search(urls[0]);
        }
        /*
        * calls the videoReady method to display the result
        */
        @Override
        protected void onPostExecute(YoutubeVideo youtubeVideo) {
            ma.videoReady(youtubeVideo);
        }
        /*
        * sends the key word to the web service, gets the json string as result and builds a
        * jsonObject. Then builds a YoutubeVideo defined in the other file with the jsonObject and
        * get the YoutubeVideo as output.
        */
        private YoutubeVideo search(String searchTerm) {
            String urlString = "https://fast-peak-64814.herokuapp.com/?searchWord=" + searchTerm;
            YoutubeVideo output = new YoutubeVideo();
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                StringBuffer sb = new StringBuffer();
                String nextLine;
                while((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
                JSONObject jsonObject = new JSONObject(sb.toString());
                // If the jsonObject is null, the service is not available or video is not available
                if (jsonObject == null) {
                // Otherwise we get the jsonObject and build a YoutubeVideo object as output
                } else {
                    output = new YoutubeVideo(jsonObject);
                }
            } catch (Exception e) {

            } finally {
                return output;
            }
        }
    }
    /*
     * sets the display with the given output youtubeVideo
     */
    public void videoReady(YoutubeVideo youtubeVideo) {
        ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
        TextView feedback = (TextView) findViewById(R.id.feedback);
        TextView searchView = (TextView) findViewById(R.id.searchTerm);
        TextView titleView = (TextView) findViewById(R.id.videoTitle);
        TextView descView = (TextView) findViewById(R.id.videoDesc);
        TextView timeView = (TextView) findViewById(R.id.publishTime);
        TextView titleDView = (TextView) findViewById(R.id.videoTitleD);
        TextView descDView = (TextView) findViewById(R.id.videoDescD);
        TextView timeDView = (TextView) findViewById(R.id.publishTimeD);
        String videoTitle = youtubeVideo.getVideoTitle();
        if (videoTitle == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
            imageView.setVisibility(View.INVISIBLE);
            titleDView.setVisibility(View.INVISIBLE);
            descDView.setVisibility(View.INVISIBLE);
            timeDView.setVisibility(View.INVISIBLE);
            titleView.setVisibility(View.INVISIBLE);
            descView.setVisibility(View.INVISIBLE);
            timeView.setVisibility(View.INVISIBLE);
            feedback.setText("Video for " + searchView.getText() +" is not available.");
        } else {
            imageView.setImageBitmap(youtubeVideo.getThumbnail());
            imageView.setVisibility(View.VISIBLE);
            titleDView.setVisibility(View.VISIBLE);
            descDView.setVisibility(View.VISIBLE);
            timeDView.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
            descView.setVisibility(View.VISIBLE);
            timeView.setVisibility(View.VISIBLE);
            feedback.setText("The first result for " + searchView.getText() + " on Youtube(click title for video):");
            descView.setText(youtubeVideo.getVideoDesc());
            timeView.setText(youtubeVideo.getPublishTime());
            SpannableString title = new SpannableString(youtubeVideo.getVideoTitle());
            String url = youtubeVideo.getVideoUrl();
            title.setSpan(new URLSpan(url), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleView.setText(title);
            titleView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
