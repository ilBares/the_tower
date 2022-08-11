package it.unibs.pajc.baresi.graphic.ui;

import it.unibs.pajc.baresi.graphic.Screen;

import java.util.ArrayList;
import java.util.List;

public class UIManager {

    private List<UIComponent> components = new ArrayList<>();

    public UIManager() {
        components.add(UIComponent.brnBack);
        components.add(UIComponent.btnMiniGolem);
        components.add(UIComponent.btnAdventurer);
        components.add(UIComponent.btnDragon);
        components.add(UIComponent.btnGolem);
    }

    public void update() {

    }

    public void render(Screen screen) {
        int x = screen.getWidth();
        int y;
        UIComponent c;

        for (int i = 0; i < components.size(); i++) {
            c = components.get(i);
            x -= c.getWidth() + 10;
            y = 10;
            c.render(x, y, screen);
        }

        // components.forEach(c -> c.render(screen));
    }
}
