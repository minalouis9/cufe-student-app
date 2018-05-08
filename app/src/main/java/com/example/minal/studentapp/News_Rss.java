package com.example.minal.studentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class News_Rss extends AppCompatActivity
{
    private URL url;
    private List<FeedItem> feedItems = new ArrayList<>();
    private ViewPager viewPager;
    private PagerTabsIndicator tabsIndicator;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView recyclerView;
    private news_Rss_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news__rss);

        //feedItems = new ArrayList<>();
        AsyncCallWS_NewsRss asyncCallWS_newsRss = new AsyncCallWS_NewsRss(News_Rss.this);
        asyncCallWS_newsRss.execute();
        //initiating shimmer viewer:
    }

    public class AsyncCallWS_NewsRss extends AsyncTask<Void, Void, Void>
    {

        public String stripHtml(String html) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                return Html.fromHtml(html).toString();
            }
        }

        Context Cntx;
        ProgressDialog progressDialog;

        public AsyncCallWS_NewsRss(Context cntx)
        {
            this.Cntx = cntx;
        }
        @Override
        protected void onPreExecute()
        {
            //Log.i(TAG, "onPreExecute");
            progressDialog= new ProgressDialog(News_Rss.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            //Log.i(TAG, "doInBackground");
            GetAdvertisement();

            try {
                Getdata();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void GetAdvertisement()
        {
            HttpClient client = new DefaultHttpClient();

            HttpGet request =null;

            if(LoginActivity.username.charAt(0) == '1')
            {
                request =  new HttpGet("http://eng.cu.edu.eg/ar/credit-hour-system/");
            }
            else
            {
                request =  new HttpGet("http://eng.cu.edu.eg/ar/bachelors-2/smester-system/");
            }

            //HttpGet request = new HttpGet("http://eng.cu.edu.eg/ar/credit-hour-system/");
            HttpResponse response = null;
            try {
                response = client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String html = "";
            InputStream in = null;
            try {
                in = response.getEntity().getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            try {
                while((line = reader.readLine()) != null)
                {
                    str.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            html = str.toString();

            String HTML_String = stripHtml(html);

            if(LoginActivity.username.charAt(0) == '1')
            {
                FillData_Credit(HTML_String);
            }
            else
            {
                FillData_Semester(HTML_String);
            }

        }

        @Override
        protected void onPostExecute(Void result)
        {
            mAdapter = new news_Rss_Adapter(News_Rss.this, feedItems); //Seif: change hre

            mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
            recyclerView = findViewById(R.id.recycler_view03);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new MyDividerItemDecoration(Cntx, LinearLayoutManager.VERTICAL, 16));

            recyclerView.setAdapter(mAdapter);
            // refreshing recycler view
            mAdapter.notifyDataSetChanged();
            // stop animating Shimmer and hide the layout
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);

            //Log.i(TAG, "onPostExecute");
            progressDialog.cancel();
            android.support.v7.widget.Toolbar toolbar_News = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar4);
            toolbar_News.setTitle("Cairo University\nFaculty of Engineering");
        }

        private void FillData_Semester(String HTML_String)
        {
            HTML_String = HTML_String.substring(HTML_String.indexOf("جامــعة القــاهـرة"),HTML_String.length());

            HTML_String = HTML_String.substring(HTML_String.indexOf("￼")+3,HTML_String.length());

            int end;
            String Advertisement;
            FeedItem feedItem;
            NewsItem newsItem;
            while( HTML_String.substring(0,3).contains("￼") == false) //stop at the end
            {
                while(HTML_String.charAt(0)=='\n' || HTML_String.charAt(0)=='\t')
                {
                    HTML_String = HTML_String.substring(1,HTML_String.length());
                }
                if(HTML_String.charAt(0)=='￼') break;

                end= HTML_String.indexOf('\n');
                Advertisement = HTML_String.substring(0,end);

                if(Advertisement.equals("") ==false && Advertisement.equals("\n")==false) {
                    feedItem = new FeedItem();
                    feedItem.setTitle("نظام الفصلين");
                    feedItem.setDescription(Advertisement);
                    feedItems.add(feedItem);
                    newsItem = new NewsItem(""+Advertisement,this.Cntx); //News Item Written
                }
                HTML_String = HTML_String.substring(Advertisement.length(),HTML_String.length());
            }
        }

        private void FillData_Credit(String HTML_String)
        {
            HTML_String = HTML_String.substring(HTML_String.indexOf('{'),HTML_String.length());

            int begin = HTML_String.indexOf("الإعلانات العامة لبرامج الساعات المعتمدة") +40 ;
            HTML_String = HTML_String.substring(begin+2,HTML_String.length());
            int end ;String Advertisement =null;
            String PubDate =null;
            int DescBegin; //Advertisement.substring(2,Advertisement.length()).indexOf('\n')+1;
            int DescEnd = 0;
            String Desc =null;

            String FullString="";
            int i=0;
            FeedItem feedItem;
            NewsItem newsItem;
            while( HTML_String.substring(0,40).contains("———————————") == false) //stop at the end
            {
                end= HTML_String.indexOf("——————————————————————————————————–");
                Advertisement = HTML_String.substring(0,end);

                PubDate= Advertisement.substring(0,Advertisement.substring(2,Advertisement.length()).indexOf('\n')+2);
                DescBegin = PubDate.length()+2;
                DescEnd = Advertisement.substring(DescBegin,Advertisement.length()).indexOf('<');
                Desc= Advertisement.substring(DescBegin, DescBegin+DescEnd );

                Desc.replace(  "لمزيد من المعلومات برجاء الاطلاع على الملفات المرفقة","");
                HTML_String = HTML_String.substring(Advertisement.length()+35,HTML_String.length());
                ++i;
                FullString+=( i+ "-PubDate: "+PubDate +"\n" +"  Desc"+Desc+"\n");
                feedItem = new FeedItem();
                feedItem.setDescription(Desc.replace('\n',' ')+'\n');
                feedItem.setTitle("ساعات معتمدة"+"\n");
                feedItem.setPubDate(PubDate.replace('\n',' ')+"\n");
                feedItem.setLink("");
                feedItems.add(feedItem);
                newsItem = new NewsItem(""+feedItem.getDescription(),this.Cntx); //News Item Written
            }

        }

        public void Getdata()
        {
            try
            {
                final Document[] xmlDoc = {null};
                // Do network action in this function
                try
                {
                    url = new URL("http://eng.cu.edu.eg/ar/feed/");
                }
                catch (MalformedURLException e1)
                {
                    e1.printStackTrace();
                }
                HttpURLConnection connection = null;

                try
                {
                    connection = (HttpURLConnection) url.openConnection();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }

                try
                {
                    connection.setRequestMethod("GET");
                }
                catch (ProtocolException e1)
                {
                    e1.printStackTrace();
                }
                InputStream inputStream = null;

                try
                {
                    inputStream = connection.getInputStream();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }

                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;

                try
                {
                    builder = builderFactory.newDocumentBuilder();
                }
                catch (ParserConfigurationException e1)
                {
                    e1.printStackTrace();
                }

                try
                {
                    xmlDoc[0] = builder.parse(inputStream);
                }
                catch (SAXException e1)
                {
                    e1.printStackTrace();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }

                ProcessXml(xmlDoc[0]);
                ShimmerStart();
                return ;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return ;
        }

        private void ProcessXml(Document data)
        {
            if (data != null)
            {
                Element root = data.getDocumentElement();
                Node channel = root.getChildNodes().item(1);
                NodeList items = channel.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    Node cureentchild = items.item(i);
                    if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                        FeedItem item = new FeedItem();
                        NodeList itemchilds = cureentchild.getChildNodes();
                        for (int j = 0; j < itemchilds.getLength(); j++) {
                            Node cureent = itemchilds.item(j);
                            if (cureent.getNodeName().equalsIgnoreCase("title")) {
                                item.setTitle(cureent.getTextContent());
                            } else if (cureent.getNodeName().equalsIgnoreCase("description")) {
                                item.setDescription(cureent.getTextContent());
                            } else if (cureent.getNodeName().equalsIgnoreCase("pubDate")) {
                                item.setPubDate(cureent.getTextContent());
                            } else if (cureent.getNodeName().equalsIgnoreCase("link")) {
                                item.setLink(cureent.getTextContent());
                            } else if (cureent.getNodeName().equalsIgnoreCase("media:thumbnail")) {
                                //this will return us thumbnail url
                                String url = cureent.getAttributes().item(0).getTextContent();
                                item.setThumbnailUrl(url);
                            }
                        }
                        feedItems.add(item);


                    }
                }
            }
        }

        private void ShimmerStart()
        {

        }

    }
}
