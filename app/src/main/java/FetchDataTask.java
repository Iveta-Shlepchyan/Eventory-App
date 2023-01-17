import android.os.AsyncTask;

import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;

class FetchDataTask extends AsyncTask<Void, Void, Void> {

    FirebaseFirestore db ;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // Fetch the HTML document
            Document doc = Jsoup.connect("https://tomsarkgh.am/events").get();
            // Extract the information you need using Jsoup
            Elements events = doc.select(".event-card");

            for (Element event : events) {
                String title = event.select(".event-card-title").text();
                String date = event.select(".event-card-date").text();
                String location = event.select(".event-card-location").text();
                // ...
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        db = FirebaseFirestore.getInstance();
        /*DatabaseReference eventsRef = db.getReference("events");
        for (Element event : events) {
            String title = event.select(".event-card-title").text();
            String date = event.select(".event-card-date").text();
            String location = event.select(".event-card-location").text();
            Event eventObject = new Event(title, date, location);
            String key = eventsRef.push().getKey();
            eventsRef.child(key).setValue(eventObject);
        }*/


        return null;
    }
}
