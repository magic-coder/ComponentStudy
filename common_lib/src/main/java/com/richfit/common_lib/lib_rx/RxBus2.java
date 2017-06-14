package com.richfit.common_lib.lib_rx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by monday on 2017/6/14.
 */

class RxBus2 {

    public static RxBus2 instance;

    private RxBus2() {
    }

    public static RxBus2 getInstance() {
        if (instance == null) {
            instance = new RxBus2();
        }
        return instance;
    }

    /*保存事件,这里采用subject,因为它实现了Observable和Subscribe接口。*/
    private ConcurrentHashMap<Object, ArrayList<PublishProcessor>> subjectsMap = new ConcurrentHashMap<>();


    /**
     * 注册事件
     *
     * @param tag:事件集合的tag
     */
    public <T> Flowable<T> register(Object tag) {
        /*获取tag对应注册的事件集合*/
        ArrayList<PublishProcessor> subjects = subjectsMap.get(tag);
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        subjectsMap.put(tag, subjects);
        /*生成事件*/
        PublishProcessor<T> subject;
        //这里采用PublishSubject，该主题的特点是当有新的订阅者时立即将事件发射出去。
        subject = PublishProcessor.create();
        subjects.add(subject);
        return subject;
    }

    public <T> Flowable<T> register(Object tag, Class<T> clazz) {
        /*获取tag对应注册的事件集合*/
        ArrayList<PublishProcessor> subjects = subjectsMap.get(tag);
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        subjectsMap.put(tag, subjects);
        /*生成事件*/
        PublishProcessor<T> subject;
        //这里采用PublishSubject，该主题的特点是当有新的订阅者时立即将事件发射出去。
        subject = PublishProcessor.create();
        subjects.add(subject);
        return subject.ofType(clazz);
    }

    /**
     * 反注册tag的事件集合
     *
     * @param tag:事件集合的tag
     */
    public void unRegister(Object tag) {
        ArrayList<PublishProcessor> subjects = subjectsMap.get(tag);
        if (subjects != null) {
            /*说明已经注册过了*/
            subjectsMap.remove(tag);
        }
    }

    /**
     * 反注册事件集合的一个具体事件
     *
     * @param tag：事件集合的tag
     * @param flowable：需要反注册的具体事件
     * @return
     */
    public RxBus2 unRegister(Object tag, Flowable<?> flowable) {
        if (flowable == null) {
            return getInstance();
        }
        ArrayList<PublishProcessor> subjects = subjectsMap.get(tag);
        if (subjects != null) {
            //先取出tag对应的事件集合，然后remove该事件集合的observable事件
            subjects.remove(flowable);
            if (isEmpty(subjects)) {
                //如果该事件集合没有事件，移除tag注册的事件
                subjectsMap.remove(tag);
            }
        }
        return getInstance();
    }

    public void post(Object event) {
        post(event.getClass().getName(), event);
    }

    /**
     * 发送事件.注意只要注册的tag的事件，那么都能够接收到event事件
     *
     * @param tag
     * @param event:发送的事件
     */
    public void post(Object tag, Object event) {
        if (subjectsMap != null && subjectsMap.size() > 0) {
            ArrayList<PublishProcessor> subjectList = subjectsMap.get(tag);
            if (subjectList != null && subjectList.size() > 0) {
                for (PublishProcessor subject : subjectList) {
                    if (subject != null && event != null)
                        subject.onNext(event);
                }
            }
        }
    }

    /**
     * 判断List集合是否为空
     */
    private boolean isEmpty(Collection<PublishProcessor> collection) {
        return null != collection && collection.isEmpty();
    }

}
