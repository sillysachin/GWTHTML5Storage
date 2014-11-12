package com.appbootup.explore.gwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTHTML5Storage implements EntryPoint
{
	private final GreetingServiceAsync greetingService = GWT.create( GreetingService.class );

	private Storage cacheStore = null;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{

		cacheStore = Storage.getLocalStorageIfSupported();
		Window.addWindowClosingHandler( new Window.ClosingHandler()
		{
			@Override
			public void onWindowClosing( ClosingEvent event )
			{
				Window.alert( "onWindowClosing" );
			}
		} );

		Window.addCloseHandler( new CloseHandler<Window>()
		{
			@Override
			public void onClose( CloseEvent<Window> event )
			{
				//Execute code when window closes!
				Window.alert( "onClose" );
			}
		} );
		FlowPanel loadListener = new FlowPanel();
		loadListener.setSize( "0", "0" );
		loadListener.addAttachHandler( new AttachEvent.Handler()
		{
			@Override
			public void onAttachOrDetach( AttachEvent event )
			{
				boolean attached = event.isAttached();
				if ( attached )
				{
					Window.alert( "onLoad" );
					String timeOfUnload = cacheStore.getItem( "unloadEventFlag" );
					Date oldDate = new Date( Long.parseLong( timeOfUnload ) );
					Date newDate = new Date();
					Long duration = newDate.getTime() - oldDate.getTime();
					if ( duration < 10 * 1000 )
					{
						//less than 10 seconds since the previous Unload event => it's a browser reload (so cancel the disconnection request)
						askServerToCancelDisconnectionRequest(); // asynchronous AJAX call
					}
					else
					{
						// last unload event was for a tab/window close => do whatever you want (I do nothing here)
					}
				}
				else
				{
					Long time = new Date().getTime();
					cacheStore.setItem( "unloadEventFlag", time.toString() );
					// notify the server that we want to disconnect the used in a few seconds (I used 5 seconds)
					askServerToDisconnectUserInAFewSeconds(); // synchronous AJAX call
				}
			}
		} );
		RootLayoutPanel.get().add( loadListener );
	}

	protected void askServerToDisconnectUserInAFewSeconds()
	{
		// TODO:
	}

	protected void askServerToCancelDisconnectionRequest()
	{
		// TODO:
	}
}