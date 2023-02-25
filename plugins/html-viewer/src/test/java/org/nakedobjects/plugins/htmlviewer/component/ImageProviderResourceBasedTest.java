package org.nakedobjects.plugins.htmlviewer.component;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.plugins.html.image.ImageProviderResourceBased;


public class ImageProviderResourceBasedTest {
    
    private ImageProviderResourceBased imageProvider;

    @Before
    public void setUp() {
        imageProvider = new ImageProviderResourceBased();
    }
    
    @After
    public void tearDown() {
        imageProvider = null;
    }
    
    @Test
    public void canFindDefaultExplicitly() {
        assertThat(imageProvider.image("Default"), equalTo("images/Default.png"));
    }
    
    @Test
    public void nonExistentImageUsesDefault() {
        assertThat(imageProvider.image("NonExistent"), equalTo("images/Default.png"));
    }
    
    @Test
    public void nonDefaultPngExistingImageIsReturned() {
        assertThat(imageProvider.image("Service"), equalTo("images/Service.png"));
    }
    
    @Test
    public void nonDefaultGifExistingImageIsReturned() {
        assertThat(imageProvider.image("Customer"), equalTo("images/Customer.gif"));
    }
    

}


// Copyright (c) Naked Objects Group Ltd.
