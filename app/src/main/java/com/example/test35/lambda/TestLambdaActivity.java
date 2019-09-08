package com.example.test35.lambda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.test35.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class TestLambdaActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lambda);

        toastMsg("静态方法引用1", str -> { });
        toastMsg("静态方法引用2", str -> {

        });
        toastMsg("静态方法引用3", str -> Toast.makeText(TestLambdaActivity.this,str,Toast.LENGTH_SHORT).show());

        /**
         * 静态方法引用：
         * 格式 ：类名::方法名
         * 1、被引用的方法参数列表和函数式接口中抽象方法的参数一致 eg: println（String str）的方法参数和Test中的run（String str）
         * 2、接口的抽象方法没有返回值，引用的方法可以有返回值也可以没有
         * 3、接口的抽象方法有返回值，引用的方法必须有相同类型的返回值
         *
         * 分别调用System.out类的println静态方法，Integer类的parseInt静态方法,由于满足抽象参数列表与引用参数列表相同，所以可以写成静态方法引用的格式
         */
        toastMsg("静态方法引用4",(s)->System.out.println(s));
        toastMsg("静态方法引用4",System.out::println);
        getInt("1314", str -> Integer.parseInt(str));
        getInt("1314",  Integer::parseInt);

        /**
         * 对象方法引用：同静态方法引用
         * 格式 ：对象名::非静态方法名
         *类中有一个方法goWalking()方法体是test3实现类对象需要的方法体
         * 且方法列表参数一致，返回值类型相同
         * 则可以利用lambda创建test的实现类对象，然后重写的抽象方法体就是调用Person对象的goWalking方法
         * 符合对象引用方法的所有要求，则可以写成t2的样式
         */
        Test3 test3 = str -> new Person().goWalking(str);
        test3=new Person()::goWalking;

        /**
         * 构造方法引用:
         * 对象名::非静态方法名
         *由于函数式接口test中抽象方法，返回值是Person对象，且参数列表与Person类中的构造方法相同
         * 则可以通过创建函数式接口的实现类对象，方法体通过调用类中的构造方法创建对象
         * 使用了构造方法引用写成了代码中t2的形式
         */
        Test4 test4=str->new Person1(str);
        test4=Person1::new;

        /**
         * 数组构造方法引用
         * 格式：数据类型[ ]::new
         *
         */

        Test5 test5=length -> new String[length];
        test5=String[]::new;

        /**
         * 特定类型的方法引用
         * 类名::非静态方法
         * 特定类型方法引用，在Comparator函数式接口的抽象方法中传入的参数有两个，
         * 可是compareToIgnoreCase()方法参数只有一个，第一个传入的参数作调用对象
         * 这就满足了特定类型的方法引用，所以可以简化成类名::非静态方法的形式
         */
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"maria","kangkang","lilei","hanmeimei");
        Collections.sort(list, (o1, o2) -> o1.compareToIgnoreCase(o2));
        Collections.sort(list,String::compareToIgnoreCase);

        /**
         * 类中方法调用父类或本类方法引用
         * 格式：this::方法名   super::方法名
         *
         *
         * 在有继承关系的类中，若方法想调用本类或父类的成员方法
         * 在函数式接口抽象方法与成员方法参数列表相同，且返回值类型相同的情况下
         * 也可以使用this和super的方法引用来简写原本的lambda代码
         */


    }
    public void test1(){
        /**
         * 函数式接口：有且仅有一个抽象方法的接口，就叫函数式接口.常用@FunctionalInterface标签标示
         * 格式：(抽象方法的参数)->{抽放方法的方法体};
         * 1、若抽象方法参数仅有一个时可以省略括号()
         * 2、若抽象方法的方法体只有一条时，可以省略大括号{}和return;
         *
         */
        Thread thread = new Thread(() -> {
        });
        Test test =str-> Toast.makeText(TestLambdaActivity.this, str, Toast.LENGTH_SHORT).show();
    }
    class Person {

        public String goWalking(String string) {
            return string.concat(" 引用方法");
        }
    }
    class Person1 {
        String name;
        public Person1(String name){
            this.name=name;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString();

        }
    }
    public static void toastMsg(String str,Test test){
        test.run(str);
    }
    public static Integer getInt(String str,Test2 test){
        return test.run(str);

    }


    interface test {
        public void itMethod();
    }

    class father {
        public void buy() {
            System.out.println("买东西");
        }
    }

    class son extends father {
        public void buy() {
            System.out.println("买糖");
        }

        public void test() {

// 实质代码:       test t = () -> buy();
            test t = this::buy;
            t.itMethod();

// 实质代码:        test t2 = ()->super.buy();
            test t2 = super::buy;
            t2.itMethod();
        }
    }


}
