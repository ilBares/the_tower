package it.unibs.pajc.baresi.graphic.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private List<UIButton> components = new ArrayList<>();

    public UIManager() {
        UIButton btnMob = UIButton.btnMob;
        UIButton btnBack = UIButton.btnBack;

        components.add(btnMob);
        components.add(btnBack);
        components.add(UIButton.btnMiniGolem);
        components.add(UIButton.btnAdventurer);
        components.add(UIButton.btnDragon);
        components.add(UIButton.btnGolem);

        btnMob.setVisible(true);

        btnMob.setUiButtonListener(new UIButtonListener() {
            @Override
            public void released(UIButton button) {
                btnMob.setVisible(false);
                components.forEach(b -> b.setVisible(true));
            }
        });

        btnBack.setUiButtonListener(new UIButtonListener() {
            @Override
            public void released(UIButton button) {
                components.stream().skip(1).forEach(b -> b.setVisible(false));
                btnMob.setVisible(true);
            }
        });
    }

    public void update() {
        components.forEach(UIButton::update);
    }

    public void render(Graphics2D g2) {
        for (UIButton c : components) {
            if (c.isVisible()) {
                c.render(g2);
            }
        }

        components.stream().filter(UIButton::isVisible).forEach(c -> c.render(g2));
    }
}
