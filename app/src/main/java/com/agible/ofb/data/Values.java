


        package com.agible.ofb.data;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.content.res.Resources;
        import android.graphics.Color;
        import android.util.Base64;
        import android.util.Log;
        import android.util.Pair;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.agible.ofb.listeners.OnFinishListener;
        import com.google.common.util.concurrent.FutureCallback;
        import com.google.common.util.concurrent.Futures;
        import com.google.common.util.concurrent.ListenableFuture;
        import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
        import com.microsoft.windowsazure.mobileservices.MobileServiceList;
        import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
        import com.microsoft.windowsazure.mobileservices.table.query.ExecutableQuery;
        import com.microsoft.windowsazure.mobileservices.table.query.Query;

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.io.Serializable;
        import java.net.MalformedURLException;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

        /**
 * Created by seth on 6/16/15.
 */
public class Values implements Serializable {


            //static values and ID's
            public static final String SENDER_ID = "205416429007";
            public static String[] TAGS = new String[]{"Android"};
            public static final String MOBILE_SERVICE_URL = "https://ofbapi.azure-mobile.net/";
            public static final String APPLICATION_KEY = "xqOyuPFrqbrXDqUsymVTYGKQDiicoK60";
            public static final String SPP_NAME = "OFB_PREFS";
            public static final String AUTH_TOKEN = "AUTH_TOKEN";
            public static final String USER_ID = "USER_ID";
            public static final String CHURCH_ID = "CHURCH_ID";
            public static final int HISTORY_LENGTH = 2678401;
            public static Double latitude;
            public static Double longitude;
            public static final boolean MALE   = true;
            public static final boolean FEMALE = false;

            public static final String USER_CLASS = "Users";
            public static final String ADDRESS_CLASS = "Addresses";

            //account status codes.
            public static final int STATUS_PENDING = 1;
            public static final int STATUS_ACTIVE = 2;
            public static final int STATUS_COMPLETED = 3;
            public static final int STATUS_APROVED = 200;
            public static final int STATUS_BLOCKED = 303;


            ////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////API/////////////////////////////////////////////////////////////////////////////////////////
            final static String userApi = "user";
            final static String eventApi = "event";
            final static String churchApi = "church";
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
            ////////////////////////////////////////////////////////////////////////////////////////////////////

            //category status codes.
            public static final int CATEGORY_DEFAULT = 101;


            public Context context;
            public MobileServiceClient mClient;
            public SharedPreferences preferences;

