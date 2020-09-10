package org.vaadin.spring.tutorial;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
@SpringUI
@SpringViewDisplay
@JavaScript ({ "canvas-all.js","json2.js"})
public class MyUI extends UI implements ViewDisplay {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MyUI.class);

    private Panel springViewDisplay;
    
    public MyUI() {
		super();
		LOGGER.info("instantiate UI: {}",this);
	}

    @Override
    protected void init(VaadinRequest request) {
    	
    	LOGGER.info("init UI: {}",this);
    
    	
    	
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("UI Scoped View",
                UIScopedView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("View Scoped View",
                ViewScopedView.VIEW_NAME));
        root.addComponent(navigationBar);

        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);
        
        System.out.println("init vaadin ui");
        
        //Page.getCurrent().getJavaScript().
        
        LOGGER.info("Vaadin request: {}",request);
        
        HttpServletRequest servlet = VaadinServletService.getCurrentServletRequest();
        
        Object canvas = request.getAttribute(CanvasFilter.CANVAS_CONTEXT_ATT);
        
        LOGGER.info("canvas context 1: {}",canvas);
        
        canvas = servlet.getAttribute(CanvasFilter.CANVAS_CONTEXT_ATT);
        
        LOGGER.info("canvas context 2: {}",canvas);
        
    }

    private Button createNavigationButton(String caption,
            final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        // If you didn't choose Java 8 when creating the project, convert this
        // to an anonymous listener class
        button.addClickListener(
                event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    @Override
    public void showView(View view) {
        springViewDisplay.setContent((Component) view);
    }
}
