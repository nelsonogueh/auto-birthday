package com.dulceprime.specialwishes.activity;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dulceprime.specialwishes.other_components.DBhelper;
import com.dulceprime.specialwishes.other_components.PrefManager;
import com.dulceprime.specialwishes.R;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);



        db = openOrCreateDatabase(DBhelper.DB_NAME, MODE_PRIVATE, null);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {

            launchHomeScreen();
            finish();
        }
        else{
            // creating the tables
            DBhelper dBhelper = new DBhelper(getApplicationContext());
            dBhelper.onCreate(db);

            insertBirthdayMessagessToDatabase(); // Insert into the database
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }



        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void insertBirthdayMessagessToDatabase() {
            // THE BIRTHDAY MESSAGES
            db.execSQL("INSERT INTO "+ DBhelper.BIRTHDAY_MESSAGES_TABLE +" ("+DBhelper.MESSAGE_TYPE+","+DBhelper.MESSAGE_BODY+", "+DBhelper.ONLINE_UNIQUE_ID+") VALUES ('system'," + DatabaseUtils.sqlEscapeString("Hope your birthday bestows you with more happiness, love and fun than you ever thought you could take...and then some! You deserve it all. Enjoy your special day!") + ", '1'), ('system'," + DatabaseUtils.sqlEscapeString("May you have such an incredibly special birthday that every day afterward starts and ends with joy, love, laughter and peace of mind.") + ", '2'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May the next 12 months of the year be the happiest of your life. Hope every day is packed with memories that you'll treasure evermore.") + ", '3'), ('system'," + DatabaseUtils.sqlEscapeString("Hope your birthday brings you whatever you want. You deserve happiness, love and, most of all, fun on your special day.") + ", '4'), ('system'," + DatabaseUtils.sqlEscapeString("Have a great birthday with all the love, laughter and joy you deserve.") + ", '5'), ('system'," + DatabaseUtils.sqlEscapeString("May this birthday and the coming year bring you good surprises — filled with sunshine, smiles and sweethearts.") + ", '6'), ('system'," + DatabaseUtils.sqlEscapeString("Hope you create and enjoy heaps of beautiful birthday memories to cherish your entire life. Happy birthday.") + ", '7'), ('system'," + DatabaseUtils.sqlEscapeString("On your special day, you should only have the good luck that comes with family and friends. Happy birthday.") + ", '8'), ('system'," + DatabaseUtils.sqlEscapeString("Hope your special day fills up your heart and soul with joy, wonder and love.") + ", '9'), ('system'," + DatabaseUtils.sqlEscapeString("I only have the best birthday blessings for you — today and forever. Happy birthday.") + ", '10'), ('system'," + DatabaseUtils.sqlEscapeString("Have an amazing birthday. Be sure to celebrate whatever brings you bliss and contentment.") + ", '11'), ('system'," + DatabaseUtils.sqlEscapeString("May your birthday be filled with hours upon hours of joy and love. Happy birthday.") + ", '12'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! May you have the best of luck on your special day, bringing you the joy, peace and wonder you so rightfully deserve.") + ", '13'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! It's time for you to have an amazing birthday and savor the joy, love and peace we aim to bring you.") + ", '14'), ('system'," + DatabaseUtils.sqlEscapeString("May every moment of your birthday be the happiest you've ever had — and may your happiness spill over to every other day of the year.") + ", '15'), ('system'," + DatabaseUtils.sqlEscapeString("May your life be a series of fortunate events with countless happy birthdays, starting with this one.") + ", '16'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. Let’s light your birthday candles and mark the most special day of the year — your birthday.") + ", '17'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. Hope you begin your special day with a gigantic smile on your beautiful face — and it only gets bigger and bigger with each hour of your birthday.") + ", '18'), ('system'," + DatabaseUtils.sqlEscapeString("Here's to your special day. Let's make it a truly special celebration. Happy birthday.") + ", '19'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May all your dreams become what you truly want.") + ", '20'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May every dream of yours set your life on fire, brightening up today, your special day, and every other day of the year.") + ", '21'), ('system'," + DatabaseUtils.sqlEscapeString("Have an amazing birthday. Light your birthday candles and wish upon a star or all the stars in the sky (you certainly deserve everything coming to you).") + ", '22'), ('system'," + DatabaseUtils.sqlEscapeString("With this birthday, you're a little older and a lot more wonderful. Have the happiest of special days.") + ", '23'), ('system'," + DatabaseUtils.sqlEscapeString("Every year, you become greater in every way that really matters. Have a great birthday.") + ", '24'), ('system'," + DatabaseUtils.sqlEscapeString("May your special day be packed with all the joy, peace and glory you wish for. Happy birthday.") + ", '25'), ('system'," + DatabaseUtils.sqlEscapeString("Light and blow out each candle on your birthday cake...not because it's what people do, but to celebrate another special day of your extraordinary life. Happy birthday.") + ", '26'), ('system'," + DatabaseUtils.sqlEscapeString("You're very special — and you should know it. So I will let you how much every second of your special day. Happy birthday.") + ", '27'), ('system'," + DatabaseUtils.sqlEscapeString("You're a wonderful person who has always deserved only the best of everything, nothing less. Happy birthday.") + ", '28'), ('system'," + DatabaseUtils.sqlEscapeString("There are so many beautiful things I want to say about you…every day. Today, though, I have only one — happy birthday.") + ", '29'), ('system'," + DatabaseUtils.sqlEscapeString("Wishing you a birthday celebration that's as amazing as you are to everyone you know and love. Happy birthday.") + ", '30'), ('system'," + DatabaseUtils.sqlEscapeString("Your birthday should be a national holiday, so you can celebrate your special day from sunrise to sunset. Happy birthday.") + ", '31'), ('system'," + DatabaseUtils.sqlEscapeString("Another birthday, another year you've been blessed with more of everything that makes you an amazing individual. Happy birthday.") + ", '32'), ('system'," + DatabaseUtils.sqlEscapeString("Everyone deserves to have a special day, at least once a year on their birthday. Special people like you deserve to have a special birthday celebration in their honor, every day of the year. Happy birthday.") + ", '33'), ('system'," + DatabaseUtils.sqlEscapeString("You are one of the truest friend I've ever had. You're the best, and the best you shall have on your special day. Happy birthday.") + ", '34'), ('system'," + DatabaseUtils.sqlEscapeString("May your birthday set your life on so much in order and light up your path to inner joy, well-being and love. Happy Birthday") + ", '35'), ('system'," + DatabaseUtils.sqlEscapeString("You may be growing older every year, but you're the same person — perfect as ever. Happy birthday.") + ", '36'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May you only enjoy the best things that life can offer — because you are definitely one of the best.") + ", '37'), ('system'," + DatabaseUtils.sqlEscapeString("Happy Birthday, I've always wanted to let you know how precious you are. You are the best. Happy Birthday once more.") + ", '38'), ('system'," + DatabaseUtils.sqlEscapeString("There's no better best friend than you in my world. Happy birthday.") + ", '39'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May the start of a new year in your life bring you every bit of happiness, health and prosperity you deserve.") + ", '40'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. Now’s the time to remember, so let's celebrate your special day in the most special of ways — with joy, with fun, and with love.") + ", '41'), ('system'," + DatabaseUtils.sqlEscapeString("Wishing you a very happy birthday, not because I have to but because I want to. You're one of the finest people I know.") + ", '42'), ('system'," + DatabaseUtils.sqlEscapeString("Have a beautifully wonderful birthday, one of the special days someone like you should have.") + ", '43'), ('system'," + DatabaseUtils.sqlEscapeString("Your birthday should be a national holiday, so all the people who know and love you can have a day off to celebrate your arrival into our lives. Happy birthday.") + ", '44'), ('system'," + DatabaseUtils.sqlEscapeString("Another birthday, another year older and, most importantly, another reason to celebrate you all day and all night.") + ", '45'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May only the world's greatest treasures — love, joy, adventure and peace — be yours, today and every day.") + ", '46'), ('system'," + DatabaseUtils.sqlEscapeString("Sure, with every birthday, you get a little older. But you also get the wisdom to see what's truly beautiful, truly full of joy, truly yours to celebrate.") + ", '47'), ('system'," + DatabaseUtils.sqlEscapeString("From one hopeful dreamer to another, may you have the happiest of birthdays, the kind that wishful thoughts are made of.") + ", '48'), ('system'," + DatabaseUtils.sqlEscapeString("Birthdays come and go so fast, before we even realize that the day we were born is truly a special one. Let's make your special day especially about you. Let's celebrate you.") + ", '49'), ('system'," + DatabaseUtils.sqlEscapeString("I'm more than proud to call you my friend — I'm grateful, honored, blessed, overjoyed, fortunate and humbled. Happy birthday.") + ", '50'), ('system'," + DatabaseUtils.sqlEscapeString("When God created friends, He made them in your image...the best friend anyone can ever have. Happy birthday") + ", '51'), ('system'," + DatabaseUtils.sqlEscapeString("Take every birthday wish you've received today, multiply all the love you found in them by 1,000, then add years of joy, wonder and prosperity to the mix...and it still wouldn't equal all the love, joy, wonder and prosperity I wish for you.") + ", '52'), ('system'," + DatabaseUtils.sqlEscapeString("Any day we can live our lives to the fullest is a gift. Savor every moment of your special day, because you'll have to wait another 364 days to feel as special. Happy birthday.") + ", '53'), ('system'," + DatabaseUtils.sqlEscapeString("For your special day, I have only two birthday wishes for you: now and forever. Be happy in the now, so you'll be happy forever!") + ", '54'), ('system'," + DatabaseUtils.sqlEscapeString("Hope you have a wonderful birthday that lasts all year round!") + ", '55'), ('system'," + DatabaseUtils.sqlEscapeString("Happier birthdays come to dreamers who dare to take action. Like you.") + ", '56'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday balloon, cake, candles, ice cream and present day!") + ", '57'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! May joy be your companion everywhere you go.") + ", '58'), ('system'," + DatabaseUtils.sqlEscapeString("Take the time to have a bright, sunshiny, fun-filled birthday!") + ", '59'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. From the depths of my blissful soul to the peak of my unbridled passion, I wish you the happiest of special days.") + ", '60'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. From the depths of my blissful soul to the peak of my unbridled passion, I wish you the happiest of special days.") + ", '61'), ('system'," + DatabaseUtils.sqlEscapeString("On your special day, look back at all the wonderful moments you’ve shared — and look forward to even more amazing ones yet to share. Happy birthday.") + ", '62'), ('system'," + DatabaseUtils.sqlEscapeString("If anyone deserves to experience joy, peace and wonder to the fullest, it’s you. Happy birthday!") + ", '63'), ('system'," + DatabaseUtils.sqlEscapeString("Every day may be the first day of the rest of your life, but your birthday was your very first. Celebrate it as if there were no tomorrows. Happy birthday.") + ", '64'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! May your special day be as warm as the sunlight, as breezy as the wind and as right as rain.") + ", '65'), ('system'," + DatabaseUtils.sqlEscapeString("Your birthday is the perfect time to stop brooding over the thorns in life and promise yourself the rose garden of your dreams.") + ", '66'), ('system'," + DatabaseUtils.sqlEscapeString("Here's to a birthday full of life and a life full of happy birthdays!") + ", '67'), ('system'," + DatabaseUtils.sqlEscapeString("Don’t think about how old you are. Think how blessed you are. Think about all the experiences you've had in life — both good and bad — that have brought you this far. Here’s to life! Happy birthday!") + ", '68'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! Hope this special day is the start of your most special year ever!") + ", '69'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! Today, celebrate the day you were born. Every day, celebrate life.") + ", '70'), ('system'," + DatabaseUtils.sqlEscapeString("Birthdays are nature's way of telling you that you are getting old, especially when you have to look in the mirror the next morning. Have a happy birthday.") + ", '71'), ('system'," + DatabaseUtils.sqlEscapeString("Jump for joy! It's your birthday! Have a truly special day. Happy Birthday.") + ", '72'), ('system'," + DatabaseUtils.sqlEscapeString("If the love you take is equal to the love you make, you must be the world's most loved and loving person. I’m glad I get to bask in the sunshine of your love. Happy birthday!") + ", '73'), ('system'," + DatabaseUtils.sqlEscapeString("Great things do happen to great people — especially on their special day — and you're certainly one of the greatest people I know and love. Happy birthday!") + ", '74'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! For most people, wisdom comes with age. For others, it's prosperity. For everyone I know, it just means getting older.") + ", '75'), ('system'," + DatabaseUtils.sqlEscapeString("There's only one thing I like about you: everything. Happy birthday!") + ", '76'), ('system'," + DatabaseUtils.sqlEscapeString("Your birthday doesn't mean you're another year older — it means you have another year to celebrate. Happy birthday.") + ", '77'), ('system'," + DatabaseUtils.sqlEscapeString("You are a role model for the truly one of a kind. You were born an original! Happy birthday.") + ", '78'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday! Hope your special day is as nice as you are and bestows you with twice as much as you wished for!") + ", '79'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. Every year you get closer and closer to who you really are, what you truly want to be. Now that's really something to wish for.") + ", '80'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May your heart beat true with the joy, love and laughter you bring to everyone around you.") + ", '81'), ('system'," + DatabaseUtils.sqlEscapeString("We were all made in God's image. When you smile, I can see God in your eyes, the window to your beautiful soul. Happy birthday.") + ", '82'), ('system'," + DatabaseUtils.sqlEscapeString("May God bless you all your days, and doubly so on your special day. Happy birthday!") + ", '83'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May God keep you young at heart and beautiful of mind.") + ", '84'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. It gives me great pleasure to see that God has blessed you with another year of life. May you be blessed with many more.") + ", '85'), ('system'," + DatabaseUtils.sqlEscapeString("Each birthday is a gift of love, joy and wonder from God. Celebrate to your heart's content and your soul's fulfilled. Happy birthday.") + ", '86'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May God bless you with years upon years of joy, health, wonder and faith!") + ", '87'), ('system'," + DatabaseUtils.sqlEscapeString("I hope your special day will bring you lots of happiness, love and fun. You deserve them a lot. Happy Birthday.") + ", '88'), ('system'," + DatabaseUtils.sqlEscapeString("Have a wonderful birthday. I wish your every day to be filled with lots of love, laughter, happiness and the warmth of sunshine.") + ", '89'), ('system'," + DatabaseUtils.sqlEscapeString("May your coming year surprise you with the happiness of smiles, the feeling of love and so on. I hope you will find plenty of sweet memories to cherish forever. Happy birthday.") + ", '90'), ('system'," + DatabaseUtils.sqlEscapeString("It’s your birthday. Now you’ve more grown up. Every year you’re becoming more perfect.") + ", '91'), ('system'," + DatabaseUtils.sqlEscapeString("On your special day, I wish you good luck. I hope this wonderful day will fill up your heart with joy and blessings. Have a fantastic birthday, celebrate the happiness on every day of your life. Happy Birthday!!") + ", '92'), ('system'," + DatabaseUtils.sqlEscapeString("May this birthday be filled with lots of happy hours and also your life with many happy birthdays, that are yet to come. Happy birthday.") + ", '93'), ('system'," + DatabaseUtils.sqlEscapeString("Let’s light the candles and celebrate this special day of your life. Happy birthday.") + ", '94'), ('system'," + DatabaseUtils.sqlEscapeString("You are very special and that’s why you need to float with lots of smiles on your lovely face. Happy birthday.") + ", '95'), ('system'," + DatabaseUtils.sqlEscapeString("Special day, special person and special celebration. May all your dreams and desires come true in this year. Happy birthday.") + ", '96'), ('system'," + DatabaseUtils.sqlEscapeString("You are a person who always deserves the best and obviously nothing less. Wish your birthday celebration will be as fantastic as you are. Happy birthday.") + ", '97'), ('system'," + DatabaseUtils.sqlEscapeString("Another birthday, so you are growing older gradually. But I find no change in you. You look perfect like before. Happy birthday.") + ", '98'), ('system'," + DatabaseUtils.sqlEscapeString("Happy birthday. May all the best things of the world happen in your life because you are definitely one of the best people too.") + ", '99'), ('system'," + DatabaseUtils.sqlEscapeString("Wishing happy birthday to one of the best persons I’ve ever met in this world.") + ", '100'), ('system'," + DatabaseUtils.sqlEscapeString("I feel proud when I call you my friend. I want to feel this today and every day.") + ", '101'), ('system'," + DatabaseUtils.sqlEscapeString("May all the best blessings rain upon you today and always. Happy birthday.") + ", '102'), ('system'," + DatabaseUtils.sqlEscapeString("Your birthday is more special to me than you, because on this day, one of the most precious friend of my entire life came into being. Happy Birthday.") + ", '103'), ('system'," + DatabaseUtils.sqlEscapeString("A birthday cake is always good, but to me a friend like you is undoubtedly great. Happy Birthday.") + ", '104'), ('system'," + DatabaseUtils.sqlEscapeString("My warmest wishes for the most wonderful friend I have. Happy Birthday.") + ", '105'), ('system'," + DatabaseUtils.sqlEscapeString("As you go through each year, remember to count your blessings, not your age. Count your amazing experiences, not your mistakes. Happy Birthday to an awesome person!") + ", '106'), ('system'," + DatabaseUtils.sqlEscapeString("On your birthday, look to the future and forget your past- the best is still to come. Happy birthday, friend.") + ", '107'), ('system'," + DatabaseUtils.sqlEscapeString("For you on your birthday, I wish you a lifetime of happiness, no worries, and a boat load of big dreams coming true. Happy Birthday.") + ", '108'), ('system'," + DatabaseUtils.sqlEscapeString("A birthday is one of the most important days of the year- may yours be filled with the light of living and the brightness of laughter. Happy Birthday.") + ", '109'), ('system'," + DatabaseUtils.sqlEscapeString("Words cannot express how happy I am to say ‘happy birthday’ to you another year of your life. You are so special to me.") + ", '110'), ('system'," + DatabaseUtils.sqlEscapeString("Roses may be red and violets blue, but you put all the colors in the rainbow to shame. Happy birthday to the brightest, best person I know!") + ", '111'), ('system'," + DatabaseUtils.sqlEscapeString("To a dear friend- thanks for always being there for me through the thick and the thin. You’re truly the best. Happy birthday to you!") + ", '112'), ('system'," + DatabaseUtils.sqlEscapeString("I know your birthday is a special day with or without me, but I have to say that you are the best friend I’ve ever had! Happy Birthday.") + ", '113'), ('system'," + DatabaseUtils.sqlEscapeString("A birthday is a time for celebration- it’s a time to celebrate the life of someone special. Here’s to you getting love, joy, and happiness on your special day. Happy Birthday.") + ", '114'), ('system'," + DatabaseUtils.sqlEscapeString("May this year be a breakthrough year for you! I hope that all your stars keep shining and your biggest dreams come true. Congrats on another great year. Happy Birthday.") + ", '115'), ('system'," + DatabaseUtils.sqlEscapeString("Every time you blow out a candle it marks another year you have contributed to the world with your humbling presence. Thanks for that and happy birthday!") + ", '116'), ('system'," + DatabaseUtils.sqlEscapeString("Another year older means another year wiser, and hotter. Glad you were born to share your wisdom. Happy birthday.") + ", '117'), ('system'," + DatabaseUtils.sqlEscapeString("I guess you thought I forgot about today. How could I ever forget a day as special as a good friend’s birthday! I want to send you a Happy Birthday and remind you of how important your friendship is to me.") + ", '118'), ('system'," + DatabaseUtils.sqlEscapeString("I have been looking for a decent gift to give you on this special day of yours but to no avail. I guess this is simply because there is no way on earth I could ever get a gift as special as you. Happy Birthday.") + ", '119'), ('system'," + DatabaseUtils.sqlEscapeString("No matter how old you get, always remember to stay young at heart and make every year count. Happy Birthday to you!") + ", '120'), ('system'," + DatabaseUtils.sqlEscapeString("No matter how old you get, always remember to stay young at heart and make every year count. Happy Birthday to you!") + ", '121'), ('system'," + DatabaseUtils.sqlEscapeString("May your heart always guide you to find the truest form of happiness- because you deserve it all. Happy Birthday.") + ", '122'), ('system'," + DatabaseUtils.sqlEscapeString("May the brightest star always light up your life path. Happy Birthday.") + ", '123'), ('system'," + DatabaseUtils.sqlEscapeString("Life is a great journey, so make sure you enjoy every mile. Happy Birthday!") + ", '124'), ('system'," + DatabaseUtils.sqlEscapeString("Our lives may have changed, but our friendship has stayed strong. Happy Birthday! May all your heart’s desires come true on your special day!") + ", '125'), ('system'," + DatabaseUtils.sqlEscapeString("This is your special day and I hope you see this day as more than just a reminder that you are getting older, but as an amazing opportunity for you to gather with great friends, have fun and relive all the amazing memories of the previous years! Happy birthday to you, dear!") + ", '126'), ('system'," + DatabaseUtils.sqlEscapeString("Dance. Party. Celebrate. Sing. Shake it. Why not? It’s your birthday!") + ", '127'), ('system'," + DatabaseUtils.sqlEscapeString("Hoping you have a wonderful birthday filled with love and laughter!") + ", '128'), ('system'," + DatabaseUtils.sqlEscapeString("Celebrate your birthday. Celebrate today. But most of all, be happy every day! Happy Birthday.") + ", '129'), ('system'," + DatabaseUtils.sqlEscapeString("Birthday brings more blessing. I wish you all the joy and happiness your heart can accommodate. Happy Birthday.") + ", '130') ");
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Start");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("Next");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
