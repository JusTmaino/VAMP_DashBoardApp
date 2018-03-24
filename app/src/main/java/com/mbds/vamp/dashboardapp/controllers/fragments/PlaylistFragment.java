package com.mbds.vamp.dashboardapp.controllers.fragments;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbds.vamp.dashboardapp.R;
import com.mbds.vamp.dashboardapp.model.Media;
import com.mbds.vamp.dashboardapp.model.Playlist;
import com.mbds.vamp.dashboardapp.model.Profile;
import com.mbds.vamp.dashboardapp.model.User;
import com.mbds.vamp.dashboardapp.services.MediaPlayerService;
import com.mbds.vamp.dashboardapp.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PlaylistFragment extends Fragment {//implements AdapterView.OnItemSelectedListener {

    private String username;
    private String access_token;
    private User loggedUser;
    private ArrayAdapter profileArrayAdapter;
    private List<Profile> userProfiles;
    private List<Media> userMedias;
    private ListView userMediaList;
    private MediaPlayerService player;
    boolean serviceBound = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview, container, false);
        return rootView;
    }



    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
            Toast.makeText(getActivity(), "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Fetching username and token from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userMediaList = (ListView) getView().findViewById(R.id.media_list);
        username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        access_token = sharedPreferences.getString(Config.ACCESS_TOKEN_SHARED_PREF, "Not Available");
        loggedUser = new User();
        userProfiles = new ArrayList<Profile>();
       getProfileDetails();
    }

    private void getProfileDetails() {
        AsyncHttpClient client2 = new AsyncHttpClient();
        client2.addHeader("X-Auth-Token", access_token);
        client2.get(Config.USERS_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray users) {
                super.onSuccess(statusCode, headers, users);
                Log.d("users", users.toString());
                Log.d("statu code : ", String.valueOf(statusCode));

                for (int n = 0; n < users.length(); n++) {
                    try {
                        final JSONObject user = users.getJSONObject(n);
                        if (user != null) {
                            if (user.get("username").toString().equals(username)) {
                                Log.d("loggedUser", user.toString());

                                JSONArray jsonArray = user.getJSONArray("profile");

                                final ArrayList<String> profileIds = new ArrayList<String>();
                                if (jsonArray != null) {
                                    int len = jsonArray.length();
                                    for (int i = 0; i < len; i++) {
                                        profileIds.add(jsonArray.getJSONObject(i).get("id").toString());
                                    }
                                }

                                AsyncHttpClient client3 = new AsyncHttpClient();
                                client3.addHeader("X-Auth-Token", access_token);

                                for (String id : profileIds) {

                                    client3.get(Config.PROFIL_URL + "/" + id + ".json", null, new JsonHttpResponseHandler() {
                                        ArrayList<String> urlMediaList = new ArrayList<>();
                                        ArrayList<HashMap<String,String>> mapList = new ArrayList<>();
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject profile) {
                                            super.onSuccess(statusCode, headers, profile);
                                            Log.d("profiles", profile.toString());
                                            Log.d("statu code : ", String.valueOf(statusCode));
                                            Profile userProfile = new Profile();

                                            Playlist[] playlistsProfile = new Playlist[2];

                                            final ArrayList<String> playlistIds = new ArrayList<String>();
                                            try {
                                                JSONArray jsonArray = profile.getJSONArray("playlists");

                                                if (jsonArray != null) {
                                                    int len = jsonArray.length();
                                                    for (int i = 0; i < len; i++) {
                                                        // playlistsProfile[0] = (Playlist)jsonArray.get(0);
                                                        // playlistsProfile[i] = (Playlist)jsonArray.getJSONObject(i).get()
                                                        userProfile.setPlaylists(playlistsProfile);
                                                        //   userProfile.setSoundVolume(jsonArray.getJSONObject(i).get("id").toString());
                                                        userProfiles.add(userProfile);
                                                        System.out.println("userProfiles==="+jsonArray.getJSONObject(i).get("id").toString());
                                                        playlistIds.add(jsonArray.getJSONObject(i).get("id").toString());
                                                    }
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            AsyncHttpClient client4 = new AsyncHttpClient();
                                            client4.addHeader("X-Auth-Token", access_token);
                                            for (String id : playlistIds) {

                                                client4.get(Config.PLAYLIST_URL + "/" + id + ".json", null, new JsonHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject playlist) {
                                                        super.onSuccess(statusCode, headers, playlist);
                                                        Log.d("Playlists", playlist.toString());
                                                        Log.d("statu code : ", String.valueOf(statusCode));

                                                        final ArrayList<String> mediaIds = new ArrayList<String>();
                                                        try {
                                                            JSONArray jsonArray = playlist.getJSONArray("medias");
                                                            if (jsonArray != null) {
                                                                int len = jsonArray.length();

                                                                for (int i = 0; i < len; i++) {
                                                                    mediaIds.add(jsonArray.getJSONObject(i).get("id").toString());
                                                                }

                                                                AsyncHttpClient client5 = new AsyncHttpClient();
                                                                client5.addHeader("X-Auth-Token", access_token);
                                                                for (String id : mediaIds) {
                                                                    client5.get(Config.MEDIA_URL + "/" + id + ".json", null, new JsonHttpResponseHandler() {
                                                                        @Override
                                                                        public void onSuccess(int statusCode, Header[] headers, JSONObject media) {
                                                                            super.onSuccess(statusCode, headers, media);
                                                                            Log.d("Medias", media.toString());
                                                                            Log.d("statu code : ", String.valueOf(statusCode));
                                                                            final ArrayList<String> mediaIds = new ArrayList<String>();
                                                                            try {
                                                                                //  JSONArray jsonArray = media.getJSONArray("url");
                                                                                String urlMedia = media.getString("url");

                                                                                urlMediaList.add(urlMedia);
                                                                                for (int i=0 ; i < profileIds.size() ; i++)
                                                                                {
                                                                                    HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
                                                                                    hashMap.put("music_title",urlMedia);
                                                                                    hashMap.put("music_author","1");
                                                                                    mapList.add(hashMap);//add the hashmap into arrayList
                                                                                }
                                                                                ListAdapter listAdapter = new SimpleAdapter(
                                                                                        getActivity().getApplicationContext(),mapList,
                                                                                        R.layout.fragment_playlist,
                                                                                        new String[]{"music_title","music_author"},
                                                                                        new int[]{R.id.music_title,R.id.music_author});
                                                                                userMediaList.setAdapter(listAdapter);
                                                                                userMediaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                                    @Override
                                                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                                        playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
                                                                                        //playAudio("http://192.168.43.247:80/images/media/"+urlMedia);
                                                                                    }
                                                                                });

                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                        @Override
                                                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                                            super.onFailure(statusCode, headers, responseString, throwable);
                                                                            Log.d("onFailure : ", String.valueOf(statusCode));
                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                        super.onFailure(statusCode, headers, responseString, throwable);
                                                        Log.d("onFailure : ", String.valueOf(statusCode));
                                                    }
                                                });
                                            }


                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                            super.onFailure(statusCode, headers, responseString, throwable);
                                            Log.d("onFailure : ", String.valueOf(statusCode));
                                        }
                                    });
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("onFailure : ", String.valueOf(statusCode));
            }

        });
    }
    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(getActivity(), MediaPlayerService.class);
            playerIntent.putExtra("media", media);

            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

}