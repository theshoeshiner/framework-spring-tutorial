package org.vaadin.spring.tutorial;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.salesforce.canvas.filter.CanvasFilter;
import com.salesforce.canvas.filter.CanvasRequest;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
@SpringUI
//@SpringViewDisplay
@JavaScript({ "canvas-all.js", "json2.js" })
public class MyUI extends UI {

	public static final Logger LOGGER = LoggerFactory.getLogger(MyUI.class);

	private Panel springViewDisplay;

	public MyUI() {
		super();
		//LOGGER.info("instantiate UI: {}",this);
	}

	@Override
	protected void init(VaadinRequest request) {

		LOGGER.info("init UI: {}", this);

		final VerticalLayout root = new VerticalLayout();
		//root.setSizeFull();
		root.setMargin(true);
		//root.setSpacing(true);
		root.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		setContent(root);
		root.setSizeUndefined();
		Label header = new Label("Canvas Filter Test");
		header.addStyleName("h1");
		root.addComponent(header);
		//root.setExpandRatio(header, 0);
		
		

		/*final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		navigationBar.addComponent(createNavigationButton("UI Scoped View",
		        UIScopedView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("View Scoped View",
		        ViewScopedView.VIEW_NAME));
		root.addComponent(navigationBar);
		
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		root.addComponent(springViewDisplay);
		root.setExpandRatio(springViewDisplay, 1.0f);*/

		//System.out.println("init vaadin ui");

		//Page.getCurrent().getJavaScript().

		//LOGGER.info("Vaadin request: {}",request);

		HttpServletRequest servlet = VaadinServletService.getCurrentServletRequest();

		HttpSession session = servlet.getSession();

		root.addComponent(new Label("Session Id: " + session.getId()));

		CanvasRequest canvas = (CanvasRequest) servlet.getSession().getAttribute(CanvasFilter.CANVAS_CONTEXT_ATT);

		LOGGER.info("CanvasRequest: {}", canvas);
		
		JsonNode node = (JsonNode) servlet.getSession().getAttribute(CanvasFilter.CANVAS_CONTEXT_JSON_ATT);

		/*Object canvas = request.getAttribute(CanvasFilter.CANVAS_CONTEXT_ATT);
		
		LOGGER.info("canvas context 1: {}",canvas);
		
		canvas = servlet.getAttribute(CanvasFilter.CANVAS_CONTEXT_ATT);
		
		LOGGER.info("canvas context 2: {}",canvas);*/
		
		//VerticalLayout object =new VerticalLayout();
		
		Label h2 = new Label("Canvas Context");
		h2.addStyleName("h2");
		root.addComponent(h2);
		VerticalLayout object = addObjectProperties(null, node);
		root.addComponent(object);
	}

	/*public void addObjectProperty(VerticalLayout layout,JsonNode node, String prop) {
		
		JsonNode sub;
		if(prop == null) sub = node;
		else sub = node.get(prop);
		
		if(node.isObject()) {
			Button b = new Button(prop);
			layout.addComponent(b);
			VerticalLayout propLayout = new VerticalLayout();
			propLayout.setMargin(true);
			for(Iterator<String> it= sub.fieldNames();it.hasNext();) {
				String name = it.next();
				addObjectProperty(propLayout, sub, name);
				
			}
		
		}
		else {
			Label l = new Label(prop+": "+sub.asText());
			layout.addComponent(l);
		}
	}*/

	public VerticalLayout addObjectProperties(VerticalLayout pl, JsonNode node) {
		//Button b = new Button(prop);
		//layout.addComponent(b);

		VerticalLayout propLayout = new VerticalLayout();
		//layout.addComponent(propLayout);

		//propLayout.setMargin(true);
		propLayout.setMargin(new MarginInfo(false,true));
		for (Iterator<String> it = node.fieldNames(); it.hasNext();) {
			String name = it.next();
			JsonNode sub = node.get(name);

			if (sub.isObject()) {
				Button b = new Button(name);
				b.addStyleName(ValoTheme.BUTTON_LINK);
				propLayout.addComponent(b);
				VerticalLayout subs = addObjectProperties(propLayout, sub);
				propLayout.addComponent(subs);
				subs.setVisible(false);
				b.addClickListener(click -> {
					subs.setVisible(!subs.isVisible());
				});
			} else {
				Label l = new Label(name + ": " + sub.asText());
				propLayout.addComponent(l);
			}

			//addObjectProperty(propLayout, node, name);
		}
		return propLayout;
	}

	public static class ObjectProperties {

	}

	/*private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		// If you didn't choose Java 8 when creating the project, convert this
		// to an anonymous listener class
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}*/

	/*@Override
	public void showView(View view) {
	    springViewDisplay.setContent((Component) view);
	}*/
}
