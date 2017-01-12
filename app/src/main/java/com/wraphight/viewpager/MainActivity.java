package com.wraphight.viewpager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewpager;
    private String[] urls = {
            "http://f.hiphotos.baidu.com/zhidao/pic/item/3b87e950352ac65cbdbeff61fcf2b21193138a6d.jpg"
            , "http://c.hiphotos.baidu.com/zhidao/pic/item/562c11dfa9ec8a1359aa88b6f103918fa0ecc030.jpg",
            "http://c.hiphotos.baidu.com/zhidao/pic/item/faf2b2119313b07e6077d3bc0ad7912396dd8cb8.jpg"
    };
    private int[] imgheights;
    private int screenWidth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenWidth=ScreenUtil.getScreenWidth(this);
        initView();


    }
    public void initView() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
//        mViewpager.setOffscreenPageLimit(3);
        Glide.with(this).load(urls[0]).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                float scale = (float) resource.getHeight() / resource.getWidth();
                int defaultheight = (int) (scale * screenWidth);
                initViewPager(defaultheight);
            }
        });

    }
   //获取第一张图片高度后，给viewpager设置adapter
    private void initViewPager(final int defaultheight) {
        mViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                if (imgheights == null || imgheights.length != urls.length){
                    imgheights = null;
                    imgheights = new int[urls.length];}
                return urls.length;
            }


            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                final ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(getApplicationContext()).load(urls[position]).asBitmap().into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    protected void setResource(Bitmap loadedImage) {
                        if(loadedImage!=null) {
                            float scale = (float) loadedImage.getHeight() / loadedImage.getWidth();
                            imgheights[position] = (int) (scale * screenWidth);
                            imageView.setImageBitmap(loadedImage);
                        }else {
                            Toast.makeText(MainActivity.this, "图片为空", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });


        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == imgheights.length - 1) {
                    return;
                }

                //计算ViewPager现在应该的高度,heights[]表示页面高度的数组。
                int height = (int) ((imgheights[position] == 0 ? defaultheight : imgheights[position])
                        * (1 - positionOffset) +
                        (imgheights[position + 1] == 0 ? defaultheight : imgheights[position + 1])
                                * positionOffset);

                //为ViewPager设置高度
                ViewGroup.LayoutParams params = mViewpager.getLayoutParams();
                params.height = height;
                mViewpager.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