            public Values(Context context) {
                try {
                    this.mClient = new MobileServiceClient(MOBILE_SERVICE_URL, APPLICATION_KEY, context);

                } catch (MalformedURLException e) {
                    Log.e("Values", e.getMessage());

                }
                this.context = context;
                preferences = context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE);
            }

            public Values(Context context, MobileServiceClient mClient) {
                this.mClient = mClient;
                this.context = context;
                preferences = context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE);
            }

            public String getAuthToken() {
                return context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE).getString(AUTH_TOKEN, null);
            }

            public String getUserId() {
                return context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE).getString(USER_ID, null);
            }

            public void setAuthToken(String authToken) {
                context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE).edit().putString(AUTH_TOKEN, authToken).apply();
            }

            public void setUserId(String userId) {
                context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE).edit().putString(USER_ID, userId).apply();
            }

            public void setChurchId(String churchId) {
                context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE).edit().putString(CHURCH_ID, churchId).apply();
            }

            public String getChurchId() {
                //return context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE).getString(CHURCH_ID, null);

                try{
                    List<Object> objects = loadObjects(Values.Users.class);
                    final Values.Users user = (Values.Users) objects.get(0);
                    return user.ChurchID;
                } catch (IOException | ClassNotFoundException e){
                    Log.e("Values", e.getMessage());
                    return null;
                }

            }


            public boolean checkAuth() {

                return mClient.getCurrentUser() != null && mClient.getCurrentUser().getAuthenticationToken() != null && mClient.getCurrentUser().getUserId() != null && mClient.getCurrentUser().getAuthenticationToken().length() > 10 && mClient.getCurrentUser().getUserId().length() > 10;
            }





            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ///API///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            //login
            public void login(final String userId, String authToken) {

                if(userId == null || authToken == null)
                    return;

                final MobileServiceUser user = new MobileServiceUser(userId);
                user.setAuthenticationToken(authToken);
                mClient.setCurrentUser(user);

                Futures.addCallback(getUser(userId), new FutureCallback<Users>() {
                    @Override
                    public void onSuccess(Users result) {
                        try {
                            saveObjects(result);
                        } catch (IOException e) {
                            Log.e("Values", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("Values", t.getMessage());
                    }
                });



            }

            //post user
            public ListenableFuture<Values.Users> addUser(Values.Users user){
                user.id = "";
                return mClient.invokeApi(userApi, user, post, null, Values.Users.class);
            }
            //put user
            public ListenableFuture<Values.Users> updateUser(Values.Users user){
                return mClient.invokeApi(userApi, user, put, null, Values.Users.class);
            }
            //get user
            public ListenableFuture<Values.Users> getUser(String userId){
                Pair<String, String> param = new Pair<>("UserId",  userId);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(userApi, get, params, Values.Users.class);
            }
            //delete user//?
            public ListenableFuture<Values.Message> deleteUser(String userId){
                Pair<String, String> param = new Pair<>("UserId",  userId);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(userApi, delete, params, Values.Message.class);
            }
            //post event
            public ListenableFuture<Values.Events> addEvent(Values.Events event){
                event.id = "";
                return mClient.invokeApi(eventApi, event, post, null, Values.Events.class);
            }
            //put event
            public ListenableFuture<Values.Events> updateEvent(Values.Events event){
                return mClient.invokeApi(eventApi, event, put, null, Values.Events.class);
            }
            //get event//?
            public ListenableFuture<Values.Events> getEvent(String eventId){
                Pair<String, String> param = new Pair<>("EventId",  eventId);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(eventApi, get, params, Values.Events.class);
            }
            //get events
            @SuppressWarnings("unchecked")
            public ListenableFuture<MobileServiceList<Values.Events>> getEvents(String churchId){
                Pair<String, String> param = new Pair<>("ChurchId", churchId);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(eventApi, get, params, (Class<MobileServiceList<Values.Events>>)(Object)Values.Events.class);

            }
            //delete event//?
            public ListenableFuture<Values.Message> deleteEvent(String eventId){
                Pair<String, String> param = new Pair<>("EventId",  eventId);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(eventApi, delete, params, Values.Message.class);
            }
            //post church
            public ListenableFuture<Values.Churches> addChurch(Values.Churches church){
                return mClient.invokeApi(churchApi, church, post, null, Values.Churches.class);
            }
            //put church
            public ListenableFuture<Values.Churches> updateChurch(Values.Churches church){
                return mClient.invokeApi(churchApi, church, put, null, Values.Churches.class);
            }
            //get church//?f
            public ListenableFuture<Values.Churches> getChurch(String churchId){
                Pair<String, String> param = new Pair<>("ChurchId", churchId);
                List<Pair<String, String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(churchApi, get, params, Values.Churches.class);
            }
            //get churches
            @SuppressWarnings("unchecked")
            public ListenableFuture<MobileServiceList<Values.Churches>> getChurches(int page){
                Pair<String, String> param = new Pair<>("Page", String.valueOf(page));
                List<Pair<String, String>>params = new ArrayList<>();
                params.add(param);

                return mClient.invokeApi(churchApi, get, params, (Class<MobileServiceList<Values.Churches>>)(Object)Values.Churches.class);
            }
            @SuppressWarnings("unchecked")
            public ListenableFuture<MobileServiceList<Values.Churches>> searchChurches(String query){
                Pair<String, String> param = new Pair<>("Query", query);
                List<Pair<String, String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(churchApi, get, params, (Class<MobileServiceList<Values.Churches>>)(Object)Values.Churches.class);
            }
            //delete church//?
            public ListenableFuture<Values.Message> deleteChurch(String churchId){
                Pair<String, String> param = new Pair<>("ChurchId", churchId);
                List<Pair<String, String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(churchApi, delete, params, Values.Message.class);
            }
            //get join_event
            public ListenableFuture<Values.Message> joinEvent(String eventId, String userId){
                Pair<String, String> param = new Pair<>("EventId", eventId);
                Pair<String, String> param1 = new Pair<>("UserId", userId);
                Pair<String, String> param2 = new Pair<>("Action", join);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                params.add(param1);
                params.add(param2);
                return mClient.invokeApi(eventApi, put, params, Values.Message.class);
            }
            //get lead_event
            public ListenableFuture<Values.Message> leadEvent(String eventId, String userId){
                Pair<String, String> param = new Pair<>("EventId", eventId);
                Pair<String, String> param1 = new Pair<>("UserId", userId);
                Pair<String, String> param2 = new Pair<>("Action", lead);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                params.add(param1);
                params.add(param2);
                return mClient.invokeApi(eventApi, put, params, Values.Message.class);
            }
            //get lead_event
            public ListenableFuture<Values.Message> startEvent(String eventId, String userId){
                Pair<String, String> param = new Pair<>("EventId", eventId);
                Pair<String, String> param1 = new Pair<>("UserId", userId);
                Pair<String, String> param2 = new Pair<>("Action", start);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                params.add(param1);
                params.add(param2);
                return mClient.invokeApi(eventApi, put, params, Values.Message.class);
            }
            //get lead_event
            public ListenableFuture<Values.Message> endEvent(String eventId, String userId){
                Pair<String, String> param = new Pair<>("EventId", eventId);
                Pair<String, String> param1 = new Pair<>("UserId", userId);
                Pair<String, String> param2 = new Pair<>("Action", end);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                params.add(param1);
                params.add(param2);
                return mClient.invokeApi(eventApi, put, params, Values.Message.class);
            }
            //get lead_event
            public ListenableFuture<Values.Message> deleteEvent(String eventId, String userId){
                Pair<String, String> param = new Pair<>("EventId", eventId);
                Pair<String, String> param1 = new Pair<>("UserId", userId);
                Pair<String, String> param2 = new Pair<>("Action", delete);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                params.add(param1);
                params.add(param2);
                return mClient.invokeApi(eventApi, put, params, Values.Message.class);
            }
            //get lead_event
            public ListenableFuture<Values.Message> cancelEvent(String eventId, String userId){
                Pair<String, String> param = new Pair<>("EventId", eventId);
                Pair<String, String> param1 = new Pair<>("UserId", userId);
                Pair<String, String> param2 = new Pair<>("Action", cancel);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                params.add(param1);
                params.add(param2);
                return mClient.invokeApi(eventApi, put, params, Values.Message.class);
            }
            //post message
            public ListenableFuture<Values.Messages> addMessage(Values.Messages message){
                message.id = "";
                return mClient.invokeApi(messagesApi, message, post, null, Values.Messages.class);
            }
            //get messages
            @SuppressWarnings("unchecked")
            public ListenableFuture<MobileServiceList<Values.Messages>> getMessages(String eventId){
                Pair<String, String> param = new Pair<>("EventId",  eventId);
                List<Pair<String,String>>params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(messagesApi, get, params, (Class<MobileServiceList<Values.Messages>>)(Object)Values.Messages.class);
            }
            //check user
            /**TODO **/
            //send code
            public ListenableFuture<Values.Message> sendCode(String userId){
                Pair<String, String> param = new Pair<>("UserId", userId);
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(codeApi, get, params, Values.Message.class);
            }

            //clear code
            public ListenableFuture<Values.Message> clearCode(String userId){
                Pair<String, String> param = new Pair<>("UserId", userId);
                List<Pair<String, String>> params = new ArrayList<>();
                params.add(param);
                return mClient.invokeApi(codeApi, delete, params, Values.Message.class);
            }



            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



            public void saveObjects(Serializable... objects) throws IOException{

                for(Serializable object : objects){
                    preferences.edit().putString(object.getClass().getName(), serialize(object)).apply();
                }

            }

            public List<Object> loadObjects(Class... classes) throws ClassNotFoundException, IOException{
                List<Object>objects = new ArrayList<>();
                for(Class c : classes){
                    objects.add(deserialize(preferences.getString(c.getName(), "")));
                }
                return objects;
            }





                /**
                 * *** Object:  Table [dbo].[Churches]    Script Date: 6/16/2015 9:39:48 PM *****
                 */
                public static class Churches implements Serializable {

                    public String id;

                    public String ChurchName; //nvarchar](50)

                    public String AdminID; //int]NULL,

                    public int Status; //int]NULL,

                    public String PastorFName; //nvarchar](50)NULL,

                    public String PastorLName; //nvarchar](50)NULL,

                    public String PastorEmail; //nvarchar](50)NULL,

                    public String PastorPhone; //nvarchar](50)NULL,

                    public String ChurchPhone; //nvarchar](50)NULL,

                    public String ChurchEmail; //nvarchar](50)NULL,

                    public String ChurchWebsite; //nvarchar](50)NULL,

                    public double Latitude; //[decimal](9,6)NULL,

                    public double Longitude; //decimal](9,6)NULL,

                    public String Address1; //nvarchar](50)NULL,

                    public String Address2; //nvarchar](50)NULL,

                    public String City; //nvarchar](50)NULL,

                    public String State; //nvarchar](50)NULL,

                    public String Country; //nvarchar](50)NULL,

                    public String PostalCode; //nvarchar](50)NULL,

                }

                /**
                 * *** Object:  Table [dbo].[Events]    Script Date: 6/16/2015 9:39:48 PM *****
                 */


                public static class Events implements Serializable{

                    public String id;

                    public String ChurchId;

                    public String CreatorUserID; //int]

                    public String LeaderUserID; //int]

                    public int PeopleNeeded; //int]NULL,

                    public int GenderNeeded; //int]NULL,

                    public String Description; //nvarchar](150)NULL,

                    public int Category; //int]NULL,

                    public int Status; //int]NULL,

                    public long CreationDate; //int

                    public long StartDate; //int]NULL,

                    public long EndDate; //int]NULL,

                    public int ViewCount; //int]NULL,

                    public boolean Anonymous; //bit]NULL,

                    public boolean PersonIsAware; //bit]NULL,

                    public String PersonFirstName; //nvarchar](50)NULL,

                    public String PersonLastName; //nvarchar](50)NULL,

                    public String Name;

                    public double Latitude; //[decimal](9,6)NULL,

                    public double Longitude; //decimal](9,6)NULL,

                    public String Address1; //nvarchar](50)NULL,

                    public String Address2; //nvarchar](50)NULL,

                    public String City; //nvarchar](50)NULL,

                    public String State; //nvarchar](50)NULL,

                    public String Country; //nvarchar](50)NULL,

                    public String PostalCode; //nvarchar](50)NULL,



                }

                /**
                 * *** Object:  Table [dbo].[Messages]    Script Date: 6/16/2015 9:39:49 PM *****
                 */

                public static class Messages {

                    public String id;

                    public String UserID; //int]NULL,

                    public String EventID; //int]NULL,

                    public String Message; //nvarchar](100)NULL,


                }


                /****** Object:  Table [dbo].[UserEvents]    Script Date: 6/16/2015 9:39:49 PM ******/


                public static class UserEvents {

                    public String id;

                    public String UserID; //int] NULL,

                    public String EventID; //int] NULL,

                }
                /****** Object:  Table [dbo].[Users]    Script Date: 6/16/2015 9:39:49 PM ******/



                public static class Users implements Serializable {

                    public String id;

                    public String Email; //nvarchar](50) NOT NULL,

                    public String PictureName; //nvarchar](50) NOT NULL,

                    public String FirstName; //nvarchar](50) NOT NULL,

                    public String LastName; //nvarchar](50) NOT NULL,

                    public long BirthDate; //int] NULL,

                    public String ChurchID; //int] NULL,

                    public String Phone; //nvarchar](50) NULL,

                    public String Bio; //nvarchar](150) NULL,

                    public String Skills; //nvarchar](100) NULL,

                    public boolean Gender; //bit] NULL,

                    public int Status; //int] NULL,

                    public double Latitude; //[decimal](9,6)NULL,

                    public double Longitude; //decimal](9,6)NULL,

                    public String Address1; //nvarchar](50)NULL,

                    public String Address2; //nvarchar](50)NULL,

                    public String City; //nvarchar](50)NULL,

                    public String State; //nvarchar](50)NULL,

                    public String Country; //nvarchar](50)NULL,

                    public String PostalCode; //nvarchar](50)NULL,

                    public String VerificationCode;

                }

            public static class Message {

                int Status;

                String Message;

            }

            /** Read the object from Base64 string. */
            public static Object deserialize( String s ) throws IOException ,
                    ClassNotFoundException {

                byte [] data = Base64.decode( s, Base64.DEFAULT );
                ObjectInputStream ois = new ObjectInputStream(
                        new ByteArrayInputStream(  data ) );
                Object o  = ois.readObject();
                ois.close();
                return o;
            }

            /** Write the object to a Base64 string. */
            public static String serialize( Serializable o ) throws IOException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream( baos );
                oos.writeObject( o );
                oos.close();
                return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            }

            public String getStringFromView(EditText view){
                String s = view.getText().toString();
                view.setHintTextColor(Color.RED);
                if(s.length() < 3){
                    Toast.makeText(context, "Please enter the " + view.getHint().toString(), Toast.LENGTH_SHORT).show();
                    return null;
                }
                return s;
            }
            public boolean checkObjects(Object... objects){
                for(Object obj : objects){
                    if(obj == null)
                        return false;
                }
                return true;
            }


        }
