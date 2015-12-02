package edu.unh.cs.cs619_2015_project2.g9.util;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.androidannotations.annotations.EBean;

/**
 * Single pool, singleton object bus
 * @Author Chris Sleys
 */
@EBean(scope = EBean.Scope.Singleton)
public class OttoBus extends Bus {
    public OttoBus() {
        super(ThreadEnforcer.MAIN);
    }
}