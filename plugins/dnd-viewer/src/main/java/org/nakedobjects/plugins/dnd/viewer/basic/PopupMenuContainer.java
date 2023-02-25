package org.nakedobjects.plugins.dnd.viewer.basic;

import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Vector;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.MenuOptions;
import org.nakedobjects.plugins.dnd.NullContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.debug.DebugOption;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.simple.AbstractView;

public class PopupMenuContainer extends AbstractView {
    

    private static final int MENU_OVERLAP = 4;
    private static final UserAction DEBUG_OPTION = new DebugOption();
    private PopupMenu menu;
    private PopupMenu submenu;
    private Color backgroundColor;
    private View target;
    private final Vector options = new Vector();
    private final Location at;
    private boolean isLayoutInvalid;
    
    public PopupMenuContainer(View target, Location at) {
        super(new NullContent(), null, null);
        this.target = target;
        this.at = at;
        setLocation(at);
        isLayoutInvalid = true;
    }



    @Override
    public void debug(final DebugString debug) {
        super.debug(debug);
        debug.appendTitle("Submenu");
        debug.append(submenu);
        debug.append("\n");
    }

    @Override
    public void dispose() {
        if (getParent() == null) {
            super.dispose();
            getViewManager().clearOverlayView(this);
        } else {
            getParent().dispose();
        }
    }

    @Override
    public Size getMaximumSize() {
        final Size size = menu.getMaximumSize();
        if (submenu != null) {
            final Size subviewSize = submenu.getMaximumSize();
            size.extendWidth(subviewSize.getWidth() - MENU_OVERLAP);
            size.ensureHeight(submenuOffset() + subviewSize.getHeight());
        }
        return size;
    }
    
    @Override
    public void layout(final Size maximumSize) {
        if (isLayoutInvalid) {
            menu.layout(maximumSize);
            Size menuSize = menu.getMaximumSize();
            menu.setSize(menuSize);
            menu.setLocation(new Location(0,0));
            
            Location containerLocation = new Location(at);
            Size bounds = getViewManager().getOverlaySize();
            if (containerLocation.getX() < 0) {
                containerLocation.setX(0);
            } else if (containerLocation.getX() + menuSize.getWidth() > bounds.getWidth()) {
                containerLocation.setX(bounds.getWidth() -  menuSize.getWidth());
            }
            
            if (containerLocation.getY() < 0) {
                containerLocation.setY(0);
            } else if (containerLocation.getY() + menuSize.getHeight() > bounds.getHeight()) {
                containerLocation.setY(bounds.getHeight() -  menuSize.getHeight());
            }
            
            if (submenu != null) {
                submenu.layout(maximumSize);
                Size submenuSize = submenu.getMaximumSize();
                submenu.setSize(submenuSize);

                
                int submenuOffset = submenuOffset();
                Location menuLocation = new Location();
                
                int containerBottom = containerLocation.getY() + submenuOffset + submenuSize.getHeight();
                if (containerBottom > bounds.getHeight()) {
                    int overstretch = containerBottom - bounds.getHeight();
                    submenuOffset  -= overstretch;
                }
                Location submenuLocation = new Location(0, submenuOffset);

                boolean placeToLeft = at.getX() + menuSize.getWidth() + submenuSize.getWidth() < getViewManager().getOverlaySize().getWidth();
                if (placeToLeft) {
                    submenuLocation.setX(menuSize.getWidth() - MENU_OVERLAP);
                } else {
                    menuLocation.setX(submenuSize.getWidth() - MENU_OVERLAP);
                    containerLocation.move(-submenu.getSize().getWidth() + MENU_OVERLAP, 0);
                }

                if (containerLocation.getY() + menuSize.getHeight() > bounds.getHeight()) {
                    containerLocation.setY(bounds.getHeight() -  menuSize.getHeight());
                }
                
                
                submenu.setLocation(submenuLocation); //// !
                menu.setLocation(menuLocation); /// !

            }           
            
            
            setLocation(containerLocation);

        }
    }



    private int submenuOffset() {
        return menu.getOptionPostion();
    }  

