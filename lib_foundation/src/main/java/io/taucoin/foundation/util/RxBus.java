package io.taucoin.foundation.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();
    private static volatile RxBus instance;

    private RxBus() {
    }

    public static RxBus getInstance() {
        RxBus inst = instance;
        if (inst == null) {
            synchronized (RxBus.class) {
                inst = instance;
                if (inst == null) {
                    inst = new RxBus();
                    instance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * register RxBus
     */
    public <T> Observable<T> register(@NonNull Class<T> tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T> subject = PublishSubject.create();
        subjectList.add(subject);
        return subject;
    }

    /**
     * unregister RxBus
     *
     */
    public <T> void unregister(@NonNull Class<T> tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(tag);
            }
        }
    }

    /**
     * send message
     */
    public void post(@NonNull Object event) {
        List<Subject> subjectList = subjectMapper.get(event.getClass());
        if (subjectList != null && !subjectList.isEmpty()) {
            for (Subject subject : subjectList) {
                subject.onNext(event);
            }
        }
    }

    /**
     * clear subject
     */
    public void clear() {
        if (subjectMapper.isEmpty()) {
            return;
        }
        subjectMapper.clear();
    }
}