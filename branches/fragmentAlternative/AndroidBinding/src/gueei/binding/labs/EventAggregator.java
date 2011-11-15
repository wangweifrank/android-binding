package gueei.binding.labs;

import gueei.binding.utility.WeakList;

import java.util.HashMap;

import android.os.Bundle;

public class EventAggregator {
	private static HashMap<String, WeakList<EventSubscriber>>
		EventNameSubscribersMap = new HashMap<String, WeakList<EventSubscriber>>();
	
	public static void publish(final String eventName, final Object publisher, final Bundle data){
		WeakList<EventSubscriber> subscribers = EventNameSubscribersMap.get(eventName);
		
		// Nobody subscribes this
		if (subscribers==null) return;
		
		int count = subscribers.size();
		
		// All Subscribers removed (maybe GCed)
		if (count==0){
			subscribers.remove(eventName);
			return;
		}
		
		for (int i=0; i<count; i++){
			EventSubscriber subscriber = subscribers.get(i);
			if (subscriber!=null) subscriber.onEventTriggered(eventName, publisher, data);
		}
	}
	
	public static void subscribe(final String eventName, final EventSubscriber subscriber){
		WeakList<EventSubscriber> subscribers = EventNameSubscribersMap.get(eventName);
		if (subscribers==null){
			subscribers = new WeakList<EventSubscriber>();
			EventNameSubscribersMap.put(eventName, subscribers);
		}
		
		subscribers.add(subscriber);
	}
}
