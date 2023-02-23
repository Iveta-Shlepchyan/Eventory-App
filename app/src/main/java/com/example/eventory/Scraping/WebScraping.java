package com.example.eventory.Scraping;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventory.ContainerActivity;
import com.example.eventory.models.CardModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebScraping {

    private Document doc;
    private Thread secThread;
    private Runnable runnable;


    public void startScraping() {
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb() {
        try {
            doc = Jsoup.connect("https://tomsarkgh.am/en").get();
            Elements hrefs = doc.getElementsByAttributeValueContaining("href", "event/");
            LinkedHashSet<String> links = new LinkedHashSet<String>();
            for (Element href : hrefs) {
                links.add(href.absUrl("href"));
            }
            for (String link : links) {

                addToFirebase(getEventInfo(link));
            }
        } catch (IOException e) {
            Log.e("Jsoup", "Connection problem");
        }


    }

    private CardModel getEventInfo(String link) {

        Document event = null;
        try {
            event = Jsoup.connect(link).get();
        } catch (IOException e) {
            Log.e("Jsoup", "Connection problem");
        }
        CardModel eventInfo = new CardModel();
        eventInfo.setLink(link);
        eventInfo.setName(event.getElementById("eventName").text());
        eventInfo.setDescription(event.getElementById("eventDesc").text());
        eventInfo.setImg_url(event.getElementById("single_default").absUrl("href"));
        eventInfo.setPlace(event.getElementsByClass("occurrence_venue").get(0).text());
        eventInfo.setMore_images(new ArrayList<String>());
        eventInfo.setDate_time_list(new ArrayList<String>());
        eventInfo.setTags(new ArrayList<String>());
        Elements tags_el = event.getElementsByClass("event_type alpha60");
        Elements images = event.getElementsByAttributeValueContaining("href", "/thumbnails/Photo/bigimage/");

        HashSet<String> tags = new HashSet<String>();
        for (Element tag : tags_el) {
            tags.add(tag.text());
        }
        eventInfo.getTags().addAll(tags);

        for (int i = 1; i < images.size(); i++) {
            eventInfo.getMore_images().add(images.get(i).absUrl("href"));
        }

        HashSet<String> date_times = new HashSet<String>();


        Elements time = event.getElementsByClass("oc_time");
        if (time.size() == 1) {
            String day = event.getElementsByClass("oc_date").text();
            String month = event.getElementsByClass("oc_month").text();
            String weekday = event.getElementsByClass("oc_weekday").text();
            String full_date = day + " " + month + ", " + weekday + "  " + time.text();
            date_times.add(full_date);
        } else {
            Elements full_date = event.getElementsByClass("oc_fulldate");
            time.remove(0);
            for (int i = 0; i < time.size(); i++) {
                date_times.add(full_date.get(i).text() + "  " + time.get(i).text());
            }
        }

        eventInfo.getDate_time_list().addAll(date_times);
        eventInfo.setDate_time(eventInfo.getDate_time_list().get(0));
        Collections.sort(eventInfo.getDate_time_list(), new DateComparator());

        eventInfo.setMin_price(extractPrice(eventInfo.getDescription()));


        return eventInfo;
    }

    private void addToFirebase(CardModel event){
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            /*for (String path:event.getTags()) {
//                Log.e("Jsoup", path + " " + ContainerActivity.paths.contains(path));
                if(ContainerActivity.paths.contains(path)){*/
//TODO return commented code back
                    CollectionReference eventsRef = db.collection("Tomsarkgh");
                    eventsRef.document(event.getName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (!document.exists()) {

                                    try {
                                        eventsRef.document(event.getName()).set(event);
                                    } catch (Exception e) {
                                        Log.e("addToFirebase", "event setting failed");
                                    }
                                }
                                else {
                                    //TODO remove it
                                    eventsRef.document(event.getName()).update("min_price",event.getMin_price());
                                }
                            }
                        }
                    });

             /*   }
            }*/

        }catch (Exception exception){
            Log.e("addToFirebase", "failed");
        }

    }


    //TODO fix price extractor
    private String extractPrice(String description){
        String price = null;
        Pattern pattern = Pattern.compile("(\\d+[-–]+\\d+)");
            Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            if(matcher.group(0).length() <=3) Log.e("WebScraping / extractPrice : ", ""+matcher.group(0));
            else price = matcher.group(0).length()>7 ? matcher.group(0) : matcher.group(0).substring(0,4)+"+";
        } else {
            pattern = Pattern.compile("(\\d{4})");
            matcher = pattern.matcher(description);
            if(matcher.find() && Integer.parseInt(matcher.group(0))%100==0){
                price = matcher.group(0) +"+";
            }else{
            }
        }
        return price;
    }


}


    class DateComparator implements Comparator<String> {
        @Override
        public int compare(String date1, String date2) {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
            try {

                String[] date = date1.replace(",","").split(" ");
                date[2] ="";
                date1= String.join(" ", date).replace("  ","");

                String[] date0 = date2.replace(",","").split(" ");
                date0[2] ="";
                date2= String.join(" ", date0).replace("  ","");

                Date date1Parsed = format.parse(date1);
                Date date2Parsed = format.parse(date2);

                return date1Parsed.compareTo(date2Parsed);
            } catch (ParseException e) {
                Log.e("Date Comparator", "format problem");
                e.printStackTrace();
                return 0;
            }
        }
    }



