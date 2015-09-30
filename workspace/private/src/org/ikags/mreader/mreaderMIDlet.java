package org.ikags.mreader;


import org.ikags.core.SystemMidlet;

/**
 * I.K.A Engine midlet simple
 * 
 * @author http://airzhangfish.spaces.live.com
 * @since 2009.5.20
 * @version 0.6
 */
public class mreaderMIDlet extends SystemMidlet
{

    public mreaderMIDlet()
    {
        this.init(DrawCanvas.getInstance());
    }
}
