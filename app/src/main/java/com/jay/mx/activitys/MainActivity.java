package com.jay.mx.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jay.mx.R;
import com.jay.mx.adapters.MyRecyclerAdapter;
import com.jay.mx.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseTitleActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //如何将一个数组转化为List，下面给出了方法：
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.function_1));
        //为什么一个匿名内部类的参数必须是final的？下面给出解释：
        //具体参考这里：http://feiyeguohai.iteye.com/blog/1500108
        //由于匿名内部类在方法中，方法的生命周期要比匿名内部类的短。
        //可能出现的状况就是：方法调用完毕，方法中的局部变量也销毁，但是该方法中的匿名内部类还是存在着的。
        //就会出现一个问题：这个匿名内部类调用了一个已经不存在的变量。
        //如何解决这个问题：
        //首先，基本类型是不会出现这个问题的，因为基本类型都是按值传递的。
        //复杂类型如何解决：
        //当方法中的局部变量向匿名内部类传参数时，并不是直接按照引用传递，而是在匿名内部类中复制一个局部变量的副本。这样即使局部变量被销毁也无妨，因为现在匿名内部类调用的是局部变量的副本。
        //但这种解决方案还是会出现一个问题：如果局部变量在复制之后发生修改了怎么办？这样匿名内部类中的副本和修改过得局部变量就不能划等号了，如何解决？
        //很简单，把这个局部变量定义为final即可。
        //总结:核心的问题是:怎么才能使得:访问"复制品"与访问真正的原始的局部变量,其语义效果是一样的呢?


        //Java中的参数传递？
        //以前学C++,知道：C++中有两种参数传递方式：
        //按值传递和按引用传递，解释下这两种传值方式：
        //按值传递：只是简单地将参数的值传递过来，传递的是值的拷贝，之后这个拷贝过来的值如何变化，对原来的值没有任何影响
        //按引用传递：传递的是引用的地址，传递到方法中的值的修改也会导致原来值的变化

        //Java中都是按值传递，即传递的都是值的拷贝。
        //对于基本类型来说，很好理解，不管传进去的值如何变化，原先的值不会变，因为传递的是拷贝嘛。
        //但是对于引用类型，就会有变化。对于引用类型来说，传递的拷贝其实这个变量的地址，所以效果上和C++中的按引用传递是一样的

        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, list);
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, list.get(position) + " clicked!");
                //在这里添加Item 点击事件

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
