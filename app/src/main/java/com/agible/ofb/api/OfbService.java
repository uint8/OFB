package com.agible.ofb.api;

import android.util.Pair;

import com.agible.ofb.data.Values;
import com.agible.ofb.listeners.OnFinishListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by seth on 12/9/15.
 */
public class OfbService {
    //@GET("/users/{user}/repos")
    //Call<List<Values.>> listRepos(@Path("user") String user);
    final static String userApi = "user";
    final static String eventApi = "event";
    final static String eventsApi = "events";
    final static String churchApi = "church";
    final static String churchesApi = "churches";
    final static String searchApi = "search";
    final static String actionsApi = "userevents";
    final static String messagesApi = "message";
    final static String codeApi = "code";
    final static String post = "POST";
    final static String get = "GET";
    final static String put = "PUT";
    final static String delete = "DELETE";
    final static String join = "JOIN";
    final static String lead = "LEAD";
    final static String leave = "LEAVE";
    final static String start = "START";
    final static String end = "END";
    final static String cancel = "CANCEL";

    MobileServiceClient client;
    public OfbService(){

    }

    //post user
    ListenableFuture<Values.Users> addUser(Values.Users user){
        user.id = "";
        return client.invokeApi(userApi, user, post, null, Values.Users.class);
    }
    //put user
    ListenableFuture<Values.Users> updateUser(Values.Users user){
        return client.invokeApi(userApi, user, put, null, Values.Users.class);
    }
    //get user
    ListenableFuture<Values.Users> getUser(String userId){
        Pair<String, String> param = new Pair<>("UserId",  userId);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(userApi, get, params, Values.Users.class);
    }
    //delete user//?
    ListenableFuture<Values.Message> deleteUser(String userId){
        Pair<String, String> param = new Pair<>("UserId",  userId);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(userApi, delete, params, Values.Message.class);
    }
    //post event
    ListenableFuture<Values.Events> addEvent(Values.Events event){
        event.id = "";
        return client.invokeApi(eventApi, event, post, null, Values.Events.class);
    }
    //put event
    ListenableFuture<Values.Events> updateEvent(Values.Events event){
        return client.invokeApi(eventApi, event, put, null, Values.Events.class);
    }
    //get event//?
    ListenableFuture<Values.Events> getEvent(String eventId){
        Pair<String, String> param = new Pair<>("EventId",  eventId);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(eventApi, get, params, Values.Events.class);
    }
    //get events
    ListenableFuture<Values.Events> getEvents(String churchId){
        Pair<String, String> param = new Pair<>("ChurchId", churchId);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(eventsApi, null, get, params, Values.Events.class);

    }
    //delete event//?
    ListenableFuture<Values.Message> deleteEvent(String eventId){
        Pair<String, String> param = new Pair<>("EventId",  eventId);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(eventApi, delete, params, Values.Message.class);
    }
    //post church
    ListenableFuture<Values.Churches> addChurch(Values.Churches church){
        return client.invokeApi(churchApi, church, post, null, Values.Churches.class);
    }
    //put church
    ListenableFuture<Values.Churches> updateChurch(Values.Churches church){
        return client.invokeApi(churchApi, church, post, null, Values.Churches.class);
    }
    //get church//?f
    ListenableFuture<Values.Churches> getChurch(String churchId){
        Pair<String, String> param = new Pair<>("ChurchId", churchId);
        List<Pair<String, String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(churchApi, get, params, Values.Churches.class);
    }
    //get churches
    ListenableFuture<Values.Churches> getChurches(int page){
        Pair<String, String> param = new Pair<>("Page", String.valueOf(page));
        List<Pair<String, String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(churchApi, get, params, Values.Churches.class);
    }
    ListenableFuture<Values.Churches> searchChurches(String query){
        Pair<String, String> param = new Pair<>("Query", query);
        List<Pair<String, String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(churchApi, get, params, Values.Churches.class);
    }
    //delete church//?
    ListenableFuture<Values.Message> deleteChurch(String churchId){
        Pair<String, String> param = new Pair<>("ChurchId", churchId);
        List<Pair<String, String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(churchApi, get, searchApi, params, Values.Message.class);
    }
    //get join_event
    ListenableFuture<Values.Message> joinEvent(String eventId, String userId){
        Pair<String, String> param = new Pair<>("EventId", eventId);
        Pair<String, String> param1 = new Pair<>("UserId", userId);
        Pair<String, String> param2 = new Pair<>("Action", join);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        params.add(param1);
        return client.invokeApi(actionsApi, post, params, Values.Message.class);
    }
    //get lead_event
    ListenableFuture<Values.Message> leadEvent(String eventId, String userId){
        Pair<String, String> param = new Pair<>("EventId", eventId);
        Pair<String, String> param1 = new Pair<>("UserId", userId);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        params.add(param1);
        return client.invokeApi(actionsApi, put, params, Values.Message.class);
    }
    //post message
    ListenableFuture<Values.Messages> addMessage(Values.Messages message){
        message.id = "";
        return client.invokeApi(messagesApi, message, post, null, Values.Messages.class);
    }
    //get messages
    ListenableFuture<Values.Messages> getMessages(String eventId){
        Pair<String, String> param = new Pair<>("EventId",  eventId);
        List<Pair<String,String>>params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(messagesApi, get, params, Values.Messages.class);
    }
    //check user

    //send code
    ListenableFuture<Values.Message> sendCode(String userId){
        Pair<String, String> param = new Pair<>("UserId", userId);
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(codeApi, get, params, Values.Message.class);
    }

    //clear code
    ListenableFuture<Values.Message> clearCode(String userId){
        Pair<String, String> param = new Pair<>("UserId", userId);
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(param);
        return client.invokeApi(codeApi, delete, params, Values.Message.class);
    }



}