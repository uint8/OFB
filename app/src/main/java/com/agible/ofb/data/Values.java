


        package com.agible.ofb.data;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.content.res.Resources;
        import android.graphics.Color;
        import android.util.Base64;
        import android.util.Log;
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
            public static final int STATUS_PENDING = 100;
            public static final int STATUS_APROVED = 200;
            public static final int STATUS_BLOCKED = 303;

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
                return context.getSharedPreferences(SPP_NAME, Context.MODE_PRIVATE).getString(CHURCH_ID, null);
            }


            public boolean checkAuth() {

                return mClient.getCurrentUser() != null && mClient.getCurrentUser().getAuthenticationToken() != null && mClient.getCurrentUser().getUserId() != null && mClient.getCurrentUser().getAuthenticationToken().length() > 10 && mClient.getCurrentUser().getUserId().length() > 10;
            }

            public void getUser(final OnFinishListener<Object> listener) {

                Futures.addCallback(getUsers(mClient.getCurrentUser().getUserId()), new FutureCallback<Values.Users>() {
                    @Override
                    public void onSuccess(final Users user) {
                        listener.onFinish(user);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }

            public void login(final String userId, String authToken) {

                if(userId == null || authToken == null)
                    return;

                final MobileServiceUser user = new MobileServiceUser(userId);
                user.setAuthenticationToken(authToken);
                mClient.setCurrentUser(user);

                getUser(new OnFinishListener<Object>() {
                    @Override
                    public void onFinish(Object... objects) {

                        Values.Users usr = (Values.Users) objects[0];
                        try {

                            saveObjects(usr);
                        } catch (IOException e) {
                            Log.e("Values", e.getMessage());
                        }
                    }
                });



            }

            public void logout(){

            }

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

            public void getEvents(final OnFinishListener<MobileServiceList<Values.Events>> listener) {

                Query query = new ExecutableQuery<>();
                query.field("ChurchID").eq(getChurchId());
                Futures.addCallback(mClient.getTable(Values.Events.class).execute(query), new FutureCallback<MobileServiceList<Values.Events>>() {
                    @Override
                    public void onSuccess(MobileServiceList<Values.Events> result) {
                        listener.onFinish(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        listener.onFinish(null);
                    }
                });

            }

                /**
                 * *** Object:  Table [dbo].[Churches]    Script Date: 6/16/2015 9:39:48 PM *****
                 */
                public static class Churches {

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

                public ListenableFuture<Churches> updateChurch (Churches church){
                    return mClient.getTable(Churches.class).update(church);
                }
                public ListenableFuture<Churches> getChurch (Object id){
                    return mClient.getTable(Churches.class).lookUp(id);
                }
                public ListenableFuture<Churches> addChurch (Churches church){
                    return mClient.getTable(Churches.class).insert(church);
                }
                public ListenableFuture<Void> deleteChurch (Churches church){
                    return mClient.getTable(Churches.class).delete(church);
                }

                /**
                 * *** Object:  Table [dbo].[Events]    Script Date: 6/16/2015 9:39:48 PM *****
                 */


                public static class Events {

                    public String id;

                    public String CreatorUserID; //int]

                    public String LeaderUserID; //int]

                    public int PeopleNeeded; //int]NULL,

                    public int GenderNeeded; //int]NULL,

                    public String Description; //nvarchar](150)NULL,

                    public int Category; //int]NULL,

                    public int Status; //int]NULL,

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

                public ListenableFuture<Events> updateEvent (Events event){
                    return mClient.getTable(Events.class).update(event);
                }
                public ListenableFuture<Events> getEvent (Object id){
                    return mClient.getTable(Events.class).lookUp(id);
                }
                public ListenableFuture<Events> addEvent (Events event){
                    return mClient.getTable(Events.class).insert(event);
                }
                public ListenableFuture<Void> deleteEvent (Events event){
                    return mClient.getTable(Events.class).delete(event);
                }

                /**
                 * *** Object:  Table [dbo].[Messages]    Script Date: 6/16/2015 9:39:49 PM *****
                 */

                public static class Messages {

                    public String id;

                    public String FromUserID; //int]NULL,

                    public String EventID; //int]NULL,

                    public String MessageText; //nvarchar](100)NULL,


                }

                /****** Object:  Table [dbo].[Statistics]    Script Date: 6/16/2015 9:39:49 PM ******/


                public static class Statistics {

                    public String id;

                    public String UserID; //int] NOT NULL,

                    public int Category; //int] NOT NULL,

                    public long Date; //int] NOT NULL,

                }
                public ListenableFuture<MobileServiceList<Statistics>> getStatistics () {
                    Query query = new ExecutableQuery<Statistics>();
                    long history = new Date().getTime() - HISTORY_LENGTH;
                    System.out.println("History " + history);
                    query.field("DateCreated").gt(history).and().field("UserId").eq(mClient.getCurrentUser().getUserId());
                    return mClient.getTable(Statistics.class).execute(query);
                }
                public ListenableFuture<Statistics> addStatistics (Statistics statistics){
                    return mClient.getTable(Statistics.class).insert(statistics);

                }

                /****** Object:  Table [dbo].[UserEvents]    Script Date: 6/16/2015 9:39:49 PM ******/


                public static class UserEvents {

                    public String id;

                    public String UserID; //int] NULL,

                    public String EventID; //int] NULL,

                }
                /****** Object:  Table [dbo].[Users]    Script Date: 6/16/2015 9:39:49 PM ******/


                public ListenableFuture<Users> updateUsers (Users user){
                    return mClient.getTable(Users.class).update(user);
                }
                public ListenableFuture<Users> getUsers (Object id){
                    return mClient.getTable(Users.class).lookUp(id);
                }
                public ListenableFuture<Users> addUsers (Users user){
                    return mClient.getTable(Users.class).insert(user);
                }
                public ListenableFuture<Void> deleteUsers (Users user){
                    return mClient.getTable(Users.class).delete(user);
                }


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


                }

            /** Read the object from Base64 string. */
            private static Object deserialize( String s ) throws IOException ,
                    ClassNotFoundException {

                byte [] data = Base64.decode( s, Base64.DEFAULT );
                ObjectInputStream ois = new ObjectInputStream(
                        new ByteArrayInputStream(  data ) );
                Object o  = ois.readObject();
                ois.close();
                return o;
            }

            /** Write the object to a Base64 string. */
            private static String serialize( Serializable o ) throws IOException {
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