    @Override
    public void mouseMoved(final Location at) {
        if (menu.getBounds().contains(at)) {
            at.subtract(menu.getLocation());
            menu.mouseMoved(at);
        } else  if (submenu  != null && submenu.getBounds().contains(at)) {
             at.subtract(submenu.getLocation());
            submenu.mouseMoved(at);
        }
    }


    public void show(final boolean forView, final boolean includeDebug, final boolean includeExploration) {
        final boolean withExploration = getViewManager().isRunningAsExploration() && includeExploration;

        final UserActionSet optionSet = new UserActionSet(withExploration, includeDebug, UserAction.USER);
        if (forView) {
            target.viewMenuOptions(optionSet);
        } else {
            target.contentMenuOptions(optionSet);
        }
        optionSet.add(DEBUG_OPTION);
        final Enumeration e = options.elements();
        while (e.hasMoreElements()) {
            final MenuOptions element = (MenuOptions) e.nextElement();
            element.menuOptions(optionSet);
        }

        menu = new PopupMenu(this);
        
        backgroundColor = optionSet.getColor();
        menu.show(target, optionSet.getMenuOptions(), backgroundColor);
        getViewManager().setOverlayView(this);
 
        final String status = changeStatus(target, forView, withExploration, includeDebug);
        getFeedbackManager().setViewDetail(status);
    }


    private String changeStatus(
            final View over,
            final boolean forView,
            final boolean includeExploration,
            final boolean includeDebug) {
        final StringBuffer status = new StringBuffer("Menu for ");
        if (forView) {
            status.append("view ");
            status.append(over.getSpecification().getName());
        } else {
            status.append("object");
            final Content content = over.getContent();
            if (content != null) {
                status.append(" '");
                status.append(content.title());
                status.append("'");
            }

        }
        if (includeDebug || includeExploration) {
            status.append(" (includes ");
            if (includeExploration) {
                status.append("exploration");
            }
            if (includeDebug) {
                if (includeExploration) {
                    status.append(" & ");
                }
                status.append("debug");
            }
            status.append(" options)");
        }
        return status.toString();
    }


    public void addMenuOptions(final MenuOptions options) {
        this.options.addElement(options);
    }

    void openSubmenu(final UserAction[] options) {
        markDamaged();

        submenu = new PopupMenu(this);
        submenu.setParent(this);
        submenu.show(target, options, backgroundColor);
        invalidateLayout();
        final Size size = getMaximumSize();
        setSize(size);
        layout(size);
        
        isLayoutInvalid = false;

        
        
        markDamaged();
    }

    

    @Override
    public void keyPressed(final KeyboardAction key) {
        if (submenu != null) {
            final int keyCode = key.getKeyCode();
            if (keyCode == KeyEvent.VK_ESCAPE) {
                markDamaged();
                invalidateLayout();
                submenu = null;
                key.consume();

            } else if (getParent() != null && keyCode == KeyEvent.VK_LEFT) {
                markDamaged();
                invalidateLayout();
                submenu = null;
                key.consume();
            } else {
                submenu.keyPressed(key);
            }
        } else {
            menu.keyPressed(key);
        }
    }
    
    public void invalidateLayout() {
        isLayoutInvalid = true;
    }
    
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (menu != null) {
            final Canvas menuCanvas = canvas.createSubcanvas(menu.getBounds());
            menu.draw(menuCanvas);
        }
        if (submenu != null) {
            final Canvas submenuCanvas = canvas.createSubcanvas(submenu.getBounds());
            submenu.draw(submenuCanvas);
        }

        if (Toolkit.debug) {
            canvas.drawRectangleAround(this, Toolkit.getColor(ColorsAndFonts.COLOR_DEBUG_BOUNDS_VIEW));
        }
    }
    
    @Override
    public void firstClick(final Click click) {
        Location location = click.getLocation();
        if (menu.getBounds().contains(location)) {
            click.subtract(menu.getLocation());
            menu.firstClick(click);
        } else if (submenu != null && submenu.getBounds().contains(location)) {
            click.subtract(submenu.getLocation());
            submenu.firstClick(click);
        }
    }

}


// Copyright (c) Naked Objects Group Ltd.
