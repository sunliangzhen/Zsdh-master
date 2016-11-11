package com.shuxiangbaima.task.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.shuxiangbaima.task.R;
import com.toocms.dink5.mylibrary.app.AppManager;
import com.toocms.dink5.mylibrary.commonutils.PreferencesUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * @date 2016/4/9 12:55
 */
public class SplashAty extends Activity {

    @ViewInject(R.id.linlay_splash)
    private LinearLayout linlay_splash;

    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_splash);
        x.view().inject(this);
        myCountDownTimer = new MyCountDownTimer();
        AppManager.getInstance().addActivity(this);
        myCountDownTimer.start();
//        Intent intent = new Intent(SplashAty.this, staService.class);
//        startService(intent);

//        Student student = new Student();
//        student.course.add(new Course("111"));
//        student.course.add(new Course("122"));
//
//        Student student2 = new Student();
//        student2.course.add(new Course("211"));
//        student2.course.add(new Course("222"));
////
//        PublishSubject<Integer> subject = PublishSubject.create();
//        subject.onNext(1);
//        subject.subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//                LogUtil.e(integer + "ppppppppppppp");
//            }
//        });
//        subject.onNext(2);
//        subject.onNext(3);
//        subject.onNext(4);

//        Student[] students = {student, student2};
//        Subscriber<Course> subscriber = new Subscriber<Course>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Course course) {
//                LogUtil.e(course.getName() + ",,,,,,,,,,,,,,,,");
//            }
//        };
//        Observable.from(students)
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        return Observable.from(student.getCourses());
//                    }
//                })
//                .subscribe(subscriber);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().killActivity(this);
        myCountDownTimer.cancel();
    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer() {
            super(3000, 1000);
        }

        public void onFinish() {
            if (!PreferencesUtils.getBoolean(SplashAty.this, "FirstG0")) {
                Intent intent1 = new Intent(SplashAty.this, GuideAty.class);
                startActivity(intent1);
                finish();
            } else {
                Intent intent1 = new Intent(SplashAty.this, MainAty.class);
                startActivity(intent1);
                finish();
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }
}
